package imcode.util;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * @author M�rten Wallin
 * @author marten@imcode.com
 * @author Christoffer Hammarstr�m
 * @author kreiger@imcode.com
 * @version 0.2
 */
public class ImageFileMetaData {

    /** PNG Header Byte Sequence 0x * */
    private static final byte[] PNG_HEADER = {-119, 80, 78, 71, 13, 10, 26, 10};

    /** JPEG Start of segment marker * */
    private static final int MARKER = 0xFF;

    /** JPEG Start of Image * */
    private static final int SOI = 0xD8;

    /** JPEG Start of frame 0 * */
    private static final int SOF0 = 0xC0;

    /** JPEG Start of frame 1 * */
    private static final int SOF1 = 0xC1;

    /** JPEG Start of frame 2 * */
    private static final int SOF2 = 0xC2;

    /** JPEG Start of frame 3 * */
    private static final int SOF3 = 0xC3;

    /** JPEG JFIF Segment Marker * */
    private static final int APP0 = 0xE0;

    /** JPEG Start of Scan * */
    private static final int SOS = 0xDA;

    /** The width of the image * */
    private int width;

    /** The height of the image * */
    private int height;

    /** The type of the image i.e. GIF87a, GIF89a, JFIF * */
    private String type;

    private final static Logger log = Logger.getLogger( "imcode.util.ImageFileMetaData" );

    public ImageFileMetaData( InputStream fileStream, String fileName ) {
        width = 0;
        height = 0;
        type = "";

        try {
            DataInputStream dis = new DataInputStream( new BufferedInputStream( fileStream ) );
            if ( fileName.toLowerCase().endsWith( ".gif" ) ) // ****************** A GIF-File
            {
                byte[] t = new byte[6];
                dis.readFully( t );
                type = new String( t, "8859_1" );
                if ( !type.equals( "GIF87a" ) && !type.equals( "GIF89a" ) ) {
                    throw( new UnsupportedOperationException( "Unsupported gif-type" ) );
                }
                width = ( dis.read() + ( dis.read() << 8 ) );
                height = ( dis.read() + ( dis.read() << 8 ) );
                dis.close();
            } else if ( fileName.toLowerCase().endsWith( ".jpg" ) || fileName.toLowerCase().endsWith( ".jpeg" ) ) { // FIXME: Move into a separate (protected?) method. Preferably one that takes just a File
                if ( dis.read() != MARKER || dis.read() != SOI ) {
                    throw( new UnsupportedOperationException( "Unsupported jpeg-type" ) );
                }
                loop: 
                for ( ; ; ) {
                    while ( dis.read() != MARKER ) ; // Skip until we get 0xFF (Next marker)
                    int blockType;
                    while ( ( blockType = dis.read() ) == MARKER ) ; // Skip until we get something not 0xFF (the segmenttype), and wish for C0 to C3
                    int lengthOfBlock = ( dis.read() << 8 ) + dis.read(); // Get length of the segment
                    switch ( blockType ) {

                        case SOF0: // SOF0 Start of frame 0
                        case SOF1: // SOF1 Start of frame 1
                        case SOF2: // SOF2 Start of frame 2
                        case SOF3: // SOF3 Start of frame 3
                            int precision = dis.read();
                            if ( precision != 0x08 ) {
                                log.warn( "Trying to read jpeg with unsupported precision (0x" + Integer.toHexString( precision ) + ")" +  fileName );
                            }
                            height = ( dis.read() << 8 ) + dis.read();
                            width = ( dis.read() << 8 ) + dis.read();
                            //log.log(Log.DEBUG, "width: " + width);
                            //log.log(Log.DEBUG, "height: " + height);
                            dis.skipBytes( lengthOfBlock - 7 );
                            break;

                        case APP0: // APP0 JFIF segment marker
                            byte[] t = new byte[5];
                            dis.readFully( t );
                            type = new String( t, "8859_1" );
                            if ( !type.equals( "JFIF\0" ) ) {
                                log.warn( "Trying to read jpeg with unsupported type (" + type + ")" + fileName);
                            }
                            dis.skipBytes( lengthOfBlock - 7 );
                            break;

                        case SOS: // SOS Start of Scan
                            break loop;

                        default:
                            if ( lengthOfBlock > 2 ) {
                                dis.skipBytes( lengthOfBlock - 2 );
                            }
                    }
                }
            } else if ( fileName.toLowerCase().endsWith( ".png" ) ) // ***************** A PNG-File
            {
                // PNG starts with 8 bytes (here in decimal): 137 80 78 71 13 10 26 10
                //  indicating this is a PNG-file
                // Then blocks in this format:
                // 		Length: 4 bytes (not including itself, chunkType or CRC)
                // 		ChunkType: 4 bytes
                // 		ChunkData: ?
                // 		CRC: 4 bytes

                // The first chunk is the IHDR (ImageHeader Chunk) containing:
                // Width				4 bytes
                // Height				4 bytes
                // Bit depth			1 byte
                // Color type			1 byte
                // Compression method	1 byte
                // Filter method		1 byte
                // Interlace method		1 byte

                byte[] t = new byte[8];
                dis.readFully( t );

                //for(int slask=0;slask<8;slask++)
                //    type += "" + dis.read() + " ";
                //if(type.equals("137 80 78 71 13 10 26 10 "))
                if ( t.equals( PNG_HEADER ) ) {
                    dis.skipBytes( 4 ); // skip the blockLength
                    byte[] u = new byte[4];
                    dis.readFully( u );
                    String header = new String( u, "8859_1" );
                    if ( header.equals( "IHDR" ) ) {
                        // FIXME: There should be no <<32 below.
                        width = ( dis.read() << 24 ) | ( dis.read() << 16 ) | ( dis.read() << 8 ) | dis.read();
                        height = ( dis.read() << 24 ) | ( dis.read() << 16 ) | ( dis.read() << 8 ) | dis.read();
                        dis.close();
                    }
                }

            } else {
                log.error( "Unknown suffix" );
            }
            dis.close();
        } catch ( RuntimeException e ) {
            log.error( "Getting size from image", e );
        } catch ( IOException e ) {
            log.error( "Getting size from image", e );
        }

    }

    /**
     * getWidth
     * 
     * @return imagewidth
     */
    public int getWidth() {
        return width;
    }

    /**
     * getHeight
     * 
     * @return imageheight
     */
    public int getHeight() {
        return height;
    }

    /*
      TODO:
      String getImageInfo() -> artist and copyright-info		JPG/GIF
      boolean isInterlaced()									GIF
      boolean isAnimated()									GIF
      int countFrames()										GIF
      int paletteSize()										GIF
      int colorDepth()										JPG/GIF
    */

}

