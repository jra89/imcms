-- diff.sql,v
-- Revision 1.2  2001/09/26 12:19:50  kreiger
-- Added default-templates to text_docs.
--

drop procedure [CheckDocSharePermissionForUser]
drop procedure [CheckUserDocSharePermission]

SET QUOTED_IDENTIFIER  OFF    SET ANSI_NULLS  OFF 
GO
CREATE PROCEDURE CheckUserDocSharePermission @user_id INT, @meta_id INT AS

SELECT m.meta_id
FROM meta m
JOIN user_roles_crossref urc
				ON	urc.user_id = @user_id
				AND	m.meta_id = @meta_id
LEFT join roles_rights rr
				ON	rr.meta_id = m.meta_id
				AND	rr.role_id = urc.role_id
WHERE				(
						shared = 1
					OR	rr.set_id < 3
					OR	urc.role_id = 0
				)
GROUP BY m.meta_id
GO

-- 2001-09-19

-- Add columns for default-templates to text-docs.

alter table text_docs 
add default_template_1 INT DEFAULT -1 NOT NULL

alter table text_docs 
add default_template_2 INT DEFAULT -1 NOT NULL

-- The procedure to suport the default_templates
GO
CREATE PROCEDURE [UpdateDefaultTemplates] 
 @meta_id INT,
 @template1 int,
 @template2 int
 AS
UPDATE text_docs
SET default_template_1= @template1,
default_template_2=@template2 
WHERE meta_id = @meta_id
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO
-- The CopyDocs-procedure must also be updated

alter PROCEDURE CopyDocs @documents_string VARCHAR(200), @parent_id INT, @menu_id INT, @user INT AS

CREATE TABLE #documents (
  meta_id VARCHAR(10)
)

CREATE TABLE #documents2 (
  meta_id VARCHAR(10)
)

DECLARE @substring VARCHAR(30)
DECLARE @index INT
DECLARE @endindex INT

IF LEN(@documents_string) > 0 BEGIN
 SET @index = 1
 WHILE @index <= LEN(@documents_string) BEGIN
  SET @endindex = CHARINDEX(',',@documents_string,@index+1)
  IF @endindex = 0 BEGIN
   SET @endindex = LEN(@documents_string)+1
  END --IF
  SET @substring = SUBSTRING(@documents_string,@index,@endindex-@index)
  INSERT INTO #documents2 VALUES (@substring)
  SET @index = @endindex + 1
 END -- WHILE
END -- IF

INSERT INTO 	#documents
SELECT		t.meta_id
FROM		#documents2 t
JOIN		meta m
					ON	t.meta_id = m.meta_id
JOIN		user_roles_crossref urc
					ON	urc.user_id = @user
LEFT JOIN	roles_rights rr
					ON	rr.set_id < 3
					AND	rr.meta_id = @parent_id
					AND	rr.role_id = urc.role_id
LEFT JOIN	doc_permission_sets_ex dpse
					ON	dpse.meta_id = @parent_id
					AND	dpse.set_id = rr.set_id
					AND	permission_id = 8
					AND	permission_data = m.doc_type
					AND	(
							rr.set_id = 1
						OR	rr.set_id = 2
					)
WHERE 		urc.role_id = 0	
	OR	rr.set_id = 0
	OR	dpse.permission_id = 8
GROUP BY	t.meta_id

DROP TABLE #documents2

DECLARE documents_cursor CURSOR FOR
SELECT	meta.meta_id,
	description,
	doc_type,
	meta_headline,
	meta_text,
	meta_image,
	owner_id,
	permissions,
	shared,
	expand,
	show_meta,
	help_text_id,
	archive,
	status_id,
	lang_prefix,
	classification,
	date_created,
	date_modified,
	sort_position,
	menu_position,
	disable_search,
	activated_date,
	activated_time,
	archived_date,
	archived_time,
	target,
	frame_name,
	activate
FROM	meta, #documents d
WHERE	meta.meta_id = d.meta_id

OPEN documents_cursor

DECLARE @meta_id int,
	@description varchar(80),
	@doc_type int,
	@meta_headline varchar(255),
	@meta_text varchar(1000),
	@meta_image varchar(255),
	@owner_id int,
	@permissions int,
	@shared int,
	@expand int,
	@show_meta int,
	@help_text_id int,
	@archive int,
	@status_id int,
	@lang_prefix varchar(3),
	@classification varchar(200),
	@date_created datetime,
	@date_modified datetime,
	@sort_position int,
	@menu_position int,
	@disable_search int,
	@activated_date varchar(10),
	@activated_time varchar(6),
	@archived_date varchar(10),
	@archived_time varchar(6),
	@target varchar(10),
	@frame_name varchar(20),
	@activate int

FETCH NEXT FROM documents_cursor
INTO	@meta_id,
 	@description,
	@doc_type,
	@meta_headline,
	@meta_text,
	@meta_image,
	@owner_id,
	@permissions,
	@shared,
	@expand,
	@show_meta,
	@help_text_id,
	@archive,
	@status_id,
	@lang_prefix,
	@classification,
	@date_created,
	@date_modified,
	@sort_position,
	@menu_position,
	@disable_search,
	@activated_date,
	@activated_time,
	@archived_date,
	@archived_time,
	@target,
	@frame_name,
	@activate

WHILE (@@FETCH_STATUS = 0) BEGIN
	INSERT INTO meta (
		description,
		doc_type,
		meta_headline,
		meta_text,
		meta_image,
		owner_id,
		permissions,
		shared,
		expand,
		show_meta,
		help_text_id,
		archive,
		status_id,
		lang_prefix,
		classification,
		date_created,
		date_modified,
		sort_position,
		menu_position,
		disable_search,
		activated_date,
		activated_time,
		archived_date,
		archived_time,
		target,
		frame_name,
		activate
	) VALUES (
		@description,
		@doc_type,
		@meta_headline + ' (2)',
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_date,
		@activated_time,
		@archived_date,
		@archived_time,
		@target,
		@frame_name,
		@activate
	)

	DECLARE @copy_id INT
	SET @copy_id = @@IDENTITY

	INSERT INTO text_docs 
	SELECT	@copy_id,
		template_id,
		group_id,
		sort_order,
		default_template_1,
		default_template_2
	FROM	text_docs
	WHERE	meta_id = @meta_id

	INSERT INTO url_docs
	SELECT	@copy_id,
		frame_name,
		target,
		url_ref,
		url_txt,
		lang_prefix
	FROM	url_docs
	WHERE	meta_id = @meta_id

	INSERT INTO browser_docs
	SELECT	@copy_id,
		to_meta_id,
		browser_id
	FROM	browser_docs
	WHERE	meta_id = @meta_id

	INSERT INTO frameset_docs
	SELECT	@copy_id,
		frame_set
	FROM	frameset_docs
	WHERE	meta_id = @meta_id

	INSERT INTO fileupload_docs
	SELECT	@copy_id,
		filename,
		mime
	FROM	fileupload_docs
	WHERE	meta_id = @meta_id

	INSERT INTO texts
	SELECT	@copy_id,
		name,
		text,
		type
	FROM	texts
	WHERE	meta_id = @meta_id

	INSERT INTO images
	SELECT	@copy_id,
		width,
		height,
		border,
		v_space,
		h_space,
		name,
		image_name,
		target,
		target_name,
		align,
		alt_text,
		low_scr,
		imgurl,
		linkurl
	FROM	images
	WHERE	meta_id = @meta_id

	INSERT INTO includes
	SELECT	@copy_id,
		include_id,
		included_meta_id
	FROM	includes
	WHERE	meta_id = @meta_id

	INSERT INTO doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	doc_permission_sets
	WHERE	meta_id = @meta_id

	INSERT INTO new_doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	new_doc_permission_sets
	WHERE	meta_id = @meta_id

	INSERT INTO doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	doc_permission_sets_ex
	WHERE	meta_id = @meta_id

	INSERT INTO new_doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	new_doc_permission_sets_ex
	WHERE	meta_id = @meta_id

	INSERT INTO roles_rights
	SELECT	role_id,
		@copy_id,
		set_id
	FROM	roles_rights
	WHERE	meta_id = @meta_id

	INSERT INTO user_rights
	SELECT	user_id,
		@copy_id,
		permission_id
	FROM	user_rights
	WHERE	meta_id = @meta_id

	INSERT INTO meta_classification
	SELECT	@copy_id,
		class_id
	FROM	meta_classification
	WHERE	meta_id = @meta_id

	INSERT INTO childs
	SELECT	@copy_id,
			to_meta_id,
			menu_sort,
			manual_sort_order
	FROM		childs
	WHERE	meta_id = @meta_id

	DECLARE @child_max INT
	-- FIXME: manual_sort_order should be an identity column
	SELECT @child_max = MAX(manual_sort_order)+10 FROM childs WHERE meta_id = @parent_id AND menu_sort = @menu_id
	INSERT INTO childs VALUES(@parent_id, @copy_id, @menu_id, @child_max)

	FETCH NEXT FROM documents_cursor
	INTO	@meta_id,
 		@description,
		@doc_type,
		@meta_headline,
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_date,
		@activated_time,
		@archived_date,
		@archived_time,
		@target,
		@frame_name,
		@activate

