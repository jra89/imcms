package com.imcode.imcms.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Content common to all document types.
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "imcms_doc_i18n_meta")
public class CommonContentJPA extends CommonContent<Language> {

    private static final int META_HEADLINE_MAX_LENGTH = 255;
    private static final int META_TEXT_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "doc_id")
    private Integer docId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    /**
     * Doc's headline label. Mainly used as HTML page title.
     */
    @Column(name = "headline")
    private String headline;

    /**
     * Menu item label.
     * Used when a doc is included in other doc's menu (as a menu item).
     */
    @Column(name = "menu_text", length = META_TEXT_MAX_LENGTH)
    private String menuText;

    /**
     * Menu item image.
     */
    @Column(name = "menu_image_url")
    private String menuImageURL;

    /**
     * Flag indicates is current language enabled for document
     */
    @Column(name = "is_enabled")
    private boolean isEnabled;

    /**
     * Related document version number
     */
    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    public CommonContentJPA(Integer docId, Language language, String headline, String menuText, String menuImageURL,
                            Boolean isEnabled, Integer versionNo) {
        this.docId = docId;
        this.language = language;
        this.headline = headline;
        this.menuText = menuText;
        this.menuImageURL = menuImageURL;
        this.isEnabled = isEnabled;
        this.versionNo = versionNo;
    }

    public CommonContentJPA(CommonContent from, Language language) {
        super(from, language);
    }
}
