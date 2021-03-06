define("imcms-document-validation-rest-api", ["imcms-rest-api"], function (rest) {
    return {
        checkIsTextDocument: docIdentifier => {
            const url = `/documents/validate/isTextDocument/${docIdentifier}`;
            return rest.ajax.call({url: url, type: "GET", json: false});
        }
    };
});
