import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import javax.servlet.*;
import javax.servlet.http.*;

import imcode.util.* ;
import javax.swing.ImageIcon;
/**
  Save image data.
  */
public class SaveImage extends HttpServlet {

	/**
	init()
	*/
	public void init( ServletConfig config ) throws ServletException {
		super.init( config ) ;
	}


	/**
	doPost()
	*/
	public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		String host 				= req.getHeader("Host") ;
		String imcserver 			= Utility.getDomainPref("adminserver",host) ;
		String start_url        	= Utility.getDomainPref( "start_url",host ) ;
		String servlet_url        	= Utility.getDomainPref( "servlet_url",host ) ;
		String image_url			= Utility.getDomainPref( "image_url",host ) ;

		imcode.server.User user ;
		String htmlStr = "" ;
		String submit_name = "" ;
		String values[] ;
		int img_no = 0 ;
		imcode.server.Image image = new imcode.server.Image( ) ;


		res.setContentType( "text/html" );
		ServletOutputStream out = res.getOutputStream( );

		// get meta_id
		String m_id = req.getParameter( "meta_id" ) ;
		int meta_id = Integer.parseInt( m_id ) ;
//		int parent_meta_id = Integer.parseInt( req.getParameter( "parent_meta_id" ) ) ;

		// get img_no
		String i_no = req.getParameter( "img_no" ) ;
		img_no = Integer.parseInt( i_no ) ;

		// get image_height
		String image_height = req.getParameter( "image_height" ) ;

		// get image_width
		String image_width = req.getParameter( "image_width" ) ;

		// get image_border
		String image_border = req.getParameter( "image_border" ) ;

		// get vertical_space
		String v_space = req.getParameter( "v_space" ) ;

		// get horizonal_space
		String h_space = req.getParameter( "h_space" ) ;

//******************************		
		boolean keepAspectRatio = (req.getParameter("keepAspectRatio") != null);
		log("SaveImage: KEEP_ASPECT_RATIO =" + req.getParameter("keepAspectRatio"));
		
		String sqlStr = "select image_name,imgurl,width,height,border,v_space,h_space,target,target_name,align,alt_text,low_scr,linkurl from images where meta_id = "+meta_id+" and name = "+img_no;
		String[] sql = IMCServiceRMI.sqlQuery(imcserver,sqlStr);
		String origWidth = req.getParameter("origW"); // width
		String origHeight = req.getParameter("origH"); // height
		
//*****************************

		try {
			image.setImageHeight( Integer.parseInt( image_height ) ) ;
		} catch ( NumberFormatException ex ) {
			image_height = "0" ;
			image.setImageHeight( 0 ) ;
		}

		try {
			image.setImageBorder( Integer.parseInt( image_border ) ) ;
		} catch ( NumberFormatException ex ) {
			image_border = "0" ;
			image.setImageBorder( 0 ) ;
		}

		try {
			image.setImageWidth( Integer.parseInt( image_width ) ) ;
		} catch ( NumberFormatException ex ) {
			image_width = "0" ;
			image.setImageWidth( 0 ) ;
		}
		
// ****************************** H�r b�rjar M�rtens lilla lekstuga
		if(keepAspectRatio && req.getParameter("ok") != null) {
		    int iHeight = 0 ;
		    try {
			iHeight = Integer.parseInt(image_height); // form width
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse image_height") ;
		    }
		    int iWidth = 0 ;
		    try {
			iWidth = Integer.parseInt(image_width); // form height
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse image_width") ;
		    }

		    int oldHeight = 0 ;
		    try {
			oldHeight = (sql.length>0)?Integer.parseInt(sql[3]):0; // database height
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse oldHeight") ;
		    }
		    int oldWidth = 0 ;
		    try {
			oldWidth = (sql.length>0)?Integer.parseInt(sql[2]):0; // database width
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse oldWidth") ;
		    }

		    //log("REQUESTED SIZE " + iWidth + "/" + iHeight);
		    int oHeight = 0 ;
		    try {
			oHeight = Integer.parseInt(origHeight); // image height
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse origHeight") ;
		    }
		    int oWidth = 0 ;
		    try {
			oWidth = Integer.parseInt(origWidth); // image width
		    } catch ( NumberFormatException ex ) {
			log("Failed to parse origHeight") ;
		    }

		    
		    double asp_rat = ((double)oWidth/(double)oHeight);
		    
		    int heightDiff = Math.abs(iHeight - oldHeight);
		    int widthDiff = Math.abs(iWidth - oldWidth);
		    
		    // Dominant value:
		    // 1. greatest diff, 2. greatest int, 3. width
	
		    if(widthDiff > heightDiff) {
			iHeight = (int)(iWidth/asp_rat);
		    } else if (heightDiff > widthDiff) {
			iWidth = (int)(iHeight*asp_rat);	
		    } else if(heightDiff == widthDiff) {
			if(iHeight>iWidth) {
			    iWidth = (int)(iHeight*asp_rat);
			} else {
			    iHeight = (int)(iWidth/asp_rat);
			}
		    } else {
			iHeight = (int)(iWidth*asp_rat);
		    }

		    image.setImageHeight( iHeight ) ;
		    image.setImageWidth( iWidth ) ;
		    image_width = "" + iWidth;
		    image_height = "" + iHeight;
		    //log("CALCULATED SIZE " + image_width + "/" + image_height);
		    
		}
		
