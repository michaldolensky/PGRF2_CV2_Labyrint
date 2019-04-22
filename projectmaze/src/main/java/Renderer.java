import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import maze.AbstractMaze;
import maze.Maze1;
import transforms.Point3D;
import utils.OglUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * trida pro zobrazeni sceny v OpenGL:
 * kamera, skybox
 *
 * @author PGRF FIM UHK
 * @version 2015
 */
public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    //static properties
    final static int COLLISION_SIZE = 5;
    final static Point3D PLAYER_OFFSET = new Point3D(0.5, 0.5, 0.5);

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

    List<Texture> texture;
    AbstractMaze curMaze;
    private String compass = "";

    private int maze = 0, debPlayerPos = 0, debPlayerStartPos = 0;


    @Override
    //fixme remove
    @SuppressWarnings("Duplicates")
    public void init(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();
        glu = new GLU();
        glut = new GLUT();

        // <editor-fold defaultstate="collapsed" desc=" Console output ">
        System.err.println("Chosen GLCapabilities: " + glDrawable.getChosenGLCapabilities());
        System.err.println("INIT GL IS: " + gl.getClass().getName());
        System.err.println("GL_VENDOR: " + gl.glGetString(GL2.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL2.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL2.GL_VERSION));
// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" Light and Colors seting ">
        float[] mat_specular =
                {1.0f, 1.0f, 1.0f, 1.0f};
        float[] mat_shininess =
                {25.0f};
        float[] light_position =
                {100000.0f, 1.0f, 1.0f, 0.0f};

        float[] red = {0.8f, 0.1f, 0.0f, 0.7f};
        float[] yellow = {0.8f, 0.75f, 0.0f, 0.7f};
        float[] blue = {0.2f, 0.2f, 1.0f, 0.7f};
        float[] brown = {0.8f, 0.4f, 0.1f, 0.7f};
// </editor-fold>
        curMaze = new Maze1();
        resetPlayer();

        // <editor-fold defaultstate="collapsed" desc=" Texture loading ">
        texture = new ArrayList<>();

        //Load textures
        try {
            System.err.println("Loading textures...");

            int index = 0;
            for (String textureUl : curMaze.getTextureUls()) {
                texture.add(TextureIO.newTexture(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(textureUl)), false, TextureIO.PNG));
                System.out.println(textureUl + " estimated memory size = " + texture.get(index).getEstimatedMemorySize());
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            e.printStackTrace(new PrintStream(bos));
            JOptionPane.showMessageDialog(null,
                    bos.toString(),
                    "Error loading texture",
                    JOptionPane.ERROR_MESSAGE);
            throw new GLException(e);
            //return;
        }

// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" Set Default Parameters ">

        gl.glShadeModel(GL2.GL_SMOOTH);                // Enable Smooth Shading
        gl.glClearDepth(1.0f);                        // Depth Buffer Setup
        gl.glEnable(GL2.GL_DEPTH_TEST);                // Enables Depth Testing
        gl.glDepthFunc(GL2.GL_LEQUAL);                // The Type Of Depth Testing To Do

        // Really Nice Perspective Calculations
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // Texture filter
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NONE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NONE);

//         Light and material
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position, 0);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        // </editor-fold>

        /* make the objects */
        // <editor-fold defaultstate="collapsed" desc=" Objects Generation ">

        // <editor-fold defaultstate="collapsed" desc=" Maze ">
        if (0 >= maze) {
            maze = gl.glGenLists(1);
            gl.glNewList(maze, GL2.GL_COMPILE);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, brown, 0);
            maze(gl);
            gl.glEndList();
            System.err.println("maze list created: " + maze);
        } else {
            System.err.println("maze list reused: " + maze);
        }
        // </editor-fold>

