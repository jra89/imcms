define("imcms-files-rest-api", ["imcms-rest-api"], function (rest) {

    let url = '/files';
    let api = new rest.API(url);

    api.download = file => rest.ajax.call({url: `${url}/file/${file}`, type: 'GET', json: false});

    api.get = file => rest.ajax.call({url: `${url}/${file}`, type: 'GET', json: false});

    api.upload = formData => rest.ajax.call({
        url: `${url}/upload/`,
        type: "POST",
        contentType: false,
        processData: false
    }, formData);

    api.copy = pathParam => rest.ajax.call({url: `${url}/copy/`, type: 'POST', json: true}, pathParam);

    api.change = content => rest.ajax.call({url: `${url}/${content}`, type: 'PUT', json: false});

    api.move = pathParam => rest.ajax.call({url: `${url}/move/`, type: 'PUT', json: true}, pathParam);

    api.rename = pathParam => rest.ajax.call({url: `${url}/rename/`, type: 'PUT', json: true}, pathParam);

    api.delete = filePath => rest.ajax.call({url: `${url}/${filePath}`, type: 'DELETE', json: false});

    return api;
});