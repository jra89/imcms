<%@taglib prefix="imcms" uri="imcms"
        %><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
        %><%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%><html>
<imcms:variables/>
<head>
<title><c:out value="${document.headline}"/> - Powered by imCMS from imCode Partner AB</title>
<style type="text/css">
<!--
.imcHeading { font: bold medium Verdana,Geneva,sans-serif; color:#000099; }
TD { font: x-small Verdana,Geneva,sans-serif; color:#000000; }
.small { font: xx-small Verdana,Geneva,sans-serif; color:#000000; }
PRE, TT { font: x-small "Courier New", Courier, monospace; color:#888888; }

A:link    { color:#000099; }
A:visited { color:#000099; }
A:active  { color:#cc0000; }
A:hover   { color:#0000ff; }

LI { padding-bottom:5; }
-->
</style>

</head>
<body bgcolor="#f0f0ff" style="margin: 10 0 10 10">
<table border="0" cellspacing="0" cellpadding="5" height="100%" align="center" bgcolor="#ffffff">
<tr>
    <td valign="top">
    <table border="0" cellspacing="0" cellpadding="0" width="760">
    <tr>
        <td colspan="5"><imcms:include url="@documentationwebappurl@/servlet/GetDoc?meta_id=1054&template=imcmsDemoTop"/></td>
    </tr>
    <tr>
        <td colspan="5" height="15">&nbsp;</td>
    </tr>
    <tr valign="top">
        <td width="200"><imcms:include url="@documentationwebappurl@/servlet/GetDoc?meta_id=1054&template=imcmsDemoLeft"/></td>

        <td width="15">&nbsp;</td>

        <td width="385">
        <%      
            // Refactor
            String queryString = request.getQueryString();
            StringBuffer baseURL = request.getRequestURL();
            
            if (queryString == null) {
                baseURL.append("?" + "lang=");
            } else {
                // TODO 18n: refactor
                queryString = queryString.replaceFirst("&?lang=..", "");
                baseURL.append("?" + queryString + "&lang=");
            }
            
            pageContext.setAttribute("baseURL", baseURL);
            
          %>
            <a href="${baseURL}en"><img src="${pageContext.request.contextPath}/imcms/eng/images/admin/flags_iso_639_1/en.gif" alt="" style="border:0;" /></a>
            <a href="${baseURL}sv"><img src="${pageContext.request.contextPath}/imcms/swe/images/admin/flags_iso_639_1/sv.gif" alt="" style="border:0;" /></a>
          
            <imcms:text no="1" label="Text (Rubrik)" pre='<span class="imcHeading">' post='</span><br><br>' />
            <imcms:text no='2' label='<br>Text' post='<br><br>' />
            <imcms:menu no='1' label='<br><br>Meny (punktlista)'>
                <ul>
                    <imcms:menuloop>
                        <imcms:menuitem>
                            <li style="padding-bottom:5; color: green;"><imcms:menuitemlink><c:out value="${menuitem.document.headline}"/></imcms:menuitemlink></li>
                        </imcms:menuitem>
                        <imcms:menuitem>
                            <imcms:menuitemhide>
                                <li style="padding-bottom:5; color: red;"><imcms:menuitemlink><c:out value="${menuitem.document.headline}"/></imcms:menuitemlink></li>
                            </imcms:menuitemhide>
                        </imcms:menuitem>
                    </imcms:menuloop>
                </ul>
            </imcms:menu>
            <imcms:include url="@documentationwebappurl@/servlet/GetDoc?meta_id=1054&template=imcmsDemoContent" pre='<hr>' post='<hr>'/>
            <imcms:image no='3' label='Bild' pre='<br><br>' post='<br>'/><br>
            <imcms:include no='1' label='Dynamisk inkludering 1'/>
        </td>
    
        <td width="10">&nbsp;</td>

        <td width="150"><imcms:include url="@documentationwebappurl@/servlet/GetDoc?meta_id=1054&template=imcmsDemoRight"/></td>
    </tr>
    </table></td>
</tr>
<tr>
    <td align="center" valign="bottom">&nbsp;<br><imcms:admin/>
        <imcms:include url="@documentationwebappurl@/servlet/GetDoc?meta_id=1054&template=imcmsDemoBottom"/>        
    </td>
</tr>
</table>

</body>
</html>
