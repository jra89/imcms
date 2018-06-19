/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 18.06.18
 */
Imcms.require(
    ['jquery', 'imcms-components-builder', 'imcms-uuid-generator', 'imcms-languages-rest-api'],
    function ($, components, uuid, languagesRestApi) {
        function activateUserAdminRoles() {
            var $form = $('#user-edit-form');
            var $userAdminRoleIds = $form.find('input[name=user_admin_role_ids]');

            if (!$userAdminRoleIds.length) return;

            $userAdminRoleIds.attr('disabled', 'disabled');

            var isUserAdminSelected = $form.find('input[name=role_ids]')
                .find("option")
                .filter(function () {
                    var $option = $(this);
                    return ($option.text() === 'Useradmin' && $option.is(':selected'));
                })
                .length;

            if (isUserAdminSelected) {
                $userAdminRoleIds.removeAttr('disabled');
            }
        }

        function onSubmit(e) {
            var $form = $('#user-edit-form');

            if ($form.find('input[name=login_name]').val() === "") {
                e.preventDefault();
                alert($('#must-fill-mandatory-fields-text').val());
                return;
            }

            var $pass1 = $form.find('input[name=password1]');
            var $pass2 = $form.find('input[name=password2]');

            if ($pass1.val() === $pass2.val()) {
                return;
            }

            e.preventDefault();
            $pass2.val("");
            $pass1.val("").focus();
            alert($('#pass-verification-failed-text').val());
        }

        function loadLanguages() {
            var $langSelectContainer = $('#languages-select-container');

            var selectAttributes = {
                text: $langSelectContainer.attr('data-text'),
                name: 'lang_id'
            };

            var $select = components.selects.imcmsSelect("<div>", selectAttributes);
            $select.appendTo($langSelectContainer);

            languagesRestApi.read().done(function (languages) {

                languages = languages.map(function (lang) {
                    return {
                        'data-value': lang.code,
                        text: lang.name
                    }
                });

                components.selects.addOptionsToSelect(languages, $select, $select.selectValue);
            });
        }

        function bindOnEditClicked($phoneRow) {
            return function () {

            }
        }

        function bindOnDeleteClicked($phoneRow) {
            return function () {

            }
        }

        function bindOnSaveClick($phoneRow) {
            return function () {

            }
        }

        function addPhone(e) {
            e.preventDefault();

            var $phoneInput = $('#phone');
            var phone = $phoneInput.val().trim();

            if (!phone) return;

            $phoneInput.val('');

            var $phoneTypeContainer = $('#phone-type-select').parent();
            var $newRow = $phoneTypeContainer.clone(true, true);

            var $editPhoneButton = components.controls.edit(bindOnEditClicked($newRow));
            var $deletePhoneButton = components.controls.remove(bindOnDeleteClicked($newRow));
            var $saveButton = components.buttons.positiveButton({
                'class': 'imcms-button--add-phone',
                style: 'display: none;',
                click: bindOnSaveClick($newRow),
                text: 'Save'
            });

            $newRow.find('.imcms-label')
                .text('')
                .removeAttr('for')
                .end()
                .find('.imcms-select')
                .attr('disabled', 'disabled')
                .end()
                .find('#phone')
                .attr('disabled', 'disabled')
                .attr('name', 'user_phone_number')
                .val(phone)
                .end()
                .find('#phone-type-select,#phone,#phone-type-selected')
                .removeAttr('id')
                .end()
                .find('#button-add-phone')
                .detach()
                .end()
                .find('.imcms-text-box')
                .append();

            $newRow.append($saveButton, $deletePhoneButton, $editPhoneButton).insertAfter($phoneTypeContainer);
        }

        function filterNonDigits(e) {
            return ((e.ctrlKey || e.altKey || e.metaKey)
                || (/^[0-9()+.,-]+$/g.test(e.key))
                || (e.key === "Backspace")
                || (e.key === "Shift")
                || (e.key && e.key.indexOf && !!~e.key.indexOf("Arrow"))
            );
        }

        $(function () {
            $('input[name=login_name]').focus();
            activateUserAdminRoles();
            loadLanguages();

            components.selects.makeImcmsSelect($('#phone-type-select'));

            $('#select-role-ids').change(activateUserAdminRoles);
            $('#edit-user-submit-button').click(onSubmit);

            $('#phone').keydown(filterNonDigits).on('paste', function (e) {
                e.preventDefault();
            });

            $('#button-add-phone').click(addPhone);
        });
    }
);
