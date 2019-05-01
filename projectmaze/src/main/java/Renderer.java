import blocks.AbstractBlock;
import blocks.Hall;
import blocks.Teleporter;
import blocks.Wall;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import maze.AbstractMaze;
import maze.Maze1;
import maze.Player;
import transforms.Point3D;
import utils.DS;
import utils.OglUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    //static properties

    private GLU glu;
    private GLUT glut;

    private int width;
    private int height;
    private int ox, oy;

    private float zenit = 0;
    private float azimut = 0;
    private double ex = 1, ey = 0, ez = 0, ux = 0, uy = 1, uz = 0;
    private float step, trans = 0;
    private boolean per = true, free = false;
    private double a_rad;
    private long oldmils = System.currentTimeMillis();

    private List<Texture> texture;
    private AbstractMaze curMaze;
    private Player player;
    private String compass = "";

    private int maze = 0, debPlayerPos = 0, debPlayerStartPos = 0;
    //fixme:
    private boolean fog = false;
    private float[] light_position;
    private int lx = 0, ly = 0;


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
                {50.0f};
//        light_position = new float[]{1.0f, 1.0f, 1.0f, 0.0f};


        float[] red = {0.8f, 0.1f, 0.0f, 0.7f};
        float[] yellow = {0.8f, 0.75f, 0.0f, 0.7f};
        float[] blue = {0.2f, 0.2f, 1.0f, 0.7f};
        float[] brown = {0.8f, 0.4f, 0.1f, 0.7f};
// </editor-fold>
        curMaze = new Maze1();
        curMaze.resetPlayer();
        player = curMaze.getPlayer();

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

        //
        gl.glFrontFace(GL2.GL_CCW);
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_LINE);
        gl.glDisable(GL2.GL_CULL_FACE);

        // Texture filter
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NONE);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NONE);

//         Light and material
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, mat_shininess, 0);

//        float[] light_amb = new float[]{1, 1, 1, 1};// nastaveni ambientni slozky
//        float[] light_dif = new float[]{1, 1, 1, 1};// nastaveni difusni slozky
//        float[] light_spec = new float[]{1, 1, 1, 1};// nastaveni zrcadlove slozky
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, light_amb, 0);
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_dif, 0);
//        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_spec, 0);

        //fixme
//        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

