SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS OFF 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[GetDocumentInfo]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[GetDocumentInfo]
GO


CREATE PROCEDURE GetDocumentInfo
 @meta_id int
AS
 SELECT meta_id,
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
	(select convert(char(16),date_created,120)) AS date_created,
	(select convert(char(16) ,date_modified,120)) AS date_modified,
	sort_position,
	menu_position,
	disable_search,
	target,
	frame_name,
	(select convert(char(16) ,activated_datetime,120)) AS activated_datetime,
	(select convert(char(16) ,archived_datetime,120)) AS archived_datetime
 FROM meta 
 WHERE meta_id=@meta_id
GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

