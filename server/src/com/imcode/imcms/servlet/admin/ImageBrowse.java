package com.imcode.imcms.servlet.admin;

import imcode.external.diverse.HtmlGenerator;
import imcode.external.diverse.VariableManager;
import imcode.server.ApplicationServer;
import imcode.server.IMCServiceInterface;
import imcode.server.user.UserDomainObject;
import imcode.util.GetImages;
import imcode.util.Utility;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Browse images in image-directory.
 */
public class ImageBrowse extends HttpServlet {

    // todo: make sure this is used in the jsp page.
    public static final String IMAGE_BROWSE_BEAN = "imagebrowsebean";

    private final static String IMG_NEXT_LIST_TEMPLATE = "Admin_Img_List_Next.html";
    private final static String IMG_PREVIOUS_LIST_TEMPLATE = "Admin_Img_List_Previous.html";

    private final static Logger log = Logger.getLogger( "ImageBrowse" );
    public static final String PARAMETER__CALLER = "caller";

    public void doPost( HttpServletRequest req, HttpServletResponse res ) throws IOException {
        doGet( req, res );
    }

    public void doGet( HttpServletRequest req, HttpServletResponse res ) throws IOException {
        getPage( req, res, this.getServletContext() );
    }

    public static void getPage( HttpServletRequest request, HttpServletResponse response, ServletContext application )
            throws IOException {
        IMCServiceInterface imcref = ApplicationServer.getIMCServiceInterface();
        String image_url = imcref.getImageUrl();
        File file_path = Utility.getDomainPrefPath( "image_path" );

        String meta_id = request.getParameter( "meta_id" );
        String img_no = request.getParameter( "img_no" );
        String img_preset = request.getParameter( "imglist" );//the choosen image to show
        String img_dir_preset = request.getParameter( "dirlist" );//the dir to chow

        // get img label
        String label = request.getParameter( "label" );
        if ( label == null ) {
            label = "";
        }

        if ( img_dir_preset == null ) {
            //if img_dir_preset null then its first time, or a prew. of choosen image
            img_dir_preset = request.getParameter( "dirlist_preset" ) == null
                             ? "" : request.getParameter( "dirlist_preset" );
        }

        if ( request.getParameter( "PREVIOUS_IMG" ) != null || request.getParameter( "NEXT_IMG" ) != null ) {
            img_preset = null;
        }

        //**handles the case when we have a image to show
        String img_tag = "";
        if ( img_preset == null ) {
            img_preset = "";
        } else {
            img_tag = "<img src='" + image_url + img_preset + "' align=\"top\">";
        }

        //*lets get some path we need later on
        String canon_path = file_path.getCanonicalPath(); //ex: C:\Tomcat3\webapps\imcms\images
        String root_dir_parent = file_path.getParentFile().getCanonicalPath();  //ex: C:\Tomcat3\webapps\webapps\imcms
        String root_dir_name = canon_path.substring( root_dir_parent.length() );
        if ( root_dir_name.startsWith( File.separator ) ) {
            root_dir_name = root_dir_name.substring( File.separator.length() );
            //ex: root_dir_name = images
        }

        //*lets get all the folders in an ArrayList
        List folderList = GetImages.getImageFolders( file_path, true );
        //lets add the rootdir to the dir list
        folderList.add( 0, file_path );


        //*lets get all the images in a folder and put them in an ArrayList
        File folderImgPath = new File( canon_path + img_dir_preset );
        List imgList = GetImages.getImageFilesInFolder( folderImgPath, true );


        //*the StringBuffers to save the lists html-code in
        StringBuffer imageOptions = new StringBuffer( imgList.size() * 64 );
        StringBuffer folderOptions = new StringBuffer( folderList.size() * 64 );


        //*hamdles the number of images to show and the buttons to admin it.
        UserDomainObject user = Utility.getLoggedOnUser( request );
        String adminImgPath = user.getLanguageIso639_2() + "/admin/";
        String previousButton = "&nbsp;";
        String nextButton = "&nbsp;";
        String startStr = request.getParameter( "img_curr_max" );
        int max = 1000;//the nr of img th show at the time
        int counter = 0; //the current startNr
        int img_numbers = imgList.size();//the total numbers of img
        if ( startStr != null ) {
            counter = Integer.parseInt( startStr );
        }
        //lest see if a previous button whas punshed
        if ( request.getParameter( "PREVIOUS_IMG" ) != null ) {
            counter = counter - ( max * 2 );
            if ( counter < 0 ) {
                counter = 0;
            }
        }
        // Lets bee ready to create buttons
        VariableManager nextButtonVm = new VariableManager();
        nextButtonVm.addProperty( "IMAGE_URL", image_url + adminImgPath );
        nextButtonVm.addProperty( "meta_id", meta_id );
        nextButtonVm.addProperty( "img_no", img_no );
        nextButtonVm.addProperty( "img_curr_max", Integer.toString( counter + max ) );
        nextButtonVm.addProperty( "SERVLET_URL", "" );

        VariableManager prevButtonVm = new VariableManager();
        prevButtonVm.addProperty( "IMAGE_URL", image_url + adminImgPath );
        prevButtonVm.addProperty( "meta_id", meta_id );
        prevButtonVm.addProperty( "img_no", img_no );
        prevButtonVm.addProperty( "img_curr_max", Integer.toString( counter + max ) );
        prevButtonVm.addProperty( "SERVLET_URL", "" );
        //lets get the teplatePath to the buttons
        File templatePath = ImageBrowse.getAdminTemplateFolder( imcref, user );
        //now we have to find out what buttons to show
        if ( counter > 0 ) {
            HtmlGenerator previousButtonHtmlObj = new HtmlGenerator( templatePath,
                                                                     ImageBrowse.IMG_PREVIOUS_LIST_TEMPLATE );
            previousButton = previousButtonHtmlObj.createHtmlString( prevButtonVm, request );
        }
        if ( img_numbers > counter + max ) {
            HtmlGenerator nextButtonHtmlObj = new HtmlGenerator( templatePath, ImageBrowse.IMG_NEXT_LIST_TEMPLATE );
            nextButton = nextButtonHtmlObj.createHtmlString( nextButtonVm, request );
        }

        //*lets create the image folder option list
        for ( int x = 0; x < folderList.size(); x++ ) {
            File fileObj = (File)folderList.get( x );

            //ok lets set up the folder name to show and the one to put as value
            String optionName = fileObj.getCanonicalPath();
            //lets remove the start of the path so we end up at the rootdir.
            if ( optionName.startsWith( canon_path ) ) {
                optionName = optionName.substring( root_dir_parent.length() );
                if ( optionName.startsWith( File.separator ) ) {
                    optionName = optionName.substring( File.separator.length() );
                }
            } else if ( optionName.startsWith( File.separator ) ) {
                optionName = optionName.substring( File.separator.length() );
            }
            //the path to put in the option value
            String optionPath = optionName;
            if ( optionPath.startsWith( root_dir_name ) ) {
                optionPath = optionPath.substring( root_dir_name.length() );
            }
            //ok now we have to replace all parent folders with a '-' char
            StringTokenizer token = new StringTokenizer( optionName, "\\", false );
            StringBuffer buff = new StringBuffer( "" );
            while ( token.countTokens() > 1 ) {
                token.nextToken();
                buff.append( "&nbsp;&nbsp;-" );
            }
            if ( token.countTokens() > 0 ) {
                optionName = buff.toString() + token.nextToken();
            }
            File urlFile = new File( optionName );
            String fileName = urlFile.getName();
            File parentDir = urlFile.getParentFile();
            if ( parentDir != null ) {
                optionName = parentDir.getPath() + "/";
            } else {
                optionName = "";
            }
            //filepathfix ex: images\nisse\kalle.gif to images/nisse/kalle.gif
            optionName = optionName.replace( File.separatorChar, '/' ) + fileName;

            optionName = optionName.replace( '-', '\\' );
            folderOptions.append(
                    "<option value=\"" + optionPath + "\""
                    + ( optionPath.equals( img_dir_preset ) ? " selected" : "" )
                    + ">"
                    + optionName
                    + "</option>\r\n" );
        }//end setUp option dir list


        //*lets create the image file option list
        for ( int i = counter; i < imgList.size() && i < counter + max; i++ ) {
            File fileObj = (File)imgList.get( i );

            String filePath = fileObj.getCanonicalPath();
            if ( filePath.startsWith( canon_path ) ) {
                filePath = filePath.substring( canon_path.length() );
            }
            if ( filePath.startsWith( File.separator ) ) {
                filePath = filePath.substring( File.separator.length() );
            }

            //lets copy the path before we gets rid of parent-dirs in the string to show
            //not whery sexy but it whill do fore now
            String imagePath = filePath;
            StringTokenizer token = new StringTokenizer( imagePath, "\\", false );
            StringBuffer buff = new StringBuffer( "" );
            while ( token.countTokens() > 1 ) {
                token.nextToken();
                //do nothing just get rid of every token exept the image name
            }
            if ( token.countTokens() > 0 ) {
                imagePath = buff.toString() + token.nextToken();
            }

            File urlFile = new File( filePath );
            String fileName = urlFile.getName();
            File parentDir = urlFile.getParentFile();

            if ( parentDir != null ) {
                filePath = parentDir.getPath() + "/";
            } else {
                filePath = "";
            }

            filePath = filePath.replace( File.separatorChar, '/' ) + fileName;
            StringTokenizer tokenizer = new StringTokenizer( filePath, "/", true );
            StringBuffer filePathSb = new StringBuffer();
            //the URLEncoder.encode() method replaces '/' whith "%2F" and the can't be red by the browser
            //that's the reason for the while-loop.
            while ( tokenizer.countTokens() > 0 ) {
                String temp = tokenizer.nextToken();
                if ( temp.length() > 1 ) {
                    filePathSb.append( java.net.URLEncoder.encode( temp ) );
                } else {
                    filePathSb.append( temp );
                }
            }

            String parsedFilePath = filePathSb.toString();

            imageOptions.append(
                    "<option value=\"" + parsedFilePath + "\""
                    + ( parsedFilePath.equals( img_preset ) ? " selected" : "" )
                    + ">"
                    + imagePath
                    + "\t["
                    + fileObj.length()
                    + "]</option>\r\n" );
        }

        ImageBrowseBean formBean = new ImageBrowseBean();

        String caller = request.getParameter( PARAMETER__CALLER );
        formBean.setCaller( caller );

        formBean.setMetaId( meta_id );
        formBean.setImageNumber( img_no );
        formBean.setImageList( img_preset );
        formBean.setDirListPreset( img_dir_preset );
        formBean.setFolders( folderOptions.toString() );
        formBean.setImagePreview( img_tag );
        formBean.setLabel( label );
        formBean.setNextButton( nextButton );
        formBean.setPreviousButton( previousButton );

        formBean.setOptions( imageOptions.toString() );

        if ( counter > img_numbers ) {
            counter = img_numbers;
        }
        formBean.setStartNumber( Integer.toString( counter - max + 1 ) );
        formBean.setStopNumber( Integer.toString( counter ) );
        formBean.setMaxNumber( Integer.toString( img_numbers ) );

        // anv�nds denna? vec.add( "#SERVLET_URL#" );

        request.setAttribute( IMAGE_BROWSE_BEAN, formBean );

        try {
            String forwardPath = "/imcms/swe/jsp/ImageBrowse.jsp";
            RequestDispatcher rd = application.getRequestDispatcher( forwardPath );
            rd.forward( request, response );
        } catch ( ServletException ex ) {
            log.error( "Error while dispatching: " + ex.getMessage(), ex );
        }

    }

