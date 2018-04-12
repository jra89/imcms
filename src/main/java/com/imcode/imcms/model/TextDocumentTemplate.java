package com.imcode.imcms.model;

import lombok.NoArgsConstructor;

/**
 * Text document to template.
 */
@NoArgsConstructor
public abstract class TextDocumentTemplate {

    public static final String DEFAULT_TEMPLATE_NAME = "demo";

    protected TextDocumentTemplate(TextDocumentTemplate createFrom) {
        setDocId(createFrom.getDocId());
        setTemplateName(createFrom.getTemplateName());
        setChildrenTemplateName(createFrom.getChildrenTemplateName());
    }

    public abstract Integer getDocId();

    public abstract void setDocId(Integer docId);

    public abstract String getTemplateName();

    public abstract void setTemplateName(String templateName);

    public abstract String getChildrenTemplateName();

    public abstract void setChildrenTemplateName(String childrenTemplateName);

}