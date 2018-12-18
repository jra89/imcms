/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 25.07.18
 */
define(
    'imcms-role-editor',
    [
        'imcms-bem-builder', 'imcms-components-builder', 'imcms-i18n-texts', 'imcms-modal-window-builder',
        'imcms-roles-rest-api', 'imcms-role-to-row-transformer'
    ],
    function (BEM, components, texts, confirmationBuilder, rolesRestAPI, roleToRow) {

        texts = texts.superAdmin.roles;

        var $roleNameRow;

        var $getPasswordByEmail;
        var $accessToAdminPages;
        var $useImagesInImageArchive;
        var $changeImagesInImageArchive;

        var permissionCheckboxes$;

        var $roleViewButtons;
        var $roleEditButtons;

        function buildRoleNameRow() {
            $roleNameRow = components.texts.textBox('<div>', {text: texts.roleName});
            $roleNameRow.$input.attr('disabled', 'disabled');
            return $roleNameRow;
        }

        function buildRolePermissions() {

            function createCheckboxWithText(text) {
                return components.checkboxes.imcmsCheckbox("<div>", {
                    disabled: 'disabled',
                    text: text
                });
            }

            permissionCheckboxes$ = [
                $getPasswordByEmail = createCheckboxWithText(texts.permissions.getPasswordByEmail),
                $accessToAdminPages = createCheckboxWithText(texts.permissions.accessToAdminPages),
                $useImagesInImageArchive = createCheckboxWithText(texts.permissions.useImagesInImageArchive),
                $changeImagesInImageArchive = createCheckboxWithText(texts.permissions.changeImagesInImageArchive)
            ];

            return components.checkboxes.checkboxContainerField(
                '<div>', permissionCheckboxes$, {title: texts.permissions.title}
            );
        }

        function onCancelChanges($roleRowElement, role) {
            getOnDiscardChanges(function () {
                onRoleView = onRoleSimpleView;
                currentRole = role;
                $roleRow = $roleRowElement;
                prepareRoleView();
            }).call();
        }

        function onEditRole() {
            onRoleView = onCancelChanges;

            $roleViewButtons.slideUp();
            $roleEditButtons.slideDown();

            $roleNameRow.$input.removeAttr('disabled').focus();

            permissionCheckboxes$.forEach(function ($checkbox) {
                $checkbox.$input.removeAttr('disabled');
            })
        }

        function onDeleteRole() {
            confirmationBuilder.buildModalWindow(texts.deleteConfirm, function (confirmed) {
                if (!confirmed) return;

                rolesRestAPI.remove(currentRole).success(function () {
                    $roleRow.remove();
                    currentRole = null;
                    onEditDelegate = onSimpleEdit;
                    $container.slideUp();
                })
            });
        }

        function buildRoleViewButtons() {
            return $roleViewButtons = components.buttons.buttonsContainer('<div>', [
                components.buttons.positiveButton({
                    text: texts.editRole,
                    click: onEditRole
                }),
                components.buttons.negativeButton({
                    text: texts.deleteRole,
                    click: onDeleteRole
                })
            ]);
        }

        function onSaveRole() {
            var name = $roleNameRow.getValue();

            if (!name) {
                $roleNameRow.$input.focus();
                return;
            }

            var saveMe = {
                id: currentRole.id,
                name: name,
                permissions: {
                    getPasswordByEmail: permissionCheckboxes$[0].isChecked(),
                    accessToAdminPages: permissionCheckboxes$[1].isChecked(),
                    useImagesInImageArchive: permissionCheckboxes$[2].isChecked(),
                    changeImagesInImageArchive: permissionCheckboxes$[3].isChecked()
                }
            };

            if (saveMe.id) {
                rolesRestAPI.update(saveMe).success(function (savedRole) {
                    // todo: maybe there is better way to reassign fields' values, not object itself
                    currentRole.id = savedRole.id;
                    $roleRow.text(currentRole.name = savedRole.name);
                    currentRole.permissions.getPasswordByEmail = savedRole.permissions.getPasswordByEmail;
                    currentRole.permissions.accessToAdminPages = savedRole.permissions.accessToAdminPages;
                    currentRole.permissions.useImagesInImageArchive = savedRole.permissions.useImagesInImageArchive;
                    currentRole.permissions.changeImagesInImageArchive = savedRole.permissions.changeImagesInImageArchive;

                    onRoleView = onRoleSimpleView;
                    prepareRoleView();
                });
            } else {
                rolesRestAPI.create(saveMe).success(function (role) {
                    $roleRow = roleToRow.transform((currentRole = role), roleEditor);
                    $container.parent().find('.roles-table').append($roleRow);

                    onRoleView = onRoleSimpleView;
                    prepareRoleView();
                });
            }
        }

        function getOnDiscardChanges(onConfirm) {
            return function () {
                confirmationBuilder.buildModalWindow(texts.discardChangesMessage, function (confirmed) {
                    if (!confirmed) return;
                    onConfirm.call();
                });
            }
        }

        function buildRoleEditButtons() {
            return $roleEditButtons = components.buttons.buttonsContainer('<div>', [
                components.buttons.saveButton({
                    text: texts.saveChanges,
                    click: onSaveRole
                }),
                components.buttons.negativeButton({
                    text: texts.cancel,
                    click: getOnDiscardChanges(function () {
                        onRoleView = onRoleSimpleView;

                        if (currentRole.id) {
                            prepareRoleView();

                        } else {
                            currentRole = null;
                            onEditDelegate = onSimpleEdit;
                            $container.slideUp();
                        }
                    })
                })
            ], {
                style: 'display: none;'
            });
        }

        function prepareRoleView() {
            onEditDelegate = onSimpleEdit;

            $roleRow.parent()
                .find('.roles-table__role-row--active')
                .removeClass('roles-table__role-row--active');

            $roleRow.addClass('roles-table__role-row--active');

            $roleEditButtons.slideUp('fast');
            $roleViewButtons.slideDown('fast');

            $roleNameRow.$input.attr('disabled', 'disabled');
            $roleNameRow.setValue(currentRole.name);

            var permissions = [
                currentRole.permissions.getPasswordByEmail,
                currentRole.permissions.accessToAdminPages,
                currentRole.permissions.useImagesInImageArchive,
                currentRole.permissions.changeImagesInImageArchive
            ];

            permissionCheckboxes$.forEach(function ($checkbox, i) {
                $checkbox.$input.attr('disabled', 'disabled');
                $checkbox.setChecked(permissions[i]);
            });

            $container.css('display', 'inline-block');
        }

        function onRoleSimpleView($roleRowElement, role) {
            if (currentRole && currentRole.id === role.id) return;
            currentRole = role;
            $roleRow = $roleRowElement;

            prepareRoleView();
        }

        var $container;
        var currentRole;
        var $roleRow;
        var onRoleView = onRoleSimpleView;

        function buildContainer() {
            return $container || ($container = new BEM({
                block: 'roles-editor',
                elements: {
                    'role-name-row': buildRoleNameRow(),
                    'role-permissions': buildRolePermissions(),
                    'role-view-buttons': buildRoleViewButtons(),
                    'role-edit-buttons': buildRoleEditButtons()
                }
            }).buildBlockStructure('<div>', {style: 'display: none;'}));
        }

        function viewRole($roleRow, role) {
            $container.slideDown();
            onRoleView($roleRow, role);
        }

        function onSimpleEdit($roleRow, role) {
            viewRole($roleRow, role);
            onEditRole();
        }

        var onEditDelegate = onSimpleEdit;

        function editRole($roleRow, role) {
            onEditDelegate($roleRow, role);
            onEditDelegate = function () {
            }
        }

        var roleEditor = {
            buildContainer: buildContainer,
            viewRole: viewRole,
            editRole: editRole
        };

        return roleEditor;
    }
);