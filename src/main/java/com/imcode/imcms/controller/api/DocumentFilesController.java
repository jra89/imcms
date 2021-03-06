package com.imcode.imcms.controller.api;

import com.imcode.imcms.domain.service.DocumentFileService;
import com.imcode.imcms.model.DocumentFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Controller for file-doc files.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 22.01.18.
 */
@RestController
@RequestMapping("/file-documents/files")
public class DocumentFilesController {

    private final DocumentFileService documentFileService;

    DocumentFilesController(DocumentFileService documentFileService) {
        this.documentFileService = documentFileService;
    }

    @PostMapping
    public void saveDocFiles(@RequestParam List<MultipartFile> files, @RequestParam int docId) {

        final Map<String, Queue<MultipartFile>> fileNameToFiles = files.stream()
                .collect(Collectors.toMap(
                        MultipartFile::getOriginalFilename,
                        (MultipartFile file) -> {
                            final Queue<MultipartFile> fileList = new ArrayDeque<>();
                            fileList.add(file);
                            return fileList;
                        },
                        (files1, files2) -> {
                            files1.addAll(files2);
                            return files1;
                        }
                ));

        final List<DocumentFile> saveUs = documentFileService.getByDocId(docId)
                .stream()
                .peek(documentFileDTO -> {
                    final String filename = documentFileDTO.getFilename();
                    final Queue<MultipartFile> multipartFiles = fileNameToFiles.get(filename);

                    if (multipartFiles != null) {
                        documentFileDTO.setMultipartFile(multipartFiles.poll());
                    }
                })
                .collect(Collectors.toList());

        documentFileService.saveAll(saveUs, docId);
    }

}
