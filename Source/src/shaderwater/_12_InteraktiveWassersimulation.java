package shaderwater;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class _12_InteraktiveWassersimulation {
	// für die Fehlerermittlung der Shader
	private static ByteBuffer infoBuffer = BufferUtils.createByteBuffer(1024);
	private static IntBuffer errorBuffer = BufferUtils.createIntBuffer(1);
	
	// *** Sound ***
	private static String SOUND_SPLASH 	= "sounds/s20_min_15db.wav"; 	
	private static String SOUND_WATER 	= "sounds/washing.wav"; 		
	private static File backgroundMusicFile;
	private static _11_MusicPlayer myMusicPlayer;
	private static AudioClip sound_smooth, sound_splash;
	// *** Sound ***
	
	public static JButton butRefraktion, butSound, but1, but2, but3, but4, but5, but6, but7;
	public static JSlider slider4;
	
	public static int 		backGround = 1;
	public static boolean 	refEnable  = true;
	public static boolean 	souEnable  = true;
	
	public static void main(String[] args) throws LWJGLException {
		try {						
			JFrame f 						= new JFrame();
			Canvas c 						= new Canvas();
			
			// *** Sound ***
			backgroundMusicFile = new File(SOUND_WATER); 	
			try {
				sound_smooth = Applet.newAudioClip(backgroundMusicFile.toURI().toURL());				
	            System.out.println("Soundfile1  successfull loaded");
	        } catch(Exception ex) {
				System.out.println("Soundfile1  error");
				System.exit(0);
			}	
			backgroundMusicFile = new File(SOUND_SPLASH); 	
			try {
				sound_splash = Applet.newAudioClip(backgroundMusicFile.toURI().toURL());				
	            System.out.println("Soundfile2  successfull loaded");
	        } catch(Exception ex) {
				System.out.println("Soundfile2  error");
				System.exit(0);
			}	        
			myMusicPlayer 	    = new _11_MusicPlayer(sound_smooth, sound_splash);
			// *** Sound ***
			
			int MAXI = 100;
		    JSlider slider1 = new JSlider();		
		    slider1.setBorder(BorderFactory.createTitledBorder("CAUSTIC INPUT"));
		    slider1.setMaximum(MAXI);
		    slider1.setValue(50);
		    slider1.setMajorTickSpacing(MAXI/5);
		    slider1.setMinorTickSpacing(MAXI/20);
		    slider1.setPaintTicks(true);
		    slider1.setPaintLabels(true);		    

		    JSlider slider2 = new JSlider();		
		    slider2.setBorder(BorderFactory.createTitledBorder("REF"));
		    slider2.setMaximum(MAXI);
		    slider2.setValue(40);
		    slider2.setMajorTickSpacing(MAXI/5);
		    slider2.setMinorTickSpacing(MAXI/20);
		    slider2.setPaintTicks(true);
		    slider2.setPaintLabels(true);
		    slider2.setOrientation(SwingConstants.VERTICAL);

		    JSlider slider3 = new JSlider();		
		    slider3.setBorder(BorderFactory.createTitledBorder("SHA"));
		    slider3.setMaximum(MAXI);
		    slider3.setValue(45);
		    slider3.setMajorTickSpacing(MAXI/5);
		    slider3.setMinorTickSpacing(MAXI/20);
		    slider3.setPaintTicks(true);
		    slider3.setPaintLabels(true);
		    slider3.setOrientation(SwingConstants.VERTICAL);
		    
		    ActionListener aktion = new KnopfdruckShader7();
		    JPanel iconBar = new JPanel();
		    butRefraktion = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/0.jpg"))); 
		    iconBar.add(butRefraktion);
		    butRefraktion.addActionListener(aktion);
		    butRefraktion.setActionCommand("ref");
		    
		    slider4 = new JSlider();		
		    slider4.setBorder(BorderFactory.createTitledBorder("SCALE"));
		    slider4.setMaximum(500);
		    slider4.setValue(200);
		    slider4.setMajorTickSpacing(100);
		    slider4.setMinorTickSpacing(25);
		    slider4.setPaintTicks(true);
		    slider4.setPaintLabels(true);
		    slider4.setOrientation(SwingConstants.HORIZONTAL);
		    iconBar.add(slider4);
		    
		    butSound = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/S.jpg")));
		    iconBar.add(butSound);
		    butSound.addActionListener(aktion);
		    butSound.setActionCommand("sou");
		    
		    but1 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/1.jpg"))); 
		    iconBar.add(but1);
		    but1.addActionListener(aktion);
		    but1.setActionCommand("1");
		    
		    but2 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/2.jpg"))); 
		    iconBar.add(but2);
		    but2.addActionListener(aktion);
		    but2.setActionCommand("2");
		    
		    but3 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/3.jpg"))); 
		    iconBar.add(but3);
		    but3.addActionListener(aktion);
		    but3.setActionCommand("3");
		    
		    but4 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/4.jpg"))); 
		    iconBar.add(but4);
		    but4.addActionListener(aktion);
		    but4.setActionCommand("4");
		    
		    but5 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/5.jpg"))); 
		    iconBar.add(but5);
		    but5.addActionListener(aktion);
		    but5.setActionCommand("5");
		    
		    but6 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/6.jpg"))); 
		    iconBar.add(but6);
		    but6.addActionListener(aktion);
		    but6.setActionCommand("6");
		    
		    but7 = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/B.jpg"))); 
		    iconBar.add(but7);
		    but7.addActionListener(aktion);
		    but7.setActionCommand("7");

		    f.getContentPane().add(c);
		    f.getContentPane().add(iconBar, BorderLayout.NORTH);
		    f.getContentPane().add(slider1, BorderLayout.SOUTH);
		    f.getContentPane().add(slider2, BorderLayout.EAST);
		    f.getContentPane().add(slider3, BorderLayout.WEST);	

			// Fenstergröße beim Start
			final int WIDTH 	= 1920;//640;
			final int HEIGHT 	= 1080;//480;
			
			int xOff = -10, yOff = -10;
			f.setBounds(xOff, yOff, xOff+WIDTH, yOff+HEIGHT);
			f.setVisible(true);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			Display.setParent(c);
			Display.create();
			Display.makeCurrent();	       		
			
			// **********************************************************************************************
			// Für die Dynamik des Wassers benötigen wir zunächst 3 FrameBuffer,
			// die ihre Plätze im Laufe der Iterationen zyklisch tauschen. 
			int WB = WIDTH/4, HB = HEIGHT/4;			
			
			int waterTexture_1 		= glGenTextures();
			int waterTexture_1_FBO 	= glGenFramebuffers();
			glBindTexture(GL_TEXTURE_2D, waterTexture_1);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_1_FBO);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture_1, 0);
			
			int waterTexture_2 		= glGenTextures();
			int waterTexture_2_FBO 	= glGenFramebuffers();
			glBindTexture(GL_TEXTURE_2D, waterTexture_2);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_2_FBO);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture_2, 0);
			
			int waterTexture_3 		= glGenTextures();
			int waterTexture_3_FBO 	= glGenFramebuffers();
			glBindTexture(GL_TEXTURE_2D, waterTexture_3);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_3_FBO);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture_3, 0);

			int waterTexture_4 		= glGenTextures();
			int waterTexture_4_FBO 	= glGenFramebuffers();
			glBindTexture(GL_TEXTURE_2D, waterTexture_4);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WB, HB, 0, GL_BGRA, GL_UNSIGNED_BYTE, (ByteBuffer)null);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_4_FBO);
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, waterTexture_4, 0);
			
			// wir wollen die drei FrameBuffer (die in drei Texturen schreiben) allerdings "unsichtbar"
			// im Hintergrund füllen und erst im letzten Schritt ausgewählte Inhalte anzeigen
			glBindFramebuffer(GL_FRAMEBUFFER, 0);			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************

			// **********************************************************************************************
			// Eine Texture mit einer 2D-Gauss-Verteilung wird erzeugt.Der Bereich läuft von 
			// 0 (Transparent) bis 1 (volle Füllung) und wird später additiv hinzugenommen.
			int gaussTexture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, gaussTexture);
			int WG = 32; // 32
			int HG = 32; // 32
			
			FloatBuffer blobBuffer = BufferUtils.createFloatBuffer(WG*HG);
			
			float sigmaSq = WG/1, S = 0.4f;
			for (int y=0,Y=-HG/2;y<HG;y++,Y++)
				for (int x=0,X=-WG/2;x<WG;x++,X++)
					blobBuffer.put(x+y*WG, S * (float)Math.exp(-0.5*(X*X+Y*Y)/sigmaSq));

			glTexImage2D(GL_TEXTURE_2D, 0, GL_LUMINANCE, WG, HG, 0, GL_LUMINANCE, GL_FLOAT, blobBuffer);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************			
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			File file = new File("images/wasser2.jpg"); 
			int WU 	= 640; 
			int HU 	= 480;			
			
			int texGround1 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround1);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			file = new File("images/steine_rot2.jpg");		
			WU 	= 640; 
			HU 	= 480;	
			
			int texGround2 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround2);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			file = new File("images/sand.jpg"); 			
			WU 	= 1280; 
			HU 	= 1024;
			
			int texGround3 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround3);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			file = new File("images/klunker2.jpg"); 			
			WU 	= 721; 
			HU 	= 480;
			
			int texGround4 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround4);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			file = new File("images/kies_grün.jpg"); 		
			WU 	= 800; 
			HU 	= 480;
			
			int texGround5 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround5);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			//file = new File("images/rechts_nah_2.jpg"); 			
			//WU 	= 800; 
			//HU 	= 532;
			
			file  = new File("images/danke2.jpg");
			
			WU = 890;
			HU = 596;
			
			int texGround6 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround6);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Texture für den Wasseruntergrund laden		
			//File file = new File("grauerUntergrund.bmp");
			//File file = new File("schwarzerUntergrund.bmp");
			file = new File("images/schwarzerUntergrund.png"); 			
			WU 	= 640; 
			HU 	= 480;
			
			int texGround7 = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texGround7);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Texture für den Sonne laden			
			File file2 = new File("images/mysun.jpg");
			final int WU2 	= 322;
			final int HU2 	= 297;			
			
			int texSun = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texSun);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU2, HU2, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU2*HU2*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file2).getRGB(0,0, WU2, HU2, new int[WU2*HU2], 0, WU2)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************			

			// **********************************************************************************************
			// Texture für den Mond laden			
			File file3 = new File("images/mymoon3.jpg"); 
			final int WU3 	= 322;
			final int HU3 	= 297;			
			
			int texMoon = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texMoon);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU3, HU3, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU3*HU3*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file3).getRGB(0,0, WU3, HU3, new int[WU3*HU3], 0, WU3)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************			

			// **********************************************************************************************
			// Texture für den Mond laden			
			File file4 = new File("images/wolke7_200_gray2.jpg"); 
			final int WU4 	= 200;
			final int HU4 	= 200;			
			
			int texCloud = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texCloud);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU4, HU4, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU4*HU4*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file4).getRGB(0,0, WU4, HU4, new int[WU4*HU4], 0, WU4)).rewind());
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);					
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Blöcke austauschbar bleiben
			// **********************************************************************************************			
			
			// **********************************************************************************************
			// Shader für die Wasserdynamik
			String fragShader_waterDynamic = 
				"uniform sampler2D tex1;"+
				"uniform sampler2D tex2;"+
				"uniform vec2 s;"+
				"" +
				"void main (void) {"+				
				"	gl_FragColor = normalize((" +
				"		texture2D(tex1, gl_TexCoord[0].st+vec2(+s.x,0)) +" +
				"		texture2D(tex1, gl_TexCoord[0].st+vec2(-s.x,0)) +" +
				"		texture2D(tex1, gl_TexCoord[0].st+vec2(0,+s.y)) +" +
				"		texture2D(tex1, gl_TexCoord[0].st+vec2(0,-s.y))" +
				"		) * 0.5 - texture2D(tex2, gl_TexCoord[0].st)) * 0.999; " +
				"}";			
				
			// Shader erzeugen, mit Sourcecode füllen und kompilieren
			int shaderObjectF = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF, fragShader_waterDynamic);
			glCompileShader(shaderObjectF);

			// Programm erzeugen, mit Shader verknüpfen und das Programm linken
			int program_waterDynamic = glCreateProgram();
			glAttachShader(program_waterDynamic, shaderObjectF);
			glLinkProgram(program_waterDynamic);	

			// Parameter, die für den Shader relevant sind, als Uniforms 
			// mit dem Programm verknüpfen
			int sLoc 	= glGetUniformLocation(program_waterDynamic, "s");
			int tex1Loc = glGetUniformLocation(program_waterDynamic, "tex1");
			int tex2Loc = glGetUniformLocation(program_waterDynamic, "tex2");
			
			// die Uniforms erzeugen und binden
			glUseProgram(program_waterDynamic);
			
			float WSTEP = 0.68235216232f;			
			glUniform2f(sLoc, WSTEP/WB, WSTEP/HB);
			glUniform1i(tex1Loc, 1);
			glUniform1i(tex2Loc, 0);
			
			glUseProgram(0);
			// **********************************************************************************************
									
			// **********************************************************************************************
			// Shader für die Verknüpfung von Wasserdynamik und einer Texture, plus ShadowEffekt
			String fragShader_caustic = 
					"uniform sampler2D tex1;" +			// wasserdynamik	
					"uniform sampler2D tex3;" +			// mond
					"" +			
					"uniform vec2 s;"+					// pixelversatz
					"uniform float scale;" +			// wassertiefe
					""+
					"void main (void) {"+
					"	vec2 myPos 			= gl_TexCoord[0].st;" +
					"	float g_x 			= (texture2D(tex1, myPos + vec2(-s.x, 0)) - texture2D(tex1, myPos + vec2(+s.x, 0))).x;" +
					"	float g_y 			= (texture2D(tex1, myPos + vec2(0, -s.y)) - texture2D(tex1, myPos + vec2(0, +s.y))).y;" +
					"" +
					"	vec3 n_surface 		= normalize(cross(vec3(1.0f, 0.0f, g_x), vec3(0.0f, 1.0f, g_y))); " +
					"	vec3 n_view 		= vec3(0.0f, 0.0f, 1.0f);" +
					"	vec3 refractVector	= refract(n_view, n_surface, 1.33f);" +
					"" +
					"	vec4 caustic		= texture2D(tex3, 10.0f * refractVector.xy + vec2(0.5f, 0.5f));" +
					"" +
					"	gl_FragColor 		= caustic;" +
					"}";		
			
			int shaderObjectF2 = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF2, fragShader_caustic);
			glCompileShader(shaderObjectF2);

			int program_caustic = glCreateProgram();
			glAttachShader(program_caustic, shaderObjectF2);
			glLinkProgram(program_caustic);			
			
			int sLoc2 		= glGetUniformLocation(program_caustic, "s");
			int scaleLoc2 	= glGetUniformLocation(program_caustic, "scale");
			int tex1Loc2 	= glGetUniformLocation(program_caustic, "tex1");
			int tex2Loc2 	= glGetUniformLocation(program_caustic, "tex2");
			int tex3Loc2 	= glGetUniformLocation(program_caustic, "tex3");
						
			glUseProgram(program_caustic);		
			
			glUniform2f(sLoc2, 		1.0f/WB, 1.0f/HB);			
			glUniform1f(scaleLoc2,  0.0f); // -200.0f
			glUniform1i(tex1Loc2, 	0);
			glUniform1i(tex2Loc2, 	1);
			glUniform1i(tex3Loc2, 	2);
			
			glUseProgram(0);
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Shader für die Verknüpfung von Wasserdynamik als farbliche Komposition
			// Shader für die Verknüpfung von Wasserdynamik und einer Texture, plus ShadowEffekt
			String fragShader_water = 
				"uniform sampler2D tex1;" +			// wasserdynamik	
				"uniform sampler2D tex2;" +			// untergrund
				"uniform sampler2D tex3;" +			// mond
				"uniform sampler2D tex4;" + 		// kaustik
				"uniform sampler2D tex5;" +			// wolken
				"" +
				"uniform bool withRefraction;" + 	// on/off refraktion			
				"uniform vec4 w;" + 				// (untergrund, kaustik, wolkenRefl, shadow/mondRefl) 
				"uniform vec2 s;"+					// pixelversatz
				"uniform float scale;" +			// wassertiefe
				"uniform float offSetCloud;" +		// offSet to shift the clouds
				""+
				"void main (void) {" +
				"	vec2 myPos 			= gl_TexCoord[0].st;" +
				"	float g_x 			= (texture2D(tex1, myPos + vec2(-s.x, 0)) - texture2D(tex1, myPos + vec2(+s.x, 0))).x;" +
				"	float g_y 			= (texture2D(tex1, myPos + vec2(0, -s.y)) - texture2D(tex1, myPos + vec2(0, +s.y))).y;" +
				"" +
				"	vec3 n_surface 		= normalize(cross(vec3(1.0f, 0.0f, g_x), vec3(0.0f, 1.0f, g_y))); " +
				"	vec3 n_view 		= vec3(0.0f, 0.0f, 1.0f);" +
				"	vec3 refractVector	= refract(n_view, n_surface, 1.33f);" +
				"	vec3 reflectVector	= reflect(n_view, n_surface);" +
				"" +
				"	float shadow   		= 1.0f + 10.0f * (g_x-g_y);" + 
				"	vec4 shadowCol 		= (1.0f - w.w) * vec4(1.0f , 1.0f, 1.0f, 1.0f) + w.w * vec4(shadow, shadow, shadow, 1.0f);" +
				"" +
				"	float maxL			= 0.025f;" + 
				"	float weight		= (maxL - length(vec2(g_x, g_y))) / maxL;" +
				"	vec4 wolkenRefl		= vec4((weight * (texture2D(tex5, myPos + vec2(offSetCloud * s.x, 0.0f)  + reflectVector.xy))).rgb, 1.0f);" +
				"	if (wolkenRefl.r<0.0f)	" +
				"		wolkenRefl.r = 0.0f;" +
				"	if (wolkenRefl.g<0.0f)	" +
				"		wolkenRefl.g = 0.0f;" +
				"	if (wolkenRefl.b<0.0f)	" +
				"		wolkenRefl.b = 0.0f;" +
				"" +
				"	vec4 mondRefl		= texture2D(tex3, 5.0f * refractVector.xy + vec2(0.5f, 0.5f));" +
				"" +
				"	if (withRefraction) {" +
				"		gl_FragColor 		= w.x * texture2D(tex2, myPos + s * scale * refractVector.xy)* shadowCol + " +
				"							  w.y * texture2D(tex4, myPos + refractVector.xy * 0.25f) + " + // + s * scale * refractVector.xy * 0.25f) + " +
				"							  w.w * mondRefl + " +
				"							  w.z * wolkenRefl;" +
				"	} else {" +
				"		gl_FragColor 		= w.x * texture2D(tex2, myPos) * shadowCol + " +
				"							  w.y * texture2D(tex4, myPos + refractVector.xy * 0.25f) + " + // + s * scale * refractVector.xy * 0.25f) + " +
				"							  w.w * mondRefl + " +
				"							  w.z * wolkenRefl;" +
				"	}" +
				"}";	
			
			int shaderObjectF3 = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF3, fragShader_water);
			glCompileShader(shaderObjectF3);

			int program_water = glCreateProgram();
			glAttachShader(program_water, shaderObjectF3);
			glLinkProgram(program_water);			
			
			int sLoc_3 		= glGetUniformLocation(program_water, "s");
			int scaleLoc_3 	= glGetUniformLocation(program_water, "scale");
			int tex1Loc_3 	= glGetUniformLocation(program_water, "tex1");
			int tex2Loc_3 	= glGetUniformLocation(program_water, "tex2");
			int tex3Loc_3 	= glGetUniformLocation(program_water, "tex3");
			int tex4Loc_3 	= glGetUniformLocation(program_water, "tex4");
			int tex5Loc_3 	= glGetUniformLocation(program_water, "tex5");
			int bool_3 		= glGetUniformLocation(program_water, "withRefraction");
			int vec_3 		= glGetUniformLocation(program_water, "w");
			int int_3 		= glGetUniformLocation(program_water, "offSetCloud");						
						
			glUseProgram(program_water);		
			
			glUniform2f(sLoc_3, 	1.0f/WB, 1.0f/HB);			
			glUniform1f(scaleLoc_3, -100.0f); // -200.0f
			glUniform1i(tex1Loc_3, 	0);
			glUniform1i(tex2Loc_3, 	1);
			glUniform1i(tex3Loc_3, 	2);
			glUniform1i(tex4Loc_3, 	3);
			glUniform1i(tex5Loc_3, 	4);
			glUniform1i(bool_3, 	0);
			glUniform4f(vec_3, 	1.0f, 0.7f, 1.0f, 1.0f);
			glUniform1f(int_3, 	0.0f);
			
			glUseProgram(0);
			// **********************************************************************************************			
						
			// **********************************************************************************************
			// Abschließend wollen wir noch den Shadercode überprüfen. Sollte der Shadercompiler
			// Fehler erkennen, geben wir diese in der Konsole aus
			errorBuffer.rewind();
			glGetProgram(program_water, GL_LINK_STATUS, errorBuffer);
			System.out.println(errorBuffer.get(0)==GL_TRUE?"OK":"ERROR");
			
			int error = errorBuffer.get(0);
			errorBuffer.put(0,1024);
			glGetProgramInfoLog(program_water, errorBuffer, infoBuffer);		
			if (error!=GL_TRUE) 
			{
				byte bytes[] = new byte[1024];
				infoBuffer.get(bytes).rewind();
				System.err.println(new String(bytes, 0, errorBuffer.get(0)));
			}						
			// **********************************************************************************************
			
			// **********************************************************************************************	
			long startTime = 0;
			double diffTime;
			int frame = 0;
			while (!Display.isCloseRequested()) {								
				// Texture anzeigen:
				glUseProgram(program_water);
				if (refEnable) {
					glUniform1i(bool_3, 1);
					glUniform4f(vec_3, 	1.0f, slider1.getValue()/100.0f, slider2.getValue()/100.0f, slider3.getValue()/100.0f);
				} else {
					glUniform1i(bool_3, 0);
					glUniform4f(vec_3, 	1.0f, slider1.getValue()/100.0f, slider2.getValue()/100.0f, slider3.getValue()/100.0f);
				}
				glUseProgram(0); 
								
				// wir tauschen die Framebuffer (und Texturen!) gegen den Uhrzeigersinn 
				int oldFBO1 = waterTexture_1_FBO, oldTexture1 = waterTexture_1;
				int oldFBO2 = waterTexture_2_FBO, oldTexture2 = waterTexture_2;
				int oldFBO3 = waterTexture_3_FBO, oldTexture3 = waterTexture_3;
				
				waterTexture_1_FBO = oldFBO2; waterTexture_1 = oldTexture2;
				waterTexture_2_FBO = oldFBO3; waterTexture_2 = oldTexture3;
				waterTexture_3_FBO = oldFBO1; waterTexture_3 = oldTexture1;

				// **************************************
				// Wenn die Maustaste gedrückt wurde, wollen wir die Gauss-Verteilung, die wir
				// in der gaussTexture erzeugt haben, verwenden, um diese in die Wasserdynamik
				// einfließen zu lassen. Schwarze Werte werden dabei durch die additive Verknüpfung
				// vernachlässigt und lassen sich daher als transparent interpretieren.
				if (Mouse.isButtonDown(0)) {
					// SOUND ***
					if (souEnable && startTime==0) {
						myMusicPlayer.playSoundSmooth(); 
						startTime = System.nanoTime();
					} 
					// SOUND ***
					
					glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_2_FBO);
					glUseProgram(0);
					
					glViewport(0, 0, WB, HB);				
					glLoadIdentity();
					glTranslated(Mouse.getX()*2f/c.getWidth()-1, Mouse.getY()*2f/c.getHeight()-1, 0);
					glScaled(0.1, 0.1, 0.1);	// wenn Bild kleiner, dann 0.2				
					
					// Durch das Blenden manipulieren wir die Wasserdynamik mit der Gauss-Verteilung
					glEnable(GL_BLEND);
					glBlendFunc(GL_SRC_COLOR, GL_ONE_MINUS_SRC_COLOR);
					
					glEnable(GL_TEXTURE_2D);
					glBindTexture(GL_TEXTURE_2D, gaussTexture);
					glColor3f(1,1,1);
					glBegin(GL_QUADS);
						glTexCoord2f(0,0);
						glVertex3f(-1.0f, -1.0f, 0.0f);
					  
						glTexCoord2f(1,0);
						glVertex3f(1.0f, -1.0f, 0.0f);
	
						glTexCoord2f(1,1);
						glVertex3f(1.0f, 1.0f, 0.0f);
					  
						glTexCoord2f(0,1);
						glVertex3f(-1.0f, 1.0f, 0.0f);
					glEnd();
					glDisable(GL_TEXTURE_2D);
					glDisable(GL_BLEND);
				} else {
					// SOUND ***										
					// Splasheffekt:
					if (souEnable && startTime != 0) {
						diffTime = ((System.nanoTime()-startTime)/1e9);
						if (diffTime < 0.08f) 							
							myMusicPlayer.playSoundSplash();
					}
					startTime=0;
					// SOUND ***
				}
				// **************************************
				
				// **************************************
				// Der Shader WasserDynamik erzeugt aus den ersten beiden
				// Texturen (waterTexture_1 und waterTexture_2) die Dynamik
				// des Wassers und speichert diese in FrameBuffer 
				// (waterTexture_3_FBO). Damit steht es später in der Texture
				// waterTexture_3 zur Verfügung.
				glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_3_FBO);				
				glUseProgram(program_waterDynamic);
				glViewport(0, 0, WB, HB);

				glClearColor(0, 0, 0, 1);
				glClear(GL_COLOR_BUFFER_BIT);
				glLoadIdentity();

				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, waterTexture_1);
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, waterTexture_2);
				
				glBegin(GL_QUADS);
					glTexCoord2f(0,0);
					glVertex3f(-1.0f, -1.0f, 0.0f);
				  
					glTexCoord2f(1,0);
					glVertex3f( 1.0f, -1.0f, 0.0f);

					glTexCoord2f(1,1);
					glVertex3f( 1.0f,  1.0f, 0.0f);
				  
					glTexCoord2f(0,1);
					glVertex3f(-1.0f,  1.0f, 0.0f);
				glEnd();

				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
				// **************************************
					
				// **************************************
				// Kaustik wird berechnet und als Bild gespeichert
				glUseProgram(program_caustic);		
					
				glUniform2f(sLoc2, 		1.0f/WB, 1.0f/HB);			
				glUniform1f(scaleLoc2,  0.0f); // 0.0f
				glUniform1i(tex1Loc2, 	0);
				glUniform1i(tex2Loc2, 	1);
				glUniform1i(tex3Loc2, 	2);
					
				glUseProgram(0);							
					
				glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_4_FBO);				
				glUseProgram(program_caustic);					
				glViewport(0, 0, WB, HB);

				glEnable(GL_TEXTURE_2D);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, waterTexture_3);
				glActiveTexture(GL_TEXTURE1);
				if (backGround==1)
					glBindTexture(GL_TEXTURE_2D, texGround1);
				else if (backGround==2)
					glBindTexture(GL_TEXTURE_2D, texGround2);
				else if (backGround==3)
					glBindTexture(GL_TEXTURE_2D, texGround3);
				else if (backGround==4)
					glBindTexture(GL_TEXTURE_2D, texGround4);
				else if (backGround==5)
					glBindTexture(GL_TEXTURE_2D, texGround5);
				else if (backGround==6)
					glBindTexture(GL_TEXTURE_2D, texGround6);
				else if (backGround==7)
					glBindTexture(GL_TEXTURE_2D, texGround7);
				glActiveTexture(GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, texSun);
					
				glBegin(GL_QUADS);
					glTexCoord2f(0,0);
					glVertex3f(-1.0f, -1.0f, 0.0f);
				  
					glTexCoord2f(1,0);
					glVertex3f( 1.0f, -1.0f, 0.0f);

					glTexCoord2f(1,1);
					glVertex3f( 1.0f,  1.0f, 0.0f);
				  
					glTexCoord2f(0,1);
					glVertex3f(-1.0f,  1.0f, 0.0f);
				glEnd();
					
				glDisable(GL_TEXTURE_2D);
				
				// Gebundene Texturen in der umgekehrten Reihenfolge
				// frei geben.
				glActiveTexture(GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
			
				glUseProgram(0);
				// **************************************
					
				// **************************************			
				// Die Wasserdynamik wird jetzt von Frame zu
				// Frame berechnet und das im nicht-sichtbaren
				// Bereich. Um das Ganze jetzt anschaulich zu
				// machen verwenden wir den FrameBuffer, der den
				// Ausgabebereich definiert, in unserem Fall das
				// erzeugte Canvasobjekt im JFrame.					
				glUseProgram(program_water);					
				float offSetCloud = (frame*0.15f) % c.getWidth(); 
				glUniform1f(int_3, 	offSetCloud);
				glUniform1f(scaleLoc_3, -slider4.getValue()); // -100.0f
				glUseProgram(0);					
				
				glBindFramebuffer(GL_FRAMEBUFFER, 0);				
				glUseProgram(program_water);													
				glViewport(0, 0, c.getWidth(), c.getHeight());

				glEnable(GL_TEXTURE_2D);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, waterTexture_3);
				glActiveTexture(GL_TEXTURE1);
				if (backGround==1)
					glBindTexture(GL_TEXTURE_2D, texGround1);
				else if (backGround==2)
					glBindTexture(GL_TEXTURE_2D, texGround2);
				else if (backGround==3)
					glBindTexture(GL_TEXTURE_2D, texGround3);
				else if (backGround==4)
					glBindTexture(GL_TEXTURE_2D, texGround4);
				else if (backGround==5)
					glBindTexture(GL_TEXTURE_2D, texGround5);
				else if (backGround==6)
					glBindTexture(GL_TEXTURE_2D, texGround6);
				else if (backGround==7)
					glBindTexture(GL_TEXTURE_2D, texGround7);
				glActiveTexture(GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, texMoon);
				glActiveTexture(GL_TEXTURE3);
				glBindTexture(GL_TEXTURE_2D, waterTexture_4);
				glActiveTexture(GL_TEXTURE4);
				glBindTexture(GL_TEXTURE_2D, texCloud);
					
				glBegin(GL_QUADS);
					glTexCoord2f(0,0);
					glVertex3f(-1.0f, -1.0f, 0.0f);
				  
					glTexCoord2f(1,0);
					glVertex3f( 1.0f, -1.0f, 0.0f);

					glTexCoord2f(1,1);
					glVertex3f( 1.0f,  1.0f, 0.0f);
				  
					glTexCoord2f(0,1);
					glVertex3f(-1.0f,  1.0f, 0.0f);
				glEnd();
					
				glDisable(GL_TEXTURE_2D);
				
				// Gebundene Texturen in der umgekehrten Reihenfolge
				// frei geben.
				glActiveTexture(GL_TEXTURE4);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE3);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE2);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
				
				glUseProgram(0);
				// **************************************
				
				// updaten des Views und kurz warten
				Display.update();
				frame++;
				Thread.sleep(5);		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Display.destroy();
			System.exit(0);
		}
	}
}