//        // <editor-fold defaultstate="collapsed" desc=" Player Position ">
//
//        if (0 >= debPlayerPos) {
//            debPlayerPos = gl.glGenLists(1);
//            gl.glNewList(debPlayerPos, GL2.GL_COMPILE);
//            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, brown, 0);
//            debPlayerPos(gl);
//            gl.glEndList();
//            System.err.println("box list created: " + debPlayerPos);
//        } else {
//            System.err.println("box list reused: " + debPlayerPos);
//        }
//        // </editor-fold>
//        // <editor-fold defaultstate="collapsed" desc=" Player Start Position ">
//        if (0 >= debPlayerStartPos) {
//            debPlayerStartPos = gl.glGenLists(1);
//            gl.glNewList(debPlayerStartPos, GL2.GL_COMPILE);
//            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, red, 0);
//            debPlayerStartPos(gl);
//            gl.glEndList();
//            System.err.println("mydesk list created: " + debPlayerStartPos);
//        } else {
//            System.err.println("mydesk list reused: " + debPlayerStartPos);
//        }
//        // </editor-fold>

        // </editor-fold>

        gl.glEnable(GL2.GL_NORMALIZE);
    }

    /**
     * Resets Player to start Position
     */
    private void resetPlayer() {
        px = curMaze.getStartPosition(PLAYER_OFFSET.getX()).getX();
        py = curMaze.getStartPosition(PLAYER_OFFSET.getY()).getY();
        pz = curMaze.getStartPosition(PLAYER_OFFSET.getZ()).getZ();
        curMaze.setCurrentLevel((int) curMaze.getStartPosition().getY());
    }

    // <editor-fold defaultstate="collapsed" desc=" Object Data generation ">
    private void maze(GL2 gl) {
        int y = 0;
        for (int[][] level : curMaze.getLevels()) {
            int size = curMaze.getSquareSize();
            for (int x = 0; x < level.length; x++) {
                for (int z = 0; z < level.length; z++) {
                    switch (level[x][z]) {
                        //Hall
                        case 0:
//                            new Hall().init(gl, maze, new Vec3D(x, y, z), new Vec3D(0, 0, 1)).draw();
                            if (texture.get(0) != null) {
                                texture.get(0).enable(gl);
                                texture.get(0).bind(gl);
                                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
//                                TextureCoords coords = texture[0].getImageTexCoords();

                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(0.0f, 0.0f, 1.0f);
                                gl.glTexCoord2f(0, 1);
                                gl.glVertex3d(x * size, y, z * size);
                                gl.glTexCoord2f(1, 1);
                                gl.glVertex3d(size * x + size, y, z * size);
                                gl.glTexCoord2f(1, 0);
                                gl.glVertex3d(x * size + size, y, z * size + size);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3d(x * size, y, size * z + size);

                                gl.glEnd();

                                texture.get(0).disable(gl);
                            }
                            if (texture.get(6) != null) {
                                texture.get(6).enable(gl);
                                texture.get(6).bind(gl);
                                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
//                                TextureCoords coords = texture[0].getImageTexCoords();

                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(0.0f, 0.0f, 1.0f);
                                gl.glTexCoord2f(0, 1);
                                gl.glVertex3d(x * size, y + curMaze.getSquareSize(), z * size);
                                gl.glTexCoord2f(1, 1);
                                gl.glVertex3d(size * x + size, y + curMaze.getSquareSize(), z * size);
                                gl.glTexCoord2f(1, 0);
                                gl.glVertex3d(x * size + size, y + curMaze.getSquareSize(), z * size + size);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3d(x * size, y + curMaze.getSquareSize(), size * z + size);

                                gl.glEnd();

                                texture.get(6).disable(gl);
                            }


                            break;
                        //Wall
                        case 1:
//                            new Wall().init(gl, curMaze, level, new Vec3D(x, y, z)).draw();
                            if (x + 1 < level.length && level[x + 1][z] != 1) {
                                if (texture.get(1) != null) {
                                    texture.get(1).enable(gl);
                                    texture.get(1).bind(gl);
                                    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
                                    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
                                    gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glColor3f(0.0f, 1.0f, 1.0f);
                                    gl.glTexCoord2f(4, 0);
                                    gl.glVertex3i(size * x + size, y, z * size);
                                    gl.glTexCoord2f(0, 0);
                                    gl.glVertex3i(x * size + size, y, z * size + size);
                                    gl.glTexCoord2f(0, 4);
                                    gl.glVertex3i(x * size + size, y + size, z * size + size);
                                    gl.glTexCoord2f(4, 4);
                                    gl.glVertex3i(size * x + size, y + size, z * size);
                                    gl.glEnd();
                                    texture.get(1).disable(gl);
                                }
                            }

                            //magenta
                            if (x - 1 > -1 && level[x - 1][z] != 1) {
                                if (texture.get(2) != null) {
                                    texture.get(2).enable(gl);
                                    texture.get(2).bind(gl);
                                    gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glColor3f(1.0f, 0.0f, 1.0f);
                                    gl.glTexCoord2f(0, 1);
                                    gl.glVertex3i(x * size, y + size, z * size);
                                    gl.glTexCoord2f(1, 1);
                                    gl.glVertex3i(x * size, y + size, z * size + size);
                                    gl.glTexCoord2f(1, 0);
                                    gl.glVertex3i(x * size, y, z * size + size);
                                    gl.glTexCoord2f(0, 0);
                                    gl.glVertex3i(x * size, y, z * size);
                                    gl.glEnd();
                                    texture.get(2).disable(gl);
                                }
                            }
                            //green
                            if (z - 1 > -1 && level[x][z - 1] != 1) {
                                if (texture.get(3) != null) {
                                    texture.get(3).enable(gl);
                                    texture.get(3).bind(gl);
                                    gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glColor3f(0.0f, 1.0f, 0.0f);
                                    gl.glTexCoord2f(0, 1);
                                    gl.glVertex3i(x * size, y, z * size);
                                    gl.glTexCoord2f(1, 1);
                                    gl.glVertex3i(size * x + size, y, z * size);
                                    gl.glTexCoord2f(1, 0);
                                    gl.glVertex3i(size * x + size, y + size, z * size);
                                    gl.glTexCoord2f(0, 0);
                                    gl.glVertex3i(x * size, y + size, z * size);
                                    gl.glEnd();
                                    texture.get(3).disable(gl);
                                }
                            }
                            //yellow
                            if (z + 1 < level.length && level[x][z + 1] != 1) {
                                if (texture.get(4) != null) {
                                    texture.get(4).enable(gl);
                                    texture.get(4).bind(gl);
                                    gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                                    gl.glBegin(GL2.GL_QUADS);
                                    gl.glColor3f(1.0f, 1.0f, 0.0f);
                                    gl.glTexCoord2f(0, 1);
                                    gl.glVertex3i(x * size, y + size, z * size + size);
                                    gl.glTexCoord2f(1, 1);
                                    gl.glVertex3i(x * size + size, y + size, z * size + size);
                                    gl.glTexCoord2f(1, 0);
                                    gl.glVertex3i(x * size + size, y, z * size + size);
                                    gl.glTexCoord2f(0, 0);
                                    gl.glVertex3i(x * size, y, z * size + size);
                                    gl.glEnd();
                                    texture.get(4).disable(gl);
                                }
                            }
                            //top of wall
                            if (texture.get(5) != null) {
                                texture.get(5).enable(gl);
                                texture.get(5).bind(gl);
                                gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(1.0f, 0.0f, 0.0f);
                                gl.glTexCoord2f(0, 1);
                                gl.glVertex3i(x * size, y + size, z * size);
                                gl.glTexCoord2f(1, 1);
                                gl.glVertex3i(size * x + size, y + size, z * size);
                                gl.glTexCoord2f(1, 0);
                                gl.glVertex3i(x * size + size, y + size, z * size + size);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(x * size, y + size, z * size + size);
                                gl.glEnd();
                                texture.get(5).disable(gl);
                            }
                            break;

                    }
                }
            }
            y += curMaze.getWallHeight();
        }

    }

    private void debPlayerPos(GL2 gl) {

    }

    private void debPlayerStartPos(GL2 gl) {
    }
