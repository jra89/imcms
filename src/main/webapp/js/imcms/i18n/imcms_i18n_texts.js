/**
 * Module holds texts used in UI in all available languages.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 09.02.18
 */
Imcms.define("imcms-i18n-texts", ["imcms"], function (imcms) {

    var texts = {
        "sv": {
            panel: {
                public: "offentlig",
                edit: "redigera",
                preview: "förhandsvisning",
                publish: "publicera offline",
                pageInfo: "sida info",
                document: "dokumentera",
                admin: "administration",
                logout: "logga ut"
            },
            pageInfo: {
                document: "Dokumentera",
                newDocument: "Nytt dokument",
                confirmMessage: "Spara ändringar?",
                title: {
                    name: "Titel",
                    title: "Titel",
                    menuText: "Titel",
                    linkToImage: "Meny text",
                    linkToImagePlaceholder: "Länk till bild",
                    chooseImage: "Bildväg",
                    showIn: "välja...",
                    sameFrame: "Visa in",
                    newWindow: "Samma ram",
                    replaceAll: "Nytt fönster",
                    alias: "Ersätt alla",
                    aliasPlaceholder: "Dokument alias",
                    missingLangRule: "detta-dokument-alias",
                    showInDefault: "Om det begärda språket saknas:",
                    doNotShow: "Visa i standardspråk om det är aktiverat"
                },
                lifeCycle: {
                    name: "Livscykel",
                    status: {
                        title: "Status",
                        inProcess: "Pågående",
                        disapproved: "Ej godkänd",
                        approved: "Godkänd"
                    },
                    now: "Nu",
                    clear: "Klar",
                    published: {
                        title: "Publicerat",
                        dateTitle: "Ange publicerat datum",
                        timeTitle: "Ange publicerad tid",
                        dateTimeTitle: "Sparade datum och tid för publicering"
                    },
                    archived: {
                        title: "Arkiverat ",
                        dateTitle: "Ange arkiverat datum",
                        timeTitle: "Ange arkiverad tid",
                        dateTimeTitle: "Sparade arkiverad datum-tid"
                    },
                    publicationEnd: {
                        title: "Publiceringsänden",
                        dateTitle: "Ange publikationsslut datum",
                        timeTitle: "Ange publikationsslut tid",
                        dateTimeTitle: "Sparat slutet av publikationen datum-tid"
                    },
                    publisher: "Utgivare",
                    currentVersion: "Aktuell version:",
                    versionHasChanges: "Denna offline version har ändringar.",
                    publishMessage: "Vänligen tryck \"Spara och publicera den här versionen\" för att publicera som: version"
                },
                appearance: {
                    name: "Utseende",
                    template: "Mall",
                    defaultChildTemplate: "Standard barnmall"
                },
                keywords: {
                    name: "Nyckelord",
                    title: "Nyckelord",
                    placeholder: "nyckelord",
                    add: "TILLÄGGA+",
                    disableSearch: "Inaktivera sökningen"
                },
                categories: {
                    name: "Kategorier"
                },
                access: {
                    name: "Tillgång",
                    role: "roll",
                    view: "se",
                    edit: "redigera",
                    restricted_1: "begränsad 1",
                    restricted_2: "begränsad 2",
                    addRole: "Lägg till roll"
                },
                permissions: {
                    name: "Tillstånd",
                    editText: "Redigera text",
                    editMenu: "Redigera meny",
                    editImage: "Redigera bild",
                    editLoop: "Redigera loop",
                    editDocInfo: "Redigera dok info"
                },
                status: {
                    name: "Status",
                    created: "Skapad",
                    modified: "Ändrad",
                    archived: "Arkiverat",
                    published: "Publicerat",
                    publicationEnd: "Publiceringsänden",
                    by: "Av"
                },
                file: {
                    name: "Files",
                    upload: "Ladda upp",
                    id: "ID",
                    fileName: "Namn",
                    isDefault: "Standard"
                },
                url: {
                    name: "URL",
                    title: "URL"
                },
                buttons: {
                    ok: "ok",
                    cancel: "annullera",
                    saveAndPublish: "spara och publicera den här versionen"
                }
            },
            modal: {
                title: "Bekräftelse",
                yes: "Ja",
                no: "Nej"
            },
            editors: {
                document: {
                    title: "Dokumenthanterare",
                    newDoc: "Ny",
                    freeText: "Fri text",
                    freeTextPlaceholder: "Skriv för att hitta dokument",
                    owner: "Ägare",
                    category: "Kategori",
                    sort: {
                        id: "ID",
                        title: "Titel",
                        alias: "Alias",
                        type: "Typ",
                        status: "Status"
                    },
                    status: {
                        published: "Godkänd, publicerad",
                        publishedWaiting: "Godkänd, väntar",
                        inProcess: "Ny",
                        disapproved: "Ej godkänd",
                        archived: "Arkiverat",
                        passed: "Publiceringsänden"
                    }
                },
                newDocument: {
                    title: "Skapa nytt dokument",
                    textDoc: "Textdokument",
                    fileDoc: "File Document",
                    urlDoc: "URL-dokument"
                },
                newDocumentProfile: {
                    title: "Skapa nytt dokument",
                    createDocButton: "Skapa",
                    chooseProfileOrParent: "Vilka inställningar ska användas för att skapa det nya dokumentet:",
                    validationErrorMessage: "Du måste välja antingen befintlig profil eller ange TEXT-dokumentidentifierare (meta-id eller alias)",
                    buildByProfile: "Välj efter profil",
                    buildByParent: "Alla textdokument ID",
                    buildByCurrentDocId: "Detta dokument är",
                    selectProfile: "Välj profil",
                    selectParent: "Välj förälder"
                },
                menu: {
                    newDoc: "Ny",
                    title: "Menyredigerare",
                    id: "ID",
                    docTitle: "Titel",
                    status: "Status",
                    removeConfirmation: "Vill du ta bort menyalternativet \"",
                    saveAndClose: "Spara och stäng"
                },
                loop: {
                    title: "Loop Editor",
                    createNew: "Skapa ny",
                    saveAndClose: "Spara och stäng",
                    id: "ID",
                    content: "textinnehåll",
                    isEnabled: "är aktiverad"
                },
                content: {
                    title: "Content Manager",
                    hideFolders: "Dölj mappar",
                    showFolders: "Visa mappar",
                    upload: "Ladda upp",
                    saveAndClose: "Spara och stäng",
                    removeFolderMessage: "Vill du ta bort mapp \"",
                    removeImageConfirm: "Vill du ta bort bilden ",
                    newFolderName: "Nytt mappnamn",
                    add: "TILLÄGGA+"
                },
                image: {
                    title: "Bildredigerare",
                    panels: {
                        bottom: {
                            hide: "Dölj bottenpanelen",
                            show: "Visa bottenpanelen"
                        },
                        right: {
                            hide: "Dölj höger panel",
                            show: "Visa rätt panel"
                        }
                    },
                    align: {
                        none: "Justera är inte angivet.",
                        center: "Justera tillämpas endast om det finns en bild i textredigeraren och det krävs att ändringar i textredigeraren sparas, 'imcms-image-align-center' klass läggs till för vanlig imcms bildtagare så utvecklaren ska hantera den.",
                        left: "Justera tillämpas endast om det finns en bild i textredigeraren och det krävs att ändringar i textredigeraren sparas, 'imcms-image-align-left' klass läggs till för vanlig imcms bildtagare så utvecklaren ska hantera den.",
                        right: "Justera tillämpas endast om det finns en bild i textredigeraren och det krävs att ändringar i textredigeraren sparas, 'imcms-image-align-right' klass läggs till för vanlig imcms bildtagare så utvecklaren ska hantera den."
                    },
                    proportionsButtonTitle: "Aktivera/inaktivera spara bildandelar",
                    displaySize: "Skärmstorlek",
                    height: "Höjd",
                    width: "Bredd",
                    preview: "Förhandsvisning",
                    original: "Original",
                    selectImage: "Välj bild",
                    altText: "Alt text",
                    imageLink: "Bildlänk",
                    allLangs: "Alla språk",
                    advanced: "Avancerad",
                    simple: "Enkel",
                    none: "Ingen",
                    top: "topp",
                    right: "höger",
                    bottom: "botten",
                    left: "vänster",
                    spaceAround: "Space around image",
                    cropCoords: "Crop Coordinates",
                    fileFormat: "Filformat",
                    alignment: "Textjustering",
                    altTextConfirm: "Alternativ text saknas. Är du säker på att fortsätta?",
                    removeAndClose: "Ta bort och stäng",
                    saveAndClose: "Spara och stäng",
                    exif: {
                        button: "Visa exif"
                        // todo: implement rest part
                    }
                }
            },
            textHistory: {
                title: "Texthistorik",
                cancel: "Annullera",
                writeToText: "Skriv till textfält",
                viewSource: "Visa källa",
                viewText: "Visa text"
            },
            textValidation: {
                title: "Valideringsresultat",
                ok: "OK",
                output: "Valideringsutgång: ",
                errors: " Fel",
                warnings: " Varningar"
            }
        },
        "en": {
            panel: {
                public: 'public',
                edit: 'edit',
                preview: "preview",
                publish: "publish offline",
                pageInfo: "page info",
                document: "document",
                admin: "admin",
                logout: "log out"
            },
            pageInfo: {
                document: "Document",
                newDocument: "New Document",
                confirmMessage: "Save changes?",
                title: {
                    name: "Title",
                    title: "Title",
                    menuText: "Menu text",
                    linkToImage: "Link to image",
                    linkToImagePlaceholder: "Image path",
                    chooseImage: "choose...",
                    showIn: "Show in",
                    sameFrame: "Same frame",
                    newWindow: "New window",
                    replaceAll: "Replace all",
                    alias: "Document alias",
                    aliasPlaceholder: "this-doc-alias",
                    missingLangRuleTitle: "If requested language is missing:",
                    showInDefault: "Show in default language if enabled",
                    doNotShow: "Don't show at all"
                },
                lifeCycle: {
                    name: "Life Cycle",
                    status: {
                        title: "Status",
                        inProcess: "In Process",
                        disapproved: "Disapproved",
                        approved: "Approved"
                    },
                    now: "Now",
                    clear: "Clear",
                    published: {
                        title: "Published",
                        dateTitle: "Set published date",
                        timeTitle: "Set published time",
                        dateTimeTitle: "Saved publish date-time"
                    },
                    archived: {
                        title: "Archived",
                        dateTitle: "Set archived date",
                        timeTitle: "Set archived time",
                        dateTimeTitle: "Saved archived date-time"
                    },
                    publicationEnd: {
                        title: "Publication end",
                        dateTitle: "Set publication end date",
                        timeTitle: "Set publication end time",
                        dateTimeTitle: "Saved publication end date-time"
                    },
                    publisher: "Publisher",
                    currentVersion: "Current version:",
                    versionHasChanges: "This offline version has changes.",
                    publishMessage: "Please press \"Save and publish this version\" to publish as: version "
                },
                appearance: {
                    name: "Appearance",
                    template: "Template",
                    defaultChildTemplate: "Default child template"
                },
                keywords: {
                    name: "Keywords",
                    title: "Keywords",
                    placeholder: "keyword",
                    add: "ADD+",
                    disableSearch: "Disable search"
                },
                categories: {
                    name: "Categories"
                },
                access: {
                    name: "Access",
                    role: "role",
                    view: "view",
                    edit: "edit",
                    restricted_1: "restricted 1",
                    restricted_2: "restricted 2",
                    addRole: "Add role"
                },
                permissions: {
                    name: "Permissions",
                    editText: "Edit text",
                    editMenu: "Edit menu",
                    editImage: "Edit image",
                    editLoop: "Edit loop",
                    editDocInfo: "Edit doc info"
                },
                status: {
                    name: "Status",
                    created: "Created",
                    modified: "Modified",
                    archived: "Archived",
                    published: "Published",
                    publicationEnd: "Publication end",
                    by: "By"
                },
                file: {
                    name: "Files",
                    upload: "Upload",
                    id: "ID",
                    fileName: "Name",
                    isDefault: "Default"
                },
                url: {
                    name: "URL",
                    title: "URL"
                },
                buttons: {
                    ok: "ok",
                    cancel: "cancel",
                    saveAndPublish: "save and publish this version"
                }
            },
            modal: {
                title: "Confirmation",
                yes: "Yes",
                no: "No"
            },
            editors: {
                document: {
                    title: "Document Manager",
                    newDoc: "New",
                    freeText: "Free text",
                    freeTextPlaceholder: "Type to find document",
                    owner: "Owner",
                    category: "Category",
                    sort: {
                        id: "ID",
                        title: "Title",
                        alias: "Alias",
                        type: "Type",
                        status: "Status"
                    },
                    status: {
                        published: "Approved, published",
                        publishedWaiting: "Approved, waiting",
                        inProcess: "New",
                        disapproved: "Disapproved",
                        archived: "Archived",
                        passed: "Publication end"
                    }
                },
                newDocument: {
                    title: "Create new document",
                    textDoc: "Text Document",
                    fileDoc: "File Document",
                    urlDoc: "URL document"
                },
                newDocumentProfile: {
                    title: "Create new document",
                    createDocButton: "Create",
                    chooseProfileOrParent: "What settings are to be used to create the new document:",
                    validationErrorMessage: "You have to choose either existing profile or specify TEXT document identifier (meta id or alias)",
                    buildByProfile: "Select by profile",
                    buildByParent: "Any text document id",
                    buildByCurrentDocId: "This document’s",
                    selectProfile: "Select profile",
                    selectParent: "Select parent"
                },
                menu: {
                    newDoc: "New",
                    title: "Menu Editor",
                    id: "ID",
                    docTitle: "Title",
                    status: "Status",
                    removeConfirmation: "Do you want to remove menu item \"",
                    saveAndClose: "Save and close"
                },
                loop: {
                    title: "Loop Editor",
                    createNew: "Create New",
                    saveAndClose: "Save and close",
                    id: "ID",
                    content: "text content",
                    isEnabled: "is enabled"
                },
                content: {
                    title: "Content manager",
                    hideFolders: "Hide folders",
                    showFolders: "Show folders",
                    upload: "Upload",
                    saveAndClose: "Save and close",
                    removeFolderMessage: "Do you want to remove folder \"",
                    removeImageConfirm: "Do you want to remove image ",
                    newFolderName: "New folder name",
                    add: "ADD+"
                },
                image: {
                    title: "Image Editor",
                    panels: {
                        bottom: {
                            hide: "Hide bottom panel",
                            show: "Show bottom panel"
                        },
                        right: {
                            hide: "Hide right panel",
                            show: "Show right panel"
                        }
                    },
                    align: {
                        none: "Align is not specified.",
                        center: "Align is applied only in case of image in text editor and saving changes in text editor is required, 'imcms-image-align-center' class is added for regular imcms image tag so developer should handle with it.",
                        left: "Align is applied only in case of image in text editor and saving changes in text editor is required, 'imcms-image-align-left' class is added for regular imcms image tag so developer should handle with it.",
                        right: "Align is applied only in case of image in text editor and saving changes in text editor is required, 'imcms-image-align-right' class is added for regular imcms image tag so developer should handle with it."
                    },
                    proportionsButtonTitle: "Enable/disable saving image proportions",
                    displaySize: "Display size",
                    height: "Height",
                    width: "Width",
                    preview: "Preview",
                    original: "Original",
                    selectImage: "Select Image",
                    altText: "Alt text",
                    imageLink: "Image link",
                    allLangs: "All languages",
                    advanced: "Advanced",
                    simple: "Simple",
                    none: "None",
                    top: "top",
                    right: "right",
                    bottom: "bottom",
                    left: "left",
                    spaceAround: "Space around image",
                    cropCoords: "Crop Coordinates",
                    fileFormat: "File format",
                    alignment: "Text alignment",
                    altTextConfirm: "Alternate text is missing. Are you sure to continue?",
                    removeAndClose: "Remove and close",
                    saveAndClose: "Save and close",
                    exif: {
                        button: "Show exif"
                        // todo: implement rest part
                    }
                }
            },
            textHistory: {
                title: "Text history",
                cancel: "Cancel",
                writeToText: "Write to text field",
                viewSource: "View Source",
                viewText: "View Text"
            },
            textValidation: {
                title: "Validation Result",
                ok: "OK",
                output: "Validation Output: ",
                errors: " Errors",
                warnings: " Warnings"
            }
        }
    };

    return texts[imcms.userLanguage];
});
