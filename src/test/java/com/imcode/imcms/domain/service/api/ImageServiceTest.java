package com.imcode.imcms.domain.service.api;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.imcode.imcms.WebAppSpringTestConfig;
import com.imcode.imcms.components.datainitializer.ImageDataInitializer;
import com.imcode.imcms.components.datainitializer.LoopDataInitializer;
import com.imcode.imcms.components.datainitializer.VersionDataInitializer;
import com.imcode.imcms.domain.dto.ImageCropRegionDTO;
import com.imcode.imcms.domain.dto.ImageDTO;
import com.imcode.imcms.domain.dto.LoopDTO;
import com.imcode.imcms.domain.dto.LoopEntryDTO;
import com.imcode.imcms.domain.exception.DocumentNotExistException;
import com.imcode.imcms.domain.service.ImageService;
import com.imcode.imcms.domain.service.LanguageService;
import com.imcode.imcms.model.Language;
import com.imcode.imcms.persistence.entity.Image;
import com.imcode.imcms.persistence.entity.LanguageJPA;
import com.imcode.imcms.persistence.entity.LoopEntryRefJPA;
import com.imcode.imcms.persistence.entity.Version;
import com.imcode.imcms.persistence.repository.ImageRepository;
import com.imcode.imcms.persistence.repository.LanguageRepository;
import com.imcode.imcms.util.Value;
import imcode.server.Imcms;
import imcode.server.ImcmsConstants;
import imcode.server.document.textdocument.ImageSource;
import imcode.server.document.textdocument.NullImageSource;
import imcode.server.user.UserDomainObject;
import imcode.util.ImcmsImageUtils;
import imcode.util.image.Format;
import imcode.util.io.FileUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
public class ImageServiceTest extends WebAppSpringTestConfig {

    private static final int TEST_DOC_ID = 1001;
    private static final int TEST_IMAGE_INDEX = 1;
    private static final ImageDTO TEST_IMAGE_DTO = new ImageDTO(TEST_IMAGE_INDEX, TEST_DOC_ID);
    private static final int VERSION_INDEX = 0;

    @org.springframework.beans.factory.annotation.Value("${ImagePath}")
    private File imagesPath;

    @Autowired
    private ImageService imageService;

    @Autowired
    private VersionDataInitializer versionDataInitializer;

    @Autowired
    private Function<Image, ImageDTO> imageToImageDTO;

    @Autowired
    private ImageDataInitializer imageDataInitializer;

    private Version workingVersion;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private LoopDataInitializer loopDataInitializer;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private LanguageService languageService;

    @BeforeEach
    public void setUp() throws Exception {
        imageRepository.deleteAll();
        workingVersion = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);