		//*******************************  
		

		try {
			image.setVerticalSpace( Integer.parseInt( v_space ) ) ;
		} catch ( NumberFormatException ex ) {
			v_space = "0" ;
			image.setVerticalSpace( 0 ) ;
		}

		try {
			image.setHorizonalSpace( Integer.parseInt( h_space ) ) ;
		} catch ( NumberFormatException ex ) {
			h_space = "0" ;
			image.setHorizonalSpace( 0 ) ;
		}

		// get imageref
		String image_ref = req.getParameter( "imageref" ) ;
		if ( image_ref.length() > 0 ) {
		    if (image_ref.indexOf(image_url)==0) {
			image_ref = image_ref.substring(image_url.length()) ;
		    } else if (image_ref.indexOf("/")!=-1) {
			image_ref = image_ref.substring(image_ref.lastIndexOf("/")) ;
		    }
		}
		image.setImageRef( image_ref ) ;

		// get image_name
		String image_name = req.getParameter( "image_name" ) ;
		image.setImageName( image_name ) ;

		// get image_align
		String image_align = req.getParameter( "image_align" ) ;
		image.setImageAlign( image_align ) ;

		// get alt_text
		String alt_text = req.getParameter( "alt_text" ) ;
		image.setAltText( alt_text ) ;

		// get low_scr
		String low_scr = req.getParameter( "low_scr" ) ;
		image.setLowScr( low_scr ) ;

		// get target
		String target = req.getParameter( "target" ) ;
		image.setTarget( target ) ;


		// get target_name
		String target_name = req.getParameter( "target_name" ) ;
		image.setTargetName( target_name ) ;

		// get image_ref_link
		String imageref_link = req.getParameter( "imageref_link" ) ;
		image.setImageRefLink( imageref_link ) ;


		// redirect data
		String scheme = req.getScheme( );
		String serverName = req.getServerName( );
		int p = req.getServerPort( );
		String port = (p == 80) ? "" : ":" + p;




		// Get the session
		HttpSession session = req.getSession( true );

		// Does the session indicate this user already logged in?
		Object done = session.getValue( "logon.isDone" );  // marker object
		user = (imcode.server.User)done ;

		if( done == null ) {
			// No logon.isDone means he hasn't logged in.
			// Save the request URL as the true target and redirect to the login page.

			res.sendRedirect( scheme + "://" + serverName + port + start_url ) ;
			return ;
		}
		// Check if user has write rights
		if ( !IMCServiceRMI.checkDocAdminRights(imcserver,meta_id,user,131072 ) ) {	// Checking to see if user may edit this
			byte[] tempbytes ;
			tempbytes = AdminDoc.adminDoc(meta_id,meta_id,host,user,req,res) ;
			if ( tempbytes != null ) {
				out.write(tempbytes) ;
			}
			return ;
		}
		user.put("flags",new Integer(131072)) ;

