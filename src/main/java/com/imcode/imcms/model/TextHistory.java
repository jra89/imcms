package com.imcode.imcms.model;

import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public abstract class TextHistory extends Text {

    public TextHistory(TextHistory from) {
        super(from);

        setModifiedDt(from.getModifiedDt());
        setDocId(from.getDocId());
    }

    public abstract void setDocId(Integer docId);

    public abstract Date getModifiedDt();

    public abstract void setModifiedDt(Date modifiedDt);

    public abstract Integer getModifierId();
}
