import blocks.Hall;
import blocks.Wall;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import maze.AbstractMaze;
import maze.Maze1;
import transforms.Point3D;
import transforms.Vec3D;
import utils.LoadTexture;
import utils.OglUtils;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * trida pro zobrazeni sceny v OpenGL:
 * kamera, skybox
 *
 * @author PGRF FIM UHK
 * @version 2015
 */
public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    GLU glu;
    GLUT glut;

    int width, height, dx = 0, dy = 0;
    int ox, oy;

    float zenit = 0;
    float azimut = 0;
    double px, py, pz, ex = 1, ey = 0, ez = 0, ux = 0, uy = 1, uz = 0;
    float step, trans = 0;
    boolean per = true, free = false;
    double a_rad, z_rad;
    long oldmils = System.currentTimeMillis();

    File file;
    Texture texture;

    AbstractMaze maze;
    private String compass = "";

    final int COLLISION_SIZE = 5;

    GLUquadric quadratic;


    @Override
    public void init(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        gl.glShadeModel(gl.GL_SMOOTH);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);
        gl.glDisable(gl.GL_LIGHTING);

        List<String> textGrapList = new ArrayList<>();
        textGrapList.add("11.jpg");
        textGrapList.add("test_texture.jpg");




        textGrapList.forEach(s -> {
            System.out.println("Loading texture...");
            InputStream is = getClass().getResourceAsStream(s); // vzhledem k adresari res v projektu
            if (is == null)
                System.out.println("File not found");
            else
                try {
                    texture = TextureIO.newTexture(is, true, "jpg");
                    texture.bind(gl);
                    texture.enable(gl);
                } catch (IOException e) {
                    System.err.println("Chyba cteni souboru s texturou");
                }
        });




//        textGrapList.forEach(s -> {
//            System.out.println("Loading texture...");
//            InputStream is = getClass().getResourceAsStream(s); // vzhledem k adresari res v projektu
//            if (is == null)
//                System.out.println("File not found");
//            else
//                try {
//                    texture = TextureIO.newTexture(is, true, "jpg");
//                    texture.enable(gl);
//                    texture.bind(gl);
//                } catch (IOException e) {
//                    System.err.println("Chyba cteni souboru s texturou");
//                }
//        });

        //        OglUtils.printOGLparameters(gl);
//



        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
//        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);

        maze = new Maze1();
        px = maze.getStartPosition().getX();
        py = maze.getStartPosition().getY();
        pz = maze.getStartPosition().getZ();
    }

    @Override
    public void display(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();

        long mils = System.currentTimeMillis();
        step = (mils - oldmils) / 1000.0f;
        //float fps = 1000 / (float) (mils - oldmils);
        oldmils = mils;
        trans = 100 * step;

        //System.out.println(fps);

        // vymazani obrazovky a Z-bufferu
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (per) glu.gluPerspective(45, width / (float) height, 0.1f, 5000.0f);
        else gl.glOrtho(-20 * width / (float) height, 20 * width / (float) height, -20, 20, 0.1f, 5000.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);


        gl.glPopMatrix();
        gl.glLoadIdentity();

        glu.gluLookAt(px, py, pz, ex + px, ey + py, ez + pz, ux, uy, uz);


        gl.glPushMatrix();
        //FIXME: temp
        gl.glBegin(GL2.GL_LINES);
        gl.glLineWidth(5);
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
        gl.glPopMatrix();


        gl.glPushMatrix();
        Point3D sP = maze.getStartPosition();
        gl.glColor3f(1, 1, 1);
        gl.glTranslated(sP.getX(), sP.getY(), sP.getZ());
        glut.glutWireCube(5);
        gl.glPopMatrix();

        //player pos
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 0);
        gl.glTranslated(px, py - 18, pz);
        glut.glutSolidCube(1);
        gl.glPopMatrix();

        drawMaze(gl);

        gl.glColor3f(1f, 1f, 1f);
        String text = this.getClass().getName() + ": [WSAD][lmb] camera";
        if (per) text = text + ", [P]ersp ";
        else text = text + ", [p]ersp ";
        if (free) text = text + ", [F]ree ";
        else text = text + ", [f]ree ";


        OglUtils.drawStr2D(glDrawable, 3, height - 20, text);
//        OglUtils.drawStr2D(glDrawable, width - 90, 3, " (c) PGRF UHK");

        OglUtils.drawStr2D(glDrawable, width - 590, 3, String.format("%f|%f|%f||%f|%f|%f||%f|%f|%f", px, py, pz, ex + py, ey + py, ez + pz, ux, uy, uz));