//        gl.glEnable(GL2.GL_LIGHTING);
//        gl.glEnable(GL2.GL_LIGHT0);
        // </editor-fold>

        /* make the objects */
        // <editor-fold defaultstate="collapsed" desc="Static Objects Generation">

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


        gl.glEnable(GL2.GL_NORMALIZE);
    }

    // <editor-fold defaultstate="collapsed" desc=" Object Data generation ">
    private void maze(GL2 gl) {
        int y = 0;
        for (AbstractBlock[][] level : curMaze.getLevels()) {
            int size = curMaze.getSquareSize();
            for (int x = 0; x < level.length; x++) {
                for (int z = 0; z < level.length; z++) {
                    int ZSS = x * size + size;
                    int XSS = z * size + size;
                    int ZS0 = x * size;
                    int XS0 = z * size;
                    int YPS = y + size;
                    AbstractBlock b = level[z][x];

                    if (b instanceof Hall) {
                        //Down
                        if (texture.get(b.getTexD()) != null) {
                            texture.get(b.getTexD()).enable(gl);
                            texture.get(b.getTexD()).bind(gl);
                            DS.drawFloor(gl, ZS0, XS0, y, ZSS, XSS);
                            texture.get(b.getTexU()).disable(gl);
                        }
                        //Up
                        if (texture.get(b.getTexU()) != null) {
                            texture.get(b.getTexU()).enable(gl);
                            texture.get(b.getTexU()).bind(gl);
                            DS.drawCeiling(gl, ZS0, XS0, YPS, ZSS, XSS);
                            texture.get(b.getTexU()).disable(gl);
                        }
                        //north - cyan - wood
                        if (level[z - 1][x] instanceof Wall) {
                            if (texture.get(b.getTexN()) != null) {
                                texture.get(b.getTexN()).enable(gl);
                                texture.get(b.getTexN()).bind(gl);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(0.0f, 1.0f, 1.0f);
                                gl.glTexCoord2f(4, 0);
                                gl.glVertex3i(ZSS, y, XS0);
                                gl.glTexCoord2f(4, 4);
                                gl.glVertex3i(ZSS, YPS, XS0);
                                gl.glTexCoord2f(0, 4);
                                gl.glVertex3i(ZS0, YPS, XS0);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZS0, y, XS0);
                                gl.glEnd();
                                texture.get(b.getTexN()).disable(gl);
                            }
                        }
                        //south - magenta - bricks
                        if (level[z + 1][x] instanceof Wall) {
                            if (texture.get(b.getTexS()) != null) {
                                texture.get(b.getTexS()).enable(gl);
                                texture.get(b.getTexS()).bind(gl);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(1.0f, 0.0f, 1.0f);
                                gl.glTexCoord2f(2, 2);
                                gl.glVertex3i(ZSS, YPS, XSS);
                                gl.glTexCoord2f(2, 0);
                                gl.glVertex3i(ZSS, y, XSS);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZS0, y, XSS);
                                gl.glTexCoord2f(0, 2);
                                gl.glVertex3i(ZS0, YPS, XSS);
                                gl.glEnd();
                                texture.get(b.getTexS()).disable(gl);
                            }
                        }
                        //east - green -- cobble
                        if (level[z][x + 1] instanceof Wall) {
                            if (texture.get(b.getTexE()) != null) {
                                texture.get(b.getTexE()).enable(gl);
                                texture.get(b.getTexE()).bind(gl);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(0.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(1, 0);
                                gl.glVertex3i(ZSS, y, XSS);
                                gl.glTexCoord2f(1, 1);
                                gl.glVertex3i(ZSS, YPS, XSS);
                                gl.glTexCoord2f(0, 1);
                                gl.glVertex3i(ZSS, YPS, XS0);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZSS, y, XS0);
                                gl.glEnd();
                                texture.get(b.getTexE()).disable(gl);
                            }
                        }
                        //west - yellow - blue_concrete_powder
                        if (level[z][x - 1] instanceof Wall) {
                            if (texture.get(b.getTexW()) != null) {
                                texture.get(b.getTexW()).enable(gl);
                                texture.get(b.getTexW()).bind(gl);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
                                gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
                                gl.glBegin(GL2.GL_QUADS);
                                gl.glColor3f(1.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(1, 0);
                                gl.glVertex3i(ZS0, YPS, XSS);
                                gl.glTexCoord2f(1, 1);
                                gl.glVertex3i(ZS0, y, XSS);
                                gl.glTexCoord2f(0, 1);
                                gl.glVertex3i(ZS0, y, XS0);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZS0, YPS, XS0);
                                gl.glEnd();
                                texture.get(b.getTexW()).disable(gl);
                            }

                        }
                    }
                    if (b instanceof Wall) {
//                        //top of wall
//                        if (texture.get(5) != null) {
//                            texture.get(5).enable(gl);
//                            texture.get(5).bind(gl);
//                            gl.glBegin(GL2.GL_QUADS);
//                            gl.glColor3f(1.0f, 0.0f, 0.0f);
//                            gl.glTexCoord2f(0, 1);
//                            gl.glVertex3i(ZS0, YPS, XS0);
//                            gl.glTexCoord2f(1, 1);
//                            gl.glVertex3i(ZSS, YPS, XS0);
//                            gl.glTexCoord2f(1, 0);
//                            gl.glVertex3i(ZSS, YPS, XSS);
//                            gl.glTexCoord2f(0, 0);
//                            gl.glVertex3i(ZS0, YPS, XSS);
//                            gl.glEnd();
//                            texture.get(5).disable(gl);
//                        }
                    }
                }
            }
            y += curMaze.getHeightBetweenLevels();
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
        trans = 300 * step;

        //System.out.println(fps);

        // vymazani obrazovky a Z-bufferu
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (per) glu.gluPerspective(45, width / (float) height, 0.1f, 5000.0f);
        else gl.glOrtho(-20 * width / (float) height, 20 * width / (float) height, -20, 20, 0.1f, 5000.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);


