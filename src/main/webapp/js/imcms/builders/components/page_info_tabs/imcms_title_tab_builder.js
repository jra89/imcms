Imcms.define("imcms-title-tab-builder",
    [
        "imcms-bem-builder", "imcms-components-builder", "imcms-page-info-tab-form-builder",
        "imcms-choose-image-builder", "imcms-i18n-texts"
    ],
    function (BEM, components, tabFormBuilder, chooseImage, texts) {

        texts = texts.pageInfo.title;

        var pageInfoInnerStructureBEM = new BEM({
            block: "imcms-field",
            elements: {
                "checkboxes": "imcms-checkboxes",
                "text-box": "imcms-text-box",
                "text-area": "imcms-text-area",
                "choose-image": "imcms-choose-image",
                "select": "imcms-select",
                "title": "imcms-title",
                "item": ""
            }
        });

        var tabData = {};

        function buildCommonContents(commonContents) {
            tabData.commonContents = [];

            return commonContents.map(buildDocumentCommonContent).reduce(function (cc1, cc2) {
                return cc1.concat(cc2);
            });
        }

        function buildDocumentCommonContent(commonContent) {
            var $checkbox = components.checkboxes.imcmsCheckbox("<div>", {
                    name: commonContent.language.name.toLowerCase(), // fixme: or native name?
                    text: commonContent.language.name,
                    checked: commonContent.enabled ? "checked" : undefined
                }),
                $checkboxWrapper = components.checkboxes.checkboxContainer("<div>", [$checkbox]),
                $checkboxContainer = pageInfoInnerStructureBEM.buildBlock("<div>", [{"checkboxes": $checkboxWrapper}]),
                $pageTitle = components.texts.textBox("<div>", {
                    name: "title",
                    text: texts.title,
                    value: commonContent.headline
                }),
                $pageTitleContainer = pageInfoInnerStructureBEM.buildBlock("<div>", [{"text-box": $pageTitle}]),
                $menuText = components.texts.textArea("<div>", {
                    text: texts.menuText,
                    html: commonContent.menuText,
                    name: "menu-text"
                }),
                $menuTextContainer = pageInfoInnerStructureBEM.buildBlock("<div>", [{"text-area": $menuText}]),
                $linkToImage = chooseImage.container("<div>", {
                    id: "path-to-image",
                    name: "image",
                    value: commonContent.menuImageURL,
                    placeholder: texts.linkToImagePlaceholder,
                    "label-text": texts.linkToImage,
                    "button-text": texts.chooseImage,
                    click: function (selectedImage) {
                        $linkToImage.setValue(selectedImage ? selectedImage.path : "");
                    }
                }),
                $linkToImageContainer = pageInfoInnerStructureBEM.buildBlock("<div>", [{"choose-image": $linkToImage}]);

            tabData.commonContents = tabData.commonContents || [];

            tabData.commonContents.push({
                name: commonContent.language.name,
                checkbox: $checkbox,
                pageTitle: $pageTitle,
                menuText: $menuText,
                linkToImage: $linkToImage
            });

            return [$checkboxContainer, $pageTitleContainer, $menuTextContainer, $linkToImageContainer];
        }

        function buildSelectTargetForDocumentLink() {
            tabData.$showIn = components.selects.imcmsSelect("<div>", {
                id: "show-in",
                text: texts.showIn,
                name: "show-in"
            }, [{
                text: texts.sameFrame,
                "data-value": "_self"
            }, {
                text: texts.newWindow,
                "data-value": "_blank"
            }, {
                text: texts.replaceAll,
                "data-value": "_top"
            }]);

            return pageInfoInnerStructureBEM.buildBlock("<div>", [{"select": tabData.$showIn}]);
        }

        function buildDocumentAliasBlock() {
            tabData.$documentAlias = components.texts.textBox("<div>", {
                name: "alias",
                text: texts.alias,
                placeholder: texts.aliasPlaceholder
            });

            return pageInfoInnerStructureBEM.buildBlock("<div>", [
                {"text-box": tabData.$documentAlias}
            ]);
        }

        function buildBlockForMissingLangSetting() {
            var $languagesTitle = components.texts.titleText("<div>", texts.missingLangRuleTitle);

            tabData.$showDefaultLang = components.radios.imcmsRadio("<div>", {
                text: texts.showInDefault,
                name: "langSetting",
                value: "SHOW_IN_DEFAULT_LANGUAGE",
                checked: "checked" // default value
            });
            tabData.$doNotShow = components.radios.imcmsRadio("<div>", {
                text: texts.doNotShow,
                name: "langSetting",
                value: "DO_NOT_SHOW"
            });

            return pageInfoInnerStructureBEM.buildBlock("<div>", [
                {"title": $languagesTitle},
                {"item": tabData.$showDefaultLang},
                {"item": tabData.$doNotShow}
            ]);
        }

        return {

            name: texts.name,

            tabIndex: null,

            isDocumentTypeSupported: function () {
                return true; // all supported
            },

            showTab: function () {
                tabFormBuilder.showTab(this.tabIndex);
            },

            hideTab: function () {
                tabFormBuilder.hideTab(this.tabIndex);
            },

            buildTab: function (index) {
                this.tabIndex = index;
                var tabElements = [
                    buildSelectTargetForDocumentLink(),
                    buildDocumentAliasBlock(),
                    buildBlockForMissingLangSetting()
                ];

                return tabData.$result = tabFormBuilder.buildFormBlock(tabElements, index);
            },

            fillTabDataFromDocument: function (document) {
                if (tabData.commonContentElements) {
                    tabData.commonContentElements.forEach(function ($element) {
                        $element.detach();
                    });
                }

                tabData.commonContentElements = buildCommonContents(document.commonContents);
                tabData.$result.prepend(tabData.commonContentElements);
                tabData.$showIn.selectValue(document.target);
                tabData.$documentAlias.setValue(document.alias);

                components.radios.group(tabData.$showDefaultLang, tabData.$doNotShow)
                    .setCheckedValue(document.disabledLanguageShowMode);
            },

            saveData: function (documentDTO) {
                documentDTO.commonContents.forEach(function (docCommonContent) {
                    tabData.commonContents.forEach(function (commonContent) {

                        if (docCommonContent.language.name !== commonContent.name) {
                            // I can't come up with better solution than double forEach
                            return;
                        }

                        docCommonContent.enabled = commonContent.checkbox.isChecked();
                        docCommonContent.headline = commonContent.pageTitle.getValue();
                        docCommonContent.menuText = commonContent.menuText.getValue();
                        docCommonContent.menuImageURL = commonContent.linkToImage.getValue();
                    });
                });

                documentDTO.alias = tabData.$documentAlias.getValue();
                documentDTO.target = tabData.$showIn.getSelectedValue();

                documentDTO.disabledLanguageShowMode = components.radios
                    .group(tabData.$showDefaultLang, tabData.$doNotShow)
                    .getCheckedValue();

                return documentDTO;
            },

            clearTabData: function () {
                var emptyString = '';

                tabData.commonContents.forEach(function (commonContent, index) {
                    commonContent.checkbox.setChecked(index === 0);//check only first
                    commonContent.pageTitle.setValue(emptyString);
                    commonContent.menuText.setValue(emptyString);
                    commonContent.linkToImage.setValue(emptyString);
                });

                tabData.$showIn.selectFirst();
                tabData.$documentAlias.setValue(emptyString);
                tabData.$showDefaultLang.setChecked(true); //default value
            }
        };
    }
);
