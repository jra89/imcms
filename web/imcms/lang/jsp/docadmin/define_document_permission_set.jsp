<%@ page import="com.imcode.imcms.flow.EditDocumentPermissionsPageFlow,
                 imcode.server.document.DocumentPermissionSetDomainObject"%>
<%@page contentType="text/html"%><%@taglib uri="/WEB-INF/velocitytag.tld" prefix="vel"%><%
    EditDocumentPermissionsPageFlow.DocumentPermissionSetPage documentPermissionSetPage = EditDocumentPermissionsPageFlow.DocumentPermissionSetPage.fromRequest(request) ;
    DocumentPermissionSetDomainObject documentPermissionSet= documentPermissionSetPage.getDocumentPermissionSet() ;
%><vel:velocity>
<html>
<head>

<title><? templates/sv/permissions/define_permissions.html/1 ?></title>

<link rel="stylesheet" type="text/css" href="$contextPath/imcms/css/imcms_admin.css.jsp">

</head>
<body bgcolor="#FFFFFF">

#gui_outer_start()
#gui_head( "<? global/imcms_administration ?>" )

<table border="0" cellspacing="0" cellpadding="0">
<form method="post" action="SavePermissions">
<input type="hidden" name="set_id" value="$documentPermissionSet.TypeId">
<input type="hidden" name="meta_id" value="$document.Id">
#if( $forNew )<input type="hidden" value="new" name="new">#end
<tr>
	<td><input type="submit" class="imcmsFormBtn" name="cancel" value="<? global/cancel ?>"></td>
	<td>&nbsp;</td>
    <td><input type="button" value="<? global/help ?>" class="imcmsFormBtn" onClick="openHelpW(41)"></td>

</tr>
</table>

#gui_mid()

<table border="0" cellspacing="0" cellpadding="2" width="660" align="center">
#if( $forNew )
#set( $heading = "<? templates/sv/permissions/define_permissions.html/permissions_for_restricted ?> $documentPermissionSet.TypeId <? templates/sv/permissions/define_permissions.html/permissions_for_restricted/new ?>" )
#else
#set( $heading = "<? templates/sv/permissions/define_permissions.html/permissions_for_restricted ?> $documentPermissionSet.TypeId <? templates/sv/permissions/define_permissions.html/permissions_for_restricted/this ?>" )
#end
<tr>
	<td colspan="3">#gui_heading( $heading )</td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_1.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="1" #if( $documentPermissionSet.getEditDocumentInformation() )checked#end></td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_4.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="4" #if( $documentPermissionSet.getEditPermissions() )checked#end></td>
</tr>
#if( $document.getDocumentTypeId() == 2 )
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_2_65536.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="65536" #if( $documentPermissionSet.getEditTexts() )checked#end></td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_2_131072.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="131072" #if( $documentPermissionSet.getEditImages() )checked#end></td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_2_1048576.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="1048576" #if( $documentPermissionSet.getEditIncludes() )checked#end></td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_2_262144.html/1 ?></td>
	<td class="imcmsAdmText"><input type="checkbox" name="permissions" value="262144" #if( $documentPermissionSet.getEditMenus() )checked#end><? templates/sv/permissions/define_permission_2_262144.html/1001 ?></td>
	<td>
	<select name="permissions_ex" size="6" multiple>
        #foreach( $documentType in $documentTypesMap.keySet() )
            <option value="8_$documentType.Id" #if( $documentTypesMap.get( $documentType ) )selected#end>$documentType.Name</option>
        #end
	</select></td>
</tr>
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_2_524288.html/1 ?></td>
	<td class="imcmsAdmText"><input type="checkbox" name="permissions" value="524288" #if( $documentPermissionSet.getEditTemplates() )checked#end><? templates/sv/permissions/define_permission_2_524288.html/1001 ?></td>
	<td>
	<select name="permissions_ex" size="6" multiple>
	    #foreach( $templateGroup in $templateGroupsMap.keySet() )
	        <option value="524288_$templateGroup.Id" #if( $templateGroupsMap.get( $templateGroup ) )selected#end>$templateGroup.Name</option>
	    #end
	</select></td>
</tr>
#else
<tr>
	<td class="imcmsAdmText"><? templates/sv/permissions/define_permission_5_65536.html/1 ?></td>
	<td colspan="2"><input type="checkbox" name="permissions" value="65536" #if( $documentPermissionSet.getEdit() )checked#end></td>
</tr>
#end
<tr>
	<td colspan="3">#gui_hr( "blue" )</td>
</tr>
<tr>
	<td colspan="3" align="right">
	<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><input type="submit" class="imcmsFormBtn" name="ok" value="<? templates/sv/permissions/define_permissions.html/2004 ?>"></td>
		<td>&nbsp;</td>
		<td><input type="reset" class="imcmsFormBtn" name="Reset" value="<? templates/sv/permissions/define_permissions.html/2005 ?>"></td>
		<td>&nbsp;</td>
		<td><input type="submit" class="imcmsFormBtn" name="cancel" value="<? templates/sv/permissions/define_permissions.html/2006 ?>"></td>
	</tr>
	</table></td>
</tr>
</form>
</table>
#gui_bottom()
#gui_outer_end()

</body>
</html>
</vel:velocity>
