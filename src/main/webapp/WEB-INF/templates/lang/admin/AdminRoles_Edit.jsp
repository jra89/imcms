<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/imcms/ui" %>

<ui:imcms_gui_start_of_page titleAndHeading="templates/sv/AdminRoles_Edit.html/1"/>

<form method="post" action="AdminRoles" name="editRole">
    <table width="400" border="0" cellspacing="0">
        <tr>
            <td colspan="2" class="imcmsAdmText">&nbsp;<? templates/sv/AdminRoles_Edit.html/2 ?>: &nbsp; <b> #CURRENT_ROLE_NAME# </b>
                <input type="HIDDEN" name="ROLE_ID" value="#CURRENT_ROLE_ID#"></td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td>#ROLE_PERMISSIONS# </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2"><ui:imcms_gui_hr wantedcolor="blue"/></td>
        </tr>
        <tr>
            <td colspan="2" align="right">
                <input type="submit" name="UPDATE_ROLE_PERMISSIONS" class="imcmsFormBtn" value="<? global/save ?>"> &nbsp;
                <input type="submit" name="CANCEL_ROLE" class="imcmsFormBtn" value="<? global/cancel ?>"></td>
        </tr>
    </table>
</form>
<ui:imcms_gui_end_of_page/>

