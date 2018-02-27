package com.imcode.imcms.controller.core;

import com.imcode.imcms.domain.service.TypedDocumentService;
import com.imcode.imcms.model.Document;
import com.imcode.imcms.security.CheckAccess;
import imcode.server.Imcms;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller for publishing new document's version.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 12.12.17.
 */
@Controller
@RequestMapping("/publish-document")
public class DocumentPublicationController {

    private final TypedDocumentService documentService;

    public DocumentPublicationController(TypedDocumentService<Document> documentService) {
        this.documentService = documentService;
    }

    @CheckAccess
    @RequestMapping("/{docIdentifier}")
    public RedirectView publishDocument(@PathVariable("docIdentifier") int docId,
                                        @RequestParam(value = "return", required = false) String returnUrl) {

        documentService.publishDocument(docId, Imcms.getUser().getId());
        return new RedirectView((returnUrl == null) ? "/" + docId : returnUrl, true);
    }

}
