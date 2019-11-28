/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 13.08.18
 */
define(
    'imcms-text-editor-utils',
    [
        'tinymce', 'imcms-texts-rest-api', 'imcms-events', 'jquery', 'imcms-modal-window-builder',
        'imcms-text-editor-types', 'imcms-i18n-texts'
    ],
    function (tinyMCE, textsRestApi, events, $, modal, editorTypes, texts) {

        texts = texts.editors.text;

        const ACTIVE_EDIT_AREA_CLASS = 'imcms-editor-area--active';
        const ACTIVE_EDIT_AREA_CLASS_$ = '.' + ACTIVE_EDIT_AREA_CLASS;

        let activeEditor;

        let blurEnabled = true;

        events.on('disable text editor blur', () => {
            blurEnabled = false;
        });
        events.on('enable text editor blur', () => {
            blurEnabled = true;
        });

        function getActiveTextEditor() {
            return activeEditor || tinyMCE.activeEditor;
        }

        function setActiveTextEditor(activeTextEditor) {
            if (activeEditor && (activeTextEditor !== activeEditor) && activeEditor.triggerBlur) {
                activeEditor.triggerBlur();
            }

            activeEditor = activeTextEditor;
        }

        function saveContent(editor, onSaved) {
            const textDTO = $(editor.$()).data();
            textDTO.text = editor.getContent();

            switch (textDTO.type) {
                case editorTypes.html:
                case editorTypes.htmlFromEditor: {
                    textDTO.text = textDTO.text.replace(/&lt;/g, '<').replace(/&gt;/g, '>');
                    if (textDTO.type === editorTypes.htmlFromEditor) textDTO.type = editorTypes.html;
                    break;
                }
                case editorTypes.text:
                case editorTypes.textFromEditor: {
                    textDTO.text = textDTO.text.replace(/</g, '&lt;').replace(/>/g, '&gt;');
                    if (textDTO.type === editorTypes.textFromEditor) textDTO.type = editorTypes.text;
                }
            }

            textsRestApi.create(textDTO)
                .done(() => {
                    events.trigger('imcms-version-modified');
                    editor.startContent = editor.getContent();
                    editor.setDirty(false);

                    onSaved && onSaved.call && onSaved.call();
                })
                .fail(() => modal.buildErrorWindow(texts.error.createFailed));
        }

        function setEditorFocus(activeTextEditor) {
            $(activeTextEditor.$()).focus(function () {
                setActiveTextEditor(activeTextEditor);

                $(ACTIVE_EDIT_AREA_CLASS_$).removeClass(ACTIVE_EDIT_AREA_CLASS)
                    .find('.mce-edit-focus')
                    .removeClass('mce-edit-focus');

                $(this).closest('.imcms-editor-area--text').addClass(ACTIVE_EDIT_AREA_CLASS);
            });
        }

        function onEditorBlur(editor) {
            if (!blurEnabled || !editor.isDirty()) {
                return;
            }

            modal.buildModalWindow(texts.confirmSave, confirmed => {
                if (!confirmed) {
                    editor.setContent(editor.startContent);
                } else {
                    saveContent(editor);
                }
            });
        }

        function showEditButton($editor) {
            $editor.parents('.imcms-editor-area--text')
                .find('.imcms-control--edit.imcms-control--text')
                .css('display', 'block');
        }

        return {
            ACTIVE_EDIT_AREA_CLASS: ACTIVE_EDIT_AREA_CLASS,
            ACTIVE_EDIT_AREA_CLASS_$: ACTIVE_EDIT_AREA_CLASS_$,
            getActiveTextEditor: getActiveTextEditor,
            setActiveTextEditor: setActiveTextEditor,
            saveContent: saveContent,
            setEditorFocus: setEditorFocus,
            onEditorBlur: onEditorBlur,
            showEditButton: showEditButton
        };
    }
);