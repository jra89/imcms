package com.imcode.imcms.domain.service;

import com.imcode.imcms.domain.dto.ImageFolderDTO;
import com.imcode.imcms.domain.dto.ImageFolderItemUsageDTO;

import java.io.IOException;
import java.util.List;

/**
 * Service for Images Content Manager.
 * CRUD operations with image folders and content.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 30.10.17.
 */
public interface ImageFolderService {

    ImageFolderDTO getImageFolder();

    boolean createImageFolder(ImageFolderDTO folderToCreate);

    boolean renameFolder(ImageFolderDTO renameMe);

    boolean canBeDeleted(ImageFolderDTO folderToCheck) throws IOException;

    boolean deleteFolder(ImageFolderDTO deleteMe) throws IOException;

    ImageFolderDTO getImagesFrom(ImageFolderDTO folderToGetImages);

    List<ImageFolderItemUsageDTO> checkFolder(ImageFolderDTO folderToCheck);
}
