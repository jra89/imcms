package com.imcode.imcms.api;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.*;

/**
 * Do not set content indexes values manually.
 * Do not modify contents. 
 *
 * @see com.imcode.imcms.dao.ContentLoopDao
 */
@Entity
@Table(name="imcms_text_doc_content_loops")
public class ContentLoop implements Cloneable {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Integer no;
	
	@Column(name="meta_id")
	private Integer metaId;
	
	@Column(name="doc_version_number")
	private Integer documentVersion;

	@OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.ALL})
    @JoinColumn(name="loop_id")
    @OrderBy("orderIndex")
	private List<Content> contents = new LinkedList<Content>();
	
	/**
	 * Modified flag.
	 * Object marked as modified is subject to update.
	 */
	@Transient
	private boolean modified = false;

    @Override
	public ContentLoop clone() {
		ContentLoop clone;
		
		try {
			clone = (ContentLoop)super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
		List<Content> contentsClone = new LinkedList<Content>();
		
		for (Content content: contents) {
			contentsClone.add(content.clone());
		}
		
		clone.setContents(contentsClone);
								
		return clone;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNo() {
		return no;
	}


	public void setNo(Integer no) {
		this.no = no;
	}

    @Deprecated
	public Integer getIndex() {
		return getNo();
	}

    @Deprecated
	public void setIndex(Integer index) {
		setNo(index);
	}	

	public Integer getMetaId() {
		return metaId;
	}

	public void setMetaId(Integer metaId) {
		this.metaId = metaId;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> loops) {
		this.contents = loops;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public Integer getDocumentVersion() {
		return documentVersion;
	}

	public void setDocumentVersion(Integer documentVersion) {
		this.documentVersion = documentVersion;
	}
}
