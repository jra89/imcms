<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="imcode.server.Imcms" %>

<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/imcms/ui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title><fmt:message key="templates/sv/no_page.html/1"/></title>

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/imcms/css/imcms_admin.css.jsp">

</head>
<body bgcolor="#FFFFFF">

<ui:imcms_gui_outer_start/>
<c:set var="heading">
    <fmt:message key="templates/sv/no_page.html/1"/>
</c:set>
<ui:imcms_gui_head heading="${heading}"/>
<table border="0" cellspacing="0" cellpadding="0">
    <tr>
        <td>
            <table border="0" cellpadding="0" cellspacing="0">
                <form action="<%= request.getContextPath() %>/servlet/StartDoc">
                    <tr>
                        <td><input type="Submit" value="<fmt:message key="templates/Startpage"/>"
                                   class="imcmsFormBtn"></td>
                    </tr>
                </form>
            </table>
        </td>
        <td>&nbsp;</td>
        <td>
            <table border="0" cellpadding="0" cellspacing="0">
                <form action="<%= request.getContextPath() %>/servlet/BackDoc">
                    <tr>
                        <td><input type="Submit" value="<fmt:message key="templates/Back"/>" class="imcmsFormBtn">
                        </td>
                    </tr>
                </form>
            </table>
        </td>
    </tr>
</table>
<ui:imcms_gui_mid/>

<table border="0" cellspacing="0" cellpadding="2" width="310">
    <tr>
        <td align="center" class="imcmsAdmText"><b><fmt:message key="templates/sv/no_page.html/2"><fmt:param
                value="<%= Imcms.getServices().getSystemData().getServerMasterAddress() %>"/></fmt:message></b><br>
        </td>
    </tr>
</table>
<ui:imcms_gui_bottom/>
<ui:imcms_gui_outer_end/>
<ui:imcms_gui_outer_end/>


<script language="JavaScript">
    <!--
    if (document.forms[1]) {
        var f = document.forms[1];
        if (f.elements[0]) {
            f.elements[0].blur();
        }
    }
    //-->
</script>

</body>
</html>
