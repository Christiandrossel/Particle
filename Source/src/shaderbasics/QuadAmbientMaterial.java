package shaderbasics;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class QuadAmbientMaterial {
    public static String fragShaderSource = "" +
            "varying vec3 color;" +
            "" +
            "void main() { " +
            "   gl_FragColor = vec4(color, 1); " +
            "}";

    public static String vertexShaderSource = "" +
            "varying vec3 color;" +
            "" +
            "vec3 ambientLight    = vec3(1, 1, 1);" +
            "vec3 ambientMaterial = vec3(.2, .2, .2);" +
            ""+
            "void main() {" +
            "   color       = ambientLight * ambientMaterial;" +
            "   gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;"+
            "}";

    public static void main(String[] args) throws Exception {
        DisplayMode dm = new DisplayMode(800, 600);
        Display.setDisplayMode(dm);
        Display.create();

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
        glUseProgram(myProgram);
        
        glEnable(GL_DEPTH_TEST);
        long start = System.nanoTime();
        while (!Display.isCloseRequested()) {
            double now = (System.nanoTime()-start)/1e9;
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            double r = dm.getHeight()*1./dm.getWidth();
            glFrustum(-1, 1, -r, r, 1, 20);
            glMatrixMode(GL_MODELVIEW);
            
            glLoadIdentity();
            
            glTranslated(0, 0, -4);
            glRotatef((float)now*5.0f,0,0,1);
            glRotatef(10*(float)now*5.0f,1,0,0);
            
            // Objekt zeichnen
            glBegin(GL_QUADS);
            glVertex3f(-1, -1, 0);
            glVertex3f( 1, -1, 0);
            glVertex3f( 1,  1, 0);
            glVertex3f(-1,  1, 0);
            glEnd();
            
            Display.update();
        }       
        Display.destroy();
    }
}

