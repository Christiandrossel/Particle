package shaderbasics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class FlächeSchwingt { 
   private static void createDisplay() throws LWJGLException {
      JFrame f = new JFrame();
      Canvas c = new Canvas();
      f.add(c);
      f.setBounds(100, 100, 600, 600);
      f.setVisible(true);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Display.setParent(c);
      Display.create();
   }

   private static void clearBackgroundWithColor(float r, float g, float b, float a) {
      glClearColor(r, g, b, a);
      glClear(GL_COLOR_BUFFER_BIT);
   }
   
   private static void renderFlaeche() {
      glBegin(GL_QUADS);
      glVertex3f(-1, -1, 0);
      glVertex3f(1, -1, 0);
      glVertex3f(1, 1, 0);
      glVertex3f(-1, 1, 0);
      glEnd();
   }

   public static void main(String[] args) throws LWJGLException {
      createDisplay();
      
      // nur Verbindungslinien rendern
      glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
      
      long start = System.nanoTime();
      while (!Display.isCloseRequested()) {
         double t = (System.nanoTime() - start)/1e9;
         clearBackgroundWithColor(0.1f, 0.2f, 0.3f, 1);

         // Wir konstruieren eine schwingende Projektionsfläche
         
         // M = I
         glLoadIdentity();
         
         // M*P
         glFrustum(-1, 1, -1, 1, 3, 10);
         
         // M*T
         glTranslated(0, 0, -4);

         // M*R
         glRotatef((float) Math.sin(t) * 120, 0, 1, 0);
         
         // M*S
         glScaled(.5, .5, .5);

         // Object-Space
         renderFlaeche();
         
         // Es wird also folgende Berechnungkette vollzogen:
         // M = I               -- zur Initialisierung 
         // M = P*T*R*S         -- Transformationskette vom Object-Space zur Ausgabe (von recht nach links)
         // v_neu = M * v_alt   -- Transformation aller Vektoren mit M
         
         Display.update();
         
      }
      Display.destroy();
      System.exit(0);
   }
}
