CREATE PROCEDURE RoleDeleteViewAffectedMetaIds
 @aRoleId int
AS
/*
 This function is used when an administrator tries to delete a role.
 All metaids where the role is used will be presenteted in i list
*/
SELECT  TOP 50 r.meta_id , r.meta_id
FROM roles_rights r
WHERE role_id = @aRoleId
-- Lets validate for null
--SELECT @returnVal = ISNULL(  @returnVal , -1 )
--SELECT @returnVal AS 'FoundRoleName'


;
