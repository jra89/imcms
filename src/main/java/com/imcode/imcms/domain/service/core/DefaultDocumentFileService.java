package com.imcode.imcms.domain.service.core;

import com.imcode.imcms.domain.dto.DocumentFileDTO;
import com.imcode.imcms.domain.service.AbstractVersionedContentService;
import com.imcode.imcms.domain.service.DocumentFileService;
import com.imcode.imcms.domain.service.VersionService;
import com.imcode.imcms.model.DocumentFile;
import com.imcode.imcms.persistence.entity.DocumentFileJPA;
import com.imcode.imcms.persistence.entity.Version;
import com.imcode.imcms.persistence.repository.DocumentFileRepository;
import imcode.util.Utility;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
class DefaultDocumentFileService
        extends AbstractVersionedContentService<DocumentFileJPA, DocumentFile, DocumentFileRepository>
        implements DocumentFileService {

    public static final Logger LOG = Logger.getLogger(DefaultDocumentFileService.class);
    private final DocumentFileRepository documentFileRepository;
    private final VersionService versionService;
    private final File filesPath;

    DefaultDocumentFileService(DocumentFileRepository documentFileRepository,
                               VersionService versionService,
                               @Value("${FilePath}") File filesPath) {

        super(documentFileRepository);
        this.documentFileRepository = documentFileRepository;
        this.versionService = versionService;
        this.filesPath = filesPath;
    }

    @PostConstruct
    private void createFilesPathDirectories() {
        filesPath.mkdirs();
    }

    /**
     * This will save list of files for specified document by id.
     * Note that all other files that are connected to document but not
     * mentioned in list will be deleted.
     * All changes applied for working document version.
     *
     * @param saveUs list of files to save
     * @param docId  id of document
     * @return list of saved files
     */
    @Override
    public List<DocumentFile> saveAll(List<DocumentFile> saveUs, int docId) {
        setDocAndFileIds(saveUs, docId);
        deleteNoMoreUsedFiles(saveUs, docId);
        saveNewFiles(saveUs);

        return saveDocumentFiles(saveUs);
    }

    @Override
    public List<DocumentFile> getByDocId(int docId) {
        return findWorkingVersionFiles(docId).stream()
                .map(DocumentFileDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void publishDocumentFiles(int docId) {
        final Version latestVersion = versionService.getLatestVersion(docId);
        final Version workingVersion = versionService.getDocumentWorkingVersion(docId);

        createVersionedContent(workingVersion, latestVersion);
    }

    @Override
    public DocumentFile getPublicByDocId(int docId) {
        final int latestVersionIndex = versionService.getLatestVersion(docId).getNo();
        return documentFileRepository.findDefaultByDocIdAndVersionIndex(docId, latestVersionIndex);
    }

    @Override
    public void deleteByDocId(Integer docIdToDelete) {
        // todo: implement, or not =)
    }

    @Override
    protected DocumentFile mapToDTO(DocumentFileJPA documentFileJPA, Version version) {
        return new DocumentFileDTO(documentFileJPA);
    }

    @Override
    protected DocumentFileJPA mapToJpaWithoutId(DocumentFile documentFile, Version version) {
        final DocumentFileJPA documentFileJPA = new DocumentFileJPA(documentFile);
        documentFileJPA.setVersionIndex(version.getNo());
        documentFileJPA.setId(null);

        return documentFileJPA;
    }

    private List<DocumentFileJPA> findWorkingVersionFiles(int docId) {
        return documentFileRepository.findByDocIdAndVersionIndex(docId, Version.WORKING_VERSION_INDEX);
    }

    private List<DocumentFile> saveDocumentFiles(List<DocumentFile> saveUs) {
        return saveUs.stream()
                .map(documentFile -> new DocumentFileDTO(
                        documentFileRepository.save(new DocumentFileJPA(documentFile))
                ))
                .collect(Collectors.toList());
    }

    private void setDocAndFileIds(List<DocumentFile> saveUs, int docId) {
        saveUs.forEach(documentFile -> {
            documentFile.setDocId(docId);
            final String fileId = documentFile.getFileId();

            if (fileId == null) {
                documentFile.setFileId(documentFile.getFilename());
            }
        });
    }

    private void deleteNoMoreUsedFiles(List<DocumentFile> saveUs, int docId) {
        final Set<Integer> existingFileIds = saveUs.stream()
                .map(DocumentFile::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        final List<DocumentFileJPA> noMoreNeededFiles = getByDocId(docId).stream()
                .filter(documentFile -> !existingFileIds.contains(documentFile.getId()))
                .map(DocumentFileJPA::new)
                .collect(Collectors.toList());

        documentFileRepository.delete(noMoreNeededFiles);
    }

    private void saveNewFiles(List<DocumentFile> saveUs) {
        // do not rewrite using Java Stream API, file transfer can be long operation. in cycle.
        for (DocumentFile documentFile : saveUs) {
            final MultipartFile file = documentFile.getMultipartFile();

            if (file == null) {
                continue;
            }

            int copiesCount = 1;
            String originalFilename = Utility.normalizeString(file.getOriginalFilename());
            originalFilename = originalFilename.replace("(", "").replace(")", "");
            File destination = new File(filesPath, originalFilename);

            while (destination.exists()) {
                final String baseName = FilenameUtils.getBaseName(originalFilename);
                final String newName = baseName + copiesCount + "." + FilenameUtils.getExtension(originalFilename);
                documentFile.setFilename(newName);
                destination = new File(filesPath, newName);
                copiesCount++;
            }

            try {
                file.transferTo(destination);
            } catch (IOException e) {
                LOG.error("Error while saving Document File.", e);
            }
        }
    }
}
