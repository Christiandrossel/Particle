package shaderwater;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.Canvas;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.swing.JFrame;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class _13_GeometrieSchwimmt {
	// f�r die Fehlerermittlung der Shader
	private static ByteBuffer infoBuffer = BufferUtils.createByteBuffer(1024);
	private static IntBuffer errorBuffer = BufferUtils.createIntBuffer(1);
	
	public static void main(String[] args) throws LWJGLException {
		try {						
			JFrame f = new JFrame();
			Canvas c = new Canvas();
			f.getContentPane().add(c);

			// Fenstergr��e beim Start
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
			// F�r die Dynamik des Wassers ben�tigen wir zun�chst 3 FrameBuffer,
			// die ihre Pl�tze im Laufe der Iterationen zyklisch tauschen. 
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
			// im Hintergrund f�llen und erst im letzten Schritt ausgew�hlte Inhalte anzeigen
			glBindFramebuffer(GL_FRAMEBUFFER, 0);			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Bl�cke austauschbar bleiben
			// **********************************************************************************************

			// **********************************************************************************************
			// Eine Texture mit einer 2D-Gauss-Verteilung wird erzeugt.Der Bereich l�uft von 
			// 0 (Transparent) bis 1 (volle F�llung) und wird sp�ter additiv hinzugenommen.
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
			
			glBindTexture(GL_TEXTURE_2D, 0);  		// steht nur hier, damit die Bl�cke austauschbar bleiben
			// **********************************************************************************************			
			
			// **********************************************************************************************
			// Shader f�r den Blur-Effekt	
			// Shader f�r die Wasserdynamik
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
			
			// Shader erzeugen, mit Sourcecode f�llen und kompilieren
			int shaderObjectF = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectF, fragShader_waterDynamic);
			glCompileShader(shaderObjectF);

			// Programm erzeugen, mit Shader verkn�pfen und das Programm linken
			int program_WasserDynamik = glCreateProgram();
			glAttachShader(program_WasserDynamik, shaderObjectF);
			glLinkProgram(program_WasserDynamik);	

			// Parameter, die f�r den Shader relevant sind, als Uniforms 
			// mit dem Programm verkn�pfen
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
			String vertShader_waterDynamic_and_mesh = 
				"uniform sampler2D tex;"+
				"uniform vec4 s;"+
				"uniform float scale;" +
				"" +
				"varying vec4 FrontColor;" +
				""+
				"void main (void)"+
				"{" +
				"	vec2 myPos 			= gl_MultiTexCoord0.st;" +
				"	float g_x 			= (texture2D(tex, myPos + vec2(-s.x, 0)) - texture2D(tex, myPos + vec2(+s.x, 0))).x;" +
				"	float g_y 			= (texture2D(tex, myPos + vec2(0, -s.y)) - texture2D(tex, myPos + vec2(0, +s.y))).y;" +
				"" +
				"	vec3 n_surface 		= normalize(cross(vec3(1.0f, 0.0f, g_x), vec3(0.0f, 1.0f, g_y))); " +
				"	vec3 n_view 		= vec3(0.0f, 0.0f, 1.0f);" +
				"	vec3 refractVector	= refract(n_view, n_surface, 1.33f);" +
				"" +
				"	FrontColor 			= vec4(1.0f, 0.0f, 0.0f, 1.0f);" +
				"" +	
				"	gl_Position 	= (gl_ModelViewProjectionMatrix * gl_Vertex) + vec4(refractVector.x, refractVector.y, 0.0f, 0.0f)*s*scale; "+  
				"}";
			
			int shaderObjectV = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(shaderObjectV, vertShader_waterDynamic_and_mesh);
			glCompileShader(shaderObjectV);
			
			/**/
			String fragShader_waterDynamic_and_mesh = 
				"varying vec4 FrontColor;" +
				"" +
				"void main(void) {"+		 		
				"   gl_FragColor = FrontColor;"+ 
				"}";
			
			int shaderObjectFM = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(shaderObjectFM, fragShader_waterDynamic_and_mesh);
			glCompileShader(shaderObjectFM);
			
			int program_WasserDynamik_auf_Mesh = glCreateProgram();
			glAttachShader(program_WasserDynamik_auf_Mesh, shaderObjectV);
			glAttachShader(program_WasserDynamik_auf_Mesh, shaderObjectFM);
			glLinkProgram(program_WasserDynamik_auf_Mesh);			
			
			int sLoc3 		= glGetUniformLocation(program_WasserDynamik_auf_Mesh, "s");
			int scaleLoc3 	= glGetUniformLocation(program_WasserDynamik_auf_Mesh, "scale");
			int tex1Loc3 	= glGetUniformLocation(program_WasserDynamik_auf_Mesh, "tex");
						
			glUseProgram(program_WasserDynamik_auf_Mesh);		
			
			glUniform4f(sLoc3, 		1.0f/WB, 1.0f/HB, 1.0f, 1.0f);			
			glUniform1f(scaleLoc3, -40.0f); // -200.0f
			glUniform1i(tex1Loc3, 	0);
			
			glUseProgram(0);			
			// **********************************************************************************************
			
			// **********************************************************************************************
			// Abschlie�end wollen wir noch den Shadercode �berpr�fen. Sollte der Shadercompiler
			// Fehler erkennen, geben wir diese in der Konsole aus
			errorBuffer.rewind();
			glGetProgram(program_WasserDynamik, GL_LINK_STATUS, errorBuffer);
			System.out.println(errorBuffer.get(0)==GL_TRUE?"OK":"ERROR");
			
			int error = errorBuffer.get(0);
			errorBuffer.put(0,1024);
			glGetProgramInfoLog(program_WasserDynamik, errorBuffer, infoBuffer);		
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
				// Wenn die Maustaste gedr�ckt wurde, wollen wir die Gauss-Verteilung, die wir
				// in der gaussTexture erzeugt haben, verwenden, um diese in die Wasserdynamik
				// einflie�en zu lassen. Schwarze Werte werden dabei durch die additive Verkn�pfung
				// vernachl�ssigt und lassen sich daher als transparent interpretieren.
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
				// (waterTexture_3_FBO). Damit steht es sp�ter in der Texture
				// waterTexture_3 zur Verf�gung.
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
				// Mesh wird erzeugt und mit Wasserdynamik
				// verbunden:
				glBindFramebuffer(GL_FRAMEBUFFER, 0);		
				glUseProgram(program_WasserDynamik_auf_Mesh);	
				glViewport(0, 0, c.getWidth(), c.getHeight());
			
				glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
				glClear(GL_COLOR_BUFFER_BIT);
				glLoadIdentity();
				
				glEnable(GL_TEXTURE_2D);
				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, waterTexture_3);
				
				float leftC 	= -1.0f;
				float rightC 	=  1.0f;
				float topC 		=  1.0f;
				float bottomC 	= -1.0f;			
					
				float stepL = leftC, sizeL = (rightC-leftC) / 100.0f;
				while (stepL <= rightC + sizeL) {					
					glBegin(GL_LINE_STRIP);						
						float startY = bottomC;
						while (startY <= topC + sizeL) {
							glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
							glMultiTexCoord2f(GL_TEXTURE0, (stepL-leftC)/(rightC-leftC), (startY-leftC)/(rightC-leftC));
							glVertex3f(stepL, startY, 0.0f);												
							startY += sizeL;
						}					
					glEnd();					
					stepL += sizeL;
				}
				
				float stepT = bottomC, sizeT = (topC-bottomC) / 100.0f;
				while (stepT <= topC + sizeT) {
					glBegin(GL_LINE_STRIP);		
						float startX = leftC;
						while (startX <= rightC + sizeT) {
							glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
							glMultiTexCoord2f(GL_TEXTURE0, (startX-bottomC)/(topC-bottomC), (stepT-leftC)/(topC-bottomC));
							glVertex3f(startX, stepT, 0.0f);
							startX += sizeT;
						}
					glEnd();
					stepT += sizeT;
				}				
				
				glDisable(GL_TEXTURE_2D);
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