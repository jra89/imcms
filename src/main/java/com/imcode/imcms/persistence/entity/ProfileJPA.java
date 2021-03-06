package com.imcode.imcms.persistence.entity;

import com.imcode.imcms.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Table(name = "profiles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProfileJPA extends Profile {

    private static final long serialVersionUID = 1073806546803135420L;

    @Id
    @Column(name = "profile_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // todo : required add new validation on NotNull
    private String name;

    @Column(name = "document_name")
    private String documentName;

    public ProfileJPA(Profile from) {
        super(from);
    }
}
