define(
    'imcms-file-editor',
    ['imcms-modal-window-builder', 'imcms-i18n-texts', 'imcms-bem-builder', 'imcms-components-builder',
        'imcms-files-rest-api', 'imcms-file-to-row-transformer', 'jquery', 'imcms-document-transformer',
        'imcms-template-groups-rest-api', "imcms-templates-rest-api", 'imcms', 'imcms-super-admin-page-builder'],
    function (modal, texts, BEM, components, fileRestApi, fileToRow, $, docToRow, groupsRestApi, templatesRestApi, imcms) {

        texts = texts.superAdmin.files;

        const CONTEXT_URL = '/api/files/file/';
        const FILE_SRC_URL = imcms.contextPath + CONTEXT_URL;
        const TEMPLATE_ROOT_PATH = '/WEB-INF/templates/text';

        const selectedFileHighlightingClassName = 'files-table__file-row--selected';
        const selectedDirHighlightingClassName = 'files-table__directory-row--selected';
        const newFileHighlightingClassName = 'files-table__file-row--active';

        let $fileSourceRow;
        let currentFile;
        let $documentsContainer;
        let selectedFiles;
        let selectedFilesRows;

        initData();

        function getMainContainer() {
            return $('.imcms-form').find('.files-table');
        }

        function getSubFilesContainerByChildRow(childRow) {
            const parent = childRow.parent();

            if (parent.parent().hasClass(getMainContainer().attr('class'))) {
                return $(childRow.get(-1));
            }
            return getSubFilesContainerByChildRow(parent);
        }

        function getAllSubFilesContainers() {
            const columns = Array.from(getMainContainer().children());
            return columns
                .map(column => $(column))
                .flatMap(column => column.children().get(-1))
                .map(container => $(container))
                .filter(container => !container.hasClass('root-directory-row'));
        }

        function getSubFilesContainerByIndex(index) {
            return $(getAllSubFilesContainers()[index]);
        }

        function getPathRowByIndex(index) {
            const $column = $(getMainContainer().children()[index]);
            return $column.find('.path-row');
        }

        function getDirPathFromFullPath(path) {
            return path.substring(0, path.lastIndexOf('/'));
        }

        function getDirPathBySubFilesContainer(container) {
            const index = getIndexOfSubFilesContainer(container);
            return getDirPathByIndex(index);
        }

        function getDirPathByIndex(index) {
            return getPathRowByIndex(index).attr('full-path');
        }

        function createBackDir(fullPath, physicalPath) {
            return {
                fileName: '/..',
                fullPath: getDirPathFromFullPath(fullPath, physicalPath),
                physicalPath: getDirPathFromFullPath(physicalPath),
                fileType: 'DIRECTORY'
            }
        }

        function getIndexOfSubFilesContainer(container) {
            const containers = getAllSubFilesContainers();

            for (let i = 0; i < containers.length; i++) {
                if (container.attr('class') === $(containers[i]).attr('class')) {
                    return i;
                }
            }
            return -1;
        }

        function integrateFileInContainerAsRow(file, filesContainer, transformFileToRowFunction) {
            let row = transformFileToRowFunction(file, fileEditor);
            filesContainer.append(row);

            row.click(event => {
                if (!event.ctrlKey) {
                    selectedFilesRows = [];
                    selectedFiles = [];
                } else if (event.ctrlKey && $fileSourceRow.hasClass(selectedFileHighlightingClassName)) {
                    deleteHighlighting($fileSourceRow);
                    selectedFilesRows.pop($fileSourceRow);
                    selectedFiles.pop(currentFile);
                    return;
                }

                const allSubFilesContainers = getAllSubFilesContainers();

                allSubFilesContainers.forEach(container =>
                    deleteAllHighlightingInSubFilesContainer(container, newFileHighlightingClassName)
                );

                selectedFilesRows.push($fileSourceRow);
                selectedFiles.push(currentFile);

                allSubFilesContainers.forEach(container =>
                    updateHighlighting(container, selectedFilesRows)
                );
            });

            return row;
        }

        function updateHighlighting(container, rows) {
            deleteAllHighlightingInSubFilesContainer(container);
            rows.forEach(elem => addHighlighting(elem));
        }

        function deleteAllHighlightingInSubFilesContainer(container, highlightingClassName = selectedFileHighlightingClassName) {
            if (!container) {
                return;
            }
            const selector = '.' + container.attr('class') + ' .' + highlightingClassName;
            $(selector).removeClass(highlightingClassName);
        }

        function addHighlighting(elem) {
            const $elem = $(elem);

            if ($elem.hasClass(selectedFileHighlightingClassName)) {
                return;
            }
            $elem.addClass(selectedFileHighlightingClassName);
        }

        function deleteHighlighting(elem) {
            $(elem).removeClass(selectedFileHighlightingClassName);
        }

        const $loadingAnimation = $('<div>', {
            class: 'loading-animation',
            style: 'display: none'
        });

        const $docsNumberLabel = $('<div>', {
            class: 'imcms-label',
            style: 'display: none',
        });

        function buildDocumentsContainer() {
            $documentsContainer = new BEM({
                block: 'table-documents',
                elements: {
                    'loading-animation': $loadingAnimation,
                    'docs-number': $docsNumberLabel,
                }
            }).buildBlockStructure('<div>');

            return $documentsContainer;
        }

        function setEnabledEditMode(isEnabled) {
            if (isEnabled) {
                $templateGroupDefaultButtons.slideUp();
                $templateGroupEditButtons.slideDown();

                $templateGroupNameTextField.$input.removeAttr('disabled').focus();
            } else {
                $templateGroupDefaultButtons.slideUp();
                $templateGroupEditButtons.slideUp();
                $templateGroupNameTextField.slideUp();
                $templatesTableTitle.hide();
                $templatesTable.hide();

                $templateGroupNameTextField.$input.attr('disabled', 'disabled');
            }
        }

        function initData() {
            groupsRestApi.read().done(templateGroups => {
                const mappedData = templateGroups.map(group => mapGroupToOption(group));

                components.selects.addOptionsToSelect(mappedData, $templateGroupSelect, onSelectTemplateGroup);
            }).fail(() => modal.buildErrorWindow(texts.error.loadGroups));
        }

        function mapGroupToOption(group) {
            return {
                text: group.name,
                'data-value': group.id
            };
        }

        const $templateGroupCreateButton = components.buttons.positiveButton({
            text: texts.groupData.create,
            click: onCreateTemplateGroup
        });

        let selectedValueBeforeSaving;

        function onCreateTemplateGroup() {
            $templateGroupNameTextField.attr('mode', 'create');
            $templateGroupNameTextField.setValue('');

            setEnabledEditMode(true);
            $templateGroupNameTextField.slideDown();
            $templatesTableTitle.hide();
            $templatesTable.hide();

            selectedValueBeforeSaving = $templateGroupSelect.getSelectedValue();
        }

        const $templateGroupSelect = components.selects.imcmsSelect('<div>', {
            id: 'template-group',
            name: 'template-group',
            text: texts.groupData.title
        });

        function onSelectTemplateGroup() {
            if ($templateGroupEditButtons.css('display') !== 'none') {
                onCancelTemplateGroup();
            } else {
                const templateName = $templateGroupSelect.selectedText();
                fillTemplatesTableByTemplateGroup(templateName);
                $templateGroupNameTextField.setValue(templateName);
                $templateGroupNameTextField.slideDown();
                $templateGroupDefaultButtons.slideDown();
            }
        }

        const $templateGroupNameTextField = components.texts.textField('<div>', {});

        const $templateGroupDefaultButtons = components.buttons.buttonsContainer('<div>', [
            components.buttons.errorButton({
                text: texts.groupData.delete,
                click: onDeleteTemplateGroup
            }),
            components.buttons.positiveButton({
                text: texts.groupData.edit,
                click: onEditTemplateGroup
            }),
        ], {
            style: 'display: none'
        });

        function onEditTemplateGroup() {
            $templateGroupNameTextField.attr('mode', 'edit');
            setEnabledEditMode(true);

            selectedValueBeforeSaving = $templateGroupSelect.getSelectedValue()
        }

        function onDeleteTemplateGroup() {
            if (!$templateGroupSelect.getSelectedValue()) {
                return;
            }

            modal.buildModalWindow(texts.groupData.deleteConfirm, confirmed => {
                if (!confirmed) return;

                const id = $templateGroupSelect.getSelectedValue();

                groupsRestApi.remove(id).done(() => {
                    $templateGroupSelect.deleteOption(id);
                    $templateGroupSelect.selectFirst();
                    setEnabledEditMode(false);
                }).fail(() => modal.buildErrorWindow(texts.error.deleteGroup));
            });
        }

        const $templateGroupEditButtons = components.buttons.buttonsContainer('<div>', [
            components.buttons.negativeButton({
                text: texts.groupData.cancel,
                click: onCancelTemplateGroup
            }),
            components.buttons.saveButton({
                text: texts.groupData.save,
                click: onSaveTemplateGroup
            }),
        ], {
            style: 'display: none;'
        });

        function onSaveTemplateGroup() {
            if (!$templateGroupNameTextField.getValue()) {
                return;
            }

            modal.buildModalWindow(texts.groupData.saveConfirm, confirmed => {
                if (!confirmed) return;

                if ($templateGroupNameTextField.attr('mode') === 'edit') {
                    editTemplateGroup();
                } else if ($templateGroupNameTextField.attr('mode') === 'create') {
                    createTemplateGroup();
                }
            });
        }

        function editTemplateGroup() {
            const editedGroup = {
                id: $templateGroupSelect.getSelectedValue(),
                name: $templateGroupNameTextField.getValue()
            };

            groupsRestApi.replace(editedGroup).done(group => {
                $templateGroupSelect.deleteOption($templateGroupSelect.getSelectedValue());
                components.selects.addOptionsToSelect([mapGroupToOption(group)], $templateGroupSelect, onSelectTemplateGroup);
                $templateGroupSelect.selectValue(group.id);

                setEnabledEditMode(false);
            }).fail(() => modal.buildErrorWindow(texts.error.editGroup));
        }

        function createTemplateGroup() {
            const created = {
                name: $templateGroupNameTextField.getValue()
            };

            groupsRestApi.create(created).done(group => {
                components.selects.addOptionsToSelect([mapGroupToOption(group)], $templateGroupSelect, onSelectTemplateGroup);
                $templateGroupSelect.selectValue(group.id);

                setEnabledEditMode(false);
            }).fail(() => modal.buildErrorWindow(texts.error.createGroup));
        }

        function onCancelTemplateGroup() {
            modal.buildModalWindow(texts.groupData.cancelConfirm, confirmed => {
                if (!confirmed) {
                    $templateGroupSelect.selectValue(selectedValueBeforeSaving);
                    return;
                }

                setEnabledEditMode(false);
            });
        }

        const $templatesTableTitle = $('<div>', {
            text: texts.groupData.templatesTableTitle,
            class: 'imcms-label',
            style: 'display: none'
        });

        const $templatesTable = $('<div>', {
            class: 'templates-data',
            style: 'display: none;'
        });

        function fillTemplatesTableByTemplateGroup(groupName) {
            groupsRestApi.get(groupName).done(group => {
                $templatesTableTitle.show();
                $templatesTable.show();
                $templatesTable.children().remove();
                group.templates.forEach(template => $templatesTable.append(templateToRow(template)));
            }).fail(() => modal.buildErrorWindow(texts.error.loadGroup));
        }

        function templateToRow(template) {
            const $removeButton = components.controls
                .remove(onClickDeleteTemplate.bind({template}))
                .attr("title", texts.title.delete);

            return new BEM({
                block: 'template-info-row',
                elements: {
                    'template-name': $('<div>').text(template.name),
                    'delete': $removeButton
                }
            }).buildBlockStructure('<div>');
        }

        function onClickDeleteTemplate() {
            const templateName = {
                template: this.template.name
            };

            fileRestApi.getDocuments(templateName).done(documents => {
                if (documents.length === 0) {
                    deleteTemplate(this.template.id);
                } else {
                    confirmDeleteTemplate(() => showDeleteTemplateModalWindow(this.template));
                }
            }).fail(() => modal.buildErrorWindow(texts.error.loadDocError));
        }

        function deleteTemplate(id) {
            templatesRestApi.delete(id).done(() => {
                fillTemplatesTableByTemplateGroup($templateGroupSelect.selectedText());
            }).fail(() => modal.buildErrorWindow(texts.error.deleteTemplate));
        }

        function confirmDeleteTemplate(onConfirm) {
            modal.buildModalWindow(texts.template.boundDocumentsWarn, confirmed => {
                if (confirmed) {
                    onConfirm();
                }
            });
        }

        function showDeleteTemplateModalWindow(template) {
            templatesRestApi.read().done(templates => {

                deleteTemplateFromArrayById(templates, template.id);
                const templatesRadioButtons = templates
                    .map(template => components.radios.imcmsRadio("<div>", {
                        text: template.name,
                        name: 'template',
                        value: template.id,
                    }));

                const templatesRadioButtonsGroup = components.radios.group.apply(null, templatesRadioButtons);

                const $templatesRadioButtonsContainer = components.radios.radioContainer('<div>', templatesRadioButtons, {
                    class: 'templates-radios-container'
                });

                templatesRadioButtons[0].setChecked(true); // todo: if templates array is empty?

                modal.buildOptionalModalWindow(texts.title.replaceTemplate, $templatesRadioButtonsContainer, confirmed => {
                    if (confirmed) {
                        const transferData = {
                            oldTemplate: template.name,
                            newTemplate: getTemplateFromArrayById(templates, templatesRadioButtonsGroup.getCheckedValue()).name
                        };

                        fileRestApi.replaceTemplateOnDoc(transferData).done(() => {
                            deleteTemplate(template.id);
                        }).fail(() => modal.buildErrorWindow(texts.error.replaceTemplate));
                    }
                });
            }).fail(() => modal.buildErrorWindow(texts.error.loadTemplates));
        }

        function getIndexOfTemplateInArrayById(array, id) {
            return array.findIndex(template => template.id == id);
        }

        function getTemplateFromArrayById(array, id) {
            return array[getIndexOfTemplateInArrayById(array, id)];
        }

        function deleteTemplateFromArrayById(array, id) {
            array.splice(getIndexOfTemplateInArrayById(array, id), 1);
            return array;
        }

        const $templateGroupEditor = new BEM({
            block: 'group-editor',
            elements: {
                'create-button': $templateGroupCreateButton,
                'select': $templateGroupSelect,
                'name-row': $templateGroupNameTextField,
                'default-buttons': $templateGroupDefaultButtons,
                'edit-buttons': $templateGroupEditButtons,
                'templates-data-title': $templatesTableTitle,
                'templates-data': $templatesTable
            }
        }).buildBlockStructure('<div>', {});

        function getTemplateGroupEditor() {
            $templateGroupNameTextField.$input.attr('disabled', 'disabled');
            $templateGroupNameTextField.css('display', 'none');

            $templateGroupEditor.hide();

            return $templateGroupEditor;
        }

        function getViewDocById($docRow, doc) {
            let question = texts.warnViewDocMessage;

            modal.buildModalWindow(question, function (answer) {
                if (!answer) {
                    return;
                }
                window.location.replace("/" + doc.id);
            });
        }

        function buildTitleRow() {
            return new BEM({
                block: 'title-doc-row',
                elements: {
                    'doc-id': $('<div>', {text: texts.documentData.docId}),
                    'doc-type': $('<div>', {text: texts.documentData.docType})
                }
            }).buildBlockStructure('<div>');
        }

        function updateHighlightingForDir(elem) {
            elem.parent()
                .find('.' + selectedDirHighlightingClassName)
                .removeClass(selectedDirHighlightingClassName);
            elem.addClass(selectedDirHighlightingClassName);
        }

        function buildViewFilesContainer($fileRow, file, isDblClick) {
            currentFile = file;
            $fileSourceRow = $fileRow;

            if (file.fileType === 'DIRECTORY') {
                updateHighlightingForDir($fileSourceRow);
                deleteAllHighlightingInSubFilesContainer(getSubFilesContainerByChildRow($fileSourceRow), selectedDirHighlightingClassName);
            }
            if (file.fileType === 'DIRECTORY' && isDblClick) {
                if (file.physicalPath.startsWith(TEMPLATE_ROOT_PATH)) {
                    $templateGroupEditor.show();
                } else {
                    $templateGroupEditor.hide();
                    $docsNumberLabel.hide();
                    $documentsContainer.find('.documents-data').remove();
                }

                onDirectoryDblClick.call(this, $fileRow, file);
            } else if (file.fileType === 'FILE' && isDblClick) {
                onFileDblClick(file);
            } else if (file.fileType === 'FILE' && isTemplate(file)) {
                onTemplateClick(file);
            }
        }

        function onDirectoryDblClick($fileRow, file) {
            getAllSubFilesContainers().forEach(container =>
                deleteAllHighlightingInSubFilesContainer(container)
            );
            selectedFilesRows = [];
            selectedFiles = [];

            const path = file.fullPath;

            fileRestApi.get(path).done(files => {
                const $subFilesContainer = $('<div>').addClass(this.subFilesClassName);
                    const $filesContainer = $('.' + this.columnClassName);
                    $filesContainer.find('.' + this.subFilesClassName).remove();
                    $filesContainer.append($subFilesContainer);

                const index = getIndexOfSubFilesContainer($subFilesContainer);

                const pathRow = getPathRowByIndex(index);
                pathRow.text(file.physicalPath);
                pathRow.attr('full-path', path);

                const transformFileToRow = fileToRow.transformFileToRow.bind({subFilesContainerIndex: index});
                $subFilesContainer.append(transformFileToRow(createBackDir(path, file.physicalPath), fileEditor));

                    files.forEach(file => integrateFileInContainerAsRow(file, $subFilesContainer, transformFileToRow));
                }
            ).fail(() => modal.buildErrorWindow(texts.error.loadError));
        }

        function onFileDblClick(file) {
            const fileName = file.fileName;

            if (isImage(fileName)) {
                const $image = fileToImgElement(file);
                const $imageContainer = $('<div>');
                $imageContainer.append($image);

                modal.buildViewModalWindow($imageContainer);
            } else if (isTextFormat(fileName)) {
                const $textViewBox = fileToTextView(file);

                modal.buildViewModalWindow($textViewBox);
            } else {
                downloadFile(file);
            }
        }

        function isImage(fileName) {
            const pattern = new RegExp('.(GIF|JPE?G|PNG)$', 'gi');
            return pattern.test(fileName);
        }

        function isTextFormat(fileName) {
            const pattern = new RegExp('.(HTML?|CSS|JS|VBS|TXT|INC|JSP|ASP|FRAG|LOG)$', 'gi');
            return pattern.test(fileName);
        }

        function isTemplate(file) {
            const pattern = new RegExp('.(JSP|HTML)$', 'gi');
            return pattern.test(file.fileName) && file.physicalPath.startsWith(TEMPLATE_ROOT_PATH);
        }

        function fileToImgElement(file) {
            return $('<img>').attr('src', FILE_SRC_URL + file.fullPath);
        }

        function fileToTextView(file) {
            const $textArea = components.texts.textAreaField('<div>', {readonly: 'readonly'});
            const pathFile = {
                path: file.fullPath
            };
            fileRestApi.getFile(pathFile).done(file => {
                $textArea.addClass('text-preview');
                $textArea.setValue(window.atob(file.contents));
            }).fail(() => modal.buildErrorWindow(texts.error.loadFileError));
            return $textArea;
        }

        function downloadFile(file) {
            window.location.replace(FILE_SRC_URL + file.fullPath);
        }

        function onTemplateClick(file) {
            $documentsContainer.find('.documents-data').remove();
            $loadingAnimation.show();

            const templateName = {
                template: file.fullPath
            };

            fileRestApi.getDocuments(templateName).done(documents => {
                    $documentsContainer.find('.documents-data').remove();

                    if (documents.length > 0) {
                        $docsNumberLabel.show();
                        $docsNumberLabel.text(texts.documentData.docsNumber + documents.length);

                        const $documentsData = $('<div>').addClass('documents-data');
                        $documentsData.append(buildTitleRow());

                        const documentsRows = documents.map(doc => docToRow.transform(doc, fileEditor));
                        $documentsData.append(documentsRows);

                        $documentsContainer.append($documentsData).show();
                    } else {
                        $docsNumberLabel.hide();
                    }

                $loadingAnimation.hide();
                }
            ).fail(() => modal.buildErrorWindow(texts.error.loadDocError));
        }

        const newFileNameField = buildFileNameField(texts.title.createFileName);
        const editDirectoryNameField = buildFileNameField(texts.title.directoryName);
        const editFileNameField = buildFileNameField(texts.title.fileName);
        const checkBoxIsDirectory = buildIsDirectoryCheckBox();
        const contentTextArea = buildContentTextArea();
        let editCheckBox;

        function setEnableEditContent() {
            const isImage = new RegExp('.(GIF|JPE?G|PNG|PDF)$', 'gi');
            contentTextArea.show();
            if (isImage.test(currentFile.fullPath) || checkBoxIsDirectory.isChecked()) {
                editCheckBox.$input.attr('disabled', 'disabled');
                contentTextArea.hide();
            }
            if (editCheckBox.isChecked()) {
                contentTextArea.$input.removeAttr('disabled');
                editFileNameField.$input.attr('disabled', 'disabled');
                checkBoxIsDirectory.$input.attr('disabled', 'disabled');
            } else {
                contentTextArea.$input.attr('disabled', 'disabled');
                editFileNameField.$input.removeAttr('disabled');
            }
        }

        function prepareOnEditFile(confirmEditFile) {
                editCheckBox = buildIsEditCheckBox();

                if (currentFile.fileType === 'FILE') {
                    editFileNameField.setValue(currentFile.fileName);
                    setEnableEditContent();
                } else {
                    editDirectoryNameField.setValue(currentFile.fileName);
                }

                const pathFile = {
                    path: currentFile.fullPath
                };
                fileRestApi.getFile(pathFile).done(file => {
                    contentTextArea.setValue(window.atob(file.contents));
                }).fail(() => modal.buildErrorWindow(texts.error.loadFileError));

                confirmEditFile();
        }

        function confirmEdit(onRenameFile, onEditFileContent) {
            return currentFile.fileType === 'DIRECTORY'
                ? modal.buildEditDirectoryModalWindow(editDirectoryNameField, confirmed => {
                    if (confirmed) {
                        onRenameFile();
                    }
                })
                : modal.buildEditFileModalWindow(editFileNameField, contentTextArea, editCheckBox, confirmed => {
                    if (confirmed && !editCheckBox.isChecked()) {
                        onRenameFile()
                    }
                    if (confirmed && editCheckBox.isChecked()) {
                        onEditFileContent()
                    }
                });
        }

        function onEditFileContent() {
            const name = currentFile.fileType === 'FILE' ? editFileNameField.getValue() : editDirectoryNameField.getValue();
            const currentFullPath = this.getTargetDirectoryPath() + "/" + name;

            const fileToSaveWithContent = {
                fileName: name,
                fullPath: currentFullPath,
                content: contentTextArea.getValue()
            };

            fileRestApi.change(fileToSaveWithContent).done(file => {
                currentFile.contents = file.contents;
                contentTextArea.setValue(file.contents);

                if ($templatesTable.css('display') !== 'none') {
                    fillTemplatesTableByTemplateGroup($templateGroupSelect.selectedText());
                }
            }).fail(() => modal.buildErrorWindow(texts.error.editFailed));
        }

        function onRenameFile() {
            const name = currentFile.fileType === 'FILE'
                ? editFileNameField.getValue()
                : editDirectoryNameField.getValue();

            if (!name) return;

            const targetSubFilesContainer = this.getTargetSubFilesContainer();
            const targetDirectoryPath = getDirPathBySubFilesContainer(targetSubFilesContainer);

            const targetPath = targetDirectoryPath + "/" + name;
            const fileToSave = {
                src: currentFile.fullPath,
                target: targetPath,
            };

            fileRestApi.rename(fileToSave).done(file => {
                currentFile = file;
                $fileSourceRow.remove();
                $fileSourceRow = integrateFileInContainerAsRow(file, targetSubFilesContainer, this.transformFileToRow);
            }).fail(() => modal.buildErrorWindow(texts.error.renameFailed));
        }

        function deleteFile(file) {
            modal.buildModalWindow(texts.warnDeleteMessage, confirmed => {
                if (!confirmed) return;

                let sourceFile = {
                    fileName: file.fileName,
                    fullPath: file.fullPath
                };

                fileRestApi.deleteFile(sourceFile).done(() => {
                    $documentsContainer.remove();
                    $fileSourceRow.remove();

                    if ($templatesTable.css('display') !== 'none') {
                        fillTemplatesTableByTemplateGroup($templateGroupSelect.selectedText());
                    }
                }).fail(() => modal.buildErrorWindow(texts.error.deleteFailed));
            });
        }

        function buildFileNameField(label) {
            return components.texts.textField('<div>', {
                text: label
            });

        }

        function buildContentTextArea() {
            return components.texts.textAreaField('<div>', {
                id: 'content',
                text: texts.title.titleContent,
            })
        }

        function buildIsEditCheckBox() {
            return components.checkboxes.imcmsCheckbox('<div>', {
                text: texts.title.titleEditContent,
                change: setEnableEditContent
            })
        }

        function buildIsDirectoryCheckBox() {
            return components.checkboxes.imcmsCheckbox("<div>", {
                text: texts.title.createDirectory
            });
        }

        function confirmAddFile(onConfirm) {
            newFileNameField.setValue('');
            checkBoxIsDirectory.$input.removeAttr('disabled');
            return modal.buildCreateFileModalWindow(
                newFileNameField, checkBoxIsDirectory, confirmed => {
                    if (confirmed) {
                        onConfirm();
                    }
                });
        }

        function onSaveFile() {
            let name = newFileNameField.getValue();
            let isDirectory = checkBoxIsDirectory.isChecked();

            if (!name) return;

            const targetSubFilesContainer = this.getTargetSubFilesContainer();
            const targetDirectoryPath = getDirPathBySubFilesContainer(targetSubFilesContainer);
            const currentFullPath = targetDirectoryPath + "/" + name;

            let fileToSave = {
                fileName: name,
                fullPath: currentFullPath,
                fileType: isDirectory ? 'DIRECTORY' : 'FILE'
            };

            fileRestApi.create(fileToSave).done(newFile => {
                currentFile = newFile;
                $fileSourceRow = integrateFileInContainerAsRow(newFile, targetSubFilesContainer, this.transformFileToRow);
                $fileSourceRow.addClass(newFileHighlightingClassName);
            }).fail(() => modal.buildErrorWindow(texts.error.createError));
        }

        function uploadFile() {
            let $fileInput = $('<input>', {
                type: 'file',
                multiple: ''
            });
            $fileInput.click();

            $fileInput.change(() => {
                let formData = new FormData();

                let files = $fileInput.prop('files');
                Array.from(files).forEach(file => {
                    const fileName = file.name.split(' ');
                    formData.append(fileName.join('_'), file)
                });

                const targetSubFilesContainer = this.getTargetSubFilesContainer();
                const targetDirectoryPath = getDirPathBySubFilesContainer(targetSubFilesContainer);
                formData.append("targetDirectory", targetDirectoryPath);

                fileRestApi.upload(formData).done(uploadedFiles => {
                    uploadedFiles.forEach(file => {
                        currentFile = file;
                        $fileSourceRow = integrateFileInContainerAsRow(file, targetSubFilesContainer, this.transformFileToRow);
                        $fileSourceRow.addClass(newFileHighlightingClassName);
                    });
                }).fail(() => modal.buildErrorWindow(texts.error.uploadError));
            });
        }

        function moveFile() {
            const targetSubFilesContainer = this.getTargetSubFilesContainer();

            let paths = {
                src: selectedFiles.map(file => file.fullPath).toString(),
                target: getDirPathBySubFilesContainer(targetSubFilesContainer)
            };

            fileRestApi.move(paths).done(files => {
                selectedFiles = files;

                const allSubFilesContainers = getAllSubFilesContainers();
                if (getDirPathByIndex(0) === getDirPathByIndex(1)) {
                    selectedFilesRows = [];
                } else {
                    selectedFilesRows.forEach(row => row.remove());
                    selectedFilesRows = [];

                    files.forEach(file =>
                        selectedFilesRows.push(
                            integrateFileInContainerAsRow(file, targetSubFilesContainer, this.transformFileToRow)
                        )
                    );
                }

                allSubFilesContainers.forEach(container =>
                    updateHighlighting(container, selectedFilesRows)
                );
            }).fail(() => modal.buildErrorWindow(texts.error.moveError));
        }

        function copyFile() {
            const targetSubFilesContainer = this.getTargetSubFilesContainer();

            let paths = {
                src: selectedFiles.map(file => file.fullPath).toString(),
                target: getDirPathBySubFilesContainer(targetSubFilesContainer)
            };

            fileRestApi.copy(paths).done(newFiles => {
                selectedFiles = newFiles;
                selectedFilesRows = [];

                const allSubFilesContainers = getAllSubFilesContainers();
                if (getDirPathByIndex(0) !== getDirPathByIndex(1)) {
                    newFiles.forEach(file =>
                        selectedFilesRows.push(
                            integrateFileInContainerAsRow(file, targetSubFilesContainer, this.transformFileToRow)
                        )
                    );
                }

                allSubFilesContainers.forEach(container =>
                    updateHighlighting(container, selectedFilesRows)
                );
            }).fail(() => modal.buildErrorWindow(texts.error.copyError));
        }

        function addTemplateToGroup() {
            modal.buildModalWindow(texts.groupData.addToGroupConfirm, confirmed => {
                if (!confirmed) return;

                const templateGroupName = $templateGroupSelect.selectedText();

                const transferData = {
                    templateGroupName,
                    templatePath: currentFile.fullPath,
                };

                fileRestApi.addTemplateToGroup(transferData).done(template => {
                    if ($templatesTable.css('display') !== 'none') {
                        fillTemplatesTableByTemplateGroup(templateGroupName);
                    }
                }).fail(() => modal.buildErrorWindow(texts.error.addTemplateToGroup));
            });
        }

        function buildBoundData(targetSubFilesContainerIndex) {
            return {
                getTargetSubFilesContainer: () => getSubFilesContainerByIndex(targetSubFilesContainerIndex),
                transformFileToRow: bindTransformFileToRow(targetSubFilesContainerIndex)
            }
        }

        function bindTransformFileToRow(subFilesContainerIndex) {
            return fileToRow.transformFileToRow.bind({subFilesContainerIndex});
        }

        function bindViewSubFilesContainer(subFilesContainerIndex) {
            const classes = {
                subFilesClassName: subFilesContainerIndex === 0 ? 'first-sub-files' : 'second-sub-files',
                columnClassName: subFilesContainerIndex === 0 ? 'files-table__first-instance' : 'files-table__second-instance'
            };
            return buildViewFilesContainer.bind(classes);
        }

        function bindUploadFile(subFilesContainerIndex) {
            return uploadFile.bind(buildBoundData(subFilesContainerIndex))
        }

        function bindAddFile(subFilesContainerIndex) {
            const saveFile = onSaveFile.bind(buildBoundData(subFilesContainerIndex));
            return () => confirmAddFile(saveFile);
        }

        function bindEditFile(subFilesContainerIndex) {
            const renameFile = onRenameFile.bind(buildBoundData(subFilesContainerIndex));
            const editFileContent = onEditFileContent.bind({
                getTargetDirectoryPath: () => getDirPathByIndex(subFilesContainerIndex)
            });
            const buildConfirmEdit = () => confirmEdit(renameFile, editFileContent);
            return () => prepareOnEditFile(buildConfirmEdit);
        }

        function bindMoveFile(subFilesContainerIndex) {
            const index = subFilesContainerIndex === 0 ? 1 : 0;
            return moveFile.bind(buildBoundData(index));
        }

        function bindCopyFile(subFilesContainerIndex) {
            const index = subFilesContainerIndex === 0 ? 1 : 0;
            return copyFile.bind(buildBoundData(index));
        }

        const fileEditor = {
            isTemplate,
            bindViewSubFilesContainer,
            bindUploadFile,
            bindAddFile,
            bindEditFile,
            bindMoveFile,
            bindCopyFile,
            deleteFile,
            downloadFile,
            getTemplateGroupEditor,
            addTemplateToGroup,
            displayDocs: buildDocumentsContainer,
            viewDoc: getViewDocById
        };

        return fileEditor;
    }
);