END --WHILE

CLOSE documents_cursor
DEALLOCATE documents_cursor

DROP TABLE #documents


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO



-- 2001-09-26
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(1048576,2,'se','�ndra include')
INSERT INTO doc_permissions (permission_id, doc_type, lang_prefix, description) VALUES(1048576,2,'uk','Change include')

-- 2001-09-28 
GO
CREATE PROCEDURE GetTemplateId
 @aTemplatename varchar(80)
 AS

SELECT template_id
FROM templates
WHERE simple_name = @aTemplatename
GO

-- 2001-10-08


ALTER PROCEDURE Classification_Fix
--peter har gjort om denna s� att den numera inte anv�nder semicolon som separator utan endast komma
 @meta_id int ,
 @string varchar(2000)
AS
declare @value varchar(50)
declare @pos int
-- Lets delete all current crossreferences, if any
DELETE 
FROM meta_classification 
WHERE meta_id = @meta_id
--SELECT @string = 'ett;tv�;tre;fyra;fem'
-- Lets search for semicolon, if not found then look for a , This is relevant 
-- when we convert the db. After convertion, only look for semicolons
--SELECT @pos = PATINDEX('%;%', @string)
--IF( @pos = 0 ) BEGIN
 SELECT @pos = PATINDEX('%,%', @string)
--END
WHILE @pos > 0
BEGIN
 SELECT @value = LEFT(@string,@pos-1)
 SELECT @pos = LEN(@string) - @pos
 SELECT @string = RIGHT(@string,@pos)
 SELECT  @value  = lTrim(rTrim( ( @value ) )) 
 EXEC ClassificationAdd @meta_id , @value
 --INSERT INTO data (value) VALUES (@value)
 SELECT @pos = PATINDEX('%,%', @string)
 -- PRINT @value
END
-- Lets get the last part of the string
--PRINT @string
SELECT @value = @string
SELECT  @value  = lTrim(rTrim( ( @value ) )) 
EXEC ClassificationAdd @meta_id , @value
-- INSERT INTO data (value) VALUES (@string)
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO


-- 2001-10-29


--Changed the procedure so the prefix who thells its a copy must bee supplied as a param

ALTER PROCEDURE CopyDocs @documents_string VARCHAR(200), @parent_id INT, @menu_id INT, @user INT, @copyPrefix VARCHAR(20) AS
CREATE TABLE #documents (
  meta_id VARCHAR(10)
)
CREATE TABLE #documents2 (
  meta_id VARCHAR(10)
)
DECLARE @substring VARCHAR(30)
DECLARE @index INT
DECLARE @endindex INT
IF LEN(@documents_string) > 0 BEGIN
 SET @index = 1
 WHILE @index <= LEN(@documents_string) BEGIN
  SET @endindex = CHARINDEX(',',@documents_string,@index+1)
  IF @endindex = 0 BEGIN
   SET @endindex = LEN(@documents_string)+1
  END --IF
  SET @substring = SUBSTRING(@documents_string,@index,@endindex-@index)
  INSERT INTO #documents2 VALUES (@substring)
  SET @index = @endindex + 1
 END -- WHILE
END -- IF
INSERT INTO 	#documents
SELECT		t.meta_id
FROM		#documents2 t
JOIN		meta m
					ON	t.meta_id = m.meta_id
JOIN		user_roles_crossref urc
					ON	urc.user_id = @user
LEFT JOIN	roles_rights rr
					ON	rr.set_id < 3
					AND	rr.meta_id = @parent_id
					AND	rr.role_id = urc.role_id
LEFT JOIN	doc_permission_sets_ex dpse
					ON	dpse.meta_id = @parent_id
					AND	dpse.set_id = rr.set_id
					AND	permission_id = 8
					AND	permission_data = m.doc_type
					AND	(
							rr.set_id = 1
						OR	rr.set_id = 2
					)
WHERE 		urc.role_id = 0	
	OR	rr.set_id = 0
	OR	dpse.permission_id = 8
GROUP BY	t.meta_id
DROP TABLE #documents2
DECLARE documents_cursor CURSOR FOR
SELECT	meta.meta_id,
	description,
	doc_type,
	meta_headline,
	meta_text,
	meta_image,
	owner_id,
	permissions,
	shared,
	expand,
	show_meta,
	help_text_id,
	archive,
	status_id,
	lang_prefix,
	classification,
	date_created,
	date_modified,
	sort_position,
	menu_position,
	disable_search,
	activated_date,
	activated_time,
	archived_date,
	archived_time,
	target,
	frame_name,
	activate
FROM	meta, #documents d
WHERE	meta.meta_id = d.meta_id
OPEN documents_cursor
DECLARE @meta_id int,
	@description varchar(80),
	@doc_type int,
	@meta_headline varchar(255),
	@meta_text varchar(1000),
	@meta_image varchar(255),
	@owner_id int,
	@permissions int,
	@shared int,
	@expand int,
	@show_meta int,
	@help_text_id int,
	@archive int,
	@status_id int,
	@lang_prefix varchar(3),
	@classification varchar(200),
	@date_created datetime,
	@date_modified datetime,
	@sort_position int,
	@menu_position int,
	@disable_search int,
	@activated_date varchar(10),
	@activated_time varchar(6),
	@archived_date varchar(10),
	@archived_time varchar(6),
	@target varchar(10),
	@frame_name varchar(20),
	@activate int
