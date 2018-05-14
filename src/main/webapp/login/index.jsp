<%@ page import="com.imcode.imcms.servlet.VerifyUser" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%@ taglib prefix="ui" tagdir="/WEB-INF/tags/imcms/ui" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="cp" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="templates/login/index.html/1"/></title>

    <link rel="stylesheet" type="text/css" href="${cp}/imcms/css/imcms_admin.css">
    <script src="${cp}/js/imcms/imcms_admin.js" type="text/javascript"></script>

</head>
<body>
<table border="0" cellspacing="0" cellpadding="0" class="imcmsAdmTable" align="center">
    <tr>
        <td class="imcmsAdmTable">
            <table border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td class="imcmsAdmBgHead" colspan="6"><img
                            src="${cp}/imcms/lang/images/admin/1x1.gif" width="1" height="20"></td>
                </tr>
                <tr class="imcmsAdmBgHead">
                    <td colspan="2"><img src="${cp}/imcms/lang/images/admin/1x1.gif" width="1" height="1"></td>
                    <td nowrap><span class="imcmsAdmHeadingTop"><fmt:message key="templates/login/index.html/2"/></span>
                    </td>
                    <td align="right"><a href="http://www.imcms.net/" target="_blank"><img
                            src="${cp}/imcms/lang/images/admin/logo_imcms_admin.gif" width="100" height="20"
                            alt="www.imcms.net" border="0"></a></td>
                    <td colspan="2"><img src="${cp}/imcms/lang/images/admin/1x1.gif" width="1" height="1"></td>
                </tr>
                <tr>
                    <td colspan="6" class="imcmsAdmBgHead"><img
                            src="${cp}/imcms/lang/images/admin/1x1.gif" width="1" height="20"></td>
                </tr>
                <tr class="imcmsAdmBgHead">
                    <td colspan="2"><img src="${cp}/imcms/lang/images/admin/1x1.gif" width="1" height="1"></td>
                    <td colspan="2">
                        <table border="0" cellspacing="0" cellpadding="0" width="310">
                            <form action="">
                                <tr>
                                    <td>
                                        <table border="0" cellspacing="0" cellpadding="0">
                                            <tr>
                                                <td><input type="button" class="imcmsFormBtn" style="width:100px"
                                                           value="<fmt:message key="templates/login/index.html/2001"/>"
                                                           onClick="top.location='${cp}/servlet/StartDoc';"></td>
                                                <td>&nbsp;</td>
                                                <td><input type="button" class="imcmsFormBtn" style="width:115px"
                                                           value="<fmt:message key="templates/login/index.html/2002"/>"
                                                           onClick="top.location='${cp}/servlet/PasswordReset';"></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                            </form>
                        </table>
                    </td>
                    <td colspan="2"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                </tr>
                <tr>
                    <td class="imcmsAdmBgHead" colspan="6"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="20"></td>
                </tr>
                <tr>
                    <td height="10" class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td class="imcmsAdmBgCont" colspan="4"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                </tr>
                <tr class="imcmsAdmBgCont">
                    <td class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td colspan="2">
                        <table border="0" cellspacing="0" cellpadding="2" width="310">
                            <tr>
                                <td colspan="2" nowrap>
                                    <span class="imcmsAdmText">
                                        <c:if test="${requestScope['error'] ne null}">
                                            <p><b>${requestScope['error'].toLocalizedString(pageContext.request)}</b></p>
                                        </c:if>
                                        <fmt:message key="templates/login/index.html/4"/>
                                        <img alt="" src="${cp}/imcms/images/1x1.gif" width="1" height="5">
                                        <fmt:message key="templates/login/index.html/1001"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <table border="0" cellspacing="0" cellpadding="1">
                                        <form action="${cp}/servlet/VerifyUser" method="post">
                                            <c:set var="nextMetaParamName"
                                                   value="<%=VerifyUser.REQUEST_PARAMETER__NEXT_META%>"/>
                                            <c:set var="nextMetaParamValue" value="${requestScope[nextMetaParamName]}"/>

                                            <c:set var="nextUrlParamName"
                                                   value="<%=VerifyUser.REQUEST_PARAMETER__NEXT_URL%>"/>
                                            <c:set var="nextUrlParamValue" value="${requestScope[nextUrlParamName]}"/>

                                            <c:if test="${nextMetaParamValue ne null}">
                                                <input type="hidden" name="${nextMetaParamName}"
                                                       value="${fn:escapeXml(nextMetaParamValue)}">
                                            </c:if>

                                            <c:if test="${nextMetaParamValue eq null and nextUrlParamValue ne null}">
                                                <input type="hidden" name="${nextUrlParamName}"
                                                       value="${fn:escapeXml(nextUrlParamValue)}">
                                            </c:if>

                                            <tr>
                                                <td><span class="imcmsAdmText"><fmt:message
                                                        key="templates/login/index.html/5"/></span></td>
                                                <td>&nbsp;</td>
                                                <td><input type="text"
                                                           name="<%=VerifyUser.REQUEST_PARAMETER__USERNAME%>" size="15"
                                                           style="width:180px"></td>
                                            </tr>
                                            <tr>
                                                <td><span class="imcmsAdmText"><fmt:message
                                                        key="templates/login/index.html/6"/></span></td>
                                                <td>&nbsp;</td>
                                                <td><input type="password"
                                                           name="<%=VerifyUser.REQUEST_PARAMETER__PASSWORD%>" size="15"
                                                           style="width:180px">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="3">&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td colspan="2">&nbsp;</td>
                                                <td>
                                                    <table border="0" cellspacing="0" cellpadding="0">
                                                        <tr>
                                                            <td><input class="imcmsFormBtn" type="submit"
                                                                       style="width:80px"
                                                                       value="<fmt:message key="templates/login/index.html/2005"/>">
                                                            </td>
                                                            <td>&nbsp;</td>
                                                            <td><input class="imcmsFormBtn" type="submit"
                                                                       style="width:80px"
                                                                       name="<%= VerifyUser.REQUEST_PARAMETER__EDIT_USER %>"
                                                                       value="<fmt:message key="templates/login/index.html/2006"/>">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </form>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                </tr>
                <tr>
                    <td height="10" class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td colspan="4" class="imcmsAdmBgCont"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td class="imcmsAdmBorder"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                </tr>
                <tr class="imcmsAdmBgCont">
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="24" height="1"></td>
                    <td colspan="2"><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="24" height="1"></td>
                    <td><img
                            src="${cp}/imcms/images/1x1.gif" width="1" height="1"></td>
                </tr>
            </table>
        <td align="right" valign="top" background="${cp}/imcms/lang/images/admin/imcms_admin_shadow_right.gif">
            <img src="${cp}/imcms/lang/images/admin/imcms_admin_shadow_right_top.gif"
                 width="12" height="12" alt="" border="0"></td>
    </tr>
    <tr>
        <td colspan="2">
            <table border="0" cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td background="${cp}/imcms/lang/images/admin/imcms_admin_shadow_bottom.gif">
                        <img src="${cp}/imcms/lang/images/admin/imcms_admin_shadow_bottom_left.gif"
                             width="12" height="12" alt="" border="0"></td>
                    <td background="${cp}/imcms/lang/images/admin/imcms_admin_shadow_bottom.gif"
                        align="right">
                        <img src="${cp}/imcms/lang/images/admin/imcms_admin_shadow_bottom_right.gif"
                             width="12" height="12" alt="" border="0"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
