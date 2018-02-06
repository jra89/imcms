package com.imcode.imcms.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "com.imcode.imcms.persistence.entity.MenuItem")
@Table(name = "imcms_menu_item")
@Data
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "document_id", nullable = false)
    private Integer documentId;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "parent_item_id")
    @OrderBy("sortOrder")
    private List<MenuItem> children = new ArrayList<>();

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public MenuItem(MenuItem from) {
        setDocumentId(from.getDocumentId());
        setSortOrder(from.getSortOrder());
    }
}
