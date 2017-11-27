package com.imcode.imcms.domain.dto;

import com.imcode.imcms.persistence.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO extends Role implements Serializable {

    private static final long serialVersionUID = -6429901776462985054L;

    private Integer id;

    private String name;

    private PermissionDTO permission;

    public RoleDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public RoleDTO(Role from) {
        super(from);
    }
}
