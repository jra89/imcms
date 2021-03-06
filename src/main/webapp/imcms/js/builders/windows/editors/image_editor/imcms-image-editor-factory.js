const rightSideBuilder = require('imcms-image-editor-right-side-builder');
const leftSideBuilder = require('imcms-image-editor-left-side-builder');
const $ = require('jquery');
const texts = require('imcms-i18n-texts').editors.image;
const bodyHeadBuilder = require('imcms-image-editor-body-head-builder');
const BEM = require('imcms-bem-builder');
const components = require('imcms-components-builder');


const $imageLinkInfo = $('<a>', {
    id: 'data-link-image'
});

const $infoData = $('<div>', {
    'class': 'common-info-image'
});

const $imageLinkContainerInfo = new BEM({
    block: 'image-editor-info',
    elements: {
        'icon-link': $('<a>', {
            html: components.controls.permalink(),
            target: '_blank'
        }),
        'data-link': $('<div>', {
            html: $imageLinkInfo
        })
    }
}).buildBlockStructure('<div>');

module.exports = {
    buildEditor: opts => {
        const $rightSidePanel = rightSideBuilder.build(opts);
        const $leftSide = leftSideBuilder.build();
        const $bodyHead = bodyHeadBuilder.build($rightSidePanel, opts.imageData);
        const $head = opts.imageWindowBuilder.buildHead();
        //need for get data after build and data in the $infoData, in another way fix build this..
        $head.find('.imcms-title').append($infoData).append($imageLinkContainerInfo);

        return new BEM({
            block: "imcms-image_editor",
            elements: {
                "head": $head,
                "image-characteristics": $bodyHead,
                "left-side": $leftSide,
                "right-side": $rightSidePanel
            }
        }).buildBlockStructure("<div>", {"class": "imcms-editor-window"});
    },
    updateImageData: ($tag, imageData) => {
        const labelText = $tag.find('.imcms-editor-area__text-label').text();
        let linkData;

        $infoData.text(`${texts.title} - ${texts.page} ${$tag.attr('data-doc-id')}, 
        ${texts.imageName}${$tag.attr('data-index')} - 
        ${texts.teaser} ${labelText}`);

        if ($tag.attr('data-loop-index')) {
            linkData = '/api/admin/image?meta-id=' + $tag.attr('data-doc-id')
                + '&index=' + $tag.attr('data-index')
                + '&loop-index=' + $tag.attr('data-loop-index')
                + '&loop-entry-index=' + $tag.attr('data-loop-entry-index');
        } else {
            linkData = '/api/admin/image?meta-id='
                + $tag.attr('data-doc-id')
                + '&index=' + $tag.attr('data-index');
        }

        $imageLinkInfo.text(linkData);
        $imageLinkInfo.attr('href', linkData);
        $('.imcms-image_editor').find('.image-editor-info__icon-link').attr('href', linkData);
        rightSideBuilder.updateImageData($tag, imageData);
    }
};