FETCH NEXT FROM documents_cursor
INTO	@meta_id,
 	@description,
	@doc_type,
	@meta_headline,
	@meta_text,
	@meta_image,
	@owner_id,
	@permissions,
	@shared,
	@expand,
	@show_meta,
	@help_text_id,
	@archive,
	@status_id,
	@lang_prefix,
	@classification,
	@date_created,
	@date_modified,
	@sort_position,
	@menu_position,
	@disable_search,
	@activated_date,
	@activated_time,
	@archived_date,
	@archived_time,
	@target,
	@frame_name,
	@activate
WHILE (@@FETCH_STATUS = 0) BEGIN
	INSERT INTO meta (
		description,
		doc_type,
		meta_headline,
		meta_text,
		meta_image,
		owner_id,
		permissions,
		shared,
		expand,
		show_meta,
		help_text_id,
		archive,
		status_id,
		lang_prefix,
		classification,
		date_created,
		date_modified,
		sort_position,
		menu_position,
		disable_search,
		activated_date,
		activated_time,
		archived_date,
		archived_time,
		target,
		frame_name,
		activate
	) VALUES (
		@description,
		@doc_type,
		@meta_headline + @copyPrefix,
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_date,
		@activated_time,
		@archived_date,
		@archived_time,
		@target,
		@frame_name,
		@activate
	)
	DECLARE @copy_id INT
	SET @copy_id = @@IDENTITY
	INSERT INTO text_docs 
	SELECT	@copy_id,
		template_id,
		group_id,
		sort_order,
		default_template_1,
		default_template_2
	FROM	text_docs
	WHERE	meta_id = @meta_id
	INSERT INTO url_docs
	SELECT	@copy_id,
		frame_name,
		target,
		url_ref,
		url_txt,
		lang_prefix
	FROM	url_docs
	WHERE	meta_id = @meta_id
	INSERT INTO browser_docs
	SELECT	@copy_id,
		to_meta_id,
		browser_id
	FROM	browser_docs
	WHERE	meta_id = @meta_id
	INSERT INTO frameset_docs
	SELECT	@copy_id,
		frame_set
	FROM	frameset_docs
	WHERE	meta_id = @meta_id
	INSERT INTO fileupload_docs
	SELECT	@copy_id,
		filename,
		mime
	FROM	fileupload_docs
	WHERE	meta_id = @meta_id
	INSERT INTO texts
	SELECT	@copy_id,
		name,
		text,
		type
	FROM	texts
	WHERE	meta_id = @meta_id
	INSERT INTO images
	SELECT	@copy_id,
		width,
		height,
		border,
		v_space,
		h_space,
		name,
		image_name,
		target,
		target_name,
		align,
		alt_text,
		low_scr,
		imgurl,
		linkurl
	FROM	images
	WHERE	meta_id = @meta_id
	INSERT INTO includes
	SELECT	@copy_id,
		include_id,
		included_meta_id
	FROM	includes
	WHERE	meta_id = @meta_id
	INSERT INTO doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	doc_permission_sets
	WHERE	meta_id = @meta_id
	INSERT INTO new_doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	new_doc_permission_sets
	WHERE	meta_id = @meta_id
	INSERT INTO doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	doc_permission_sets_ex
	WHERE	meta_id = @meta_id
	INSERT INTO new_doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	new_doc_permission_sets_ex
	WHERE	meta_id = @meta_id
	INSERT INTO roles_rights
	SELECT	role_id,
		@copy_id,
		set_id
	FROM	roles_rights
	WHERE	meta_id = @meta_id
	INSERT INTO user_rights
	SELECT	user_id,
		@copy_id,
		permission_id
	FROM	user_rights
	WHERE	meta_id = @meta_id
	INSERT INTO meta_classification
	SELECT	@copy_id,
		class_id
	FROM	meta_classification
	WHERE	meta_id = @meta_id
	INSERT INTO childs
	SELECT	@copy_id,
			to_meta_id,
			menu_sort,
			manual_sort_order
	FROM		childs
	WHERE	meta_id = @meta_id
	DECLARE @child_max INT
	-- FIXME: manual_sort_order should be an identity column
	SELECT @child_max = MAX(manual_sort_order)+10 FROM childs WHERE meta_id = @parent_id AND menu_sort = @menu_id
	INSERT INTO childs VALUES(@parent_id, @copy_id, @menu_id, @child_max)
	FETCH NEXT FROM documents_cursor
	INTO	@meta_id,
 		@description,
		@doc_type,
		@meta_headline,
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_date,
		@activated_time,
		@archived_date,
		@archived_time,
		@target,
		@frame_name,
		@activate
END --WHILE
CLOSE documents_cursor
DEALLOCATE documents_cursor
DROP TABLE #documents
GO

-- 2001-10-30

--Changed the procedure so it only gets the document ones

ALTER PROCEDURE ListDocsGetInternalDocTypesValue AS
/* selct all internal doc types */
select distinct doc_type
from doc_types
where doc_type <= 100
GO

-- 2001-11-12

--Changed the procedure so it only select stuff from one langue at the time

ALTER PROCEDURE [GetPermissionSet] @meta_id INT, @set_id INT, @lang_prefix VARCHAR(3) AS
/*
 Nice little query that returns which permissions a permissionset consists of.
 Column 1: The id of the permission
 Column 2: The description of the permission
 Column 3: Wether the permission is set. 0 or 1.
*/
SELECT p.permission_id AS p_id, p.description,CAST(ISNULL((p.permission_id & dps.permission_id),0) AS BIT)
FROM   doc_permission_sets dps
RIGHT JOIN permissions p
       ON (p.permission_id & dps.permission_id) > 0
       WHERE dps.meta_id = @meta_id
       AND dps.set_id = @set_id
       AND p.lang_prefix = @lang_prefix
UNION
SELECT dp.permission_id AS p_id, dp.description,CAST(ISNULL((dp.permission_id & dps.permission_id),0) AS BIT)
FROM   meta m
JOIN  doc_permissions dp
       ON dp.doc_type = m.doc_type
       AND m.meta_id = @meta_id
       AND dp.lang_prefix = @lang_prefix
LEFT JOIN doc_permission_sets dps
       ON (dp.permission_id & dps.permission_id) > 0
       AND dps.set_id = @set_id
       AND dps.meta_id = m.meta_id
GO

