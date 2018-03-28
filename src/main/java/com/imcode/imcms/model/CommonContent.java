package com.imcode.imcms.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class CommonContent {

    protected CommonContent(CommonContent from) {
        setId(from.getId());
        setDocId(from.getDocId());
        setHeadline(from.getHeadline());
        setLanguage(from.getLanguage());
        setMenuText(from.getMenuText());
        setMenuImageURL(from.getMenuImageURL());
        setEnabled(from.isEnabled());
        setVersionNo(from.getVersionNo());
    }

    public abstract Integer getId();

    public abstract void setId(Integer id);

    public abstract Integer getDocId();

    public abstract void setDocId(Integer docId);

    public abstract String getHeadline();

    public abstract void setHeadline(String headline);

    public abstract Language getLanguage();

    public abstract void setLanguage(Language language);

    public abstract String getMenuText();

    public abstract void setMenuText(String menuText);

    public abstract String getMenuImageURL();

    public abstract void setMenuImageURL(String menuImageURL);

    public abstract boolean isEnabled();

    public abstract void setEnabled(boolean isEnabled);

    public abstract Integer getVersionNo();

    public abstract void setVersionNo(Integer versionNo);

}