class KnopfdruckShader7 implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ref")) {
			_12_InteraktiveWassersimulation.refEnable = !_12_InteraktiveWassersimulation.refEnable;
			if (_12_InteraktiveWassersimulation.refEnable)
				_12_InteraktiveWassersimulation.butRefraktion.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("0.jpg")));
			else 
				_12_InteraktiveWassersimulation.butRefraktion.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("02.jpg")));
		}
		else if (cmd.equals("sou")) {
			_12_InteraktiveWassersimulation.souEnable = !_12_InteraktiveWassersimulation.souEnable;
			if (_12_InteraktiveWassersimulation.souEnable) 
				_12_InteraktiveWassersimulation.butSound.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("S.jpg")));
			else 
				_12_InteraktiveWassersimulation.butSound.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("S2.jpg")));
		}
		else if (cmd.equals("1")) 
			_12_InteraktiveWassersimulation.backGround = 1;
		else if (cmd.equals("2")) 
			_12_InteraktiveWassersimulation.backGround = 2;
		else if (cmd.equals("3")) 
			_12_InteraktiveWassersimulation.backGround = 3;
		else if (cmd.equals("4")) 
			_12_InteraktiveWassersimulation.backGround = 4;
		else if (cmd.equals("5")) 
			_12_InteraktiveWassersimulation.backGround = 5;
		else if (cmd.equals("6")) 
			_12_InteraktiveWassersimulation.backGround = 6;
		else if (cmd.equals("7")) 
			_12_InteraktiveWassersimulation.backGround = 7;
	}
}
