package com.imcode.imcms.domain.service;

import com.imcode.imcms.model.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryTypeService {

    Optional<CategoryType> get(int id);

    List<CategoryType> getAll();

    CategoryType save(CategoryType saveMe);

    void delete(int id);

}