package com.imcode.imcms.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "template")
@AllArgsConstructor
@NoArgsConstructor
public class TemplateJPA extends Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "template_name", unique = true, nullable = false)
    private String name;

    @Column(name = "is_hidden", nullable = false)
    private boolean hidden;

    public TemplateJPA(Template templateFrom) {
        super(templateFrom);
    }
}
