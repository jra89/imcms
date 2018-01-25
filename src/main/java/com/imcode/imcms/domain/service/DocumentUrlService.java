package com.imcode.imcms.domain.service;

import com.imcode.imcms.model.DocumentURL;

public interface DocumentUrlService {

    DocumentURL getByDocId(int docId);

    DocumentURL save(DocumentURL documentURL);
}
