<%@ page import="com.imcode.imcms.api.*,
                 java.util.*" errorPage="error.jsp" %>

<h1>Show document permissions</h1>
There are three basic kinds of permissions<br>
<ul>
  <li><%=DocumentPermissionSet.FULL%></li>
  <li><%=DocumentPermissionSet.READ%></li>
  <li><%=DocumentPermissionSet.NONE%></li>
</ul>

In between "<%=DocumentPermissionSet.FULL%>" and "<%=DocumentPermissionSet.READ%>" there can also be defined two that can be modified, they are called
<ul>
  <li><%=DocumentPermissionSet.RESTRICTED_1 %></li>
  <li><%=DocumentPermissionSet.RESTRICTED_2 %></li>
</ul>
and can be set differently for different pages (and sub pages).<br>
Every document has a mapping of permissions to roles.<br>
This is a map of the format (RoleName,DocumentPermissionSet)<br><br>
<% int documentId = 1001; %>
This is the mapping for document <%= documentId %>:<br>
<%
    ContentManagementSystem imcmsSystem = (ContentManagementSystem)request.getAttribute( RequestConstants.SYSTEM );
    DocumentService documentService = imcmsSystem.getDocumentService();
    Document doc = documentService.getTextDocument(documentId);
    Map permissionsMap = doc.getAllRolesMappedToPermissions();
    Set roles = permissionsMap.keySet();
    Iterator roleIteratore = roles.iterator();
    while( roleIteratore.hasNext() ) {
        String roleName = (String)roleIteratore.next();
        DocumentPermissionSet documentPermission = (DocumentPermissionSet)permissionsMap.get( roleName );%>
        The role "<%=roleName%>" has permission "<%= documentPermission.toString() %>" <br><br><%
    }
%>
<br>
Notice: Only the roles that has some permissions is shown above. If a role has <%=DocumentPermissionSet.NONE%> then
that role is not part of the result map.<br>

