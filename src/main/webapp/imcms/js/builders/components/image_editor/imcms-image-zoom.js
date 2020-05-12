define(
    'imcms-image-zoom',
    [
        'jquery', 'imcms-originally-image', 'imcms-originally-area', 'imcms-preview-image-area', 'imcms-i18n-texts',
        'imcms-components-builder', 'check-browser'
    ],
    function ($, originalImage, originallyImageArea, previewImageArea, i18nTexts,
              components, checkerBrowser) {

        const texts = i18nTexts.editors.image;

        let $zoomGradeField;

        function buildZoomGradeField() {
            $zoomGradeField = $('<div>', {
                class: 'percentage-image-info imcms-input imcms-number-box imcms-number-box__input',
            });
            components.overlays.defaultTooltip($zoomGradeField, texts.zoomGrade);

            return $zoomGradeField;
        }

        function updateZoomGradeValue() {
            const isPreview = isPreviewTab();
            const $image = isPreview ? previewImageArea.getPreviewImage() : originalImage.getImage();
            const currentZoom = parseFloat($image.css('zoom'));

            updateZoomGradeValueByCssProperty(currentZoom);
        }

        function updateZoomGradeValueByCssProperty(cssZoomProperty) {
            const percentValue = Number((cssZoomProperty * 100).toFixed(1));
            $zoomGradeField.text(`${percentValue}%`);
        }

        function isPreviewTab() {
            return $('.imcms-editable-img-control-tabs__tab--active').data('tab') === 'prev';
        }

        function fitImage() {
            const isPreview = isPreviewTab();

            const $imageArea = isPreview ? previewImageArea.getPreviewImageArea() : originallyImageArea.getOriginalImageArea();
            const $image = isPreview ? previewImageArea.getPreviewImage() : originalImage.getImage();

            const currentZoom = parseFloat($image.css('zoom'));
            const imageBorderWidth = parseInt($image.css('border-width'));

            const imageWidth = ($image.width() + imageBorderWidth * 2) * currentZoom;
            const imageHeight = ($image.height() + imageBorderWidth * 2) * currentZoom;

            if (imageWidth < $imageArea.width() && imageHeight < $imageArea.height()) {
                return;
            }

            const widthScale = imageWidth / $imageArea.width();
            const heightScale = imageHeight / $imageArea.height();

            const zoomScale = widthScale > heightScale ? (1 / widthScale) : (1 / heightScale);
            const newZoomValue = currentZoom * zoomScale;

            $image.css('zoom', newZoomValue);
            updateZoomGradeValueByCssProperty(newZoomValue);
        }

        function getRelativeZoomValueByOriginalImg() {
            const $imageArea = originallyImageArea.getOriginalImageArea();
            const $image = originalImage.getImage();

            const currentZoom = parseFloat($image.css('zoom'));
            const imageBorderWidth = parseInt($image.css('border-width'));

            const imageWidth = ($image.width() + imageBorderWidth * 2) * currentZoom;
            const imageHeight = ($image.height() + imageBorderWidth * 2) * currentZoom;

            if (imageWidth < $imageArea.width() && imageHeight < $imageArea.height()) {
                return 1;
            }

            const widthScale = imageWidth / $imageArea.width();
            const heightScale = imageHeight / $imageArea.height();

            const zoomScale = widthScale > heightScale ? (1 / widthScale) : (1 / heightScale);
            const newZoomValue = currentZoom * zoomScale;

            updateZoomGradeValueByCssProperty(newZoomValue);
            return newZoomValue;
        }

        function zoom(scale) {
            const isPreview = isPreviewTab();
            const $image = isPreview ? previewImageArea.getPreviewImage() : originalImage.getImage();
            const isFireFox = checkerBrowser.isFirefox()

            if (!scale) {
                if (isFireFox) {
                    $image.css('transform', `scale(1)`);
                } else {
                    $image.css('zoom', 1);
                }
                updateZoomGradeValueByCssProperty(1);
                return;
            }

            let currentZoom;
            if (isFireFox) {
                const scaleTransform = $image[0].style.transform.replace(/[^\d\\.]*/g, '');
                currentZoom = parseFloat(scaleTransform);
            } else {
                currentZoom = parseFloat($image.css('zoom'));
            }
            const newZoomValue = currentZoom * scale;

            isFireFox ? $image.css('transform', `scale(${newZoomValue})`) : $image.css('zoom', newZoomValue);
            updateZoomGradeValueByCssProperty(newZoomValue);
        }

        function zoomPlus() {
            zoom(2);
        }

        function zoomMinus() {
            zoom(0.5);
        }

        function resetZoom() {
            zoom(0);
        }

        function clearData() {
            $zoomGradeField.text('');
        }

        return {
            buildZoomGradeField,
            updateZoomGradeValue,
            fitImage,
            getRelativeZoomValueByOriginalImg,
            zoom,
            zoomPlus,
            zoomMinus,
            resetZoom,
            clearData
        }
    }
);