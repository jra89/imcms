package com.imcode.imcms.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class LoopEntryRef {

    protected LoopEntryRef(LoopEntryRef from) {
        setLoopIndex(from.getLoopIndex());
        setLoopEntryIndex(from.getLoopEntryIndex());
    }

    public abstract int getLoopIndex();

    public abstract void setLoopIndex(int loopIndex);

    public abstract int getLoopEntryIndex();

    public abstract void setLoopEntryIndex(int loopEntryIndex);
}
