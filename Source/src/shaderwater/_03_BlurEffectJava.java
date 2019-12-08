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

public class _03_BlurEffectJava {
	final static float D = 0.99f; // Dämpfung

	// Bestimmung des Farbwertes
	static int colorForAmplitudeGray(float a) {
		int gray = (int)((a<0 ? 0 : a)*128);
		gray     = gray>128 ? 128 : gray;
		gray	+= 127;
		return 0xFF000000 | (gray<<16) | (gray<<8) | gray;
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		final int W = 320;
		final int H = 240;
		
		float surfaceA[] = new float[W*H];
		float surfaceB[] = new float[W*H];
		
		int pixels[] = new int[W*H];
		final MemoryImageSource mis = new MemoryImageSource(W, H, pixels, 0, W); 
		mis.setAnimated(true);
				
		// Frame und Ausgabe vorbereiten
		JFrame f = new JFrame();
		final JComponent viewer = new JComponent() {
			private static final long serialVersionUID = 1L;
			Image im = createImage(mis);
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(im, 0, 0, getWidth(), getHeight(), 0, 0, W, H, this);
			}
		};
		f.getContentPane().add(viewer);
		f.setBounds(100, 100, W*2, H*2);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final Point mouseCoords = new Point();
		viewer.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				mouseCoords.setLocation(e.getX()*W/viewer.getWidth(), e.getY()*H/viewer.getHeight());
			}
		});
		
		while (f.isVisible()) {	
			float[] newB = surfaceA;
			float[] newA = surfaceB;
			surfaceA = newA;
			surfaceB = newB;
			
			// blur effect
			for (int y=1,o=W+1; y<H-1; y++,o+=2)
				for (int x=1; x<W-1; x++,o++) 
					surfaceB[o] = ((surfaceA[o]+surfaceA[o-1]+surfaceA[o+1]+surfaceA[o-W]+surfaceA[o+W])*0.2f) * D; 

			// Quaderobjekt
			int Q = W/20;
			int posX = Math.min(Math.max(mouseCoords.x-Q/2,1), W-Q);
			int posY = Math.min(Math.max(mouseCoords.y-Q/2,1), H-Q);

			for (int y=0,o=posX+posY*W, r=W-Q; y<Q;y++,o+=r)
				for (int x=0; x<Q; x++,o++)
					surfaceB[o] = 1;
			
			for (int i=0,l=pixels.length;i<l;i++)
				pixels[i] = colorForAmplitudeGray(surfaceB[i]);
			
			mis.newPixels();
			Thread.sleep(10);
		}			
	}
}

