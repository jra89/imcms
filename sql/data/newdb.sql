SET IDENTITY_INSERT sys_data ON
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(0, 0, 1001)
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(1, 1, 0)
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(2, 2, convert(char(10),getDate(),120))
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(3, 3, '')
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(4, 4, '@servermaster-name@')
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(5, 5, '@servermaster-email@')
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(6, 6, '@webmaster-name@')
INSERT INTO sys_data (sys_id, type_id, value)
 VALUES(7, 7, '@webmaster-email@')
SET IDENTITY_INSERT sys_data OFF
INSERT INTO users
 VALUES (1,'admin', 'admin', 'Admin', 'Super','','','','','','','','',0,1001,0,<? sql/default_lang_id ?>,1,1,convert(char(10),getDate(),120))
INSERT INTO users VALUES (2,'user', 'user', 'User', 'Extern','','','','','','','','',0,1001,0,<? sql/default_lang_id ?>,1,1,convert(char(10),getDate(),120))
INSERT INTO roles
 VALUES(0, 'Superadmin', 0, 1)
INSERT INTO roles
 VALUES(1, 'Useradmin', 0, 2)
INSERT INTO roles
 VALUES(2, 'Users', 1, 0)
INSERT INTO user_roles_crossref
 VALUES(1,0)
INSERT INTO user_roles_crossref
 VALUES(2,2)

SET IDENTITY_INSERT meta ON
INSERT INTO meta (meta_id, doc_type, meta_headline,                meta_text, meta_image, owner_id, permissions, shared, show_meta, lang_prefix,         date_created,                    date_modified,                   disable_search, archived_datetime, target,  activate, status, publication_start_datetime,      publication_end_datetime)
 VALUES           (1001,   2,        '<? sql/sql/newdb.sql/1 ?>',  '',        '',         1,        0,           0,      0,         '@defaultlanguage@', convert(char(10),getDate(),120), convert(char(10),getDate(),120), 0,              null,              '_self', 1,        2,      convert(char(10),getDate(),120), null)
SET IDENTITY_INSERT meta OFF
INSERT INTO templates
 VALUES (1,'demo.html', 'demo', '<? sql/default_lang ?>', 1,1,1)
INSERT INTO templategroups
 VALUES (0, 'normal')
INSERT INTO templates_cref
 VALUES(0,1)

INSERT INTO text_docs
 VALUES (1001, 1, 0, -1, -1)
INSERT INTO roles_rights
 VALUES (2,1001,3)
INSERT INTO texts
 VALUES( 1001, 1, '<h2>imCode imCMS</h2><br><a href="@rooturl@/login/"><? sql/sql/newdb.sql/2 ?>!</a><br><a href="@rooturl@/servlet/SearchDocuments"><? sql/sql/newdb.sql/4 ?></a><br><br><a href="@documentationwebappurl@/"><? sql/sql/newdb.sql/5 ?></a><br><br><a href="@rooturl@/imcms/docs/apisamples/"><? sql/sql/newdb.sql/6 ?></a><br>',1)
INSERT INTO images ( meta_id , width , height , border , v_space , h_space , name , image_name , target , align , alt_text , low_scr , imgurl , linkurl )
values (1001,100,29,0,0,0,3,'','_blank','top','','','imCMSpower.gif','http://www.imcms.net')


