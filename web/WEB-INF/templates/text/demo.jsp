<%@ page
	
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"

%><%@taglib prefix="imcms" uri="imcms"
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" 
%><%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<imcms:variables/>
<head>
<title><c:out value="${document.headline}"/> - Powered by imCMS from imCode Partner AB</title>

<style type="text/css">
/*<![CDATA[*/
.imcHeading { margin-bottom:1em; font: bold medium Verdana,Geneva,sans-serif; color:#009; }
TD { font: x-small Verdana,Geneva,sans-serif; color:#000; }
.small { font: xx-small Verdana,Geneva,sans-serif; color:#000; }
PRE, TT { font: x-small "Courier New", Courier, monospace; color:#888; }

A:link    { color:#009; }
A:visited { color:#009; }
A:active  { color:#c00; }
A:hover   { color:#00f; }

LI { padding-bottom:5px; }
/*]]>*/
</style>

</head>
<body bgcolor="#f0f0ff" style="margin: 10px 0 10px 10px;">


<table border="0" cellspacing="0" cellpadding="5" align="center" style="height:100%; background-color:#fff;">
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
            <imcms:text no="1" label="Text (Rubrik)" pre='<div class="imcHeading">' post='</div>' />
            <imcms:text no='2' label='<br>Text' post='<br><br>' />
            <imcms:menu no='1' label='<br><br>Meny (punktlista)'>
                <ul>
                    <imcms:menuloop>
                        <imcms:menuitem>
                            <li style="color: green;"><imcms:menuitemlink><c:out value="${menuitem.document.headline}"/></imcms:menuitemlink></li>
                        </imcms:menuitem>
                        <imcms:menuitem>
                            <imcms:menuitemhide>
                                <li style="color: red;"><imcms:menuitemlink><c:out value="${menuitem.document.headline}"/></imcms:menuitemlink></li>
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