//        OglUtils.drawStr2D(glDrawable, frame.getWidth() / 2, height - 20, compass);


    }

    public void drawMaze(GL2 gl) {
        gl.glPushMatrix();
        int y = 0;
        for (int[][] level : maze.getLevels()) {
            int size = maze.getSquareSize();
            for (int x = 0; x < level.length; x++) {
                for (int z = 0; z < level.length; z++) {
                    switch (level[x][z]) {
                        //Hall
                        case 0:
                            new Hall().init(gl, maze, new Vec3D(x, y, z), new Vec3D(0, 0, 1)).draw();
                            break;
                        //Wall
                        case 1:
                            new Wall().init(gl, maze,level, new Vec3D(x, y, z)).draw();
                            break;
                    }

                }
            }
            y += maze.getWallHeight();
        }
        gl.glPopMatrix();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, this.width, this.height);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) ;
        ox = e.getX();
        oy = e.getY();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        dx = e.getX() - ox;
        dy = e.getY() - oy;
        ox = e.getX();
        oy = e.getY();

        zenit -= dy;
        if (zenit > 90) zenit = 90;
        if (zenit <= -90) zenit = -90;
        azimut -= dx;
        azimut = azimut % 360;
        a_rad = -1 * azimut * Math.PI / 180;
        z_rad = zenit * Math.PI / 180;
        ex = Math.sin(a_rad) * Math.cos(z_rad);
        ey = Math.sin(z_rad);
        ez = -Math.cos(a_rad) * Math.cos(z_rad);
        ux = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
        uy = Math.sin(z_rad + Math.PI / 2);
        uz = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);

//        System.out.println("azimut = " + azimut);
//        if (azimut >= -fovy / 2.0 && azimut <= fovy / 2.0) {
//            compass = "N";
//        }
//        if (azimut >= 90 - fovy / 2.0 && azimut <= 90 + fovy / 2.0) {
//            compass = "E";
//        }
//        if (azimut >= 180 - fovy / 2.0 && azimut <= 180 + fovy / 2.0) {
//            compass = "S";
//        }
//        if (azimut >= 270 - fovy / 2.0 && azimut <= 270 + fovy / 2.0) {
//            compass = "W";
//        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (free) {
                px += ex * trans;
                py += ey * trans;
                pz += ez * trans;
            } else {
                double kpx = px + ex * trans;
                double kpz = pz + ez * trans;
                detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (free) {
                px -= ex * trans;
                py -= ey * trans;
                pz -= ez * trans;
            } else {
                double kpx = px - ex * trans;
                double kpz = pz - ez * trans;
                detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (free) {
                px += Math.sin(a_rad - Math.PI / 2) * trans;
                pz -= Math.cos(a_rad - Math.PI / 2) * trans;
            } else {
                double kpx = px + Math.sin(a_rad - Math.PI / 2) * trans;
                double kpz = pz - Math.cos(a_rad - Math.PI / 2) * trans;
                detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (free) {
                px -= Math.sin(a_rad - Math.PI / 2) * trans;
                pz += Math.cos(a_rad - Math.PI / 2) * trans;
            } else {
                double kpx = px - Math.sin(a_rad - Math.PI / 2) * trans;
                double kpz = pz + Math.cos(a_rad - Math.PI / 2) * trans;
                detectColision(kpx, kpz);
            }
        }

    }

    public void detectColision(double x, double z) {
        double posX = x / maze.getSquareSize();
        double posZ = z / maze.getSquareSize();
        double curPosX = px / maze.getSquareSize();
        double curPosZ = pz / maze.getSquareSize();

        if (posZ - curPosZ > 0) {
            posZ = (z + COLLISION_SIZE) / maze.getSquareSize();
        } else posZ = (z - COLLISION_SIZE) / maze.getSquareSize();
        if (maze.getLevels().get(0)[(int) posX][(int) posZ] != 1) pz = z;

        posZ = z / maze.getSquareSize();
        if (posX - curPosX > 0) {
            posX = (x + COLLISION_SIZE) / maze.getSquareSize();
        } else posX = (x - COLLISION_SIZE) / maze.getSquareSize();
        if (maze.getLevels().get(0)[(int) posX][(int) posZ] != 1) px = x;

//        System.out.println("-----");
//        System.out.println("posX - curPosX = " + (posX - curPosX));
//        System.out.println("posZ - curPosZ = " + (posZ - curPosZ));
//        System.out.println("posX = " + posX);
//        System.out.println("posZ = " + posZ);
//        System.out.println("curPosX = " + curPosX);
//        System.out.println("curPosZ = " + curPosZ);
//        System.out.println("maze.getLevels().get(0)[posX][posZ] = " + maze.getLevels().get(0)[(int) posX][(int) posZ]);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                free = !free;
                break;
            case KeyEvent.VK_P:
                per = !per;
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
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
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
        }
    }
}