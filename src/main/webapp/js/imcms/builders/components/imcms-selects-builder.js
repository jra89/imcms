/**
 * Created by Serhii Maksymchuk from Ubrainians for imCode
 * 25.07.17.
 */
const logger = require('imcms-logger');

define("imcms-selects-builder",
    ["imcms-bem-builder", "imcms-primitives-builder", "imcms-buttons-builder", "jquery", "imcms-checkboxes-builder"],
    function (BEM, primitives, buttons, $, checkboxesBuilder) {

        var SELECT__CLASS = "imcms-select",
            SELECT__CLASS_$ = "." + SELECT__CLASS,
            DROP_DOWN_LIST__CLASS = "imcms-drop-down-list",
            DROP_DOWN_LIST__CLASS_$ = "." + DROP_DOWN_LIST__CLASS,
            DROP_DOWN_LIST__ACTIVE__CLASS = "imcms-select__drop-down-list--active",
            SELECT__DROP_DOWN_LIST__CLASS_$ = ".imcms-select__drop-down-list",
            DROP_DOWN_LIST__ITEMS__CLASS_$ = ".imcms-drop-down-list__items",
            DROP_DOWN_LIST__ITEM__CLASS_$ = ".imcms-drop-down-list__item"
        ;

        var fieldBEM = new BEM({
                block: "imcms-field",
                elements: {
                    "select": SELECT__CLASS
                }
            }),
            selectBEM = new BEM({
                block: SELECT__CLASS,
                elements: {
                    "drop-down-list": DROP_DOWN_LIST__CLASS
                }
            }),
            dropDownListBEM = new BEM({
                block: DROP_DOWN_LIST__CLASS,
                elements: {
                    "select-item": "",
                    "items": "",
                    "item": "",
                    "checkbox": "",
                    "select-item-value": "",
                    "button": "imcms-button"
                }
            })
        ;

        function closeSelect(e) {
            if (!$(e.target).parents(SELECT__CLASS_$).length) {
                $(SELECT__DROP_DOWN_LIST__CLASS_$).removeClass(DROP_DOWN_LIST__ACTIVE__CLASS);
                e.stopPropagation();
            }
        }

        $(document).click(closeSelect);

        function toggleSelect() {
            var $select = $(this).closest(SELECT__CLASS_$);

            if ($select.is('[disabled]')) return;

            $select.find(DROP_DOWN_LIST__CLASS_$).toggleClass(DROP_DOWN_LIST__ACTIVE__CLASS)
        }

        function onOptionSelected(onSelected) {

            var $this = $(this),
                content = $this.text(),
                value = $this.data("value"),
                $select = $this.closest(SELECT__DROP_DOWN_LIST__CLASS_$),
                itemValue = $select.find(".imcms-drop-down-list__select-item-value").html(content)
            ;

            // todo: implement labeling selected item by [selected] attribute

            $select.removeClass(DROP_DOWN_LIST__ACTIVE__CLASS)
                .parent()
                .find("input")
                .data("content", content)
                .val(value);

            onSelected && onSelected.call && onSelected(value);

            return itemValue;
        }

        function mapMultiSelectOptionsToItemsArr(options) {
            return options.map(function (option) {
                return dropDownListBEM.makeBlockElement(
                    "item", checkboxesBuilder.imcmsCheckbox("<div>", option)
                );
            });
        }

        function mapOptionsToItemsArr(options, onSelected) {
            return options.map(function (option) {
                option.click = function () {
                    onOptionSelected.call(this, onSelected)
                };
                return dropDownListBEM.buildBlockElement("item", "<div>", option);
            });
        }

        function addOptionsToExistingDropDown(options, $select, onSelected) {
            return $select.find(DROP_DOWN_LIST__ITEMS__CLASS_$)
                .append(mapOptionsToItemsArr(options, onSelected))
                .end()
                .selectFirst();
        }

        function addMultiSelectOptionsToExistingDropDown(options, $select) {
            return $select.find(DROP_DOWN_LIST__ITEMS__CLASS_$)
                .append(mapMultiSelectOptionsToItemsArr(options));
        }

        function bindGetSelectedValues($select) {
            return function () {
                return $select.find("input:checked")
                    .map(function () {
                        return $(this).val();
                    })
                    .toArray();
            }
        }

        function buildMultiSelectOptions(options) {
            var $itemsContainer = dropDownListBEM.buildElement("items", "<div>").append(
                mapMultiSelectOptionsToItemsArr(options)
            );

            var $button = dropDownListBEM.makeBlockElement("button", buttons.dropDownButton());

            var $selectedValue = dropDownListBEM.buildBlockElement("select-item-value", "<span>", {
                text: "Choose values"
            });
            var $selectItem = dropDownListBEM.buildElement("select-item", "<div>", {click: toggleSelect})
                .append($selectedValue, $button);

            var $dropDownList = dropDownListBEM.buildBlock("<div>", [
                {"select-item": $selectItem},
                {"items": $itemsContainer}
            ]);

            return selectBEM.makeBlockElement("drop-down-list", $dropDownList);
        }

        function buildSelectOptions(options, onSelected) {
            var $itemsContainer = dropDownListBEM.buildElement("items", "<div>").append(
                mapOptionsToItemsArr(options, onSelected)
            );

            var $button = dropDownListBEM.makeBlockElement("button", buttons.dropDownButton());

            var $selectedValue = dropDownListBEM.buildBlockElement("select-item-value", "<span>", {
                text: (options[0] && options[0].text) || "None"
            });
            var $selectItem = dropDownListBEM.buildElement("select-item", "<div>", {click: toggleSelect})
                .append($selectedValue, $button);

            var $dropDownList = dropDownListBEM.buildBlock("<div>", [
                {"select-item": $selectItem},
                {"items": $itemsContainer}
            ]);

            return selectBEM.makeBlockElement("drop-down-list", $dropDownList);
        }

        function bindSelectValue($resultImcmsSelect, $selectedValInput) {
            return function (value) {
                var $selectCandidate = $resultImcmsSelect.find("[data-value='" + value + "']");

                if ($selectCandidate.length) {
                    onOptionSelected.call($selectCandidate, $resultImcmsSelect.onSelected);
                    $selectedValInput && $selectedValInput.val(value);
                    return $resultImcmsSelect;

                } else {
                    logger.log("%c Value '" + value + "' for select doesn't exist", "color: red;");
                    logger.log($resultImcmsSelect[0]);
                }
            }
        }

        function bindSelectFirst($resultImcmsSelect) {
            return function () {
                var $selectCandidate = $resultImcmsSelect.find(DROP_DOWN_LIST__ITEMS__CLASS_$)
                    .find(DROP_DOWN_LIST__ITEM__CLASS_$).first();

                if ($selectCandidate.length) {
                    onOptionSelected.call($selectCandidate, $resultImcmsSelect.onSelected);
                    return $resultImcmsSelect;

                } else {
                    logger.log("%c Select is empty, nothing to choose", "color: red;");
                    logger.log($resultImcmsSelect[0]);
                }
            }
        }

        function bindGetSelect($select) {
            return function () {
                return $select;
            }
        }

        function bindGetSelectedValue($input) {
            return function () {
                return $input.val();
            }
        }

        function bindSelectedText($input) {
            return function () {
                return $input.data("content");
            }
        }

        function bindClearSelect($resultImcmsSelect, $input) {
            return function () {
                $input.val("");
                $input.removeProp("data-content");
                return $resultImcmsSelect.find(DROP_DOWN_LIST__CLASS_$).remove();
            }
        }

        function bindDeleteOption($resultImcmsSelect) {
            return function (optionValue) {
                return $resultImcmsSelect.find("[data-value='" + optionValue + "']").remove();

            }
        }

        function bindHasOptions($resultImcmsSelect) {
            return function () {
                return $resultImcmsSelect.find("[data-value]").length > 0;
            }
        }

        function bindApi($select, $selectedValInput) {
            $select.selectValue = bindSelectValue($select, $selectedValInput);
            $select.selectFirst = bindSelectFirst($select);
            $select.getSelectedValue = bindGetSelectedValue($selectedValInput);
            $select.selectedText = bindSelectedText($selectedValInput);
            $select.clearSelect = bindClearSelect($select, $selectedValInput);
            $select.deleteOption = bindDeleteOption($select);
            $select.hasOptions = bindHasOptions($select);
        }

        function buildSelectLabel(attributes) {
            return primitives.imcmsLabel(attributes.id, attributes.text, {click: toggleSelect});
        }

        return {
            multipleSelect: function (tag, attributes, options) {
                attributes = attributes || {};
                options = options || [];

                var blockElements = [];

                if (attributes.text) {
                    blockElements = [{"label": buildSelectLabel(attributes)}];
                }

                var $selectElements = [];

                if (options && options.length) {
                    $selectElements.push(buildMultiSelectOptions(options));
                }

                var $select = selectBEM.buildBlock(
                    "<div>", blockElements, (attributes["class"] ? {"class": attributes["class"]} : {})
                ).append($selectElements);

                $select.selectValue = bindSelectValue($select);
                $select.getSelectedValues = bindGetSelectedValues($select);

                return $select;
            },
            imcmsSelect: function (tag, attributes, options) {
                attributes = attributes || {};
                options = options || [];

                var blockElements = [];

                if (attributes.text) {
                    blockElements = [{"label": buildSelectLabel(attributes)}];
                }

                var $selectElements = [];

                if (attributes.emptySelect) {
                    options.unshift({
                        text: "None",
                        "data-value": null
                    });
                }

                if (options && options.length) {
                    $selectElements.push(buildSelectOptions(options, attributes.onSelected));
                }

                var $selectedValInput = $("<input>", {
                    type: "hidden",
                    id: attributes.id,
                    name: attributes.name
                });

                $selectElements.push($selectedValInput);

                var $resultImcmsSelect = selectBEM.buildBlock(
                    "<div>", blockElements, (attributes["class"] ? {"class": attributes["class"]} : {})
                ).append($selectElements);

                bindApi($resultImcmsSelect, $selectedValInput);

                return $resultImcmsSelect;
            },
            makeImcmsSelect: function ($existingSelect) {
                $existingSelect.find('.imcms-drop-down-list__select-item').click(toggleSelect);
                $existingSelect.find(DROP_DOWN_LIST__ITEM__CLASS_$).each(function () {
                    $(this).click(onOptionSelected)
                });

                bindApi($existingSelect, $existingSelect.find('input[type=hidden]'));

                return $existingSelect;
            },
            addOptionsToSelect: function (options, $select, onSelected) {
                var selectContainsDropDownList = $select.find(SELECT__DROP_DOWN_LIST__CLASS_$).length;

                return selectContainsDropDownList
                    ? addOptionsToExistingDropDown(options, $select, onSelected)
                    : $select.append(buildSelectOptions(options, onSelected)).selectFirst();
            },
            addOptionsToMultiSelect: function (options, $select) {
                var selectContainsDropDownList = $select.find(SELECT__DROP_DOWN_LIST__CLASS_$).length;

                return selectContainsDropDownList
                    ? addMultiSelectOptionsToExistingDropDown(options, $select)
                    : $select.append(buildMultiSelectOptions(options));
            },
            selectContainer: function (tag, attributes, options) {
                var clas = (attributes && attributes["class"]) || "";

                if (clas) {
                    delete attributes["class"];
                }

                var $select = this.imcmsSelect("<div>", attributes, options),
                    resultContainer = fieldBEM.buildBlock("<div>", [$select], (clas ? {"class": clas} : {}), "select");

                resultContainer.getSelect = bindGetSelect($select);

                return resultContainer;
            }
        }
    }
);