// </editor-fold>


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

        // <editor-fold defaultstate="collapsed" desc=" Test Objects ">
        gl.glPushMatrix();
        Point3D sP = curMaze.getStartPosition();
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
        // </editor-fold>


        gl.glPushMatrix();
        gl.glCallList(maze);
        gl.glPopMatrix();

        // <editor-fold defaultstate="collapsed" desc=" Draw text ">
        gl.glColor3f(1f, 1f, 1f);
        String text = this.getClass().getName() + ": [WSAD][lmb] camera";
        if (per) text += ", [P]ersp ";
        else text += ", [p]ersp ";
        if (free) text += ", [F]ree ";
        else text += ", [f]ree ";

        text += ", [R]eset player to start ";

        OglUtils.drawStr2D(glDrawable, 3, height - 20, text);
//        OglUtils.drawStr2D(glDrawable, width - 90, 3, " (c) PGRF UHK");
        OglUtils.drawStr2D(glDrawable, width - 590, 3, String.format("%f|%f|%f||%f|%f|%f||%f|%f|%f", px, py, pz, ex + py, ey + py, ez + pz, ux, uy, uz));
//        OglUtils.drawStr2D(glDrawable, frame.getWidth() / 2, height - 20, compass);


        // </editor-fold>
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        //</editor-fold> defaultstate="collapsed"
        GL2 gl = drawable.getGL().getGL2();
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, this.width, this.height);
    }


    public void detectColision(double x, double z) {
        //</editor-fold> defaultstate="collapsed"
        double posX = x / curMaze.getSquareSize();
        double posZ = z / curMaze.getSquareSize();
        double curPosX = px / curMaze.getSquareSize();
        double curPosZ = pz / curMaze.getSquareSize();

        if (posZ - curPosZ > 0) {
            posZ = (z + COLLISION_SIZE) / curMaze.getSquareSize();
        } else posZ = (z - COLLISION_SIZE) / curMaze.getSquareSize();
        if (curMaze.getLevels().get(0)[(int) posX][(int) posZ] != 1) pz = z;

        posZ = z / curMaze.getSquareSize();
        if (posX - curPosX > 0) {
            posX = (x + COLLISION_SIZE) / curMaze.getSquareSize();
        } else posX = (x - COLLISION_SIZE) / curMaze.getSquareSize();
        if (curMaze.getLevels().get(0)[(int) posX][(int) posZ] != 1) px = x;

//        System.out.println("-----");
//        System.out.println("posX - curPosX = " + (posX - curPosX));
//        System.out.println("posZ - curPosZ = " + (posZ - curPosZ));
//        System.out.println("posX = " + posX);
//        System.out.println("posZ = " + posZ);
//        System.out.println("curPosX = " + curPosX);
//        System.out.println("curPosZ = " + curPosZ);
//        System.out.println("curMaze.getLevels().get(0)[posX][posZ] = " + curMaze.getLevels().get(0)[(int) posX][(int) posZ]);
    }

    // <editor-fold defaultstate="collapsed" desc=" Controls ">
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) ;
        ox = e.getX();
        oy = e.getY();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        //</editor-fold> defaultstate="collapsed"
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


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                free = !free;
                //Returns player to playing height
                if (!free)
                    py = curMaze.getCurrentLevel() * curMaze.getWallHeight() + PLAYER_OFFSET.getY() * curMaze.getSquareSize();
                break;
            case KeyEvent.VK_P:
                per = !per;
                break;
            case KeyEvent.VK_E:
                break;
            case KeyEvent.VK_R:
                resetPlayer();
                break;
        }
    }

    //fixme
    // <editor-fold defaultstate="collapsed" desc=" Unused ">
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
// </editor-fold>
    }
    // </editor-fold>

}