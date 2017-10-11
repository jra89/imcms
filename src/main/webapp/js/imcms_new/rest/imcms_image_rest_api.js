/**
 * Created by Serhii Maksymchuk from Ubrainians for imCode
 * 05.09.17
 */
Imcms.define("imcms-image-rest-api", ["imcms-rest-api"], function (rest) {
    var api = new rest.API("/image");

    // mock data
    api.read = function (data) {
        var mockData = {
            "1001": [
                {
                    index: 0,
                    name: "img3",
                    path: "img/choose_img/img3.png",
                    format: "PNG",
                    width: 32,
                    height: 29,
                    docId: 1001,
                    langCode: "en",
                    loopEntryRef: null
                }, {
                    index: 1,
                    name: "demo1",
                    path: "demo/img/demo1.jpg",
                    format: "JPG",
                    width: 5509,
                    height: 3673,
                    docId: 1001,
                    langCode: "en",
                    loopEntryRef: null
                }, {
                    index: 2,
                    name: "img2",
                    path: "img/choose_img/img2.png",
                    format: "PNG",
                    width: 147,
                    height: 146,
                    docId: 1001,
                    langCode: "en",
                    loopEntryRef: null
                }, {
                    index: 3,
                    name: "img4",
                    path: "img/choose_img/img4.png",
                    format: "PNG",
                    width: 102,
                    height: 146,
                    docId: 1001,
                    langCode: "en",
                    loopEntryRef: null
                }
            ]
        };

        return {
            done: function (onDone) {
                onDone(mockData[data.docId][+data.index]);
            }
        }
    };

    return api;
});