		if( req.getParameter( "cancel" )!=null ) {
//			htmlStr = IMCServiceRMI.interpretTemplate( imcserver,meta_id,user ) ;
			byte[] tempbytes = AdminDoc.adminDoc(meta_id,meta_id,host,user,req,res) ;
			if ( tempbytes != null ) {
				out.write(tempbytes) ;
			}
			return ;

		} else if( req.getParameter( "show_img" )!=null ) {
					String imagePath = Utility.getDomainPref( "image_path",host );
		//****************************************************************
			// Delete relative path if there...
					//String imageName = (image_ref.lastIndexOf("/") != -1)?image_ref.substring(image_ref.lastIndexOf("/") +1):image_ref;	
			//ImageIcon icon = new ImageIcon(imagePath + imageName);
			int width = 0 ;//icon.getIconWidth();
			int height = 0 ; //icon.getIconHeight();
		//****************************************************************
			Vector vec = new Vector () ;
			vec.add("#imgName#") ;
			vec.add(image_name) ;
			vec.add("#imgRef#") ;
			vec.add(image_url+image_ref) ;
			vec.add("#imgWidth#") ;
			vec.add(image_width) ;
			vec.add("#imgHeight#") ;
			vec.add(image_height) ;
			
			vec.add("#origW#");
			vec.add("" + width);
			vec.add("#origH#");
			vec.add("" + height);
			
			vec.add("#imgBorder#") ;
			vec.add(image_border) ;
			vec.add("#imgVerticalSpace#") ;
			vec.add(v_space) ;
			vec.add("#imgHorizontalSpace#") ;
			vec.add(h_space) ;
			if ( "_top".equals(target) ) {
				vec.add("#target_name#") ;
				vec.add("") ;
				vec.add("#top_checked#") ;
			} else if ( "_self".equals(target) ) {
				vec.add("#target_name#") ;
				vec.add("") ;
				vec.add("#self_checked#") ;
			} else if ( "_blank".equals(target) ) {
				vec.add("#target_name#") ;
				vec.add("") ;
				vec.add("#blank_checked#") ;
			} else if ( "_parent".equals(target) ) {
				vec.add("#target_name#") ;
				vec.add("") ;
				vec.add("#blank_checked#") ;
			} else {
				vec.add("#target_name#") ;
				vec.add(target_name) ;
				vec.add("#other_checked#") ;
			}
			vec.add("selected") ;

			if ( "baseline".equals(image_align) ) {
				vec.add("#baseline_selected#") ;
			} else if ( "top".equals(image_align) ) {
				vec.add("#top_selected#") ;
			} else if ( "middle".equals(image_align) ) {
				vec.add("#middle_selected#") ;
			} else if ( "bottom".equals(image_align) ) {
				vec.add("#bottom_selected#") ;
			} else if ( "texttop".equals(image_align) ) {
				vec.add("#texttop_selected#") ;
			} else if ( "absmiddle".equals(image_align) ) {
				vec.add("#absmiddle_selected#") ;
			} else if ( "absbottom".equals(image_align) ) {
				vec.add("#absbottom_selected#") ;
			} else if ( "left".equals(image_align) ) {
				vec.add("#left_selected#") ;
			} else if ( "right".equals(image_align) ) {
				vec.add("#right_selected#") ;
			} else {
				vec.add("#none_selected#") ;
			}
			vec.add("selected") ;

			vec.add("#imgAltText#") ;
			vec.add(alt_text) ;
			vec.add("#imgLowScr#") ;
			vec.add(low_scr) ;
			vec.add("#imgRefLink#") ;
			vec.add(imageref_link) ;
			vec.add("#getMetaId#") ;
			vec.add(m_id) ;
			vec.add("#img_no#") ;
			vec.add(i_no) ;
			//IMCServiceRMI.saveImage( imcserver,meta_id,user,img_no,image ) ;
			//res.sendRedirect( scheme + "://" + serverName + port + servlet_url + "ChangeImage?meta_id=" + meta_id + "&img=" + img_no );
			String lang_prefix = IMCServiceRMI.sqlQueryStr(imcserver, "select lang_prefix from lang_prefixes where lang_id = "+user.getInt("lang_id")) ;
			htmlStr = IMCServiceRMI.parseDoc(imcserver,vec,"change_img.html", lang_prefix) ;
			out.print(htmlStr) ;
			return ;
		} else if ( req.getParameter("delete") != null ) {
			Vector vec = new Vector () ;
			vec.add("#imgName#") ;
			vec.add("") ;
			vec.add("#imgRef#") ;
			vec.add("") ;
			vec.add("#imgWidth#") ;
			vec.add("0") ;
			vec.add("#imgHeight#") ;
			vec.add("0") ;
			vec.add("#imgBorder#") ;
			vec.add("0") ;
			vec.add("#imgVerticalSpace#") ;
			vec.add("0") ;
			vec.add("#imgHorizontalSpace#") ;
			vec.add("0") ;
			vec.add("#target_name#") ;
			vec.add("") ;
			vec.add("#self_checked#") ;
			vec.add("selected") ;
			vec.add("#top_selected#") ;
			vec.add("selected") ;
			vec.add("#imgAltText#") ;
			vec.add("") ;
			vec.add("#imgLowScr#") ;
			vec.add("") ;
			vec.add("#imgRefLink#") ;
			vec.add("") ;
			vec.add("#getMetaId#") ;
			vec.add(String.valueOf(meta_id)) ;
			vec.add("#img_no#") ;
			vec.add(String.valueOf(img_no)) ;
			String lang_prefix = IMCServiceRMI.sqlQueryStr(imcserver, "select lang_prefix from lang_prefixes where lang_id = "+user.getInt("lang_id")) ;
			htmlStr = IMCServiceRMI.parseDoc(imcserver,vec,"change_img.html", lang_prefix) ;
			out.print(htmlStr) ;
			return ;
		} else {
			IMCServiceRMI.saveImage( imcserver,meta_id,user,img_no,image ) ;

			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd") ;
			Date dt = IMCServiceRMI.getCurrentDate(imcserver) ;

			sqlStr = "update meta set date_modified = '"+dateformat.format(dt)+"' where meta_id = "+meta_id ;
			IMCServiceRMI.sqlUpdateQuery(imcserver,sqlStr);

			//			htmlStr = IMCServiceRMI.interpretTemplate( imcserver,meta_id,user ) ;
			byte[] tempbytes = AdminDoc.adminDoc(meta_id,meta_id,host,user,req,res) ;
			if ( tempbytes != null ) {
				out.write(tempbytes) ;
			}
			return ;
		}
	}
}
