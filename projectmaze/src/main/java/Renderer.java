import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import maze.AbstractMaze;
import maze.Maze1;
import transforms.Point3D;
import utils.OglUtils;

import java.awt.event.*;
import java.nio.ByteBuffer;

//import com.jogamp.opengl.util.texture.Texture;
//import com.jogamp.opengl.util.texture.TextureIO;

/**
 * trida pro zobrazeni sceny v OpenGL:
 * modelovani pomoci Push a Pop, orezani clipovou rovinou, zobrazovaci seznam
 *
 * @author PGRF FIM UHK
 * @version 2015
 */
public class Renderer implements GLEventListener, MouseListener,
        MouseMotionListener, KeyListener {

    GLU glu;
    GLUT glut;

    int width, height, dx = 0, dy = 0;
    int ox, oy;

    double azimut = 180, zenit = 0;

    boolean per = true, clip = true, anim = true;

    AbstractMaze maze;

    @Override
    public void init(GLAutoDrawable glDrawable) {

        GL2 gl = glDrawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glFrontFace(GL2.GL_CCW);
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);

        OglUtils.printOGLparameters(gl);

        maze = new Maze1();
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();

        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);


        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        if (per) glu.gluPerspective(90, width / (float) height, 0.1f, 500.0f);
        else gl.glOrtho(-40 * width / (float) height, 40 * width / (float) height, -40, 40, 0.1f, 300.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        zenit += dy;
        dy = 0;
        if (zenit > 90) zenit = 90;
        if (zenit < -90) zenit = -90;
        azimut += dx;
        dx = 0;
        azimut = azimut % 360;
        gl.glTranslatef(0f, 0f, -300f);
        gl.glRotated(zenit - 90, 1, 0, 0);
        gl.glRotated(azimut, 0, 0, 1);


        //FIXME: temp
        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(3);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(50f, 0f, 0f);

        gl.glColor3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(0f, 50f, 0f);

        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(0f, 0f, 0f);
        gl.glVertex3f(0f, 0f, 50f);
        gl.glEnd();

        Point3D sP = maze.getStartPosition();

        gl.glColor3f(1, 1, 1);

        glut.glutSolidCube(5);
        gl.glTranslated(sP.getX()*maze.getSquareSize(), sP.getY()*maze.getSquareSize(), sP.getZ()*maze.getSquareSize());





        int z = 0;
        for (int[][] level : maze.getLevels()) {
            int size = maze.getSquareSize();
            for (int x = 0; x < level.length; x++) {
                for (int y = 0; y < level.length; y++) {
                    gl.glBegin(GL2.GL_QUADS);
                    switch (level[x][y]) {
                        //Hall
                        case 0:
                            gl.glColor3f(0.0f, 0.0f, 1.0f);
                            gl.glVertex3i(x * size, y * size, z);
                            gl.glVertex3i(size * x + size, y * size, z);
                            gl.glVertex3i(x * size + size, y * size + size, z);
                            gl.glVertex3i(x * size, size * y + size, z);
                            break;
                        //Wall
                        case 1:

                            //cyan
                            if (x + 1 < level.length && level[x + 1][y] != 1) {
                                gl.glColor3f(0.0f, 1.0f, 1.0f);
                                gl.glVertex3i(size * x + size, y * size, z);
                                gl.glVertex3i(x * size + size, y * size + size, z);
                                gl.glVertex3i(x * size + size, y * size + size, z + size);
                                gl.glVertex3i(size * x + size, y * size, z + size);
                            }
                            //magenta
                            if (x - 1 > -1 && level[x - 1][y] != 1) {
                                gl.glColor3f(1.0f, 0.0f, 1.0f);
                                gl.glVertex3i(x * size, y * size, z + size);
                                gl.glVertex3i(x * size, size * y + size, z + size);
                                gl.glVertex3i(x * size, size * y + size, z);
                                gl.glVertex3i(x * size, y * size, z);
                            }


                            //green
                            if (y - 1 > -1 && level[x][y - 1] != 1) {
                                gl.glColor3f(0.0f, 1.0f, 0.0f);

                                gl.glVertex3i(x * size, y * size, z);
                                gl.glVertex3i(size * x + size, y * size, z);
                                gl.glVertex3i(size * x + size, y * size, z + size);
                                gl.glVertex3i(x * size, y * size, z + size);
                            }
                            //yellow
                            if (y + 1 < level.length && level[x][y + 1] != 1) {
                                gl.glColor3f(1.0f, 1.0f, 0.0f);
                                gl.glVertex3i(x * size, size * y + size, z + size);
                                gl.glVertex3i(x * size + size, y * size + size, z + size);
                                gl.glVertex3i(x * size + size, y * size + size, z);
                                gl.glVertex3i(x * size, size * y + size, z);
                            }

                            //top of wall
                            gl.glColor3f(1.0f, 0.0f, 0.0f);
                            gl.glVertex3i(x * size, y * size, z + size);
                            gl.glVertex3i(size * x + size, y * size, z + size);
                            gl.glVertex3i(x * size + size, y * size + size, z + size);
                            gl.glVertex3i(x * size, size * y + size, z + size);
                            break;
                    }

                    gl.glEnd();
                }

            }
            z += maze.getWallHeight();
        }





        gl.glColor3f(0.5f, 0.5f, 0.5f);
        glut.glutWireCube(50);


    }


    @Override
    public void reshape(GLAutoDrawable glDrawable, int x, int y, int width,
                        int height) {
        GL2 gl = glDrawable.getGL().getGL2();
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, this.width, this.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
        }
        ox = e.getX();
        oy = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dx = e.getX() - ox;
        dy = e.getY() - oy;
        ox = e.getX();
        oy = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // switch (e.getKeyCode()) {
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P:
                per = !per;
                break;
            case KeyEvent.VK_A:
                anim = !anim;
                break;
            case KeyEvent.VK_C:
                clip = !clip;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }
}