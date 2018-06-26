<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/imcms/ui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<c:set var="heading">
    <fmt:message key="templates/sv/AdminManager_adminTask_element.htm/14"/>
</c:set>
<ui:imcms_gui_start_of_page titleAndHeading="${heading}"/>

<table width="600">
    <tr>
        <td colspan="4">
            <c:set var="heading">
                <fmt:message key="templates/sv/AdminSystemMessage.htm/3"/>
            </c:set>
            <ui:imcms_gui_heading heading="${heading}"/>
        </td>
    </tr>
    <tr>
        <form method="post" action="AdminSystemInfo">
            <td><? templates/sv/AdminSystemMessage.htm/7 ?></td>
            <td>${STARTDOCUMENT}</td>
            <td><input type="text" name="STARTDOCUMENT" value="${STARTDOCUMENT}" maxlength="10" size="5"></td>
            <td><input type="submit" class="imcmsFormBtnSmall" name="SetStartDoc"
                       value="<? templates/sv/AdminSystemMessage.htm/2001 ?>"></td>
        </form>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td colspan="4">
            <c:set var="heading">
                <fmt:message key="templates/sv/AdminSystemMessage.htm/8"/>
            </c:set>
            <ui:imcms_gui_heading heading="${heading}"/>
        </td>
    </tr>
    <tr>
        <form method="post" action="AdminSystemInfo">
            <td valign="top"><? templates/sv/AdminSystemMessage.htm/12 ?></td>
            <td valign="top">${SYSTEM_MESSAGE} </td>
            <td><textarea name="SYSTEM_MESSAGE" cols="40" rows="4">${SYSTEM_MESSAGE}</textarea></td>
            <td><input type="submit" class="imcmsFormBtnSmall" name="SetSystemMsg"
                       value="<? templates/sv/AdminSystemMessage.htm/2001 ?>">
            </td>
        </form>
    </tr>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td colspan="4">
            <c:set var="heading">
                <fmt:message key="templates/sv/AdminSystemMessage.htm/14"/>
            </c:set>
            <ui:imcms_gui_heading heading="${heading}"/>
        </td>
    </tr>
    <form method="post" action="AdminSystemInfo">
        <td>
            <tr>
                <td><? templates/sv/AdminSystemMessage.htm/18 ?></td>
                <td>${SERVER_MASTER}</td>
                <td><input type="text" name="SERVER_MASTER" value="${SERVER_MASTER}" maxlength="80" size="40"></td>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><? templates/sv/AdminSystemMessage.htm/21 ?></td>
                <td>${SERVER_MASTER_EMAIL}</td>
                <td><input type="text" name="SERVER_MASTER_EMAIL" value="${SERVER_MASTER_EMAIL}" maxlength="80"
                           size="40"></td>
                <td><input type="submit" class="imcmsFormBtnSmall" name="SetServerMasterInfo"
                           value="<? templates/sv/AdminSystemMessage.htm/2001 ?>"></td>
            </tr>
    </form>
    <tr>
        <td>&nbsp;</td>
    </tr>
    <tr>
        <td colspan="4">
            <c:set var="heading">
                <fmt:message key="templates/sv/AdminSystemMessage.htm/22"/>
            </c:set>
            <ui:imcms_gui_heading heading="${heading}"/>
        </td>
    </tr>
    <form method="post" action="AdminSystemInfo">
        <tr>
            <td><? templates/sv/AdminSystemMessage.htm/18 ?></td>
            <td>${WEB_MASTER}</td>
            <td><input type="text" name="WEB_MASTER" value="${WEB_MASTER}" maxlength="80" size="40"></td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td><? templates/sv/AdminSystemMessage.htm/21 ?></td>
            <td>${WEB_MASTER_EMAIL}</td>
            <td><input type="text" name="WEB_MASTER_EMAIL" value="${WEB_MASTER_EMAIL}" maxlength="80" size="40"></td>
            <td><input type="submit" class="imcmsFormBtnSmall" name="SetWebMasterInfo"
                       value="<? templates/sv/AdminSystemMessage.htm/2001 ?>"></td>
        </tr>
    </form>
</table>

<ui:imcms_gui_end_of_page/>