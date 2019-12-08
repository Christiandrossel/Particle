package particle_version01_basisobjekt;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import frame.BasisFenster;

public class WeltEinesEinsamenKreises extends BasisFenster {
   private Kreis kreis;
   
   public WeltEinesEinsamenKreises() {
      super("Welt eines einsamen Kreises", 640, 480);
      kreis = new Kreis(320, 240);
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
         
         // einen Kreis anzeigen
         kreis.render();

         Display.update();
      }
   }
   
   public static void main(String[] args) {
      new WeltEinesEinsamenKreises().start();
   }
}

