import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App {
	private static final int FPS = 120; // animator's target frames per second

	public void start(){
		try {
			Frame frame = new Frame();
            frame.setSize(1000, 750);
            frame.setLocationRelativeTo(null);


			// setup OpenGL Version 2
	    	GLProfile profile = GLProfile.get(GLProfile.GL2);
	    	GLCapabilities capabilities = new GLCapabilities(profile);
	    	capabilities.setRedBits(8);
			capabilities.setBlueBits(8);
			capabilities.setGreenBits(8);
			capabilities.setAlphaBits(8);
			capabilities.setDepthBits(24);

	    	// The canvas is the widget that's drawn in the JFrame
	    	GLCanvas canvas = new GLCanvas(capabilities);
			final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

			Renderer ren = new Renderer(new Maze1());
//			Renderer ren = new Renderer(animator);
			canvas.addGLEventListener(ren);
			canvas.addMouseListener(ren);
			canvas.addMouseMotionListener(ren);
			canvas.addKeyListener(ren);
            canvas.setSize(1000, 750);
	    	
	    	
	    	frame.add(canvas);
			

	    	frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					new Thread(() -> {
					   if (animator.isStarted()) animator.stop();
					   System.exit(0);
						System.out.println(animator.getFPS());
					}).start();
				}
			});
	    	frame.setTitle("Maze 2019 - Michal Dolenský");
	    	frame.pack();
	    	frame.setVisible(true);
            animator.start(); // start the animation loop
            
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App().start());
	}
}