    /**
     * GetAdminTemplateFolder. Takes the userobject as argument to detect the language
     * from the user and and returns the base path to the internal folder, hangs on the
     * language prefix and an "/admin/" string afterwards...
     * <p/>
     */
    private static File getAdminTemplateFolder( IMCServiceInterface imcref, imcode.server.user.UserDomainObject user ) {

        // Since our templates are located into the admin folder, we'll have to hang on admin
        File templateLib = imcref.getTemplatePath();
        // Lets get the users language id. Use the langid to get the lang prefix from db.
        String langPrefix = user.getLanguageIso639_2();
        templateLib = new File( templateLib, langPrefix + "/admin" );
        return templateLib;
    }

    public static class ImageBrowseBean {

        private String metaId;
        private String imageNumber;
        private String label;
        private String imageList;
        private String dirListPreset;
        private String folders;
        private String imagePreview;
        private String options;
        private String nextButton;
        private String previousButton;
        private String startNumber;
        private String stopNumber;
        private String maxNumber;

        private String caller;

        public String getMetaId() {
            return metaId;
        }

        public String getImageNumber() {
            return imageNumber;
        }

        public String getLabel() {
            return label;
        }

        public String getImageList() {
            return imageList;
        }

        public String getDirListPreset() {
            return dirListPreset;
        }