//        gl.glPopMatrix();
        gl.glLoadIdentity();
        glu.gluLookAt(player.getPX(), player.getPY(), player.getPZ(), ex + player.getPX(), ey + player.getPY(), ez + player.getPZ(), ux, uy, uz);


        float[] light2_pos = {(float) player.getPX(), (float) player.getPY(), (float) player.getPZ(), 1};
        float[] light2_color_am = {0, 0, 1, 1};
        float[] light2_color_diff = {1, 0, 0, 0};
        float[] light2_color_spec = {1, 1, 1, 1};
        float[] light2_spot_dir = {(float) ex, (float) ey, (float) ez};
//        float[] light2_spot_dir = {0,0,1 };

        System.out.println("light2_pos = " + Arrays.toString(light2_pos));
        System.out.println("light2_spot_dir = " + Arrays.toString(light2_spot_dir));
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, light2_pos, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_AMBIENT, light2_color_am, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, light2_color_diff, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, light2_color_spec, 0);

        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPOT_DIRECTION, light2_spot_dir, 0);
        gl.glLightf(GL2.GL_LIGHT2, GL2.GL_SPOT_CUTOFF, 20);
        gl.glEnable(GL2.GL_LIGHT2);







        // <editor-fold defaultstate="collapsed" desc=" Test Objects ">
        gl.glPushMatrix();
        Point3D sP = curMaze.calcPos(curMaze.getStartPosition());
        gl.glColor3f(1, 0, 1);
        gl.glTranslated(sP.getX(), sP.getY(), sP.getZ());
        glut.glutWireCube(5);
        gl.glPopMatrix();

        //player pos
        gl.glPushMatrix();
        gl.glColor3f(1, 0, 0);
        gl.glTranslated(player.getPX(), player.getPY() - 18, player.getPZ());
        glut.glutSolidCube(1);
        gl.glPopMatrix();
        // </editor-fold>

        gl.glPushMatrix();
        gl.glCallList(maze);
        gl.glPopMatrix();

        if (fog) {
            //Fog
            float Fog_distance = 250;
            float[] Fog_colour = {0, 0, 0, 0};

            gl.glEnable(GL2.GL_FOG);
            gl.glHint(GL2.GL_FOG_HINT, GL2.GL_NICEST);

            gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_EXP2);

            gl.glFogf(GL2.GL_FOG_DENSITY, 0.009f);
            gl.glFogfv(GL2.GL_FOG_COLOR, Fog_colour, 0);
            gl.glFogf(GL2.GL_FOG_START, Fog_distance - 50);
            gl.glFogf(GL2.GL_FOG_END, Fog_distance);
        } else {
            gl.glDisable(GL2.GL_FOG);
        }

        // <editor-fold defaultstate="collapsed" desc=" Draw text ">
        gl.glColor3f(1f, 1f, 1f);
        String text = this.getClass().getName() + ": [WSAD][lmb] camera";
        if (per) text += ", [P]ersp ";
        else text += ", [p]ersp ";
        if (free) text += ", [F]ree ";
        else text += ", [f]ree ";
        if (fog) text += ", F[O]g ";
        else text += ", F[o]g ";

        text += ", [R]eset player to start";


        if (curMaze.getCurrentBlockAtPlayerLocation() != null) {
            if (curMaze.getCurrentBlockAtPlayerLocation() instanceof Teleporter) {
                OglUtils.drawStr2D(glDrawable, width / 2, height / 3, "Teleport [E]", 20);
            }
        }

        OglUtils.drawStr2D(glDrawable, 3, height - 20, text);
        OglUtils.drawStr2D(glDrawable, 90, 3, " (c) Michal Dolenský 2019 | Textures:  © 2016 Sapix");
        OglUtils.drawStr2D(glDrawable, width - 590, 3, String.format("%f|%f|%f||%f|%f|%f||%f|%f|%f", player.getPX(), player.getPY(), player.getPZ(), ex + player.getPX(), ey + player.getPY(), ez + player.getPZ(), ux, uy, uz));
        OglUtils.drawStr2D(glDrawable, width / 2, height - 20, compass);


        // </editor-fold>
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        this.width = width;
        this.height = height;
        gl.glViewport(0, 0, this.width, this.height);
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
        int dx = e.getX() - ox;
        int dy = e.getY() - oy;
        ox = e.getX();
        oy = e.getY();

        zenit -= dy;
        if (zenit > 90) zenit = 90;
        if (zenit <= -90) zenit = -90;
        azimut -= dx;
        azimut = azimut % 360;
        a_rad = -1 * azimut * Math.PI / 180;
        double z_rad = zenit * Math.PI / 180;
        ex = Math.sin(a_rad) * Math.cos(z_rad);
        ey = Math.sin(z_rad);
        ez = -Math.cos(a_rad) * Math.cos(z_rad);
        ux = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
        uy = Math.sin(z_rad + Math.PI / 2);
        uz = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);

