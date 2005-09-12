package com.imcode.imcms.api;

import imcode.server.document.TemplateDomainObject;

public class Template {

    private final TemplateDomainObject internalTemplate;

    public Template(TemplateDomainObject internalTemplate) {
        this.internalTemplate = internalTemplate;
    }

    public String getName() {
        return internalTemplate.getName();
    }

    public int getId() {
        return internalTemplate.getId();
    }

    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof Template ) ) {
            return false;
        }

        final Template template = (Template) o;

        return internalTemplate.equals(template.internalTemplate);

    }

    public int hashCode() {
        return internalTemplate.hashCode() ;
    }

    TemplateDomainObject getInternal() {
        return internalTemplate;
    }

    public String toString() {
        return getName();
    }
}
