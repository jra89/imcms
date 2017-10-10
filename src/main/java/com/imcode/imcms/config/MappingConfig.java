package com.imcode.imcms.config;

import com.imcode.imcms.domain.dto.*;
import com.imcode.imcms.mapping.jpa.doc.Version;
import com.imcode.imcms.persistence.entity.*;
import com.imcode.imcms.util.Value;
import imcode.server.Imcms;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MappingConfig {
    @Bean
    public Function<LoopEntryDTO, LoopEntry> loopEntryDtoToEntry() {
        return loopEntryDTO -> new LoopEntry(loopEntryDTO.getIndex(), loopEntryDTO.isEnabled());
    }

    @Bean
    public Function<LoopEntry, LoopEntryDTO> loopEntryToLoopEntryDTO() {
        return entry -> new LoopEntryDTO(entry.getIndex(), entry.isEnabled());
    }

    @Bean
    public Function<Category, CategoryDTO> categoryToCategoryDTO() {
        return category -> new CategoryDTO(category.getId(), category.getName());
    }

    @Bean
    public BiFunction<LoopDTO, Version, Loop> loopDtoToLoop(Function<LoopEntryDTO, LoopEntry> loopEntryDtoToEntry) {
        return (loopDTO, version) -> {
            final List<LoopEntry> entries = Objects.requireNonNull(loopDTO)
                    .getEntries()
                    .stream()
                    .map(loopEntryDtoToEntry)
                    .collect(Collectors.toList());

            return new Loop(version, loopDTO.getIndex(), entries);
        };
    }

    @Bean
    public Function<MenuItem, MenuItemDTO> menuItemToDTO() {
        return new Function<MenuItem, MenuItemDTO>() {
            @Override
            public MenuItemDTO apply(MenuItem menuItem) {
                final MenuItemDTO menuItemDTO = new MenuItemDTO();
                menuItemDTO.setId(menuItem.getId());
                menuItemDTO.setDocumentId(menuItem.getDocumentId());
                menuItemDTO.setChildren(menuItem.getChildren().stream()
                        .map(this)
                        .collect(Collectors.toList()));
                return menuItemDTO;
            }
        };
    }

    @Bean
    public Function<MenuItemDTO, MenuItem> menuItemDtoToMenuItem() {
        return new Function<MenuItemDTO, MenuItem>() {
            @Override
            public MenuItem apply(MenuItemDTO menuItemDTO) {
                final MenuItem menuItem = new MenuItem();
                menuItem.setId(menuItemDTO.getId());
                menuItem.setDocumentId(menuItemDTO.getDocumentId());
                final AtomicInteger counter = new AtomicInteger(1);
                menuItem.setChildren(menuItemDTO.getChildren().stream()
                        .map(this)
                        .peek(menuItemChild -> menuItemChild.setSortOrder(counter.getAndIncrement()))
                        .collect(Collectors.toList()));
                return menuItem;
            }
        };
    }

    @Bean
    public Function<Loop, LoopDTO> loopToLoopDTO(Function<LoopEntry, LoopEntryDTO> loopEntryToDtoMapper) {
        return loop -> {
            final List<LoopEntryDTO> loopEntryDTOs = Objects.requireNonNull(loop)
                    .getEntries()
                    .stream()
                    .map(loopEntryToDtoMapper)
                    .collect(Collectors.toList());

            return new LoopDTO(loop.getVersion().getDocId(), loop.getIndex(), loopEntryDTOs);
        };
    }

    @Bean
    public Function<CategoryType, CategoryTypeDTO> categoryTypeToCategoryTypeDTO(Function<Category, CategoryDTO> categoryMapper) {
        return categoryType -> new CategoryTypeDTO(
                categoryType.getId(),
                categoryType.getName(),
                categoryType.isMultiSelect(),
                categoryType.getCategories()
                        .stream()
                        .map(categoryMapper)
                        .collect(Collectors.toList())
        );
    }

    @Bean
    public Function<Image, ImageDTO> imageToImageDTO() {
        return image -> Value.with(new ImageDTO(), dto -> {
            dto.setIndex(image.getIndex());

            final String name = image.getName();
            dto.setName(name);

            final File imagePath = Imcms.getServices().getConfig().getImagePath();
            final String path = imagePath + "generated/" + image.getGeneratedFilename();

            dto.setPath(path);
            dto.setFormat(name.contains(".") ? name.substring(name.lastIndexOf('.')) : "");
            dto.setHeight(image.getHeight());
            dto.setWidth(image.getWidth());
        });
    }
}
