package particle_version02_bewegendesobjekt;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

public class Flummi extends BewegendesObjekt {
   private static int objCounter = 0; 
   private float radius;
   private float r, g, b;

   public Flummi() {
      this(0, 0, 2, 20.0f, 1, 1, 0);
   }
   
   public Flummi(float xPos, float yPos, float ySpeed) {
      this(xPos, yPos, ySpeed, 20.0f, 1, 1, 0);
   }
   
   public Flummi(float xPos, float yPos, float ySpeed, float radius, float r, float g, float b) {
      super(xPos, yPos, ySpeed);
      this.radius = radius;
      this.r=r;
      this.g=g;
      this.b=b;
      this.id = ++objCounter;
   }

   @Override
   public void update() {
      yPos=yPos+ySpeed;

      if (yPos>480 || yPos<0)
         ySpeed = ySpeed * -1;
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