-- 2001-11-13  -- Add proper fields for activated and archived date-times. ALTER TABLE meta ADD activated_datetime DATETIME ALTER TABLE meta ADD archived_datetime DATETIME  -- Migrate the old ugly fields to the new nice ones UPDATE meta SET activated_datetime = NULLIF(activated_date+' '+activated_time,'') UPDATE meta SET archived_datetime = NULLIF(archived_date+' '+archived_time,'')  -- Drop the old bastards. ALTER TABLE meta DROP COLUMN activated_date ALTER TABLE meta DROP COLUMN activated_time ALTER TABLE meta DROP COLUMN archived_date ALTER TABLE meta DROP COLUMN archived_time  GO DROP PROCEDURE GetChilds GO -- -- Procedure Create -- dbo.GetChilds -- CREATE PROCEDURE GetChilds  @meta_id int,  @user_id int AS /* Nice little query that lists the children of a document that a particular user may see, and includes a field that tells you wether he may do something to it or not. */ declare @sort_by int select @sort_by = sort_order from text_docs where meta_id = @meta_id -- Manual sort order if @sort_by = 2 begin select  to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   min(urc.role_id * ISNULL(~CAST(dps.permission_id AS BIT),1) * ISNULL(rr.set_id,1)),   fd.filename from   childs c join   meta m          on    m.meta_id = c.to_meta_id     -- meta.meta_id corresponds to childs.to_meta_id      and  m.activate > 0       -- Only include the documents that are active in the meta table.      and  c.meta_id = @meta_id      -- Only include documents that are children to this particular meta_id left join roles_rights rr            -- We may have permission, even though we don't have anything in role-permissions... That is, if we're docowner or superadmin.      on  c.to_meta_id = rr.meta_id      -- Only include rows with the documents we are interested in left join doc_permission_sets dps           -- Include the permission_sets      on  c.to_meta_id = dps.meta_id     -- for each document      and dps.set_id = rr.set_id      -- and only the sets for the roles we are interested in      and dps.permission_id > 0      -- and only the sets that have any permission join user_roles_crossref urc           -- This table tells us which users have which roles      on urc.user_id = @user_id      -- Only include the rows with the user we are interested in...      and (         rr.role_id = urc.role_id     -- Include rows where the users roles match the roles that have permissions on the documents       or  urc.role_id = 0      -- and also include the rows that tells us this user is a superadmin       or  (         m.show_meta != 0    -- and also include documents that are to be shown regardless of rights. (Visa �ven f�r obeh�riga)        and ISNULL(~CAST(dps.permission_id AS BIT),1) != 1       )      ) left join fileupload_docs fd      on  fd.meta_id = c.to_meta_id group by to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   fd.filename order by  menu_sort,c.manual_sort_order desc end else if @sort_by = 3 begin select  to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   min(urc.role_id * ISNULL(~CAST(dps.permission_id AS BIT),1) * ISNULL(rr.set_id,1)),   fd.filename from   childs c join   meta m          on    m.meta_id = c.to_meta_id     -- meta.meta_id corresponds to childs.to_meta_id      and  m.activate > 0       -- Only include the documents that are active in the meta table.      and  c.meta_id = @meta_id      -- Only include documents that are children to this particular meta_id left join roles_rights rr            -- We may have permission, even though we don't have anything in role-permissions... That is, if we're docowner or superadmin.      on  c.to_meta_id = rr.meta_id      -- Only include rows with the documents we are interested in left join doc_permission_sets dps           -- Include the permission_sets      on  c.to_meta_id = dps.meta_id     -- for each document      and dps.set_id = rr.set_id      -- and only the sets for the roles we are interested in      and dps.permission_id > 0      -- and only the sets that have any permission join user_roles_crossref urc           -- This table tells us which users have which roles      on urc.user_id = @user_id      -- Only include the rows with the user we are interested in...      and (         rr.role_id = urc.role_id     -- Include rows where the users roles match the roles that have permissions on the documents       or  urc.role_id = 0      -- and also include the rows that tells us this user is a superadmin       or  (         m.show_meta != 0    -- and also include documents that are to be shown regardless of rights. (Visa �ven f�r obeh�riga)        and ISNULL(~CAST(dps.permission_id AS BIT),1) != 1       )      ) left join fileupload_docs fd      on  fd.meta_id = c.to_meta_id group by to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   fd.filename order by  menu_sort,convert (varchar,date_created,120) desc end else if @sort_by = 1 begin select  to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   min(urc.role_id * ISNULL(~CAST(dps.permission_id AS BIT),1) * ISNULL(rr.set_id,1)),   fd.filename from   childs c join   meta m          on    m.meta_id = c.to_meta_id     -- meta.meta_id corresponds to childs.to_meta_id      and  m.activate > 0       -- Only include the documents that are active in the meta table.      and  c.meta_id = @meta_id      -- Only include documents that are children to this particular meta_id left join roles_rights rr            -- We may have permission, even though we don't have anything in role-permissions... That is, if we're docowner or superadmin.      on  c.to_meta_id = rr.meta_id      -- Only include rows with the documents we are interested in left join doc_permission_sets dps           -- Include the permission_sets      on  c.to_meta_id = dps.meta_id     -- for each document      and dps.set_id = rr.set_id      -- and only the sets for the roles we are interested in      and dps.permission_id > 0      -- and only the sets that have any permission join user_roles_crossref urc           -- This table tells us which users have which roles      on urc.user_id = @user_id      -- Only include the rows with the user we are interested in...      and (         rr.role_id = urc.role_id     -- Include rows where the users roles match the roles that have permissions on the documents       or  urc.role_id = 0      -- and also include the rows that tells us this user is a superadmin       or  (         m.show_meta != 0    -- and also include documents that are to be shown regardless of rights. (Visa �ven f�r obeh�riga)        and ISNULL(~CAST(dps.permission_id AS BIT),1) != 1       )      ) left join fileupload_docs fd      on  fd.meta_id = c.to_meta_id group by to_meta_id, c.menu_sort,manual_sort_order, doc_type,   archive,target, convert (varchar,date_created,120), convert (varchar,date_modified,120),   meta_headline,meta_text,meta_image,frame_name,   activated_datetime,archived_datetime,   fd.filename order by  menu_sort,meta_headline end GO  GO DROP PROCEDURE CopyDocs GO CREATE PROCEDURE CopyDocs @documents_string VARCHAR(200), @parent_id INT, @menu_id INT, @user INT, @copyPrefix VARCHAR(20) AS
CREATE TABLE #documents (
  meta_id VARCHAR(10)
)
CREATE TABLE #documents2 (
  meta_id VARCHAR(10)
)
DECLARE @substring VARCHAR(30)
DECLARE @index INT
DECLARE @endindex INT
IF LEN(@documents_string) > 0 BEGIN
 SET @index = 1
 WHILE @index <= LEN(@documents_string) BEGIN
  SET @endindex = CHARINDEX(',',@documents_string,@index+1)
  IF @endindex = 0 BEGIN
   SET @endindex = LEN(@documents_string)+1
  END --IF
  SET @substring = SUBSTRING(@documents_string,@index,@endindex-@index)
  INSERT INTO #documents2 VALUES (@substring)
  SET @index = @endindex + 1
 END -- WHILE
END -- IF
INSERT INTO 	#documents
SELECT		t.meta_id
FROM		#documents2 t
JOIN		meta m
					ON	t.meta_id = m.meta_id
JOIN		user_roles_crossref urc
					ON	urc.user_id = @user
LEFT JOIN	roles_rights rr
					ON	rr.set_id < 3
					AND	rr.meta_id = @parent_id
					AND	rr.role_id = urc.role_id
LEFT JOIN	doc_permission_sets_ex dpse
					ON	dpse.meta_id = @parent_id
					AND	dpse.set_id = rr.set_id
					AND	permission_id = 8
					AND	permission_data = m.doc_type
					AND	(
							rr.set_id = 1
						OR	rr.set_id = 2
					)
WHERE 		urc.role_id = 0	
	OR	rr.set_id = 0
	OR	dpse.permission_id = 8
GROUP BY	t.meta_id
DROP TABLE #documents2
DECLARE documents_cursor CURSOR FOR
SELECT	meta.meta_id,
	description,
	doc_type,
	meta_headline,
	meta_text,
	meta_image,
	owner_id,
	permissions,
	shared,
	expand,
	show_meta,
	help_text_id,
	archive,
	status_id,
	lang_prefix,
	classification,
	date_created,
	date_modified,
	sort_position,
	menu_position,
	disable_search,
	activated_datetime,
	archived_datetime,
	target,
	frame_name,
	activate
