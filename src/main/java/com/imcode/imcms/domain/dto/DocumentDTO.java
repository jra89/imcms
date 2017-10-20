package com.imcode.imcms.domain.dto;

import com.imcode.imcms.mapping.jpa.doc.Meta.DisabledLanguageShowMode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class DocumentDTO implements Serializable {

    private static final long serialVersionUID = -2317764204932918145L;

    private int id;

    private String title;

    private String type;

    private String target;

    private String alias;

    private List<LanguageDTO> languages;

    private int publicationStatus;

    private AuditDTO published;

    private AuditDTO archived;

    private AuditDTO publicationEnd;

    private AuditDTO modified;

    private AuditDTO created;

    private int publisher;

    private DisabledLanguageShowMode disabledLanguageShowMode;

    private AuditDTO currentVersion;

    private Set<String> keywords;

    private boolean searchDisabled;

    private Set<CategoryDTO> categories;

    private Set<RestrictedPermissionDTO> permissions;

    private Set<RoleDTO> roles;

    private int template;

    private int childTemplate;

}
