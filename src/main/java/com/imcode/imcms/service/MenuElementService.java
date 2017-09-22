package com.imcode.imcms.service;

import com.imcode.imcms.mapping.DocumentMapper;
import com.imcode.imcms.mapping.dto.MenuElementDTO;
import com.imcode.imcms.mapping.mappers.Mappable;
import imcode.server.document.textdocument.MenuItemDomainObject.TreeMenuItemDomainObject;
import imcode.server.document.textdocument.TextDocumentDomainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuElementService {

    private final DocumentMapper documentMapper;
    private final Mappable<TreeMenuItemDomainObject, MenuElementDTO> mapper;

    @Autowired
    public MenuElementService(DocumentMapper documentMapper, Mappable<TreeMenuItemDomainObject, MenuElementDTO> mapper) {
        this.documentMapper = documentMapper;
        this.mapper = mapper;
    }

    public List<MenuElementDTO> getMenuElements(int menuNo, int documentId) {
        return documentMapper
                .<TextDocumentDomainObject>getWorkingDocument(documentId)
                .getMenu(menuNo)
                .getMenuItemsAsTree()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

}