FROM	meta, #documents d
WHERE	meta.meta_id = d.meta_id
OPEN documents_cursor
DECLARE @meta_id int,
	@description varchar(80),
	@doc_type int,
	@meta_headline varchar(255),
	@meta_text varchar(1000),
	@meta_image varchar(255),
	@owner_id int,
	@permissions int,
	@shared int,
	@expand int,
	@show_meta int,
	@help_text_id int,
	@archive int,
	@status_id int,
	@lang_prefix varchar(3),
	@classification varchar(200),
	@date_created datetime,
	@date_modified datetime,
	@sort_position int,
	@menu_position int,
	@disable_search int,
	@activated_datetime datetime,
	@archived_datetime datetime,
	@target varchar(10),
	@frame_name varchar(20),
	@activate int
FETCH NEXT FROM documents_cursor
INTO	@meta_id,
 	@description,
	@doc_type,
	@meta_headline,
	@meta_text,
	@meta_image,
	@owner_id,
	@permissions,
	@shared,
	@expand,
	@show_meta,
	@help_text_id,
	@archive,
	@status_id,
	@lang_prefix,
	@classification,
	@date_created,
	@date_modified,
	@sort_position,
	@menu_position,
	@disable_search,
	@activated_datetime,
	@archived_datetime,
	@target,
	@frame_name,
	@activate
WHILE (@@FETCH_STATUS = 0) BEGIN
	INSERT INTO meta (
		description,
		doc_type,
		meta_headline,
		meta_text,
		meta_image,
		owner_id,
		permissions,
		shared,
		expand,
		show_meta,
		help_text_id,
		archive,
		status_id,
		lang_prefix,
		classification,
		date_created,
		date_modified,
		sort_position,
		menu_position,
		disable_search,
		activated_datetime,
		archived_datetime,
		target,
		frame_name,
		activate
	) VALUES (
		@description,
		@doc_type,
		@meta_headline + @copyPrefix,
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_datetime,
		@archived_datetime,
		@target,
		@frame_name,
		@activate
	)
	DECLARE @copy_id INT
	SET @copy_id = @@IDENTITY
	INSERT INTO text_docs 
	SELECT	@copy_id,
		template_id,
		group_id,
		sort_order,
		default_template_1,
		default_template_2
	FROM	text_docs
	WHERE	meta_id = @meta_id
	INSERT INTO url_docs
	SELECT	@copy_id,
		frame_name,
		target,
		url_ref,
		url_txt,
		lang_prefix
	FROM	url_docs
	WHERE	meta_id = @meta_id
	INSERT INTO browser_docs
	SELECT	@copy_id,
		to_meta_id,
		browser_id
	FROM	browser_docs
	WHERE	meta_id = @meta_id
	INSERT INTO frameset_docs
	SELECT	@copy_id,
		frame_set
	FROM	frameset_docs
	WHERE	meta_id = @meta_id
	INSERT INTO fileupload_docs
	SELECT	@copy_id,
		filename,
		mime
	FROM	fileupload_docs
	WHERE	meta_id = @meta_id
	INSERT INTO texts
	SELECT	@copy_id,
		name,
		text,
		type
	FROM	texts
	WHERE	meta_id = @meta_id
	INSERT INTO images
	SELECT	@copy_id,
		width,
		height,
		border,
		v_space,
		h_space,
		name,
		image_name,
		target,
		target_name,
		align,
		alt_text,
		low_scr,
		imgurl,
		linkurl
	FROM	images
	WHERE	meta_id = @meta_id
	INSERT INTO includes
	SELECT	@copy_id,
		include_id,
		included_meta_id
	FROM	includes
	WHERE	meta_id = @meta_id
	INSERT INTO doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	doc_permission_sets
	WHERE	meta_id = @meta_id
	INSERT INTO new_doc_permission_sets
	SELECT	@copy_id,
		set_id,
		permission_id
	FROM	new_doc_permission_sets
	WHERE	meta_id = @meta_id
	INSERT INTO doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	doc_permission_sets_ex
	WHERE	meta_id = @meta_id
	INSERT INTO new_doc_permission_sets_ex
	SELECT	@copy_id,
		set_id,
		permission_id,
		permission_data
	FROM	new_doc_permission_sets_ex
	WHERE	meta_id = @meta_id
	INSERT INTO roles_rights
	SELECT	role_id,
		@copy_id,
		set_id
	FROM	roles_rights
	WHERE	meta_id = @meta_id
	INSERT INTO user_rights
	SELECT	user_id,
		@copy_id,
		permission_id
	FROM	user_rights
	WHERE	meta_id = @meta_id
	INSERT INTO meta_classification
	SELECT	@copy_id,
		class_id
	FROM	meta_classification
	WHERE	meta_id = @meta_id
	INSERT INTO childs
	SELECT	@copy_id,
			to_meta_id,
			menu_sort,
			manual_sort_order
	FROM		childs
	WHERE	meta_id = @meta_id
	DECLARE @child_max INT
	-- FIXME: manual_sort_order should be an identity column
	SELECT @child_max = MAX(manual_sort_order)+10 FROM childs WHERE meta_id = @parent_id AND menu_sort = @menu_id
	INSERT INTO childs VALUES(@parent_id, @copy_id, @menu_id, @child_max)
	FETCH NEXT FROM documents_cursor
	INTO	@meta_id,
 		@description,
		@doc_type,
		@meta_headline,
		@meta_text,
		@meta_image,
		@owner_id,
		@permissions,
		@shared,
		@expand,
		@show_meta,
		@help_text_id,
		@archive,
		@status_id,
		@lang_prefix,
		@classification,
		@date_created,
		@date_modified,
		@sort_position,
		@menu_position,
		@disable_search,
		@activated_datetime,
		@archived_datetime,
		@target,
		@frame_name,
		@activate
END --WHILE
CLOSE documents_cursor
DEALLOCATE documents_cursor
DROP TABLE #documents
GO
  -- 2001-11-15

-- Added a parameter that tells if only to search on words that starts with the search-string
-- the SearchDocs-method is used by the servlets GetExistingDoc and SearchDocuments.

GO

ALTER PROCEDURE SearchDocs
  @user_id INT,
  @keyword_string VARCHAR(128),  -- Must be large enough to encompass an entire searchstring.
  @and_mode VARCHAR(3),  -- 'AND' or something else
  @doc_types_string VARCHAR(30), -- Must be large enough to encompass all possible doc_types, commaseparated and expressed in decimal notation.
  @fromdoc INT,
  @num_docs INT,
  @sortorder VARCHAR(256),  -- doc_type, date_modified, date_created, archived_datetime, activated_datetime, meta_id, meta_headline
  @created_startdate DATETIME,
  @created_enddate DATETIME,
  @modified_startdate DATETIME,
  @modified_enddate DATETIME,
  @activated_startdate DATETIME,
  @activated_enddate DATETIME,
  @archived_startdate DATETIME,
  @archived_enddate DATETIME,
  @only_addable TINYINT,  -- 1 to show only documents the user may add.
  @starts_with TINYINT -- 1 to search only on words that starts with the search-string
AS
/*
SET @keyword_string = 'kreiger'+CHAR(13)+'test'
SET @doc_types_string = '2,5,6,7,8,101,102'
SET @created_startdate = '1999-09-01'
SET @created_enddate = '2000-01-01'
SET @modified_startdate = ''
SET @modified_enddate = ''
SET @activated_startdate = ''
SET @activated_enddate = ''
SET @archived_startdate = ''
SET @archived_enddate = ''
SET @and_mode = 'OR'
SET @user_id = 98
SET @fromdoc = 1
SET @num_docs = 10
SET @sortorder = 'doc_type,meta_id DESC'
*/
SET nocount on
SET @fromdoc = @fromdoc - 1
DECLARE @created_sd DATETIME,
  @modified_sd DATETIME,
  @activated_sd DATETIME,
  @archived_sd DATETIME,
  @created_ed DATETIME,
  @modified_ed DATETIME,
  @activated_ed DATETIME,
  @archived_ed DATETIME,
  @search_start VARCHAR(5)
