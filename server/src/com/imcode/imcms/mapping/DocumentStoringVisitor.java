package com.imcode.imcms.mapping;

import com.imcode.db.Database;
import com.imcode.db.commands.SqlUpdateCommand;
import imcode.server.Imcms;
import imcode.server.ImcmsServices;
import imcode.server.document.BrowserDocumentDomainObject;
import imcode.server.document.DocumentVisitor;
import imcode.server.document.FileDocumentDomainObject;
import imcode.server.document.textdocument.ImageDomainObject;
import imcode.server.document.textdocument.ImageSource;
import imcode.server.document.textdocument.TextDocumentDomainObject;
import imcode.server.document.textdocument.TextDomainObject;
import imcode.server.user.UserDomainObject;
import imcode.util.DateConstants;
import imcode.util.io.FileInputStreamSource;
import imcode.util.io.FileUtility;
import imcode.util.io.InputStreamSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.UnhandledException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentStoringVisitor extends DocumentVisitor {

    protected Database database ;
    protected ImcmsServices services;

    private static final int FILE_BUFFER_LENGTH = 2048;
    private static final int DB_FIELD_MAX_LENGTH__FILENAME = 255;

    public DocumentStoringVisitor(Database database, ImcmsServices services) {
        this.database = database ;
        this.services = services ;
    }

    protected void saveFileDocumentFile( int fileDocumentId, FileDocumentDomainObject.FileDocumentFile fileDocumentFile,
                                         String fileId ) {
        try {
            InputStreamSource inputStreamSource = fileDocumentFile.getInputStreamSource();
            InputStream in;
            try {
                in = inputStreamSource.getInputStream();
            } catch (FileNotFoundException e) {
                throw new UnhandledException("The file for filedocument " + fileDocumentId
                        + " has disappeared.", e);
            }
            if (null == in) {
                return;
            }

            File file = getFileForFileDocumentFile( fileDocumentId, fileId );

            FileInputStreamSource fileInputStreamSource = new FileInputStreamSource(file);
            boolean sameFileOnDisk = file.exists() && inputStreamSource.equals(fileInputStreamSource) ;
            if ( sameFileOnDisk ) {
                return;
            }

            byte[] buffer = new byte[FILE_BUFFER_LENGTH];
            final OutputStream out = new FileOutputStream(file);
            try {
                for (int bytesRead; -1 != (bytesRead = in.read(buffer));) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
                in.close();
            }
            fileDocumentFile.setInputStreamSource(fileInputStreamSource);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

    public static File getFileForFileDocumentFile( int fileDocumentId, String fileId ) {
        File filePath = Imcms.getServices().getConfig().getFilePath();
        String filename = "" + fileDocumentId ;
        if (StringUtils.isNotBlank( fileId )) {
            filename += "."+FileUtility.escapeFilename(fileId) ;
        }
        return new File(filePath, filename);
    }

    static String makeSqlInsertString(String tableName, String[] columnNames) {
        return "INSERT INTO " + tableName + " (" + StringUtils.join(columnNames, ",") + ")"
                + "VALUES(?" + StringUtils.repeat(",?", columnNames.length - 1) + ")";
    }

    void updateTextDocumentTexts(TextDocumentDomainObject textDocument, TextDocumentDomainObject oldTextDocument, UserDomainObject user) {
        Map texts = textDocument.getTexts();
        String sqlDeleteTexts = "DELETE FROM texts WHERE meta_id = ?";
        final Object[] parameters = new String[]{"" + textDocument.getId()};
        database.execute(new SqlUpdateCommand(sqlDeleteTexts, parameters));
        for (Iterator iterator = texts.keySet().iterator(); iterator.hasNext();) {
            Integer textIndex = (Integer) iterator.next();
            TextDomainObject text = (TextDomainObject) texts.get(textIndex.intValue());
            if(oldTextDocument != null && oldTextDocument.getText(textIndex.intValue()) !=null && !oldTextDocument.getText(textIndex.intValue()).toString().equals("") && !text.equals(oldTextDocument.getText(textIndex.intValue()))){
                sqlInsertTextHistory(oldTextDocument, textIndex, oldTextDocument.getText(textIndex.intValue()), user);
            }
            sqlInsertText(textDocument, textIndex, text);
        }
    }

    void updateTextDocumentImages(TextDocumentDomainObject textDocument, TextDocumentDomainObject oldTextDocument, UserDomainObject user) {
        Map images = textDocument.getImages();
        String sqlDeleteImages = "DELETE FROM images WHERE meta_id = ?";
        final Object[] parameters = new String[]{"" + textDocument.getId()};
        database.execute(new SqlUpdateCommand(sqlDeleteImages, parameters));
        for (Iterator iterator = images.keySet().iterator(); iterator.hasNext();) {
            Integer imageIndex = (Integer) iterator.next();
            ImageDomainObject image = (ImageDomainObject) images.get(imageIndex.intValue());
            if(oldTextDocument != null && oldTextDocument.getImage(imageIndex.intValue())!=null && !oldTextDocument.getImage(imageIndex.intValue()).getSource().toStorageString().equals("") &&  !image.equals(oldTextDocument.getImage(imageIndex.intValue()))){
                sqlInsertImageHistory(oldTextDocument, imageIndex.intValue(), user);
            }
            saveDocumentImage(textDocument.getId(), imageIndex.intValue(), image);
        }
    }

    private void sqlInsertImageHistory(TextDocumentDomainObject textDocument, Integer imageIndex, UserDomainObject user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( DateConstants.DATETIME_FORMAT_STRING);
        String[] columnNames = new String[] {"imgurl", "width", "height", "border", "v_space", "h_space", "image_name", "target", "align", "alt_text", "low_scr", "linkurl", "type", "meta_id", "name", "modified_datetime", "user_id" };
        ImageDomainObject image = textDocument.getImage(imageIndex.intValue());
        final String[] parameters = getSqlImageParameters(image, textDocument.getId(), imageIndex.intValue());
        List <String> param =  new ArrayList <String>( Arrays.asList(parameters) ) ;
        param.add(dateFormat.format(new Date()));
        param.add(""+user.getId());
        database.execute(new SqlUpdateCommand(makeSqlInsertString("images_history", columnNames), param.toArray(new String[param.size()])));
    }

    void updateTextDocumentIncludes(TextDocumentDomainObject textDocument) {
        Map includes = textDocument.getIncludes();
        String sqlDeleteDocumentIncludes = "DELETE FROM includes WHERE meta_id = ?";
        final Object[] parameters = new String[]{"" + textDocument.getId()};
        database.execute(new SqlUpdateCommand(sqlDeleteDocumentIncludes, parameters));
        for (Iterator iterator = includes.keySet().iterator(); iterator.hasNext();) {
            Integer includeIndex = (Integer) iterator.next();
            Integer includedDocumentId = (Integer) includes.get(includeIndex);
            sqlInsertTextDocumentInclude(textDocument, includeIndex, includedDocumentId);
        }
    }

    private void sqlInsertText(TextDocumentDomainObject textDocument, Integer textIndex, TextDomainObject text) {
        final Object[] parameters = new String[]{
            "" + textDocument.getId(), "" + textIndex, text.getText(), "" + text.getType()
        };
        database.execute(new SqlUpdateCommand("INSERT INTO texts (meta_id, name, text, type) VALUES(?,?,?,?)", parameters));
    }

    private void sqlInsertTextHistory(TextDocumentDomainObject textDocument, Integer textIndex, TextDomainObject text, UserDomainObject user) {
        SimpleDateFormat dateFormat = new SimpleDateFormat( DateConstants.DATETIME_FORMAT_STRING);
        final Object[] parameters = new String[]{
            "" + textDocument.getId(), "" + textIndex, text.getText(), "" + text.getType(), dateFormat.format(new Date()), ""+user.getId()
        };
        database.execute(new SqlUpdateCommand("INSERT INTO texts_history (meta_id, name, text, type, modified_datetime, user_id) VALUES(?,?,?,?,?,?)", parameters));
    }

    private void sqlInsertTextDocumentInclude(TextDocumentDomainObject textDocument, Integer includeIndex,
                                              Integer includedDocumentId) {
        final Object[] parameters = new String[]{
            "" + textDocument.getId(), "" + includeIndex, "" + includedDocumentId
        };
        database.execute(new SqlUpdateCommand("INSERT INTO includes (meta_id, include_id, included_meta_id) VALUES(?,?,?)", parameters));
    }

    public void saveDocumentImage(int meta_id, int img_no, ImageDomainObject image) {
        String sqlStr = "update images\n"
                + "set imgurl  = ?, \n"
                + "width       = ?, \n"
                + "height      = ?, \n"
                + "border      = ?, \n"
                + "v_space     = ?, \n"
                + "h_space     = ?, \n"
                + "image_name  = ?, \n"
                + "target      = ?, \n"
                + "align       = ?, \n"
                + "alt_text    = ?, \n"
                + "low_scr     = ?, \n"
                + "linkurl     = ?, \n"
                + "type        = ?  \n"
                + "where meta_id = ? \n"
                + "and name = ? \n";

        int rowUpdateCount = sqlImageUpdateQuery(sqlStr, image, meta_id, img_no);
        if (0 == rowUpdateCount) {
            sqlStr = "insert into images (imgurl, width, height, border, v_space, h_space, image_name, target, align, alt_text, low_scr, linkurl, type, meta_id, name)"
                    + " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?)";

            sqlImageUpdateQuery(sqlStr, image, meta_id, img_no);
        }
    }

    private int sqlImageUpdateQuery(String sqlStr, ImageDomainObject image, int meta_id, int img_no) {
        final String[] parameters = getSqlImageParameters(image, meta_id, img_no);
        return ((Number)database.execute(new SqlUpdateCommand(sqlStr, parameters))).intValue();
    }

    private static String[] getSqlImageParameters(ImageDomainObject image, int meta_id, int img_no) {
        ImageSource imageSource = image.getSource();
        return new String[] {
            imageSource.toStorageString(),
            "" + image.getWidth(),
            "" + image.getHeight(),
            "" + image.getBorder(),
            "" + image.getVerticalSpace(),
            "" + image.getHorizontalSpace(),
            image.getName(),
            image.getTarget(),
            image.getAlign(),
            image.getAlternateText(),
            image.getLowResolutionUrl(),
            image.getLinkUrl(),
            "" + imageSource.getTypeId(),
            "" + meta_id,
            "" + img_no,
        };
    }

    public void visitFileDocument( FileDocumentDomainObject fileDocument ) {
        Map fileDocumentFiles = fileDocument.getFiles();

        String sqlDelete = "DELETE FROM fileupload_docs WHERE meta_id = ?";
        final Object[] parameters1 = new String[]{"" + fileDocument.getId()};
        database.execute(new SqlUpdateCommand(sqlDelete, parameters1));

        for ( Iterator iterator = fileDocumentFiles.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry)iterator.next();
            String fileId = (String)entry.getKey();
            FileDocumentDomainObject.FileDocumentFile fileDocumentFile = (FileDocumentDomainObject.FileDocumentFile)entry.getValue();

            String filename = fileDocumentFile.getFilename();
            if ( filename.length() > DB_FIELD_MAX_LENGTH__FILENAME ) {
                filename = truncateFilename( filename, DB_FIELD_MAX_LENGTH__FILENAME );
            }
            String sqlInsert = "INSERT INTO fileupload_docs (meta_id, variant_name, filename, mime, created_as_image, default_variant) VALUES(?,?,?,?,?,?)";
            boolean isDefaultFile = fileId.equals( fileDocument.getDefaultFileId());
            final Object[] parameters = new String[]{""+ fileDocument.getId(), fileId, filename, fileDocumentFile.getMimeType(), fileDocumentFile.isCreatedAsImage() ? "1" : "0", isDefaultFile ? "1" : "0"};
            database.execute(new SqlUpdateCommand(sqlInsert, parameters));
            saveFileDocumentFile( fileDocument.getId(), fileDocumentFile, fileId );
        }
        DocumentMapper.deleteOtherFileDocumentFiles( fileDocument ) ;
    }

    private String truncateFilename(String filename, int length) {
        String truncatedFilename = StringUtils.left(filename, length);
        String extensions = getExtensionsFromFilename(filename);
        if (extensions.length() > length) {
            return truncatedFilename;
        }
        String basename = StringUtils.chomp(filename, extensions);
        String truncatedBasename = StringUtils.substring(basename, 0, length - extensions.length());
        truncatedFilename = truncatedBasename + extensions;
        return truncatedFilename;
    }

    private String getExtensionsFromFilename(String filename) {
        String extensions = "";
        Matcher matcher = Pattern.compile("(?:\\.\\w+)+$").matcher(filename);
        if (matcher.find()) {
            extensions = matcher.group();
        }
        return extensions;
    }

    public void visitBrowserDocument( BrowserDocumentDomainObject browserDocument ) {
        deleteBrowserDocument( browserDocument );
        saveNewBrowserDocument( browserDocument );
    }

    private void deleteBrowserDocument( BrowserDocumentDomainObject browserDocument ) {
        String sqlStr = "DELETE FROM browser_docs WHERE meta_id = ?";
        final Object[] parameters = new String[]{"" + browserDocument.getId()};
        database.execute(new SqlUpdateCommand(sqlStr, parameters));
    }

    public void saveNewBrowserDocument( BrowserDocumentDomainObject document ) {
        String[] browserDocumentColumns = {"meta_id", "to_meta_id", "browser_id"};

        String sqlBrowserDocsInsertStr = makeSqlInsertString( "browser_docs", browserDocumentColumns );

        Map browserDocumentMap = document.getBrowserDocumentIdMap();
        for ( Iterator iterator = browserDocumentMap.keySet().iterator(); iterator.hasNext(); ) {
            BrowserDocumentDomainObject.Browser browser = (BrowserDocumentDomainObject.Browser)iterator.next();
            Integer metaIdForBrowser = (Integer)browserDocumentMap.get( browser );
            final Object[] parameters = new String[]{
                "" + document.getId(), "" + metaIdForBrowser, "" + browser.getId()
            };
            database.execute(new SqlUpdateCommand(sqlBrowserDocsInsertStr, parameters));
        }
    }

    protected void updateTextDocumentMenus(final TextDocumentDomainObject textDocument, final TextDocumentDomainObject oldTextDocument, final UserDomainObject savingUser) {
        MenuSaver menuSaver = new MenuSaver(database) ;
        menuSaver.updateTextDocumentMenus(textDocument, services, oldTextDocument, savingUser);
    }
}
