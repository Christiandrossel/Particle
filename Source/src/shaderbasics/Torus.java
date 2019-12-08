package shaderbasics;

import static org.lwjgl.opengl.GL11.GL_QUAD_STRIP;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Torus {
   int I, J;
   float theta, phi, theta1;
   float cosTheta, sinTheta, cosTheta1, sinTheta1;
   float ringDelta, sideDelta;
   float cosPhi, sinPhi, dist;
   
   int sides, rings;
   float radius, tubeRadius;
   
   public Torus(float TubeRadius, float Radius, int Sides, int Rings) {
      this.rings = Rings;
      this.sides = Sides;
      this.radius = Radius;
      this.tubeRadius = TubeRadius;
      intialize();
   }
   
   public void setSteps(int step) {
      this.rings = step;
      intialize();
   }

   private void intialize() {
      sideDelta = (float) (2.0 * Math.PI / sides);
      ringDelta = (float) (2.0 * Math.PI / rings);
      theta     = 0.0f;
      cosTheta  = 1.0f;
      sinTheta  = 0.0f;
   }
   
   public void render() {
      for (int i=rings-1; i>=0; i--) {
         theta1     = theta + ringDelta;
         cosTheta1  = (float) Math.cos(theta1);
         sinTheta1  = (float) Math.sin(theta1);
         
         glBegin(GL_QUAD_STRIP);
            phi = 0.0f;
            for (int j=sides; j>=0; j--) {
               phi = phi + sideDelta;
               cosPhi = (float)(Math.cos(phi));
               sinPhi = (float)(Math.sin(phi));
               dist = radius + (tubeRadius * cosPhi);
  
               glNormal3f(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
               glVertex3f(cosTheta1 * dist, -sinTheta1 * dist, tubeRadius * sinPhi);
  
               glNormal3f(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
               glVertex3f(cosTheta * dist, -sinTheta * dist, tubeRadius * sinPhi);
            }
        glEnd();

        theta = theta1;
        cosTheta = cosTheta1;
        sinTheta = sinTheta1;
      }
   }
}
