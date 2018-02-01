/**
 * Text history window builder in text editor.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 29.01.18
 */
Imcms.define("imcms-text-history-window-builder",
    [
        "imcms-window-builder", "imcms-bem-builder", "imcms-components-builder", "jquery",
        "imcms-texts-history-rest-api", "tinyMCE"
    ],
    function (WindowBuilder, BEM, components, $, textsHistoryRestAPI, tinyMCE) {

        var $historyListContainer, $textHistoryView;

        function onWriteToTextField() {
            var textButton = textHistoryWindowBuilder.$editor.find(".view-text-button")[0];
            viewText.call(textButton);

            tinyMCE.activeEditor.setContent($textHistoryView.html());
            tinyMCE.activeEditor.setDirty(true);
            textHistoryWindowBuilder.closeWindow();
        }

        function onCancel() {
            textHistoryWindowBuilder.closeWindow();
        }

        function buildFooter() {
            return textHistoryWindowBuilder.buildFooter([
                components.buttons.negativeButton({
                    text: "Cancel",
                    "class": "imcms-text-history-cancel",
                    click: onCancel
                }),
                components.buttons.saveButton({
                    text: "Write to text field",
                    "style": "display: none;",
                    click: onWriteToTextField
                })
            ]);
        }

        function isClickAllowed(element) {
            var $element = $(element);

            if ($element.hasClass("imcms-button--disabled")) {
                return false;
            }

            textHistoryWindowBuilder.$editor.find(".imcms-button--disabled")
                .removeClass("imcms-button--disabled");

            $element.addClass("imcms-button--disabled");

            return true;
        }

        function viewSource() {
            isClickAllowed(this) && $textHistoryView.text($textHistoryView.html());
        }

        function viewText() {
            isClickAllowed(this) && $textHistoryView.html($textHistoryView.text());
        }

        function buildTextHistoryControlButtons() {
            return components.buttons.buttonsContainer("<div>", [
                components.buttons.negativeButton({
                    "class": "view-source-button",
                    "style": "display: none;",
                    text: "View Source",
                    click: viewSource
                }),
                components.buttons.negativeButton({
                    "class": "view-text-button",
                    "style": "display: none;",
                    text: "View Text",
                    click: viewText
                })
            ]);
        }

        function buildHistoryView() {
            return new BEM({
                block: "imcms-right-side",
                elements: {
                    "text-history-view": $textHistoryView = $("<div>"),
                    "buttons": buildTextHistoryControlButtons()
                }
            }).buildBlockStructure("<div>");
        }

        function buildHistoryListContainer() {
            return $("<div>", {"class": "imcms-left-side text-history-list"});
        }

        function buildTextHistory() {
            return new BEM({
                block: "imcms-pop-up-modal",
                elements: {
                    "head": textHistoryWindowBuilder.buildHead("Text history"),
                    "left-side": $historyListContainer = buildHistoryListContainer(),
                    "right-side": buildHistoryView(),
                    "footer": buildFooter()
                }
            }).buildBlockStructure("<div>", {"class": "text-history"});
        }

        function buildTextHistoriesForDateContainer(date) {
            var $date = $("<div>", {
                "class": "text-history-date",
                text: date
            });

            var $separator = $("<div>", {"class": "text-history-date-separator"});

            return new BEM({
                block: "text-history-date-container",
                elements: {
                    "date": $date,
                    "separator": $separator
                }
            }).buildBlockStructure("<div>");
        }

        function showTextHistoryUnit(text) {
            $textHistoryView.html(text);
        }

        function onTextHistoryUnitClicked(unit) {
            $(".text-history-unit").removeClass("text-history-date-unit__unit--active");
            $(unit).addClass("text-history-date-unit__unit--active");

            textHistoryWindowBuilder.$editor.find(".imcms-buttons")
                .find(".imcms-button")
                .css("display", "block");

            textHistoryWindowBuilder.$editor.find(".imcms-button--disabled")
                .removeClass("imcms-button--disabled");

            textHistoryWindowBuilder.$editor.find(".view-text-button")
                .addClass("imcms-button--disabled");
        }

        function buildTextHistoriesForDate(textHistoriesForDate) {
            return textHistoriesForDate.map(function (textHistory) {
                var $textHistoryUnit = $("<div>", {
                    "class": "text-history-unit",
                    text: textHistory.modified.time + " | " + textHistory.modifiedBy.username,
                    click: function () {
                        onTextHistoryUnitClicked(this);
                        showTextHistoryUnit(textHistory.text);
                    }
                });

                return {"unit": $textHistoryUnit};
            });
        }

        function buildTextHistoryUnit(date, textHistoriesForDate) {
            var elements = [{"date": buildTextHistoriesForDateContainer(date)}];

            new BEM({
                block: "text-history-date-unit",
                elements: elements.concat(buildTextHistoriesForDate(textHistoriesForDate))
            }).buildBlockStructure("<div>").appendTo($historyListContainer);
        }

        function loadData(textDTO) {
            textsHistoryRestAPI.read(textDTO).done(function (textsHistory) {
                var dateToTextHistoryUnits = {};

                textsHistory.forEach(function (textHistory) {
                    var date = textHistory.modified.date;
                    dateToTextHistoryUnits[date] = (dateToTextHistoryUnits[date] || []);
                    dateToTextHistoryUnits[date].push(textHistory);
                });

                $.each(dateToTextHistoryUnits, buildTextHistoryUnit);

                $historyListContainer.find(".text-history-date-unit__unit")
                    .first()
                    .click();
            });
        }

        function clearData() {
            $historyListContainer.empty();
            $textHistoryView.html('');
            textHistoryWindowBuilder.$editor.find(".imcms-buttons")
                .find(".imcms-button")
                .not(".imcms-text-history-cancel")
                .css("display", "none");
        }

        var textHistoryWindowBuilder = new WindowBuilder({
            factory: buildTextHistory,
            loadDataStrategy: loadData,
            clearDataStrategy: clearData
        });

        return {
            buildTextHistory: function (textData) {
                textHistoryWindowBuilder.buildWindowWithShadow.applyAsync(arguments, textHistoryWindowBuilder);
            }
        };
    }
);
