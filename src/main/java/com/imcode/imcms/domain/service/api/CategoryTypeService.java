package com.imcode.imcms.domain.service.api;

import com.imcode.imcms.domain.dto.CategoryTypeDTO;
import com.imcode.imcms.persistence.entity.CategoryTypeJPA;
import com.imcode.imcms.persistence.repository.CategoryTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CategoryTypeService {

    private final CategoryTypeRepository categoryTypeRepository;
    private final Function<CategoryTypeJPA, CategoryTypeDTO> mapper;

    @Autowired
    CategoryTypeService(CategoryTypeRepository categoryTypeRepository, Function<CategoryTypeJPA, CategoryTypeDTO> mapper) {
        this.categoryTypeRepository = categoryTypeRepository;
        this.mapper = mapper;
    }

    public List<CategoryTypeDTO> getAll() {
        return categoryTypeRepository.findAll()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

}
