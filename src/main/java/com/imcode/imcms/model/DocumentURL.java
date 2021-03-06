package com.imcode.imcms.model;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public abstract class DocumentURL implements Serializable {

    private static final long serialVersionUID = 3063393658924665003L;

    public DocumentURL(DocumentURL from) {
        setId(from.getId());
        setUrlFrameName(from.getUrlFrameName());
        setUrlTarget(from.getUrlTarget());
        setUrl(from.getUrl());
        setUrlText(from.getUrlText());
        setUrlLanguagePrefix(from.getUrlLanguagePrefix());
    }

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract String getUrlFrameName();

    public abstract void setUrlFrameName(String urlFrameName);

    public abstract String getUrlTarget();

    public abstract void setUrlTarget(String urlTarget);

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public abstract String getUrlText();

    public abstract void setUrlText(String urlText);

    public abstract String getUrlLanguagePrefix();

    public abstract void setUrlLanguagePrefix(String urlLanguagePrefix);

    public abstract Integer getDocId();
}