if(@starts_with=1) Begin
	set @search_start='% '
end else begin
	set @search_start='%'
end
IF (@created_startdate = '') BEGIN
 SET @created_sd = '1753-01-01'
END ELSE BEGIN
 SET @created_sd = @created_startdate
END
IF (@modified_startdate = '') BEGIN
 SET @modified_sd = '1753-01-01'
END ELSE BEGIN
 SET @modified_sd = @modified_startdate
END
IF (@activated_startdate = '') BEGIN
 SET @activated_sd = '1753-01-01'
END ELSE BEGIN
 SET @activated_sd = @activated_startdate
END
IF (@archived_startdate = '') BEGIN
 SET @archived_sd = '1753-01-01'
END ELSE BEGIN
 SET @archived_sd = @archived_startdate
END
IF (@created_enddate = '') BEGIN
 IF (@created_startdate = '') BEGIN
  SET @created_ed = '1753-01-01'
 END ELSE BEGIN
  SET @created_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @created_ed = @created_enddate
END
IF (@modified_enddate = '') BEGIN
 IF (@modified_startdate = '') BEGIN
  SET @modified_ed = '1753-01-01'
 END ELSE BEGIN
  SET @modified_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @modified_ed = @modified_enddate
END
IF (@activated_enddate = '') BEGIN
 IF (@activated_startdate = '') BEGIN
  SET @activated_ed = '1753-01-01'
 END ELSE BEGIN
  SET @activated_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @activated_ed = @activated_enddate
END
IF (@archived_enddate = '') BEGIN
 IF (@archived_startdate = '') BEGIN
  SET @archived_ed = '1753-01-01'
 END ELSE BEGIN
  SET @archived_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @archived_ed = @archived_enddate
END
CREATE TABLE #doc_types (
 doc_type INT 
)
CREATE TABLE #keywords (
  keyword VARCHAR(30)
)
DECLARE @substring VARCHAR(30)
DECLARE @index INT
DECLARE @endindex INT
IF LEN(@doc_types_string) > 0 BEGIN
 SET @index = 1
 WHILE @index <= LEN(@doc_types_string) BEGIN
  SET @endindex = CHARINDEX(',',@doc_types_string,@index+1)
  IF @endindex = 0 BEGIN
   SET @endindex = LEN(@doc_types_string)+1
  END --IF
  SET @substring = SUBSTRING(@doc_types_string,@index,@endindex-@index)
  INSERT INTO #doc_types VALUES (@substring)
  SET @index = @endindex + 1
 END -- WHILE
END -- IF
IF LEN(@keyword_string) > 0 BEGIN
 SET @index = 1
 WHILE @index <= LEN(@keyword_string) BEGIN
  SET @endindex = CHARINDEX(CHAR(13),@keyword_string,@index+1)
  IF @endindex = 0 BEGIN
   SET @endindex = LEN(@keyword_string)+1
  END --IF
  SET @substring = SUBSTRING(@keyword_string,@index,@endindex-@index)
  INSERT INTO #keywords VALUES (@substring)
  SET @index = @endindex + 1
 END -- WHILE
END -- IF
DECLARE @num_keywords INT
SELECT @num_keywords = COUNT(keyword) from #keywords
/* A table to contain all the pages matched, one row per keyword matched */
CREATE TABLE #keywords_matched (
 meta_id INT,
 doc_type INT,
 meta_headline VARCHAR(256),
 meta_text VARCHAR(1024),
 date_created DATETIME,
 date_modified DATETIME,
 date_activated DATETIME,
 date_archived DATETIME,
 archive TINYINT,
 shared TINYINT,
 show_meta TINYINT,
 disable_search TINYINT,
 keyword VARCHAR(30) 
)
INSERT INTO #keywords_matched
SELECT  
  m.meta_id,
  m.doc_type,
  m.meta_headline,
  m.meta_text,
  m.date_created,
  m.date_modified,
  activated_datetime,
  archived_datetime,
  archive,
  shared,
  show_meta,
  disable_search,
  k.keyword
FROM
  meta m
JOIN
  #doc_types dt  ON m.doc_type = dt.doc_type
     AND activate = 1
     AND (
       (
        date_created >= @created_sd
       AND date_created <= @created_ed
      ) OR (
        date_modified >= @modified_sd
       AND date_modified <= @modified_ed
      ) OR (
        activated_datetime >= @activated_sd
       AND activated_datetime <= @activated_ed
      ) OR (
        archived_datetime >= @archived_sd
       AND archived_datetime <= @archived_ed
      ) OR (
        @created_startdate = ''
       AND @created_enddate = ''
       AND @modified_startdate = ''
       AND @modified_enddate = ''
       AND @activated_startdate = ''
       AND @activated_enddate = ''
       AND @archived_startdate = ''
       AND @archived_enddate = ''
      )
     )
LEFT JOIN
  roles_rights rr  ON rr.meta_id = m.meta_id
JOIN
  user_roles_crossref urc ON urc.user_id = @user_id
     AND (
       urc.role_id = 0   -- Superadmin may always see everything
      OR (
        rr.role_id = urc.role_id  -- As does a user...
       AND (
         rr.set_id < 3   -- ... with a privileged role
        OR (
          (
           rr.set_id = 3   -- ... or a user with read-rights
          OR show_meta != 0   -- ... or if the document lets anyone see
         )
         AND m.disable_search = 0   -- ... that is, if searching is not turned off for this document
         AND (
           m.shared != 0   -- ... and the document is shared
          OR @only_addable = 0  -- ... unless we've selected to only see addable (shared) documents.
         )
        )
       )
      )
     )
LEFT JOIN
  texts t   ON m.meta_id = t.meta_id
JOIN
  #keywords k  ON m.meta_headline  LIKE @search_start+k.keyword+'%'
     OR m.meta_text  LIKE @search_start+k.keyword+'%'
     OR t.text   LIKE @search_start+k.keyword+'%'


GROUP BY
  m.meta_id,
  k.keyword,
  m.doc_type,
  m.meta_headline,
  m.meta_text,
  m.date_created,
  m.date_modified,
  activated_datetime,
  archived_datetime,
  archive,
  shared,
  show_meta,
  disable_search
DECLARE @doc_count INT
SET @doc_count = @@ROWCOUNT
IF @and_mode = 'AND' BEGIN
 DELETE FROM #keywords_matched
 WHERE meta_id IN (
  SELECT meta_id
  FROM #keywords_matched
  GROUP BY meta_id
  HAVING (COUNT(keyword) < @num_keywords)
 )
 SET @doc_count = @doc_count - @@ROWCOUNT
