INSERT INTO languages VALUES('swe','swe','Svenska')
INSERT INTO languages VALUES('swe','eng','Swedish')
INSERT INTO languages VALUES('eng','swe','Engelska')
INSERT INTO languages VALUES('eng','eng','English')
GO

INSERT INTO lang_prefixes VALUES(1,'swe')
INSERT INTO lang_prefixes VALUES(2,'eng')
GO

INSERT INTO doc_types VALUES(2, 'swe', 'Textsida')
INSERT INTO doc_types VALUES(5, 'swe', 'Extern l�nk')
INSERT INTO doc_types VALUES(6, 'swe', 'Browserkontroll')
INSERT INTO doc_types VALUES(7, 'swe', 'HTML-dokument')
INSERT INTO doc_types VALUES(8, 'swe', 'Fil')
INSERT INTO doc_types VALUES(102, 'swe', 'Konferens')
INSERT INTO doc_types VALUES(103, 'swe', 'Chatt')
INSERT INTO doc_types VALUES(104, 'swe', 'Anslagstavla')
GO

INSERT INTO doc_types VALUES(2, 'eng', 'Text page')
INSERT INTO doc_types VALUES(5, 'eng', 'External link')
INSERT INTO doc_types VALUES(6, 'eng', 'Browser controlled link')
INSERT INTO doc_types VALUES(7, 'eng', 'HTML-document')
INSERT INTO doc_types VALUES(8, 'eng', 'File')
INSERT INTO doc_types VALUES(102, 'eng', 'Conference')
INSERT INTO doc_types VALUES(103, 'eng', 'Chat')
INSERT INTO doc_types VALUES(104, 'eng', 'Billboard')
GO

INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,2,'swe','�ndra text')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,5,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,6,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,7,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,8,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,101,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,102,'swe','Redigera')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(131072,2,'swe','�ndra bild')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(262144,2,'swe','�ndra meny')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(524288,2,'swe','�ndra utseende')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(1048576,2,'swe','�ndra include')
GO

INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,2,'eng','Edit texts')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,5,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,6,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,7,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,8,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,101,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(65536,102,'eng','Edit')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(131072,2,'eng','Edit pictures')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(262144,2,'eng','Edit menus')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(524288,2,'eng','Change template')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(1048576,2,'eng','Change include')
GO

INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(1,'swe','�ndra rubrik')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(2,'swe','�ndra dokinfo')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(4,'swe','�ndra r�ttigheter f�r roller')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(8,'swe','Skapa dokument')
GO

INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(1,'eng','Edit headline')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(2,'eng','Edit docinfo')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(4,'eng','Edit permissions')
INSERT INTO permissions (permission_id, lang_prefix, description) VALUES(8,'eng','Create document')
GO

INSERT INTO roles_permissions (permission_id, lang_prefix, description) VALUES(1,'swe','R�tt att f� l�senord per mail')
INSERT INTO roles_permissions (permission_id, lang_prefix, description) VALUES(2,'swe','Sj�lvregistreringsr�tt i konferens')
GO

INSERT INTO roles_permissions (permission_id, lang_prefix, description) VALUES(1,'eng','Permission to get password by email')
INSERT INTO roles_permissions (permission_id, lang_prefix, description) VALUES(2,'eng','Selfregister rights in conference')
GO

INSERT INTO permission_sets (set_id, description) VALUES(0,'Full')
INSERT INTO permission_sets (set_id, description) VALUES(1,'Begr�nsad 1')
INSERT INTO permission_sets (set_id, description) VALUES(2,'Begr�nsad 2')
INSERT INTO permission_sets (set_id, description) VALUES(3,'L�s')
GO

INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(1,'Internet Explorer','%MSIE%',2)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(2,'Netscape','Mozilla%(%;%[UIN][);]%',2)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(3,'Internet Explorer 3','%MSIE 3%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(4,'Internet Explorer 4','%MSIE 4%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(5,'Internet Explorer 5','%MSIE 5%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(6,'Internet Explorer 6','%MSIE 6%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(7,'Netscape 3','Mozilla/3%(%;%[UIN][ );]%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(8,'Netscape 4','Mozilla/4%(%;%[UIN][ );]%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(9,'Netscape 6','Mozilla/5%(%;%[UIN][ );]%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(10,'Netscape 7','Mozilla%/5;%netscape/7%',4)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(11,'Windows','%win%',1)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(12,'Macintosh','%mac%',1)
GO

INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(13,'Windows Internet Explorer','%MSIE%win%',3)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(14,'Windows Internet Explorer 3','%MSIE 3%win%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(15,'Windows Internet Explorer 4','%MSIE 4%win%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(16,'Windows Internet Explorer 5.0','%MSIE 5.0%win%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(17,'Windows Internet Explorer 5.5','%MSIE 5.5%win%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(18,'Windows Internet Explorer 6','%MSIE 6%win%',5)
GO

INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(19,'Windows Netscape','Mozilla%(%win%;%[UIN][ );]%',3)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(20,'Windows Netscape 3','Mozilla/3%(%win%;%[UIN][ );]%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(21,'Windows Netscape 4','Mozilla/4%(%win%;%[UIN][ );]%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(22,'Windows Netscape 6','Mozilla/5%(%win%;%[UIN][ );]%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(23,'Windows Netscape 7','Mozilla/5%(%win%netscape/7%',5)
GO

INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(24,'Macintosh Internet Explorer','%MSIE%mac%',3)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(25,'Macintosh Internet Explorer 3','%MSIE 3%mac%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(26,'Macintosh Internet Explorer 4','%MSIE 4%mac%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(27,'Macintosh Internet Explorer 5','%MSIE 5%mac%',5)
GO

INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(28,'Macintosh Netscape','Mozilla%(%mac%;%[UIN][ );]%',3)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(29,'Macintosh Netscape 3','Mozilla/3%(%mac%;%[UIN][ );]%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(30,'Macintosh Netscape 4','Mozilla/4%(%mac%;%[UIN][ );]%',5)
INSERT INTO browsers (browser_id, name, user_agent, value) VALUES(31,'Macintosh Netscape 6','Mozilla/5%(%mac%;%[UIN][ );]%',5)
GO

SET IDENTITY_INSERT sys_types ON
INSERT INTO sys_types (type_id,name) VALUES(0,'StartDocument')
INSERT INTO sys_types (type_id,name) VALUES(1,'SessionCounter')
INSERT INTO sys_types (type_id,name) VALUES(2,'SessionCounterDate')
INSERT INTO sys_types (type_id,name) VALUES(3,'SystemMessage')
INSERT INTO sys_types (type_id,name) VALUES(4,'ServerMaster')
INSERT INTO sys_types (type_id,name) VALUES(5,'ServerMasterAddress')
INSERT INTO sys_types (type_id,name) VALUES(6,'WebMaster')
INSERT INTO sys_types (type_id,name) VALUES(7,'WebMasterAddress')
SET IDENTITY_INSERT sys_types OFF
GO

SET IDENTITY_INSERT mime_types ON
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(0,'Annan...','other','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(1,'Vanlig text','text/plain','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(2,'HTML-dokument','text/html','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(3,'Bin�rfil','application/octet-stream','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(4,'Shockwave Flash','application/x-shockwave-flash','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(5,'Shockwave Director','application/x-director','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(6,'PNG-bild','image/png','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(7,'GIF-bild','image/gif','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(8,'JPEG-bild','image/jpeg','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(9,'Adobe Acrobat-dokument','application/pdf','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(10,'Wav-ljud','audio/x-wav','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(11,'Zip-fil','application/zip','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(12,'AVI-film','video/x-msvideo','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(13,'Quicktime-film','video/quicktime','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(14,'MPEG-film','video/mpeg','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(15,'MS Word-dokument','application/msword','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(16,'MS Excel-dokument','application/vnd.ms-excel','swe')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(17,'MS Powerpoint-dokument','application/vnd.ms-powerpoint','swe')

INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(0,'Other...','other','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(1,'Plain text','text/plain','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(2,'HTML-document','text/html','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(3,'Binary file','application/octet-stream','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(4,'Shockwave Flash','application/x-shockwave-flash','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(5,'Shockwave Director','application/x-director','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(6,'PNG-image','image/png','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(7,'GIF-image','image/gif','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(8,'JPEG-image','image/jpeg','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(9,'Adobe Acrobat-document','application/pdf','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(10,'Wav-sound','audio/x-wav','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(11,'Zip-file','application/zip','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(12,'AVI-movie','video/x-msvideo','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(13,'Quicktime-movie','video/quicktime','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(14,'MPEG-movie','video/mpeg','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(15,'MS Word-document','application/msword','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(16,'MS Excel-document','application/vnd.ms-excel','eng')
INSERT INTO mime_types (mime_id, mime_name, mime, lang_prefix) VALUES(17,'MS Powerpoint-document','application/vnd.ms-powerpoint','eng')
SET IDENTITY_INSERT mime_types OFF
GO

SET IDENTITY_INSERT SORT_BY ON
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (1, 'meta_headline')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (2, 'meta_id')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (3, 'doc_type')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (4, 'date_modified')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (5, 'date_created')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (6, 'date_archived')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (7, 'date_activated')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (8, 'meta_text')
INSERT INTO SORT_BY ( sort_by_id, sort_by_type ) VALUES (9, 'archive')
SET IDENTITY_INSERT SORT_BY OFF
GO

INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(1,1,'Rubrik')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(1,2,'Meta headline')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(2,1,'Meta ID')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(2,2,'Meta ID')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(3,1,'Sidans typ')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(3,2,'Page type')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(4,1,'�ndrat datum')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(4,2,'Date modified')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(5,1,'Skapat datum')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(5,2,'Date created')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(6,1,'Arkiverat datum')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(6,2,'Archived date')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(7,1,'Aktiverat datum')
INSERT INTO display_name ( sort_by_id, lang_id, display_name ) VALUES(7,2,'Activated date')
GO

INSERT INTO phonetypes VALUES(0, 'Annat', 1 )
INSERT INTO phonetypes VALUES(1, 'Bostad', 1 )
INSERT INTO phonetypes VALUES(2, 'Arbete', 1 )
INSERT INTO phonetypes VALUES(3, 'Mobil', 1 )
INSERT INTO phonetypes VALUES(4, 'Fax', 1 )
INSERT INTO phonetypes VALUES(0, 'Other', 2 )
INSERT INTO phonetypes VALUES(1, 'Home', 2 )
INSERT INTO phonetypes VALUES(2, 'Work', 2 )
INSERT INTO phonetypes VALUES(3, 'Mobile', 2 )
INSERT INTO phonetypes VALUES(4, 'Fax', 2 )
GO
