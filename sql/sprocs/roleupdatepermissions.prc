SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[RoleUpdatePermissions]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)
drop procedure [dbo].[RoleUpdatePermissions]
GO


CREATE PROCEDURE RoleUpdatePermissions @role_id int,  @permissions int AS
/* update permissions for role */
update roles 
Set permissions = @permissions 
where role_id = @role_id


GO
SET QUOTED_IDENTIFIER OFF 
GO
SET ANSI_NULLS ON 
GO

