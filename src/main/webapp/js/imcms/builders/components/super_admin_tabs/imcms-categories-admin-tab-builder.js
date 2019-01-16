/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 23.06.18
 */
define(
    'imcms-categories-admin-tab-builder',
    ['imcms-super-admin-tab', 'imcms-i18n-texts', 'imcms-components-builder', 'imcms-field-wrapper',
        'imcms-bem-builder', 'jquery', 'imcms-category-types-rest-api', 'imcms-categories-rest-api',
        'imcms-category-types-editor'],
    function (SuperAdminTab, texts, components, fieldWrapper, BEM, $, typesRestApi, categoriesRestApi, typeEditor) {

        texts = texts.superAdmin.categories;

        let $categoryTypeSelect;
        let createContainer;

        var categoryTypesLoader = {
            categoryType: false,
            callback: [],
            whenCtgTypesLoaded: function (callback) {
                (this.categoryType) ? callback(this.categoryType) : this.callback.push(callback);
            },
            runCallbacks: function (categoryTypes) {
                this.categoryType = categoryTypes;

                this.callback.forEach(function (callback) {
                    callback(categoryTypes)
                })
            }
        };

        typesRestApi.read().done(function (ctgTypes) {
            categoryTypesLoader.runCallbacks(ctgTypes);
        });


        function buildViewCategoriesTypes() {
            $categoryTypeSelect = components.selects.multipleSelect('<div>', {
                text: texts.chooseType
            });

            categoryTypesLoader.whenCtgTypesLoaded(function (ctgTypes) {
                let categoriesTypesDataMapped = ctgTypes.map(function (categoryType) {
                    return {
                        text: categoryType.name,
                        value: categoryType.id
                    }
                });

                components.selects.addOptionsToSelect(categoriesTypesDataMapped, $categoryTypeSelect);
            });

            return $categoryTypeSelect;
        }

        function onCreateNewCategoryType() {

            typeEditor.editCategoryType($('<div>'), {
                id: null,
                name: '',
                singleSelect: '',
                multiSelect: '',
                inherited: false,
                imageArchive: false

            });
        }

        function create() {
            createContainer = typeEditor.buildCategoryTypeCreateContainer();
            return createContainer;
        }

        function buildCategoryTypeButtonsContainer() {

            function clickOn() {
                return createContainer.css('display', 'inline-block').slideDown();
            }

            function buildCategoryTypeCreateButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.createButtonName,
                    click: clickOn
                });
                return components.buttons.buttonsContainer('<div>', [$button]);
            }

            function buildCategoryTypeEditButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.editButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }


            function buildCategoryTypeRemoveButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.removeButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }

            return new BEM({
                block: 'type-buttons-block',
                elements: {
                    'title': $('<div>', {text: texts.titleCategoryType}),
                    'create': buildCategoryTypeCreateButton(),
                    'edit': buildCategoryTypeEditButton(),
                    'remove': buildCategoryTypeRemoveButton()
                }
            }).buildBlockStructure('<div>');

        }

        function buildCategoryButtonsContainer() {

            function buildCategoryCreateButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.createButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }

            function buildCategoryEditButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.editButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }


            function buildCategoryRemoveButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.removeButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }


            function buildCategoryViewButton() {
                let $button = components.buttons.positiveButton({
                    text: texts.viewButtonName,
                    click: function () {

                    }
                });

                return components.buttons.buttonsContainer('<div>', [$button]);
            }

            return new BEM({
                block: 'category-buttons-block',
                elements: {
                    'title': $('<div>', {text: texts.titleCategory}),
                    'create': buildCategoryCreateButton(),
                    'edit': buildCategoryEditButton(),
                    'remove': buildCategoryRemoveButton(),
                    'view': buildCategoryViewButton()
                }
            }).buildBlockStructure('<div>');
        }

        return new SuperAdminTab(texts.name, [
            buildCategoryTypeButtonsContainer(),
            buildCategoryButtonsContainer(),
            buildViewCategoriesTypes(),
            create()
            //buildCategoryTypeCreateContainer()
        ]);
    }
);
