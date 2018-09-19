/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 11.09.18
 */
const $ = require('jquery');
const BEM = require('imcms-bem-builder');

let $previewImageArea;
let $previewImgContainer;
let $previewImg;

function getPreviewImage() {
    return $previewImg || ($previewImg = $('<img>', {'class': 'imcms-preview-img'}))
}

function getPreviewImageContainer() {
    return $previewImgContainer || ($previewImgContainer = $('<div>', {
        'class': 'imcms-preview-img-container',
        html: getPreviewImage()
    }))
}

function buildPreviewImageArea() {
    return new BEM({
        block: 'imcms-preview-img-area',
        elements: {
            'container': getPreviewImageContainer(),
        }
    }).buildBlockStructure('<div>');
}

function getPreviewImageArea() {
    return $previewImageArea || ($previewImageArea = buildPreviewImageArea())
}

module.exports = {
    getPreviewImage: getPreviewImage,
    getPreviewImageWrap: getPreviewImageContainer,
    getPreviewImageArea: getPreviewImageArea,
};
