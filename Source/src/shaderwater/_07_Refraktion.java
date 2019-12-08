package shaderwater;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.Canvas;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class _07_Refraktion {
	// für die Fehlerermittlung der Shader
	private static ByteBuffer infoBuffer = BufferUtils.createByteBuffer(1024);
	private static IntBuffer errorBuffer = BufferUtils.createIntBuffer(1);
	
	public static void main(String[] args) throws LWJGLException {
		try {						
			JFrame f = new JFrame();
			Canvas c = new Canvas();
			f.getContentPane().add(c);

			// Fenstergröße beim Start
			final int WIDTH 	= 640;
			final int HEIGHT 	= 480;
			
			int xOff = 100, yOff = 100;
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
			int WG = 32;
			int HG = 32;
			
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
			final int WU 	= 640;
			final int HU 	= 480;			
			
			int myTexture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, myTexture);			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WU, HU, 0, GL_BGRA, GL_UNSIGNED_BYTE,
				(IntBuffer)ByteBuffer.allocateDirect(WU*HU*4).order(ByteOrder.nativeOrder()).asIntBuffer().put(
				ImageIO.read(file).getRGB(0,0, WU, HU, new int[WU*HU], 0, WU)).rewind());
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
					"		) * 0.5 - texture2D(tex2, gl_TexCoord[0].st)) * 0.998; " +
					"}";	
				
			// Shader erzeugen, mit Sourcecode füllen und kompilieren
			int shaderObjectF = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF, fragShader_waterDynamic);
			glCompileShader(shaderObjectF);

			// Programm erzeugen, mit Shader verknüpfen und das Programm linken
			int program_WasserDynamik = glCreateProgram();
			glAttachShader(program_WasserDynamik, shaderObjectF);
			glLinkProgram(program_WasserDynamik);	

			// Parameter, die für den Shader relevant sind, als Uniforms 
			// mit dem Programm verknüpfen
			int sLoc 	= glGetUniformLocation(program_WasserDynamik, "s");
			int tex1Loc = glGetUniformLocation(program_WasserDynamik, "tex1");
			int tex2Loc = glGetUniformLocation(program_WasserDynamik, "tex2");
			
			// die Uniforms erzeugen und binden
			glUseProgram(program_WasserDynamik);
			
			float WSTEP = 0.68235216232f;			
			glUniform2f(sLoc, WSTEP/WB, WSTEP/HB);
			glUniform1i(tex1Loc, 1);
			glUniform1i(tex2Loc, 0);
			
			glUseProgram(0);
			// **********************************************************************************************
									
			// **********************************************************************************************
			// Shader für die Verknüpfung von Wasserdynamik und einer Texture
			String fragShader_waterDynamic_and_texture = 
				"uniform sampler2D tex1;"+
				"uniform sampler2D texT;" +
				"" +
				"uniform vec2 s;"+
				"uniform float scale;" +
				""+
				"void main (void) {"+
				"	float g_x = (texture2D(tex1, gl_TexCoord[0].st + vec2(-s.x, 0)) - " +
				"				 texture2D(tex1, gl_TexCoord[0].st + vec2(+s.x, 0))).x;" +
				"	float g_y = (texture2D(tex1, gl_TexCoord[0].st + vec2(0, -s.y)) - " +
				"				 texture2D(tex1, gl_TexCoord[0].st + vec2(0, +s.y))).y;" +
				"	" +
				"	vec3 n_surface 	= normalize(cross(vec3(1.0f, 0.0f, g_x), vec3(0.0f, 1.0f, g_y)));" +
				"	vec3 n_view 	= vec3(0.0f, 0.0f, 1.0f);" +
				"	vec3 refractVec	= refract(n_view, n_surface, 1.33f);" +
				"" +
				"	gl_FragColor 	= texture2D(texT, gl_TexCoord[0].st + refractVec.xy * s * scale);" +
				"}";
			
			int shaderObjectF2 = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF2, fragShader_waterDynamic_and_texture);
			glCompileShader(shaderObjectF2);

			int program_WasserDynamik_auf_Texture = glCreateProgram();
			glAttachShader(program_WasserDynamik_auf_Texture, shaderObjectF2);
			glLinkProgram(program_WasserDynamik_auf_Texture);			
			
			int sLoc2 		= glGetUniformLocation(program_WasserDynamik_auf_Texture, "s");
			int scaleLoc2 	= glGetUniformLocation(program_WasserDynamik_auf_Texture, "scale");
			int tex1Loc2 	= glGetUniformLocation(program_WasserDynamik_auf_Texture, "tex1");
			int tex2Loc2 	= glGetUniformLocation(program_WasserDynamik_auf_Texture, "texT");
						
			glUseProgram(program_WasserDynamik_auf_Texture);		
			
			glUniform2f(sLoc2, 1.0f/WB, 1.0f/HB);			
			glUniform1f(scaleLoc2, -100.0f);
			glUniform1i(tex1Loc2, 0);
			glUniform1i(tex2Loc2, 1);
			
			glUseProgram(0);
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Abschließend wollen wir noch den Shadercode überprüfen. Sollte der Shadercompiler
			// Fehler erkennen, geben wir diese in der Konsole aus
			errorBuffer.rewind();
			glGetProgram(program_WasserDynamik_auf_Texture, GL_LINK_STATUS, errorBuffer);
			System.out.println(errorBuffer.get(0)==GL_TRUE?"OK":"ERROR");
			
			int error = errorBuffer.get(0);
			errorBuffer.put(0,1024);
			glGetProgramInfoLog(program_WasserDynamik_auf_Texture, errorBuffer, infoBuffer);		
			if (error!=GL_TRUE) 
			{
				byte bytes[] = new byte[1024];
				infoBuffer.get(bytes).rewind();
				System.err.println(new String(bytes, 0, errorBuffer.get(0)));
			}						
			// **********************************************************************************************
			
			// **********************************************************************************************	
			while (!Display.isCloseRequested()) {
				Thread.sleep(10);

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
				}
				// **************************************
				
				// **************************************
				// Der Shader WasserDynamik erzeugt aus den ersten beiden
				// Texturen (waterTexture_1 und waterTexture_2) die Dynamik
				// des Wassers und speichert diese in FrameBuffer 
				// (waterTexture_3_FBO). Damit steht es später in der Texture
				// waterTexture_3 zur Verfügung.
				glBindFramebuffer(GL_FRAMEBUFFER, waterTexture_3_FBO);				
				glUseProgram(program_WasserDynamik);
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
					glVertex3f(1.0f, -1.0f, 0.0f);

					glTexCoord2f(1,1);
					glVertex3f(1.0f, 1.0f, 0.0f);
				  
					glTexCoord2f(0,1);
					glVertex3f(-1.0f, 1.0f, 0.0f);
				glEnd();

				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
				// **************************************
		
				// **************************************			
				// Die Wasserdynamik wird jetzt von Frame zu
				// Frame berechnet und das im nicht-sichtbaren
				// Bereich. Um das Ganze jetzt anschaulich zu
				// machen verwenden wir den FrameBuffer, der den
				// Ausgabebereich definiert, in unserem Fall das
				// erzeugte Canvasobjekt im JFrame.
				glBindFramebuffer(GL_FRAMEBUFFER, 0);				
				glUseProgram(program_WasserDynamik_auf_Texture);					
				glViewport(0, 0, c.getWidth(), c.getHeight());

				glEnable(GL_TEXTURE_2D);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, waterTexture_3);
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, myTexture);
				
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
				
				// Gebundene Texturen in der umgekehrten Reihenfolge
				// frei geben.
				glActiveTexture(GL_TEXTURE1);
				glBindTexture(GL_TEXTURE_2D, 0);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, 0);
				
				glUseProgram(0);		
				// **************************************			
				
				// updaten des Views
				Display.update();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			Display.destroy();
			System.exit(0);
		}
	}
}