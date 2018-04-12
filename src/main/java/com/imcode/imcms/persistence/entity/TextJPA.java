package com.imcode.imcms.persistence.entity;

import com.imcode.imcms.model.LoopEntryRef;
import com.imcode.imcms.model.Text;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "imcms_text_doc_texts")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TextJPA extends Text {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "doc_id", referencedColumnName = "doc_id"),
            @JoinColumn(name = "doc_version_no", referencedColumnName = "no")
    })
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Version version;

    @NotNull
    @Column(name = "`index`")
    private Integer index;

    @Column(columnDefinition = "longtext")
    private String text;

    @NotNull
    private Type type;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "language_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private LanguageJPA language;

    private LoopEntryRefJPA loopEntryRef;

    public TextJPA(Text from, Version version, LanguageJPA language) {
        super(from);
        setVersion(version);
        setLanguage(language);
    }

    public TextJPA(TextJPA from, Version version) {
        this(from, version, from.language);
    }

    @Override
    public Integer getDocId() {
        return version.getDocId();
    }

    @Override
    public String getLangCode() {
        return language.getCode();
    }

    @Override
    public void setLoopEntryRef(LoopEntryRef loopEntryRef) {
        this.loopEntryRef = (loopEntryRef == null) ? null : new LoopEntryRefJPA(loopEntryRef);
    }
}