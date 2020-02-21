const $ = require('jquery');
const editableArea = require('imcms-editable-area');
const previewImageArea = require('imcms-preview-image-area');
const croppingArea = require('imcms-cropping-area');

module.exports = {
    build: () => $("<div>").append(
        previewImageArea.getPreviewImageArea(),
        editableArea.getEditableImageArea(),
        croppingArea.getCroppingBlock(),
    )
};
