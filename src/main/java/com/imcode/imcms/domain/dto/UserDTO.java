package com.imcode.imcms.domain.dto;

import com.imcode.imcms.persistence.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -1878691076340113546L;

    private int id;

    private String username;

    public UserDTO(User from) {
        id = from.getId();
        username = from.getLogin();
    }
}