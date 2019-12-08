package shaderbasics;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSlider;

import org.lwjgl.opengl.Display;
//import zebra.Torus;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class TorusRotiertFarbeAmbientUniform {
   public static String fragShaderSource = "" +
       "varying vec4 color;" +
       "" +
       "void main() { " +
       "   gl_FragColor = color; " +
       "}";

 public static String vertexShaderSource = "" +
      "varying vec4 color;" +
      "" +
      "uniform float ambient;" +
      "" +
      "void main() {" +
      "   color       = vec4(1,1,1,1) * ambient;" +
      "   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;"+
      "}";

    public static void main(String[] args) throws Exception {
      JFrame f = new JFrame();
      Canvas c = new Canvas();

      int MAXI = 100;
      JSlider sliderAmbient = new JSlider();
      sliderAmbient.setBorder(BorderFactory.createTitledBorder("ANBIENT LIGHT [in %]"));
      sliderAmbient.setMaximum(MAXI);
      sliderAmbient.setValue(50);
      sliderAmbient.setMajorTickSpacing(MAXI / 5);
      sliderAmbient.setMinorTickSpacing(MAXI / 20);
      sliderAmbient.setPaintTicks(true);
      sliderAmbient.setPaintLabels(true);

      f.getContentPane().add(c);
      f.getContentPane().add(sliderAmbient,   BorderLayout.NORTH);

      final int WIDTH  = 800, HEIGHT = 600;
      f.setBounds(0, 0, WIDTH, HEIGHT);
      f.setVisible(true);
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Display.setParent(c);
      Display.create();
      Display.makeCurrent();      
       
      // Shader-Code
      int myProgram = glCreateProgram();

      int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
      glShaderSource(fragShader, fragShaderSource);
      glCompileShader(fragShader);
      System.out.println(glGetShaderInfoLog(fragShader, 1024));
      glAttachShader(myProgram, fragShader);

      int vertShader = glCreateShader(GL_VERTEX_SHADER);
      glShaderSource(vertShader, vertexShaderSource);
      glCompileShader(vertShader);
      System.out.println(glGetShaderInfoLog(vertShader, 1024));
      glAttachShader(myProgram, vertShader);

      glLinkProgram(myProgram);
      int ambientPercent = glGetUniformLocation(myProgram, "ambient");
      glUseProgram(myProgram);

      glEnable(GL_DEPTH_TEST);
      //Torus tt = new Torus(0, 0, 0, 1, 0.3);
      Torus tt = new Torus(0.6f, 1.8f, 128, 256);
      long start = System.nanoTime();
      
      // Renderloop
      while (!Display.isCloseRequested()) {
         double now = (System.nanoTime() - start) / 1e9;
         glUniform1f(ambientPercent,  sliderAmbient.getValue()/100.0f);
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

         glMatrixMode(GL_PROJECTION);
         glLoadIdentity();
         double r = f.getHeight() * 1. / f.getWidth();
         glFrustum(-1, 1, -r, r, 1, 20);
         glMatrixMode(GL_MODELVIEW);
         glLoadIdentity();
         glTranslated(0, 0, -4);
         glRotatef((float) now * 4.0f, 0, 0, 1);
         glRotatef(10 * (float) now * 4.0f, 1, 0, 0);

         tt.render();

         Display.update();
      }
      Display.destroy();
   }
}