        public void setMetaId( String metaId ) {
            this.metaId = metaId;
        }

        public void setImageNumber( String imageNumber ) {
            this.imageNumber = imageNumber;
        }

        public void setLabel( String label ) {
            this.label = label;
        }

        public void setImageList( String imageList ) {
            this.imageList = imageList;
        }

        public void setDirListPreset( String dirListPreset ) {
            this.dirListPreset = dirListPreset;
        }

        public String getFolders() {
            return folders;
        }

        public void setFolders( String folders ) {
            this.folders = folders;
        }

        public String getImagePreview() {
            return imagePreview;
        }

        public void setImagePreview( String imagePreview ) {
            this.imagePreview = imagePreview;
        }

        public String getOptions() {
            return options;
        }

        public void setOptions( String options ) {
            this.options = options;
        }

        public String getNextButton() {
            return nextButton;
        }

        public void setNextButton( String nextButton ) {
            this.nextButton = nextButton;
        }

        public String getPreviousButton() {
            return previousButton;
        }

        public void setPreviousButton( String previousButton ) {
            this.previousButton = previousButton;
        }

        public String getStartNumber() {
            return startNumber;
        }

        public void setStartNumber( String startNumber ) {
            this.startNumber = startNumber;
        }

        public String getStopNumber() {
            return stopNumber;
        }

        public String getMaxNumber() {
            return maxNumber;
        }

        public void setStopNumber( String stopNumber ) {
            this.stopNumber = stopNumber;
        }

        public void setMaxNumber( String maxNumber ) {
            this.maxNumber = maxNumber;
        }

        public void setCaller( String caller ) {
            this.caller = caller;
        }

        public String getCaller() {
            return caller;
        }
    }
}
