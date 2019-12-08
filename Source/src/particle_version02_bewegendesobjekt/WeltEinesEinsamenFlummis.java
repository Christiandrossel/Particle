package particle_version02_bewegendesobjekt;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import frame.BasisFenster;

public class WeltEinesEinsamenFlummis extends BasisFenster {
   private Flummi flummi;
   
   public WeltEinesEinsamenFlummis() {
      super("Welt eines einsamen Flummis", 640, 480);
      flummi = new Flummi(320, 240, 2);
   }

   @Override
   public void renderLoop() {
      while (!Display.isCloseRequested()) {
         glClearColor(0.1f, 0.2f, 0.3f, 1);
         glClear(GL_COLOR_BUFFER_BIT);
         
         // ist ja 2d
         glMatrixMode (GL_PROJECTION);
         glLoadIdentity ();
         glOrtho (0, 640, 480, 0, 0, 1);
         glMatrixMode (GL_MODELVIEW);
         glDisable(GL_DEPTH_TEST);
         
         // einen Flummi anzeigen
         flummi.render();
         
         // Physik updaten
         flummi.update();

         Display.update();
      }
   }
   
   public static void main(String[] args) {
      new WeltEinesEinsamenFlummis().start();
   }
}

