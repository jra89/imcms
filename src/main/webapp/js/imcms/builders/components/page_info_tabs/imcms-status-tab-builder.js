define("imcms-status-tab-builder",
    [
        "imcms-bem-builder", "imcms-components-builder", "imcms-i18n-texts", "jquery", "imcms-page-info-tab"
    ],
    function (BEM, components, texts, $, PageInfoTab) {

        texts = texts.pageInfo.status;

        var tabData = {};

        function buildRowBlock($dateTimeField, $doneByInput) {
            $dateTimeField.modifiers = ["col-3", "float-l"];

            var $doneBy = buildLabelWithInputs(texts.by, [$doneByInput]);
            $doneBy.modifiers = ["col-2-3", "float-l"];

            return new BEM({
                block: "imcms-field",
                elements: {
                    "item": [$dateTimeField, $doneBy]
                }
            }).buildBlockStructure("<div>");
        }

        function buildLabelWithInputs(title, inputFields) {
            var $label = $("<div>", {
                "class": "imcms-label",
                text: title
            });

            var inputs = inputFields.map($input => {
                $input.modifiers = ["float-l"];
                return $input;
            });

            return new BEM({
                block: "imcms-item",
                elements: {
                    "label": $label,
                    "input": inputs
                }
            }).buildBlockStructure("<div>");
        }

        function saveStatusInfoRowData(rowName, $dateBlock, $timeBlock, $doneByBlock) {
            tabData["$" + rowName + "Date"] = $dateBlock;
            tabData["$" + rowName + "Time"] = $timeBlock;
            tabData["$" + rowName + "By"] = $doneByBlock;
        }

        function buildStatusInfoRow(statusTab) {
            var $dateBlock = components.dateTime.dateBoxReadOnly({id: statusTab.dataTitle + "Date"});
            var $timeBlock = components.dateTime.timeBoxReadOnly({id: statusTab.dataTitle + "Time"});
            var $doneByBlock = components.texts.textBox("<div>", {
                id: statusTab.dataTitle + "By",
                readonly: "readonly"
            });

            saveStatusInfoRowData(statusTab.dataTitle, $dateBlock, $timeBlock, $doneByBlock);

            var $dateTimeField = buildLabelWithInputs(statusTab.title, [$dateBlock, $timeBlock]);
            return buildRowBlock($dateTimeField, $doneByBlock);
        }

        function setStatusInfoRowDataFromDocument(rowName, document) {
            setStatusInfoRowData(rowName, document[rowName].date, document[rowName].time, document[rowName].by);
        }

        function setStatusInfoRowData(rowName, date, time, by) {
            tabData["$" + rowName + "Date"].setDate(date);
            tabData["$" + rowName + "Time"].setTime(time);
            tabData["$" + rowName + "By"].setValue(by);
        }

        var statusRows = [
            {title: texts.created, dataTitle: "created"},
            {title: texts.modified, dataTitle: "modified"},
            {title: texts.archived, dataTitle: "archived"},
            {title: texts.published, dataTitle: "published"},
            {title: texts.publicationEnd, dataTitle: "publicationEnd"}
        ];

        var StatusTab = function (name) {
            PageInfoTab.call(this, name);
        };

        StatusTab.prototype = Object.create(PageInfoTab.prototype);

        StatusTab.prototype.isDocumentTypeSupported = () => {
            return true; // all supported
        };

        StatusTab.prototype.tabElementsFactory = () => statusRows.map(buildStatusInfoRow);

        StatusTab.prototype.fillTabDataFromDocument = document => {
            statusRows.forEach(statusTab => {
                setStatusInfoRowDataFromDocument(statusTab.dataTitle, document);
            });
        };

        StatusTab.prototype.clearTabData = () => {
            var emptyString = '';
            statusRows.forEach(statusTab => {
                setStatusInfoRowData(statusTab.dataTitle, emptyString, emptyString, emptyString);
            });
        };

        return new StatusTab(texts.name);
    }
);
