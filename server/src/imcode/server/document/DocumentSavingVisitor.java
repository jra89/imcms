package imcode.server.document;

import imcode.server.document.textdocument.TextDocumentDomainObject;
import imcode.server.user.UserDomainObject;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentSavingVisitor extends DocumentStoringVisitor {

    public DocumentSavingVisitor( UserDomainObject user ) {
        super( user );
    }

    public void visitBrowserDocument( BrowserDocumentDomainObject browserDocument ) {
        deleteBrowserDocument( browserDocument );
        saveNewBrowserDocument( browserDocument );
    }

    private void deleteBrowserDocument( BrowserDocumentDomainObject browserDocument ) {
        String sqlStr = "DELETE FROM browser_docs WHERE meta_id = ?";
        service.sqlUpdateQuery( sqlStr, new String[]{"" + browserDocument.getId()} );
    }

    public void saveNewBrowserDocument( BrowserDocumentDomainObject document ) {
        String[] browserDocumentColumns = {"meta_id", "to_meta_id", "browser_id"};

        String sqlBrowserDocsInsertStr = makeSqlInsertString( "browser_docs", browserDocumentColumns );

        Map browserDocumentMap = document.getBrowserDocumentIdMap();
        for ( Iterator iterator = browserDocumentMap.keySet().iterator(); iterator.hasNext(); ) {
            BrowserDocumentDomainObject.Browser browser = (BrowserDocumentDomainObject.Browser)iterator.next();
            Integer metaIdForBrowser = (Integer)browserDocumentMap.get( browser );
            service.sqlUpdateQuery( sqlBrowserDocsInsertStr, new String[]{
                "" + document.getId(), "" + metaIdForBrowser, "" + browser.getId()
            } );
        }
    }

    public void visitHtmlDocument( HtmlDocumentDomainObject htmlDocument ) {
        String sqlStr = "UPDATE frameset_docs SET frame_set = ? WHERE meta_id = ?";
        service.sqlUpdateQuery( sqlStr, new String[]{htmlDocument.getHtml(), "" + htmlDocument.getId()} );
    }

    public void visitUrlDocument( UrlDocumentDomainObject urlDocument ) {
        String sqlStr = "UPDATE url_docs SET url_ref = ? WHERE meta_id = ?";
        service.sqlUpdateQuery( sqlStr, new String[]{urlDocument.getUrl(), "" + urlDocument.getId()} );
    }

    public void visitTextDocument( TextDocumentDomainObject textDocument ) {
        String sqlStr = "UPDATE text_docs SET template_id = ?, group_id = ?,\n"
                        + "default_template_1 = ?, default_template_2 = ? WHERE meta_id = ?";
        service.sqlUpdateQuery( sqlStr, new String[]{
            "" + textDocument.getTemplate().getId(),
            "" + textDocument.getTemplateGroupId(),
            "" + textDocument.getDefaultTemplateIdForRestrictedPermissionSetOne(),
            "" + textDocument.getDefaultTemplateIdForRestrictedPermissionSetTwo(),
            "" + textDocument.getId()
        } );

        updateTextDocumentTexts( textDocument );
        updateTextDocumentImages( textDocument );
        updateTextDocumentIncludes( textDocument );
        updateTextDocumentMenus( textDocument );
    }
}
