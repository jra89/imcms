package com.imcode.imcms.domain.dto;

import com.imcode.imcms.model.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CategoryTypeDTO extends CategoryType implements Serializable {

    private static final long serialVersionUID = -4636053716188761920L;

    private Integer id;

    private String name;

    private boolean multiSelect;

    public CategoryTypeDTO(CategoryType from) {
        super(from);
    }

    @Override
    public boolean isInherited() {
        return false;
    }

    @Override
    public void setInherited(boolean isInherited) {
        // not implemented
    }

    @Override
    public boolean isImageArchive() {
        return false;
    }

    @Override
    public void setImageArchive(boolean imageArchive) {
        // not implemented
    }
}
