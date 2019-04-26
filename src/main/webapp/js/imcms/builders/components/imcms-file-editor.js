define(
    'imcms-file-editor',
    ['imcms-modal-window-builder', 'imcms-i18n-texts', 'imcms-bem-builder', 'imcms-components-builder',
        'imcms-files-rest-api', 'imcms-file-to-row-transformer', 'jquery'],
    function (modal, texts, BEM, components, fileRestApi, fileToRow, $) {

        texts = texts.superAdmin.files;

        let windowCreateFile;
        let currentFile;
        let firstSubFilesContainer;
        let secondSubFilesContainer;

        function buildViewFile($fileRow, file) {
            firstSubFilesContainer = $('<div>', {
                'class': 'first-sub-files'
            });

            onFileView($fileRow, file);
            fileRestApi.get(file).done(files => {
                    $('.files-table__first-instance').append(files.map(file => fileToRow.transform(file, this)));
                }
            ).fail(() => modal.buildErrorWindow(texts.error.loadError));
        }

        function buildEditFile($fileRow, file) {

        }

        function buildDeleteFile() {
            return alert("delete!=)");
        }

        let newFileNameField = buildCreateField();
        let checkBoxIsDirectory = buildIsDirectoryCheckBox();

        function buildCreateField() {
            return components.texts.textField('<div>', {
                text: texts.title.createFileName
            });

        }

        function buildIsDirectoryCheckBox() {
            return components.checkboxes.imcmsCheckbox("<div>", {
                text: texts.title.createDirectory
            });

        }

        function onSaveFile() {
            let name = newFileNameField.getValue();
            let isDirectory = checkBoxIsDirectory.isChecked();

            if (!name) return;

            let fileToSave = {
                name: name,
                isDirectory: isDirectory
            };

            fileRestApi.create(fileToSave).done(newFile => {
                $fileRow = fileToRow.transform((currentFile = newFile), fileEditor);

                // $container.parent().find('.files-table').append($fileRow);
                $('.first-files').append($fileRow);
                $('.second-files').append($fileRow);

                onFileView = onFileSimpleView;
                prepareFileView();
            })
        }


        function buildAddFile() {

            windowCreateFile =
                modal.buildCreateFileModalWindow(
                    texts.createFile, newFileNameField, checkBoxIsDirectory, confirmed => {

                    });

            return windowCreateFile;
        }

        function downloadFile() {

        }

        function uploadFile() {

        }

        function onCancelChanges($fileRowElement, file) {

            getOnDiscardChanges(() => {
                onFileView = onFileSimpleView;
                currentFile = file;
                $fileRow = $fileRowElement;
                prepareFileView();
            }).call();
        }

        function onEditFile() {
            onFileView = onCancelChanges;

            let name = newFileNameField.getValue();
            let isDirectory = checkBoxIsDirectory.isChecked();

            if (!name) return;

            let fileToSave = {
                name: name,
                isDirectory: isDirectory
            };

            fileRestApi.replace(fileToSave).done(savedFile => {
                currentFile = savedFile;
            })
            // $profileEditButtons.slideDown();
            //
            // $profileNameRow.$input.focus();
            // $profileDocNameRow.$input.focus();
        }

        function getOnDiscardChanges(onConfirm) {
            return () => {
                modal.buildModalWindow(texts.warnChangeMessage, confirmed => {
                    if (!confirmed) return;
                    onConfirm.call();
                });
            }
        }

        let onEditDelegate = onSimpleEdit;
        let onFileView = onFileSimpleView;
        let $fileRow;

        function prepareFileView() {
            onEditDelegate = onSimpleEdit;

            $fileRow.parent()
                .find('.files-table__file-row--active')
                .removeClass('files-table__file-row--active');

            $fileRow.addClass('files-table__file-row--active');
        }

        function onSimpleEdit($fileRow, file) {
            buildViewFile($fileRow, file);
            onEditFile();
        }

        function onFileSimpleView($fileRowElement, file) {
            if (currentFile) return;
            currentFile = file;
            $fileRow = $fileRowElement;

            prepareFileView();
        }

        let fileEditor = {
            addFile: buildAddFile,
            viewFile: buildViewFile,
            editFile: buildEditFile,
            deleteFile: buildDeleteFile,
            downloadFile: downloadFile,
            uploadFile: uploadFile
        };

        return fileEditor;
    }
);