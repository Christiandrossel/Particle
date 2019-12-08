package shaderwater;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class _05_WasserdynamikJava {
	final static float D = 0.99f; // Dämpfung
	
	// Bestimmung des Farbwertes
	static int colorForAmplitude(float a) {
		int red  = (int)((a<0?0:a)*128);
		int blue = (int)((-a<0?0:-a)*128);
		red  = red>255?255:red;
		blue = blue>255?255:blue;
		return 0xFF000000 | (blue<<16) | (blue<<8) | (red<<8);
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		final int W = 640;
		final int H = 480;
		
		float surfaceA[] = new float[W*H];
		float surfaceB[] = new float[W*H];
		float surfaceC[] = new float[W*H];
		
		int pixels[] = new int[W*H];
		final MemoryImageSource mis = new MemoryImageSource(W,H,pixels,0,W); mis.setAnimated(true);
				
		// Frame und Ausgabe vorbereiten
		JFrame f = new JFrame();
		final JComponent viewer = new JComponent() {
			private static final long serialVersionUID = 1L;
			Image im = createImage(mis);
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(im, 0,0,getWidth(),getHeight(),0,0,W,H,this);
			}
		};
		f.getContentPane().add(viewer);
		f.setBounds(100, 100, W*2, H*2);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Point mouseCoords = new Point();
		viewer.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				mouseCoords.setLocation(e.getX()*W/viewer.getWidth(),e.getY()*H/viewer.getHeight());
			}
		});
		
		float D = 0.99f; 
		while (f.isVisible()) {
			float[] newC = surfaceA;
			float[] newB = surfaceC;
			float[] newA = surfaceB;
			
			surfaceA = newA;
			surfaceB = newB;
			surfaceC = newC;

			// Wasserdynamik
			for (int y=1,o=W+1;y<H-1;y++,o+=2)
				for (int x=1;x<W-1;x++,o++) 
					surfaceC[o] = Math.min(1.0f, Math.max(-1.0f, D * ((surfaceB[o-1]+surfaceB[o+1]+surfaceB[o-W]+surfaceB[o+W])*0.5f - surfaceA[o])));

			// Quader:
			int Q = W/20;
			int posX = Math.min(Math.max(mouseCoords.x-Q/2,1),W-Q);
			int posY = Math.min(Math.max(mouseCoords.y-Q/2,1),H-Q);
			
			for (int y=0,o=posX+posY*W,r=W-Q;y<Q;y++,o+=r)
				for (int x=0;x<Q;x++,o++)
					surfaceC[o] = 1;
		
			for (int i=0,l=pixels.length;i<l;i++)
				pixels[i] = colorForAmplitude(surfaceC[i]);
			
			mis.newPixels();
			Thread.sleep(10);
		}			
	}
}