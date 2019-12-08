package particle_version05_vektoren;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2d;
import math.Vektor2D;

public class Flummi extends BewegendesObjekt {
   private static int objCounter = 0; 
   private float radius;
   private float r, g, b;

   public Flummi() {
      this(new Vektor2D(0, 0), new Vektor2D(2, 0), 20.0f, 1, 1, 0);
   }
   
   public Flummi(Vektor2D position, Vektor2D velocity) {
      this(position, velocity, 20.0f, 1, 1, 0);
   }
   
   public Flummi(Vektor2D position, Vektor2D velocity, float radius, float r, float g, float b) {
      super(position, velocity);
      this.radius = radius;
      this.r=r;
      this.g=g;
      this.b=b;
      this.id = ++objCounter;
   }

   @Override
   public void render() {
      glColor3d(r, g, b);
      glBegin(GL_TRIANGLE_FAN);
     glVertex2f((float)position.x, (float)position.y);
     // glVertex2d(position.x, position.y);
      for (int angle=0; angle<360; angle+=5) {
         glVertex2f((float)position.x + (float)Math.sin(angle) * radius, (float)position.y + (float)Math.cos(angle) * radius);
    	  //glVertex2d(position.x + Math.sin(angle) * radius, position.y + Math.cos(angle) * radius);
      }
      glEnd(); 
   }
}

