
-- New schema version to assign after upgrade
SET @schema_version__major_new = 6;
SET @schema_version__minor_new = 2;

-- fix i18n language!!!

--
-- Document version support.
--

CREATE TABLE imcms_doc_versions (
    id int AUTO_INCREMENT,
    meta_id int NOT NULL,
    number int NOT NULL,
    created_by INT NULL,
    created_dt datetime NOT NULL,
    -- modified by, dt, etc

    CONSTRAINT pk__imcms_doc_versions PRIMARY KEY (id),
    CONSTRAINT uk__imcms_doc_versions__meta_id__number UNIQUE KEY (meta_id, number),
    CONSTRAINT fk__imcms_doc_versions__meta FOREIGN KEY (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
    CONSTRAINT fk__imcms_doc_versions__user FOREIGN KEY (created_by) REFERENCES users (user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO
    imcms_doc_versions (meta_id, number, created_by, created_dt)
SELECT
    meta_id, 0, owner_id, date_created
FROM
    meta;


--
-- Content loops support
--
CREATE TABLE imcms_text_doc_content_loops (
    id int AUTO_INCREMENT,
    meta_id int NOT NULL,
    doc_version_number int NOT NULL,
    no int NOT NULL,

    CONSTRAINT pk__imcms_text_doc_content_loops PRIMARY KEY (id),
    UNIQUE KEY uk__imcms_text_doc_content_loops__content (meta_id, doc_version_number, no),
    CONSTRAINT fk__imcms_text_doc_content_loops__imcms_doc_versions FOREIGN KEY (meta_id, doc_version_number) REFERENCES imcms_doc_versions (meta_id, number) ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Contnts are never deleted physically - they are disabled.
CREATE TABLE imcms_text_doc_contents (
  id int NOT NULL AUTO_INCREMENT,
  loop_id int DEFAULT NULL,
  `index` int NOT NULL,      
  order_index int NOT NULL,
  enabled tinyint NOT NULL DEFAULT TRUE,
        
  CONSTRAINT pk__imcms_text_doc_contents PRIMARY KEY (id),
  UNIQUE KEY uk__imcms_text_doc_contents_loop_id__order_index (loop_id, order_index),
  CONSTRAINT fk__imcms_text_doc_contents__imcms_text_doc_content_loops FOREIGN KEY (loop_id) REFERENCES imcms_text_doc_content_loops (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- text documents texts
CREATE TABLE imcms_text_doc_texts (
    id int NOT NULL AUTO_INCREMENT,
    meta_id int default NULL,
    doc_version_number INT NOT NULL,
    no int NOT NULL,
    text longtext NOT NULL,
    type int default NULL,
    language_id smallint NOT NULL,
    loop_no int DEFAULT NULL,
    loop_content_index int DEFAULT NULL,

    CONSTRAINT pk__imcms_text_doc_texts PRIMARY KEY (id),
    UNIQUE KEY uk__imcms_text_doc_texts__text (meta_id,doc_version_number,no,language_id,loop_no,loop_content_index),
    CONSTRAINT fk__imcms_text_doc_texts__languages FOREIGN KEY (language_id) REFERENCES i18n_languages (language_id),
    CONSTRAINT fk__imcms_text_doc_texts__meta FOREIGN KEY (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
    CONSTRAINT fk__imcms_text_doc_texts__doc_version FOREIGN KEY (meta_id, doc_version_number) REFERENCES imcms_doc_versions (meta_id, number) ON DELETE CASCADE
                
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Remove dublicates from texts table before copying
--
DELETE FROM texts
USING texts, texts AS self
WHERE texts.counter < self.counter
AND texts.meta_id = self.meta_id AND texts.name = self.name AND texts.language_id = self.language_id;


INSERT INTO imcms_text_doc_texts (
    meta_id,
    doc_version_number,
    no,
    text,
    type,
    language_id,
    loop_no,
    loop_content_index
) SELECT
    meta_id, 0, name, text, type, language_id, NULL, NULL
FROM texts;


-- #####TEXTS HISTORY######


-- Imqges
CREATE TABLE imcms_text_doc_images (
  id int NOT NULL AUTO_INCREMENT,
  meta_id int DEFAULT NULL,
  doc_version_number int NOT NULL,
  width int NOT NULL,
  height int NOT NULL,
  border int NOT NULL,
  v_space int NOT NULL,
  h_space int NOT NULL,
  no int NOT NULL,
  image_name varchar(40) NOT NULL DEFAULT '',
  target varchar(15) NOT NULL,
  align varchar(15) NOT NULL,
  alt_text varchar(255) NOT NULL,
  low_scr varchar(255) NOT NULL,
  imgurl varchar(255) NOT NULL,
  linkurl varchar(255) NOT NULL,
  type int NOT NULL,
  language_id smallint(6) NOT NULL,
  loop_no int DEFAULT NULL,
  loop_content_index int DEFAULT NULL,
        
  CONSTRAINT pk__imcms_text_doc_images PRIMARY KEY (id),
  UNIQUE KEY uk__imcms_text_doc_images__image (meta_id,doc_version_number,no,language_id,loop_no,loop_content_index),
  CONSTRAINT fk__imcms_text_doc_images__i18n_languages FOREIGN KEY (language_id) REFERENCES i18n_languages (language_id),
  CONSTRAINT fk__imcms_text_doc_images__meta FOREIGN KEY (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
  CONSTRAINT fk__imcms_text_doc_images__doc_version FOREIGN KEY (meta_id, doc_version_number) REFERENCES imcms_doc_versions (meta_id, number) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO imcms_text_doc_images (
  id,
  meta_id,
  doc_version_number,
  width,
  height,
  border,
  v_space,
  h_space,
  no,
  image_name,
  target,
  align,
  alt_text,
  low_scr,
  imgurl,
  linkurl,
  type,
  language_id,

  loop_no,
  loop_content_index

) SELECT
  image_id,
  meta_id,
  0,
  width,
  height,
  border,
  v_space,
  h_space,
  name,
  image_name,
  target,
  align,
  alt_text,
  low_scr,
  imgurl,
  linkurl,
  type,
  language_id,
  NULL,
  NULL
FROM images;

-- ####IMAGES_HISOTRY#####



-- ********************************************************************
-- FROM 5.3 - 6.0
--

-- Delete unused tables and related data
DROP TABLE browser_docs;
DROP TABLE browsers;

DELETE FROM meta WHERE doc_type = 6;
DELETE FROM doc_types WHERE doc_type = 6;
DELETE FROM doc_permissions WHERE doc_type NOT IN (2,5,7,8);

-- Text docuemnt menu items
--
CREATE TABLE __childs (
  id int auto_increment PRIMARY KEY,
  to_meta_id int(11) NOT NULL,
  manual_sort_order int(11) NOT NULL,
  tree_sort_index varchar(64) NOT NULL,
  menu_id int(11) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __childs
  (menu_id, manual_sort_order, tree_sort_index, to_meta_id)
SELECT
  menu_id, manual_sort_order, tree_sort_index, to_meta_id
FROM
  childs;

DROP TABLE childs;
RENAME TABLE __childs TO childs;

ALTER TABLE childs
  ADD CONSTRAINT fk__childs__menus FOREIGN KEY  (menu_id) REFERENCES menus (menu_id),
  ADD CONSTRAINT uk__childs__menu_id__meta_id UNIQUE INDEX  (menu_id, to_meta_id);


--
-- Includes table
--
CREATE TABLE __includes (
  id int auto_increment PRIMARY KEY,
  meta_id int NULL,
  include_id int NOT NULL,
  included_meta_id int NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __includes (meta_id, include_id, included_meta_id)
SELECT meta_id, include_id, included_meta_id FROM includes;

DROP TABLE includes;
RENAME TABLE __includes TO includes;

ALTER TABLE includes ADD UNIQUE INDEX ux__includes__meta_id__include_id(meta_id, include_id);
ALTER TABLE includes ADD FOREIGN KEY fk__includes__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE;
ALTER TABLE includes ADD FOREIGN KEY fk__includes__included_document (included_meta_id) REFERENCES meta (meta_id);


--
-- text_docs (template names) table
--

CREATE TABLE __text_docs (
  id int auto_increment PRIMARY KEY,
  meta_id int(11) NULL,
  template_name varchar(255) NOT NULL,
  group_id int(11) NOT NULL default '1',
  default_template_1 varchar(255) default NULL,
  default_template_2 varchar(255) default NULL,
  default_template varchar(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __text_docs (meta_id, template_name, group_id, default_template_1, default_template_2, default_template)
SELECT meta_id, template_name, group_id, default_template_1, default_template_2, default_template FROM text_docs;

DROP TABLE text_docs;
RENAME TABLE __text_docs TO text_docs;
ALTER TABLE text_docs ADD FOREIGN KEY fk__text_docs__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE;


--
-- Table new_doc_permission_sets_ex
--

CREATE TABLE __new_doc_permission_sets_ex (
  id int auto_increment PRIMARY KEY,
  meta_id int(11) NOT NULL,
  set_id int(11) NOT NULL,
  permission_id int(11) NOT NULL,
  permission_data int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __new_doc_permission_sets_ex (
  meta_id,
  set_id,
  permission_id,
  permission_data
) SELECT
  meta_id, set_id, permission_id, permission_data
FROM new_doc_permission_sets_ex;

DROP TABLE new_doc_permission_sets_ex;
RENAME TABLE __new_doc_permission_sets_ex TO new_doc_permission_sets_ex;

ALTER TABLE new_doc_permission_sets_ex
  ADD UNIQUE INDEX ux__new_doc_permission_sets_ex__1 (meta_id, set_id, permission_id, permission_data),
  ADD FOREIGN KEY  fk__new_doc_permission_sets_ex__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
  ADD FOREIGN KEY  fk__new_doc_permission_sets_ex__permission_sets (set_id) REFERENCES permission_sets (set_id);



CREATE TABLE __doc_permission_sets_ex (
  id int auto_increment PRIMARY KEY,
  meta_id int(11) NOT NULL,
  set_id int(11) NOT NULL,
  permission_id int(11) NOT NULL,
  permission_data int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __doc_permission_sets_ex (
  meta_id,
  set_id,
  permission_id,
  permission_data
) SELECT
  meta_id, set_id, permission_id, permission_data
FROM doc_permission_sets_ex;

DROP TABLE doc_permission_sets_ex;
RENAME TABLE __doc_permission_sets_ex TO doc_permission_sets_ex;

ALTER TABLE doc_permission_sets_ex
  ADD UNIQUE INDEX ux__doc_permission_sets_ex__1 (meta_id, set_id, permission_id, permission_data),
  ADD FOREIGN KEY  fk__doc_permission_sets_ex__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
  ADD FOREIGN KEY  fk__doc_permission_sets_ex__permission_sets (set_id) REFERENCES permission_sets (set_id);


CREATE TABLE __new_doc_permission_sets (
  id int auto_increment PRIMARY KEY,
  meta_id int(11) NOT NULL,
  set_id int(11) NOT NULL,
  permission_id int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO __new_doc_permission_sets (
  meta_id,
  set_id,
  permission_id
) SELECT
  meta_id, set_id, permission_id
FROM new_doc_permission_sets;

DROP TABLE new_doc_permission_sets;
RENAME TABLE __new_doc_permission_sets TO new_doc_permission_sets;

ALTER TABLE new_doc_permission_sets
  ADD UNIQUE INDEX ux__new_doc_permission_sets__meta_id__set_id (meta_id, set_id),
  ADD FOREIGN KEY  fk__new_doc_permission_sets__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
  ADD FOREIGN KEY  fk__new_doc_permission_sets__permission_sets (set_id) REFERENCES permission_sets (set_id);

--
-- Update permissions:
--

CREATE TABLE __doc_permission_sets (
  id int auto_increment PRIMARY KEY,
  meta_id int(11) NOT NULL,
  set_id int(11) NOT NULL,
  permission_id int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __doc_permission_sets (
  meta_id,
  set_id,
  permission_id
) SELECT
  meta_id, set_id, permission_id
FROM doc_permission_sets;

DROP TABLE doc_permission_sets;
RENAME TABLE __doc_permission_sets TO doc_permission_sets;

ALTER TABLE doc_permission_sets
  ADD UNIQUE INDEX ux__doc_permission_sets__meta_id__set_id (meta_id, set_id),
  ADD FOREIGN KEY  fk__doc_permission_sets__meta (meta_id) REFERENCES meta (meta_id) ON DELETE CASCADE,
  ADD FOREIGN KEY  fk__doc_permission_sets__permission_sets (set_id) REFERENCES permission_sets (set_id);


--
-- File upload table:
--
CREATE TABLE __fileupload_docs (
  id int auto_increment PRIMARY KEY,
  meta_id int NOT NULL,
  variant_name varchar(100) NOT NULL,
  filename varchar(255) NOT NULL,
  mime varchar(50) NOT NULL,
  created_as_image int(11) NOT NULL,
  default_variant tinyint(1) NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO __fileupload_docs (
  meta_id,
  variant_name,
  filename,
  mime,
  created_as_image,
  default_variant
) SELECT
  meta_id,
  variant_name,
  filename,
  mime,
  created_as_image,
  default_variant
FROM fileupload_docs;

DROP TABLE fileupload_docs;
RENAME TABLE __fileupload_docs TO fileupload_docs;

ALTER TABLE fileupload_docs
  ADD UNIQUE INDEX ux__fileupload_docs__meta_id__variant_name (meta_id, variant_name),
  ADD FOREIGN KEY fk__fileupload_docs__meta(meta_id) REFERENCES meta(meta_id) ON DELETE CASCADE;




--
-- Update schema version
--
UPDATE database_version
SET
  major = @schema_version__major_new,
  minor = @schema_version__minor_new;