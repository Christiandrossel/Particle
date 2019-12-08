package particle_version01_basisobjekt;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Kreis extends BasisObjekt {
   private float radius;
   private float r, g, b;
   
   public Kreis() {
      this(0, 0, 20.0f, 1, 1, 0);
   }
   
   public Kreis(float xPos, float yPos) {
      this(xPos, yPos, 20.0f, 1, 1, 0);
   }
   
   public Kreis(float xPos, float yPos, float radius, float r, float g, float b) {
      super(xPos, yPos);
      this.radius = radius;
      this.r=r;
      this.g=g;
      this.b=b;
   }
   
   @Override
   public void render() {
      glColor3d(r, g, b);
      glBegin(GL_TRIANGLE_FAN);
      glVertex2f(xPos, yPos);
      for (int angle=0; angle<360; angle+=5) {
         glVertex2f(xPos + (float)Math.sin(angle) * radius, yPos + (float)Math.cos(angle) * radius);
      }
      glEnd(); 
   }
}