//        System.out.println("azimut = " + azimut);
        if (azimut >= -90 / 2.0 && azimut <= 90 / 2.0) {
            compass = "N";
        }
        if (azimut >= 90 - 90 / 2.0 && azimut <= 90 + 90 / 2.0) {
            compass = "E";
        }
        if (azimut >= 180 - 90 / 2.0 && azimut <= 180 + 90 / 2.0) {
            compass = "S";
        }
        if (azimut >= 270 - 90 / 2.0 && azimut <= 270 + 90 / 2.0) {
            compass = "W";
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (free) {
                player.getPos().add(new Point3D(ex * trans, ey * trans, ez * trans));
            } else {
                double kpx = player.getPX() + ex * trans;
                double kpz = player.getPZ() + ez * trans;
                curMaze.detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (free) {
                player.getPos().sub(new Point3D(ex * trans, ey * trans, ez * trans));
            } else {
                double kpx = player.getPX() - ex * trans;
                double kpz = player.getPZ() - ez * trans;
                curMaze.detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (free) {
                player.getPos().addX(Math.sin(a_rad - Math.PI / 2) * trans);
                player.getPos().subZ(Math.cos(a_rad - Math.PI / 2) * trans);
            } else {
                double kpx = player.getPos().getX() + Math.sin(a_rad - Math.PI / 2) * trans;
                double kpz = player.getPos().getZ() - Math.cos(a_rad - Math.PI / 2) * trans;
                curMaze.detectColision(kpx, kpz);
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (free) {
                player.getPos().subX(Math.sin(a_rad - Math.PI / 2) * trans);
                player.getPos().addZ(Math.cos(a_rad - Math.PI / 2) * trans);
            } else {
                double kpx = player.getPos().getX() - Math.sin(a_rad - Math.PI / 2) * trans;
                double kpz = player.getPos().getZ() + Math.cos(a_rad - Math.PI / 2) * trans;
                curMaze.detectColision(kpx, kpz);
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
                    player.getPos().setY(curMaze.getPlayer().getCurLev() * curMaze.getHeightBetweenLevels() + curMaze.PLAYER_OFFSET.getY() * curMaze.getSquareSize());
                break;
            case KeyEvent.VK_P:
                per = !per;
                break;
            case KeyEvent.VK_E:
                curMaze.checkForTeleport();
                break;
            case KeyEvent.VK_R:
                curMaze.resetPlayer();
                break;
            case KeyEvent.VK_O:
                fog = !fog;
                break;
        }
    }


    //fixme
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
    // </editor-fold>

}