/**
 * Created by Serhii Maksymchuk from Ubrainians for imCode
 * 26.07.17.
 */
Imcms.define("imcms-choose-image-builder",
    ["imcms-bem-builder", "imcms-texts-builder", "imcms-buttons-builder", "imcms-content-manager-builder"],
    function (BEM, texts, buttons, contentManager) {
        return {
            container: function (tag, attributes) {
                var $textField = texts.textField("<div>", {
                        id: attributes.id,
                        name: attributes.name,
                        text: attributes["label-text"],
                        placeholder: attributes.placeholder
                    }),
                    $result = new BEM({
                        block: "imcms-choose-image",
                        elements: {
                            "text-box": $textField,
                            "button": buttons.neutral("<button>", {
                                text: attributes["button-text"],
                                click: attributes.click || contentManager.build
                            })
                        }
                    }).buildBlockStructure("<div>");

                $result.setValue = $textField.setValue;
                return $result;
            }
        }
    }
);
