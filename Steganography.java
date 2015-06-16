/*
CS361 Intro to Computer Security Program 3: Steganography.java
UTEID: ys5828
FIRSTNAME: Youngern
LASTNAME: Song
CSACCOUNT: songye
EMAIL: songye@utexas.edu
*/


import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;


public class Steganography{
    public static final int SUCCESS_E = 1;
    public static final int FAILURE = 2;
    public static final int TRUNCATE = 3;
    public static final int SUCCESS_D = 4;
	public static void main (String args[]) throws IOException{
		/*
		   Names of both the image file and message file 
		   should be passed on the command line. 
		   Additionally, a flag should be used to indicate whether you are 
		   encoding or decoding.

           Steganography -E image.png my-message
               takes message in file "my-message", creates file image-steg.png
           Steganography -D image-steg.png my-message-out
               takes image-steg.png, decodes, puts the message in my-message-out
		*/
		System.out.println("Running Steganography.java");
		//System.out.println();
        if(!(args[0].equals("-E") || args[0].equals("-D"))){
            System.out.printf("Invalid format. java Steganography -E/-D <imagefile> <text>\n");
            System.out.printf("Exiting...\n");
        	System.exit(0);
        }
		File imageFile = new File(args[1]);
		File plainText = new File(args[2]);
		String edtype = args[0];
		BufferedImage img = null;
        try {
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
        	System.out.printf("Invalid file, exiting...\n");
        	System.exit(0);
        }
        int height = img.getHeight();
        int width = img.getWidth();
        int pixels = height * width;
		String[] filename = args[1].split("\\.");	
		BufferedImage convertedImage = null;
		//Convert the image under type 3-BYTE BGR, as per piazza
		convertedImage = Steganography.createDuplicate(img);
		ImageIO.write(convertedImage, filename[1],new File(args[1]));
        EncoderDecoder ed = new EncoderDecoder();

        int result = 0;
        if(edtype.equals("-E")){
           result = ed.encode(convertedImage, plainText, args[1]);
        }
        else{
           result = ed.decode(imageFile, args[2]);
        }

        System.out.printf("Number of pixels: %d\n", pixels);
        switch(result){
        	case SUCCESS_E:
        	     System.out.println("Encryption successful.");
        	break;
        	case FAILURE:
        	     System.out.println("Unable to read or write file. Abort.");
        	break;
        	case TRUNCATE:
        	     System.out.println("File created, but message truncated.");
        	break;
        	case SUCCESS_D:
        	     System.out.println("Decryption successful");
        	break;
        	default:
        	     System.out.println("Default Case");
        }


	}
	public static BufferedImage createDuplicate(BufferedImage img){  //takes the input image, returns a duplicate
		BufferedImage dup = new BufferedImage(img.getWidth(),img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D grap = dup.createGraphics();
		grap.drawRenderedImage(img, null);
		grap.dispose();
		return dup;
	}
}
