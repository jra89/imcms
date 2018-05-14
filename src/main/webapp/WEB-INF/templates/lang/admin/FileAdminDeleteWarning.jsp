<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/imcms/ui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<head>
<title><? templates/sv/FileAdminDeleteWarning.html/1 ?></title>

    <link rel="stylesheet" type="text/css" href="${contextPath}/imcms/css/imcms_admin.css">
    <script src="${contextPath}/js/imcms/imcms_admin.js.jsp" type="text/javascript"></script>

</head>
<body onLoad="focusField(1, 'deleteok')">

<ui:imcms_gui_outer_start/>
<c:set var="heading">
    <fmt:message key="templates/sv/FileAdminDeleteWarning.html/1"/>
</c:set>
<ui:imcms_gui_head heading="${heading}"/>
<form method="post" action="FileAdmin" enctype="multipart/form-data">
<table border="0" cellspacing="0" cellpadding="0">
    <input type="HIDDEN" name="dir1" value="${dir1}">
    <input type="HIDDEN" name="dir2" value="${dir2}">
    <input type="HIDDEN" name="source" value="${source}">
    <input type="HIDDEN" name="files" value="${files}">
<tr>
    <td><input type="submit" class="imcmsFormBtn" name="no" value="<? global/back ?>"></td>
    <td>&nbsp;</td>
    <td><input type="button" class="imcmsFormBtn" value="<? global/help ?>" title="<? global/openthehelppage ?>" onClick="openHelpW('FileManager')"></td>
</tr>
</table>
    <ui:imcms_gui_mid/>

    <table border="0" cellspacing="0" cellpadding="0" width="500">
<tr>
    <td>
        <c:set var="heading">
            <fmt:message key="templates/sv/FileAdminDeleteWarning.html/3"/>
        </c:set>
        <ui:imcms_gui_heading heading="${heading}"/>
    </td>
</tr>
<tr>
    <td>
        <select size="10" style="width:100%" readonly>${filelist}</select></td>
</tr>
<tr>
    <td height="20"><? templates/sv/FileAdminDeleteWarning.html/5 ?></td>
</tr>
<tr>
    <td><ui:imcms_gui_hr wantedcolor="blue"/></td>
</tr>
<tr>
    <td align="right">
        <input class="imcmsFormBtn" style="width:70px" type="submit" name="deleteok"
               value="<? templates/sv/FileAdminDeleteWarning.html/2001 ?>">
        <input class="imcmsFormBtn" style="width:70px" type="submit" name="no"
               value="<? templates/sv/FileAdminDeleteWarning.html/2002 ?>"></td>
</tr>
</table>
</form>
<ui:imcms_gui_bottom/>
<ui:imcms_gui_outer_end/>


</body>
</html>