        final UserDomainObject user = new UserDomainObject(1);
        user.setLanguageIso639_2("eng"); // user lang should exist in common content
        Imcms.setUser(user);
    }

    @AfterEach
    public void tearDown() {
        Imcms.removeUser();
    }

    @Test
    public void getImage_When_DocumentNotExist_Expect_Exception() {
        final int nonExistingDocId = 0;
        final ImageDTO imageDTO = new ImageDTO(TEST_IMAGE_INDEX, nonExistingDocId);

        assertThrows(DocumentNotExistException.class,
                () -> imageService.getImage(imageDTO));
    }

    @Test
    public void getImage_When_NotExist_Expect_EmptyDTO() {
        final ImageDTO image = imageService.getImage(TEST_IMAGE_DTO);

        assertEquals(image, TEST_IMAGE_DTO);
    }

    @Test
    public void getImage_When_LoopEntryRefIsNull_Expect_EqualResult() {
        final Image image = imageDataInitializer.createData(TEST_IMAGE_INDEX, TEST_DOC_ID, VERSION_INDEX);
        final ImageDTO imageDTO = imageToImageDTO.apply(image);
        final ImageDTO resultImage = imageService.getImage(TEST_IMAGE_DTO);

        assertEquals(imageDTO, resultImage);
    }

    @Test
    public void getImage_When_LoopEntryRefIsNotNull_Expect_EqualResult() {
        final LoopEntryRefJPA loopEntryRef = new LoopEntryRefJPA(1, 1);
        final Image image = imageDataInitializer.createData(TEST_IMAGE_INDEX, TEST_DOC_ID, VERSION_INDEX, loopEntryRef);
        final ImageDTO imageDTO = imageToImageDTO.apply(image);
        final ImageDTO resultImage = imageService.getImage(imageDTO);

        assertEquals(imageDTO, resultImage);
    }

    @Test
    public void saveImage_When_LoopEntryRefIsNull_Expect_EqualResult() {
        final Image image = imageDataInitializer.createData(TEST_IMAGE_INDEX, TEST_DOC_ID, VERSION_INDEX);
        final ImageDTO imageDTO = imageToImageDTO.apply(image);

        imageService.saveImage(imageDTO);

        final ImageDTO result = imageService.getImage(TEST_IMAGE_DTO);

        assertEquals(result, imageDTO);
    }

    @Test
    public void saveImage_When_InLoopAndAllLanguagesFlagIsSet_Expect_ImageSavedForAllLanguages() {
        saveImageWhenAllLanguagesFlagIsSet(true);
    }

    @Test
    public void saveImage_When_NotInLoopAndAllLanguagesFlagIsSet_Expect_ImageSavedForAllLanguages() {
        saveImageWhenAllLanguagesFlagIsSet(false);
    }

    @Test
    public void saveImage_When_InLoopAndFlagAllLanguagesIsFalse_Expect_ImagesWithDiffLangCodeHaveFalseFlag() {
        saveImageWhenAllLanguagesFlagIsNotSet(true);
    }

    @Test
    public void saveImage_When_NotInLoopAndFlagAllLanguagesIsFalse_Expect_ImagesWithDiffLangCodeHaveFalseFlag() {
        saveImageWhenAllLanguagesFlagIsNotSet(false);
    }

    @Test
    public void saveImage_When_LoopEntryRefIsNotNull_Expect_EqualResult() {
        final LoopEntryRefJPA loopEntryRef = new LoopEntryRefJPA(1, 1);
        final Image image = imageDataInitializer.createData(TEST_IMAGE_INDEX, TEST_DOC_ID, VERSION_INDEX, loopEntryRef);
        final ImageDTO imageDTO = imageToImageDTO.apply(image);

        imageService.saveImage(imageDTO);

        final ImageDTO result = imageService.getImage(imageDTO);

        assertEquals(result, imageDTO);
    }

    @Test
    public void saveImage_When_ImageFileExists_Expect_GeneratedFileExists() throws IOException {
        final ImageDTO imageDTO = Value.with(new ImageDTO(), img -> {
            img.setIndex(TEST_IMAGE_INDEX);
            img.setDocId(TEST_DOC_ID);
            img.setPath("img1.jpg");
            img.setFormat(Format.JPEG);
            img.setLangCode("en");
            img.setName("img1");
            img.setWidth(100);
            img.setHeight(100);
            img.setAlternateText("");
            img.setLinkUrl("");
            img.setBorder(0);
            img.setAlign("");
            img.setLowResolutionUrl("");
            img.setTarget("");
            img.setType(0);
            img.setRotateAngle(0);
        });

        imageService.saveImage(imageDTO);

        final ImageDTO result = imageService.getImage(imageDTO);

        assertNotNull(result);

        final File croppedImage = new File(imagesPath, ImcmsConstants.IMAGE_GENERATED_FOLDER + File.separator + result.getGeneratedFilename());

        assertTrue(croppedImage.exists());
        assertTrue(FileUtility.forceDelete(croppedImage));
    }

    @Test
    public void saveImage_When_CroppingIsNotDefault_Expect_EqualCroppingAndGeneratedImageExist() throws IOException {
        final ImageCropRegionDTO cropRegion = new ImageCropRegionDTO(10, 10, 20, 20);
        final ImageDTO imageDTO = Value.with(new ImageDTO(), img -> {
            img.setIndex(TEST_IMAGE_INDEX);
            img.setDocId(TEST_DOC_ID);
            img.setPath("img1.jpg");
            img.setFormat(Format.JPEG);
            img.setLangCode("en");
            img.setName("img1");
            img.setWidth(100);
            img.setHeight(100);
            img.setCropRegion(cropRegion);
            img.setAlternateText("");
            img.setLinkUrl("");
            img.setBorder(0);
            img.setAlign("");
            img.setLowResolutionUrl("");
            img.setTarget("");
            img.setType(0);
            img.setRotateAngle(0);
        });

        File generatedImage = null;

        try {
            imageService.saveImage(imageDTO);

            final ImageDTO result = imageService.getImage(imageDTO);

            assertNotNull(result);
            assertEquals(result.getCropRegion(), cropRegion);

            generatedImage = new File(imagesPath, ImcmsConstants.IMAGE_GENERATED_FOLDER + File.separator + result.getGeneratedFilename());
            assertTrue(generatedImage.exists());

        } finally {
            if (generatedImage != null) {
                assertTrue(FileUtility.forceDelete(generatedImage));
            }
        }
    }

    @Test
    public void createVersionedContent() {
        int testImageIndex = TEST_IMAGE_INDEX;
        final Image workingVersionImage = imageDataInitializer.createData(++testImageIndex, TEST_DOC_ID, VERSION_INDEX);

        final Version latestVersion = versionDataInitializer.createData(VERSION_INDEX + 1, TEST_DOC_ID);

        imageService.createVersionedContent(workingVersion, latestVersion);

        final List<Image> latestVersionImages = imageRepository.findByVersion(latestVersion);

        assertEquals(1, latestVersionImages.size());

        final Image image = latestVersionImages.get(0);

        assertEquals(workingVersionImage.getGeneratedFilename(), image.getGeneratedFilename());
        assertEquals(workingVersionImage.getName(), image.getName());
        assertEquals(workingVersionImage.getUrl(), image.getUrl());
        assertEquals(workingVersionImage.getAlign(), image.getAlign());
        assertEquals(workingVersionImage.getAlternateText(), image.getAlternateText());
        assertEquals(workingVersionImage.getLinkUrl(), image.getLinkUrl());
        assertEquals(workingVersionImage.getIndex(), image.getIndex());
        assertEquals(workingVersionImage.getHeight(), image.getHeight());
        assertEquals(workingVersionImage.getWidth(), image.getWidth());
        assertEquals(workingVersionImage.getLoopEntryRef(), image.getLoopEntryRef());
        assertEquals(workingVersionImage.getCropRegion(), image.getCropRegion());
        assertEquals(workingVersionImage.getLanguage(), image.getLanguage());
        assertEquals(workingVersionImage.getVersion().getDocId(), image.getVersion().getDocId());
    }

    @Test
    public void deleteByDocId_Expect_Deleted() {
        imageDataInitializer.cleanRepositories();
        assertTrue(imageRepository.findAll().isEmpty());

        final Version version = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);
        final Version newVersion = versionDataInitializer.createData(VERSION_INDEX + 1, TEST_DOC_ID);

        final LoopDTO loopDTO = new LoopDTO(TEST_DOC_ID, 1, Collections.singletonList(LoopEntryDTO.createEnabled(1)));
        final LoopEntryRefJPA loopEntryRef = new LoopEntryRefJPA(1, 1);
        loopDataInitializer.createData(loopDTO);

        final List<LanguageJPA> languages = languageRepository.findAll();
        final Version[] versions = {version, newVersion};
        final LoopEntryRefJPA[] loops = {loopEntryRef, null};

        for (int i = 0; i < 20; i++) {
            for (LanguageJPA language : languages) {
                for (Version vers : versions) {
                    for (LoopEntryRefJPA loopEntryRefJPA : loops) {
                        imageDataInitializer.generateImage(i, language, vers, loopEntryRefJPA);
                    }
                }
            }
        }

        assertFalse(imageRepository.findAll().isEmpty());

        imageRepository.deleteByDocId(TEST_DOC_ID);

        assertTrue(imageRepository.findAll().isEmpty());
    }

    @Test
    public void getPublicImageLinks_When_FewVersionExist_Expect_Found() {
        assertTrue(imageRepository.findAll().isEmpty());

        final Version workingVersion = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);
        final Version newVersion = versionDataInitializer.createData(VERSION_INDEX + 1, TEST_DOC_ID);
        final int loopIndex = 1;
        final int loopEntryIndex = 1;
        final LoopDTO loopDTO = new LoopDTO(
                TEST_DOC_ID, loopIndex, Collections.singletonList(LoopEntryDTO.createEnabled(loopEntryIndex))
        );
        final LoopEntryRefJPA loopEntryRef = new LoopEntryRefJPA(loopIndex, loopEntryIndex);
        loopDataInitializer.createData(loopDTO);

        final List<LanguageJPA> languages = languageRepository.findAll();
        final Version[] versions = {workingVersion, newVersion};
        final int imagesPerVersionPerLanguage = 20;
        final String testLinkUrl = "link_url";

        for (Version version : versions) {
            for (LanguageJPA language : languages) {
                IntStream.range(TEST_IMAGE_INDEX, TEST_IMAGE_INDEX + imagesPerVersionPerLanguage)
                        .forEach(index -> {
                            final Image image = new Image();
                            image.setIndex(index);
                            image.setLanguage(language);
                            image.setVersion(version);
                            image.setLoopEntryRef((index % 2 == 0) ? loopEntryRef : null);
                            image.setFormat(Format.JPEG);
                            image.setLinkUrl(testLinkUrl + index);
                            imageRepository.save(image);
                        });
                IntStream.range(TEST_IMAGE_INDEX + imagesPerVersionPerLanguage, TEST_IMAGE_INDEX + (2 * imagesPerVersionPerLanguage))
                        .forEach(index -> {
                            final Image image = new Image();
                            image.setIndex(index);
                            image.setLanguage(language);
                            image.setVersion(version);
                            image.setLoopEntryRef((index % 2 == 0) ? loopEntryRef : null);
                            image.setFormat(Format.JPEG);
                            image.setLinkUrl("");
                            imageRepository.save(image);
                        });
            }
        }

        assertFalse(imageRepository.findAll().isEmpty());

        for (LanguageJPA language : languages) {
            final Set<String> links = imageService.getPublicImageLinks(TEST_DOC_ID, language);

            links.forEach(s -> assertTrue(s.startsWith(testLinkUrl)));
        }
    }

    @Test
    public void getFreeIndexForImageInTextEditor_When_SomePositiveIndexesExist_Expect_MinusOne() {
        final int minIndex = TEST_IMAGE_INDEX;
        final LanguageJPA lang = languageRepository.findAll().get(0);

        IntStream.range(minIndex, minIndex + 10)
                .forEach(index -> imageDataInitializer.generateImage(index, lang, workingVersion, null));

        final ImageDTO imageDTO = new ImageDTO(null, TEST_DOC_ID, null, lang.getCode());
        imageDTO.setInText(true);

        final ImageDTO receivedImage = imageService.getImage(imageDTO);

        assertNotNull(receivedImage);
        assertEquals(-1, receivedImage.getIndex().intValue());
    }

    @Test
    public void getFreeIndexForImageInTextEditor_When_SomeNegativeIndexExist_Expect_MinReturned() {
        final int minIndex = TEST_IMAGE_INDEX - 10;
        final LanguageJPA lang = languageRepository.findAll().get(0);

        IntStream.range(minIndex + 1, TEST_IMAGE_INDEX + 10)
                .forEach(index -> imageDataInitializer.generateImage(index, lang, workingVersion, null));

        final ImageDTO imageDTO = new ImageDTO(null, TEST_DOC_ID, null, lang.getCode());
        imageDTO.setInText(true);

        final ImageDTO receivedImage = imageService.getImage(imageDTO);

        assertNotNull(receivedImage);
        assertEquals(minIndex, receivedImage.getIndex().intValue());
    }

    @Test
    public void deleteImage_When_InLoopAndAllLanguagesFlagIsSet_Expect_AllImagesWithDiffLangCodeAreDeleted() {
        deleteImageWhenAllLanguagesFlagIsSet(true);
    }

    @Test
    public void deleteImage_When_NotInLoopAndAllLanguagesFlagIsSet_Expect_AllImagesWithDiffLangCodeAreDeleted() {
        deleteImageWhenAllLanguagesFlagIsSet(false);
    }

    @Test
    public void deleteImage_When_InLoopAndAllLanguagesIsNotSet_Expect_AllImagesWithDiffLangCodeHaveFalseFlag() {
        deleteImageWhenAllLanguagesFlagIsNotSet(true);
    }

    @Test
    public void deleteImage_When_NotInLoopAndAllLanguagesIsNotSet_Expect_AllImagesWithDiffLangCodeHaveFalseFlag() {
        deleteImageWhenAllLanguagesFlagIsNotSet(false);
    }

    private void saveImageWhenAllLanguagesFlagIsSet(boolean inLoop) {
        final LoopEntryRefJPA loopEntryRef = inLoop
                ? new LoopEntryRefJPA(1, 1)
                : null;

        final Image image = imageDataInitializer.createData(TEST_IMAGE_INDEX, TEST_DOC_ID, VERSION_INDEX, loopEntryRef);
        image.setAllLanguages(true);

        final ImageDTO expected = imageToImageDTO.apply(image);

        imageService.saveImage(expected);

        final List<Language> languages = languageService.getAll();

        assertEquals(2, languages.size());

        languages.forEach(language -> {
            final ImageDTO actual = imageService
                    .getImage(TEST_DOC_ID, TEST_IMAGE_INDEX, language.getCode(), loopEntryRef);

            expected.setLangCode(language.getCode());

            assertEquals(expected, actual);
        });
    }

    private void saveImageWhenAllLanguagesFlagIsNotSet(boolean inLoop) {
        final LoopEntryRefJPA loopEntryRef = inLoop
                ? new LoopEntryRefJPA(1, 1)
                : null;

        final Version version = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);

        languageService.getAll().forEach(language -> {
            final Image image = imageDataInitializer
                    .generateImage(TEST_IMAGE_INDEX, new LanguageJPA(language), version, loopEntryRef);

            image.setAllLanguages(true);
            imageRepository.save(image);
        });

        assertEquals(languageRepository.findAll().size(), imageRepository.findAll().size());
        imageRepository.findAll().forEach(image -> assertTrue(image.isAllLanguages()));

        final Image newImage = imageRepository.findAll().get(0);
        newImage.setAllLanguages(false);

        imageService.saveImage(imageToImageDTO.apply(newImage));

        imageRepository.findAll().forEach(image -> assertFalse(image.isAllLanguages()));
    }

    private void deleteImageWhenAllLanguagesFlagIsSet(boolean inLoop) {
        final Version version = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);

        final LoopEntryRefJPA loopEntryRefJPA = inLoop
                ? new LoopEntryRefJPA(1, 1)
                : null;

        languageService.getAll().forEach(language -> {
            final Image image = imageDataInitializer
                    .generateImage(TEST_IMAGE_INDEX, new LanguageJPA(language), version, loopEntryRefJPA);

            image.setAllLanguages(true);
            imageRepository.save(image);
        });

        final List<Image> images = imageRepository.findAll();

        assertEquals(languageRepository.findAll().size(), images.size());

        imageService.deleteImage(imageToImageDTO.apply(images.get(0)));

        assertEquals(0, imageRepository.findAll().size());
    }

    private void deleteImageWhenAllLanguagesFlagIsNotSet(boolean inLoop) {
        final Version version = versionDataInitializer.createData(VERSION_INDEX, TEST_DOC_ID);

        final LoopEntryRefJPA loopEntryRefJPA = inLoop
                ? new LoopEntryRefJPA(1, 1)
                : null;

        languageService.getAll().forEach(language -> {
            final Image image = imageDataInitializer
                    .generateImage(TEST_IMAGE_INDEX, new LanguageJPA(language), version, loopEntryRefJPA);

            image.setAllLanguages(true);
            imageRepository.save(image);
        });

        final List<Image> images = imageRepository.findAll();

        assertEquals(languageRepository.findAll().size(), images.size());

        final Image image = images.get(0);
        image.setAllLanguages(false);

        imageRepository.save(image);
        imageService.deleteImage(imageToImageDTO.apply(image));

        imageRepository.findAll()
                .forEach(imageJPA -> assertFalse(imageJPA.isAllLanguages()));
    }

    @Test
    public void exifData() throws ImageProcessingException, IOException {
        final ImageSource imageSource = ImcmsImageUtils.getImageSource("/img1.jpg");

        if (!(imageSource instanceof NullImageSource)) {

            try (final InputStream inputStream = imageSource.getInputStreamSource().getInputStream()) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);

                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        System.out.println(tag);
                    }
                    for (String error : directory.getErrors()) {
                        System.err.println("ERROR: " + error);
                    }
                }

            }

        }
    }
}