END
DECLARE @eval VARCHAR(2000)
SET @eval = ('
IF '+STR(@fromdoc)+' > 0 BEGIN
 DELETE FROM #keywords_matched
 WHERE meta_id IN (
  SELECT TOP '+STR(@fromdoc)+' meta_id FROM #keywords_matched
  ORDER BY meta_id
 )
END
SELECT TOP '+STR(@num_docs)+' 
  meta_id,
  doc_type,
  meta_headline,
  meta_text,
  date_created,
  date_modified,
  ISNULL(CONVERT(VARCHAR,NULLIF(date_activated,''''),121),'''') AS date_activated,
  ISNULL(CONVERT(VARCHAR,NULLIF(date_archived,''''),121),'''') AS date_archived,
  archive,
  shared,
  show_meta,
  disable_search,
  ' + STR(@doc_count) + ' AS doc_count
FROM
  #keywords_matched
GROUP BY
  meta_id,
  doc_type,
  meta_headline,
  meta_text,
  date_created,
  date_modified,
  date_activated,
  date_archived,
  archive,
  shared,
  show_meta,
  disable_search
ORDER BY 
  '+@sortorder)
EXEC (@eval)
DROP TABLE #keywords
DROP TABLE #doc_types
DROP TABLE #keywords_matched
GO

-- 2001-11-21

--here is the release of v1_5_0-pre8

--adding a new method used by IMCServise to save text in db

CREATE PROCEDURE InsertText
 @meta_id int,
 @name char(15),
 @type int,
 @text text

AS

declare  @number int 

 select @number=counter from texts
 where meta_id = @meta_id
 and name = @name

if(@number is null) begin
     insert into texts (meta_id,name,type,text)
     values(@meta_id,@name,@type,@text)
  end
else begin
  update texts
  set text = @text
  where counter=@number
  update texts
  set type=@type
  where counter=@number
end
GO
-- 2001-12-20


--this is the stuff needed to create full-text index on the colums we want to search on
BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
ALTER TABLE dbo.texts
	DROP CONSTRAINT FK_texts_meta
GO
COMMIT
BEGIN TRANSACTION
CREATE TABLE dbo.Tmp_texts
	(
	meta_id int NOT NULL,
	name int NOT NULL,
	text text NOT NULL,
	type int NULL,
	counter int NOT NULL IDENTITY (1, 1)
	)  ON [PRIMARY]
	 TEXTIMAGE_ON [PRIMARY]
GO
SET IDENTITY_INSERT dbo.Tmp_texts OFF
GO
IF EXISTS(SELECT * FROM dbo.texts)
	 EXEC('INSERT INTO dbo.Tmp_texts (meta_id, name, text, type)
		SELECT meta_id, name, text, type FROM dbo.texts TABLOCKX')
GO
DROP TABLE dbo.texts
GO
EXECUTE sp_rename N'dbo.Tmp_texts', N'texts', 'OBJECT'
GO
CREATE NONCLUSTERED INDEX IX_texts ON dbo.texts
	(
	meta_id
	) ON [PRIMARY]
GO
ALTER TABLE dbo.texts ADD CONSTRAINT
	PK_texts PRIMARY KEY CLUSTERED 
	(
	counter
	) ON [PRIMARY]

GO
ALTER TABLE dbo.texts WITH NOCHECK ADD CONSTRAINT
	FK_texts_meta FOREIGN KEY
	(
	meta_id
	) REFERENCES dbo.meta
	(
	meta_id
	)
GO
COMMIT


--Enable full-text searching in the database.
EXEC sp_fulltext_database 'enable'
GO
-- Create a new full-text catalog.
EXEC sp_fulltext_catalog 'full_text_index',
			'create'
GO
--Register the new tales and colums in it
--then activate the table
--classification-table
EXEC sp_fulltext_table  'classification',
			'create',
			'full_text_index',
			'PK_classification'
EXEC sp_fulltext_column 'classification',
			'code',
			'add'
EXEC sp_fulltext_table  'classification',
			'activate'
GO
--meta-table
EXEC sp_fulltext_table  'meta',
			'create',
			'full_text_index',
			'PK_meta'
EXEC sp_fulltext_column 'meta',
			'meta_headline',
			'add'
EXEC sp_fulltext_column 'meta',
			'meta_text',
			'add'
EXEC sp_fulltext_table  'meta',
			'activate'
GO
--texts-table
EXEC sp_fulltext_table  'texts',
			'create',
			'full_text_index',
			'PK_texts'
EXEC sp_fulltext_column 'texts',
			'text',
			'add'
EXEC sp_fulltext_table  'texts',
			'activate'
GO
--start the population
EXEC sp_fulltext_catalog 'full_text_index',
			 'start_full'
while (SELECT fulltextcatalogproperty('full_text_index','populatestatus')) <> 0
	BEGIN
	WAITFOR DELAY '00:00:02'
	CONTINUE
	END
GO

--start the trace of changes
--classification table
EXEC sp_fulltext_table classification, 'Start_change_tracking'
EXEC sp_fulltext_table classification, 'Start_background_updateindex'
Go
--meta-table
EXEC sp_fulltext_table meta, 'Start_change_tracking'
EXEC sp_fulltext_table meta, 'Start_background_updateindex'
GO
-- texts-table
EXEC sp_fulltext_table texts, 'Start_change_tracking'
EXEC sp_fulltext_table texts, 'Start_background_updateindex'

GO
-- 2001-12-20

--this is the new search querry

CREATE PROCEDURE SearchDocsIndex
  @user_id INT,
  @serchString varchar (180),  -- Must be large enough to encompass an entire searchstring.
  @doc_types_string VARCHAR(30), -- Must be large enough to encompass all possible doc_types, commaseparated and expressed in decimal notation.
  @fromdoc INT,
  @num_docs INT,
  @sortorder VARCHAR(256),  -- doc_type, date_modified, date_created, archived_datetime, activated_datetime, meta_id, meta_headline
  @created_startdate DATETIME,
  @created_enddate DATETIME,
  @modified_startdate DATETIME,
  @modified_enddate DATETIME,
  @activated_startdate DATETIME,
  @activated_enddate DATETIME,
  @archived_startdate DATETIME,
  @archived_enddate DATETIME,
  @only_addable TINYINT  -- 1 to show only documents the user may add.
  
AS 

SET nocount on
SET @fromdoc = @fromdoc - 1
DECLARE @created_sd DATETIME,
  @modified_sd DATETIME,
  @activated_sd DATETIME,
  @archived_sd DATETIME,
  @created_ed DATETIME,
  @modified_ed DATETIME,
  @activated_ed DATETIME,
  @archived_ed DATETIME,
  @search_start VARCHAR(5)

IF (@created_startdate = '') BEGIN
 SET @created_sd = '1753-01-01'
END ELSE BEGIN
 SET @created_sd = @created_startdate
END
IF (@modified_startdate = '') BEGIN
 SET @modified_sd = '1753-01-01'
END ELSE BEGIN
 SET @modified_sd = @modified_startdate
END
IF (@activated_startdate = '') BEGIN
 SET @activated_sd = '1753-01-01'
END ELSE BEGIN
 SET @activated_sd = @activated_startdate
END
IF (@archived_startdate = '') BEGIN
 SET @archived_sd = '1753-01-01'
END ELSE BEGIN
 SET @archived_sd = @archived_startdate
END
IF (@created_enddate = '') BEGIN
 IF (@created_startdate = '') BEGIN
  SET @created_ed = '1753-01-01'
 END ELSE BEGIN
  SET @created_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @created_ed = @created_enddate
END
IF (@modified_enddate = '') BEGIN
 IF (@modified_startdate = '') BEGIN
  SET @modified_ed = '1753-01-01'
 END ELSE BEGIN
  SET @modified_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @modified_ed = @modified_enddate
END
IF (@activated_enddate = '') BEGIN
 IF (@activated_startdate = '') BEGIN
  SET @activated_ed = '1753-01-01'
 END ELSE BEGIN
  SET @activated_ed = '9999-12-31'
 END
END ELSE BEGIN
 SET @activated_ed = @activated_enddate
END
IF (@archived_enddate = '') BEGIN
     IF (@archived_startdate = '') BEGIN
          SET @archived_sd = '1753-01-01'
         END
       SET @archived_ed = '9999-12-31'
    END 
ELSE BEGIN
   SET @archived_ed = @archived_enddate
END

/* start setup table that contains the docctypes to search on */
CREATE TABLE #doc_types ( doc_type INT )

DECLARE @substring VARCHAR(30)
DECLARE @index INT
DECLARE @endindex INT
IF LEN(@doc_types_string) > 0 BEGIN
 	SET @index = 1
 	WHILE @index <= LEN(@doc_types_string) BEGIN
  		SET @endindex = CHARINDEX(',',@doc_types_string,@index+1)
  		IF @endindex = 0 BEGIN
   			SET @endindex = LEN(@doc_types_string)+1
 		 END --IF
 		 SET @substring = SUBSTRING(@doc_types_string,@index,@endindex-@index)
 		 INSERT INTO #doc_types VALUES (@substring)
  		SET @index = @endindex + 1
	END -- WHILE
END -- IF
/*end doctype setup*/


/* here have we the start of the search engine stuff */
 create table #temp (meta_id int)
 create table #and ( meta_id int )
 create table #or    (meta_id int )
 create table #not  ( meta_id int )
 DECLARE @sWord varchar(180)
 DECLARE @start int
 DECLARE @stop int
 Declare @doneAnAnd int
 set @doneAnAnd = 0

 DECLARE @counter int
 declare @orCounter int
 set @orCounter = 0
 set @counter = 0

 IF LEN( @serchString ) > 0  BEGIN
	set @start = 1
	while @start <= LEN( @serchString ) BEGIN
		set @stop = charindex(',' , @serchString , @start + 1 )
		if  @stop = 0
		 begin
			set @stop =  len( @serchString ) +1
		 end --if 
		set @sWord = substring( @serchString , @start , @stop - @start )
		if @sWord like '"and"'  begin
			set @doneAnAnd = @doneAnAnd +1
			set @start = @stop +1
			set @stop = charindex(',' , @serchString , @start + 1 )
			if @stop = 0 begin
				set @stop =  len( @serchString ) +1
			end  			if   @start <= LEN( @serchString ) BEGIN
				
				
				set @sWord = substring( @serchString , @start , @stop - @start )
				insert into #temp select meta_id from meta where contains(meta_headline,  @sWord )or contains(meta_text,  @sWord )
				insert into #temp select meta_id from texts where contains(text,  @sWord )
				insert into #temp select meta_id from classification, meta_classification where contains(code,  @sWord ) and meta_classification.class_id=classification.class_id
				select @counter = count(meta_id) from #and
				if @counter  > 0 begin
					delete from #temp where meta_id not in (select meta_id from #and) 
					delete from #and
				end
				insert into #and select * from #temp		
			end
		end 
		else if @sWord = '"not"'  begin
			set @start = @stop +1
			set @stop = charindex(',' , @serchString , @start + 1 )
			if @stop = 0 begin
				set @stop =  len( @serchString ) +1
			end 
			if   @start <= LEN( @serchString ) BEGIN
				set @sWord = substring( @serchString , @start , @stop - @start )
				insert into #not select meta_id from meta where contains(meta_headline,  @sWord )or contains(meta_text,  @sWord )
				insert into #not select meta_id from texts where contains(text,  @sWord )
				insert into #not select meta_id from classification, meta_classification where contains(code,  @sWord ) and meta_classification.class_id=classification.class_id
			end
		end
		else begin
			set @orCounter = @orCounter + 1
			if @stop = 0 begin
				set @stop =  len( @serchString ) +1
			end 
			if   @start <= LEN( @serchString ) BEGIN
				set @sWord = substring( @serchString , @start , @stop - @start )
				insert into #or select meta_id from meta where contains(meta_headline,  @sWord ) or contains(meta_text,  @sWord )
				insert into #or select meta_id from texts where contains(text,  @sWord )
				insert into #or select meta_id from classification, meta_classification where contains(code,  @sWord ) and meta_classification.class_id=classification.class_id
			end
		end	
		set @start = @stop + 1
	end --while
 END --begin

/*plockar ut de meta idn som ska vara kvar*/
if @orCounter = 0 begin
  insert into #or select meta_id from #and	
end
if @doneAnAnd  > 0 begin
	delete from #or where meta_id not in (select meta_id from #and) 
end
select @counter = count(meta_id) from #not
if @counter > 0 begin
	select @counter = count(meta_id) from #or
	if @counter > 0 begin
		delete from #or  where meta_id in(select meta_id from #not)
--		#or.meta_id = #not.meta_id
	end
end

--select  * from #or
/* end of the search engine*/

/*lets get the pages to return*/

SELECT  distinct
  m.meta_id,
  m.doc_type,
  m.meta_headline,
  m.meta_text,
  m.date_created,
  m.date_modified,
  activated_datetime,
  archived_datetime,
  archive,
  shared,
  show_meta,
  disable_search,
  meta_image
FROM
  meta m
 JOIN
  #doc_types dt  ON m.doc_type = dt.doc_type
     AND activate = 1
     AND (
       (
        date_created >= @created_sd
       AND date_created <= @created_ed
      ) OR (
        date_modified >= @modified_sd
       AND date_modified <= @modified_ed
      ) OR (
        activated_datetime >= @activated_sd
       AND activated_datetime <= @activated_ed
      ) OR (
        archived_datetime >= @archived_sd
       AND archived_datetime <= @archived_ed
      ) OR (
        @created_startdate = ''
       AND @created_enddate = ''
       AND @modified_startdate = ''
       AND @modified_enddate = ''
       AND @activated_startdate = ''
       AND @activated_enddate = ''
       AND @archived_startdate = ''
       AND @archived_enddate = ''
      )
     )

left  JOIN
  roles_rights rr  ON rr.meta_id = m.meta_id and m.meta_id != null
JOIN
  user_roles_crossref urc ON urc.user_id = @user_id
     AND (
       urc.role_id = 0   -- Superadmin may always see everything
      OR (
        rr.role_id = urc.role_id  -- As does a user...
       AND (
         rr.set_id < 3   -- ... with a privileged role
        OR (
          (
           rr.set_id = 3   -- ... or a user with read-rights
          OR show_meta != 0   -- ... or if the document lets anyone see
         )
         AND m.disable_search = 0   -- ... that is, if searching is not turned off for this document
         AND (
           m.shared != 0   -- ... and the document is shared
          OR @only_addable = 0  -- ... unless we've selected to only see addable (shared) documents.
         )
        )
       )
      )
     )
JOIN 
#or on #or.meta_id=m.meta_id and m.meta_id != null

GROUP BY
  m.meta_id,
  m.doc_type,
  m.meta_headline,
  m.meta_text,
  m.date_created,
  m.date_modified,
  activated_datetime,
  archived_datetime,
  archive,
  shared,
  show_meta,
  disable_search,
  meta_image
order by m.meta_headline


DROP TABLE #doc_types
drop table #temp
drop table #and
drop table #or
drop table #not
GO

-- 2001-12-20



