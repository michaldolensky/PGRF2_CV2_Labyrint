import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import maze.AbstractMaze;
import maze.CollisionTestMaze;
import maze.Maze1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App implements ActionListener {
	private static final int FPS = 120; // animator's target frames per second
	private static final AbstractMaze defaultMaze = new Maze1();
	private Frame frame;
	private GLCanvas canvas;
	private FPSAnimator animator;

	public void start(AbstractMaze maze){
		try {
			// setup OpenGL Version 2
	    	GLProfile profile = GLProfile.get(GLProfile.GL2);
	    	GLCapabilities capabilities = new GLCapabilities(profile);
	    	capabilities.setRedBits(8);
			capabilities.setBlueBits(8);
			capabilities.setGreenBits(8);
			capabilities.setAlphaBits(8);
			capabilities.setDepthBits(24);

			if(canvas!=null) frame.remove(canvas);

			// The canvas is the widget that's drawn in the JFrame
			canvas = new GLCanvas(capabilities);
			animator = new FPSAnimator(canvas, FPS, true);

			Renderer ren = new Renderer(maze);
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


	private void initWindow(AbstractMaze defaultMaze){
		frame = new Frame();
		frame.setSize(1000, 750);
		frame.setLocationRelativeTo(null);

		MenuBar menuBar = new MenuBar();
		Menu menu1 = new Menu("Maze selection");

		MenuItem mItem1 = new MenuItem("Maze 1");
		menu1.add(mItem1);
		mItem1.setActionCommand("Maze1");
		mItem1.addActionListener(this);

		MenuItem mItem2 = new MenuItem("Test maze");
		menu1.add(mItem2);
		mItem2.setActionCommand("testMaze");
		mItem2.addActionListener(this);

		menu1.add(mItem2);
		menuBar.add(menu1);
		frame.setMenuBar(menuBar);
		start(defaultMaze);
	}


	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App().initWindow(defaultMaze));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Maze1")) start(new Maze1());
		if(e.getActionCommand().equals("testMaze")) start(new CollisionTestMaze());

	}
}