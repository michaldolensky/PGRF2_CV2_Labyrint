import blocks.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import maze.AbstractMaze;
import player.Player;
import utils.DS;
import utils.OglUtils;

import javax.swing.*;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;

class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    //static properties

    private GLU glu;
    private GLUT glut;

    private int width;
    private int height;
    private int ox, oy;

    private boolean per = true, free = false;
    private long oldmils = System.currentTimeMillis();
    private final long startTime;


    private HashMap<Integer, Texture> texture;
    private final AbstractMaze curMaze;
    private final Player player;

    private int maze = 0;
    private boolean fog = true;
    private Long finishTime;

    public Renderer(AbstractMaze maze) {
        curMaze = maze;
        curMaze.resetPlayer();
        player = curMaze.getPlayer();
        startTime = System.currentTimeMillis();
    }


    @Override
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
        // <editor-fold defaultstate="collapsed" desc=" Light">
        //spotlight
        float[] spot_ambient = {1f, 1f, 1f, 1.0f};
        float[] spot_diffuse = {0.0f, 0.0f, 0.0f, 1.0f};
        float[] spot_specular = {1f, 1f, 1f, 1.0f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, spot_ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, spot_diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, spot_specular, 0);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        float[] fix_ambient = {0.01f, 0.01f, 0.01f, 1f};
        float[] fix_diffuse = {0.01f, 0.01f, 0.01f, 1f};
        float[] fix_specula = {0.01f, 0.01f, 0.01f, 1f};
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, fix_ambient, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, fix_diffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, fix_specula, 0);
        gl.glEnable(GL2.GL_LIGHT1);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc=" Texture loading ">
        texture = new HashMap<>();

        //Load textures

        System.err.println("Loading textures...");
        curMaze.getTextureUls().forEach((k, v) -> {
            try {
                texture.put(k, TextureIO.newTexture(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(v)), false, TextureIO.PNG));
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
            System.out.println(v + " estimated memory size = " + texture.get(k).getEstimatedMemorySize());
        });


// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" Set Default Parameters ">

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);

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

        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);

        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Static Objects Generation">

        if (0 >= maze) {
            maze = gl.glGenLists(1);
            gl.glNewList(maze, GL2.GL_COMPILE);
            maze(gl);
            gl.glEndList();
            System.err.println("maze list created: " + maze);
        } else {
            System.err.println("maze list reused: " + maze);
        }
        // </editor-fold>


    }

    // <editor-fold defaultstate="collapsed" desc=" Object Data generation ">
    @SuppressWarnings("Duplicates")
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
                            DS.drawFloor(gl, ZS0, XS0, y, ZSS, XSS, b.getTexRepXY());
                            texture.get(b.getTexU()).disable(gl);
                        }
                        //Up
                        if (texture.get(b.getTexU()) != null) {
                            texture.get(b.getTexU()).enable(gl);
                            texture.get(b.getTexU()).bind(gl);
                            DS.drawCeiling(gl, ZS0, XS0, YPS, ZSS, XSS, b.getTexRepXY());
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
                                gl.glNormal3f(0.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(b.getTexRepXY(), 0);
                                gl.glVertex3i(ZSS, y, XS0);
                                gl.glTexCoord2f(b.getTexRepXY(), b.getTexRepXY());
                                gl.glVertex3i(ZSS, YPS, XS0);
                                gl.glTexCoord2f(0, b.getTexRepXY());
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
                                gl.glNormal3f(0.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(b.getTexRepXY(), b.getTexRepXY());
                                gl.glVertex3i(ZSS, YPS, XSS);
                                gl.glTexCoord2f(b.getTexRepXY(), 0);
                                gl.glVertex3i(ZSS, y, XSS);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZS0, y, XSS);
                                gl.glTexCoord2f(0, b.getTexRepXY());
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
                                gl.glNormal3f(0.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(b.getTexRepXY(), 0);
                                gl.glVertex3i(ZSS, y, XSS);
                                gl.glTexCoord2f(b.getTexRepXY(), b.getTexRepXY());
                                gl.glVertex3i(ZSS, YPS, XSS);
                                gl.glTexCoord2f(0, b.getTexRepXY());
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
                                gl.glNormal3f(0.0f, 1.0f, 0.0f);
                                gl.glTexCoord2f(b.getTexRepXY(), 0);
                                gl.glVertex3i(ZS0, YPS, XSS);
                                gl.glTexCoord2f(b.getTexRepXY(), b.getTexRepXY());
                                gl.glVertex3i(ZS0, y, XSS);
                                gl.glTexCoord2f(0, b.getTexRepXY());
                                gl.glVertex3i(ZS0, y, XS0);
                                gl.glTexCoord2f(0, 0);
                                gl.glVertex3i(ZS0, YPS, XS0);
                                gl.glEnd();
                                texture.get(b.getTexW()).disable(gl);
                            }

                        }
                    }
//                    if (b instanceof Wall) {
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
//                    }
                }
            }
            y += curMaze.getHeightBetweenLevels();
        }

    }
// </editor-fold>


    @Override
    public void display(GLAutoDrawable glDrawable) {
        GL2 gl = glDrawable.getGL().getGL2();

        long mils = System.currentTimeMillis();
        float step = (mils - oldmils) / 1000.0f;
        //float fps = 1000 / (float) (mils - oldmils);
        oldmils = mils;
        player.setTrans(300 * step);

        // vymazani obrazovky a Z-bufferu
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        if (per) glu.gluPerspective(45, width / (float) height, 0.1f, 5000.0f);
        else gl.glOrtho(-20 * width / (float) height, 20 * width / (float) height, -20, 20, 0.1f, 5000.0f);

        gl.glMatrixMode(GL2.GL_MODELVIEW);


//        gl.glPopMatrix();
        gl.glLoadIdentity();
        player.look(glu);


        float[] spot_position = {(float) player.getPX(), (float) player.getPY(), (float) player.getPZ(), 1.0f};
        float[] spot_direction = {(float) player.getEx(), (float) player.getEy(), (float) player.getEz()};
        float spot_angle = 35f;
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, spot_position, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPOT_DIRECTION, spot_direction, 0);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_SPOT_CUTOFF, spot_angle);
        gl.glLighti(GL2.GL_LIGHT0, GL2.GL_SPOT_EXPONENT, 15);

        // <editor-fold defaultstate="collapsed" desc=" Test Objects ">
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


        // <editor-fold defaultstate="collapsed" desc=" Fog ">
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
        // </editor-fold>
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
            if (curMaze.getCurrentBlockAtPlayerLocation() instanceof Teleport) {
                OglUtils.drawStr2D(glDrawable, width / 2, height / 3, "Teleport [E]", 20);
            }
            if (curMaze.getCurrentBlockAtPlayerLocation() instanceof Finish) {
                if (finishTime == null) finishTime = System.currentTimeMillis();
                OglUtils.drawStr2D(glDrawable, width / 2, height / 3, "Finished. Time: " + getTime(finishTime - startTime), 25);
            }
        }

        if (finishTime == null) {
            OglUtils.drawStr2D(glDrawable, width -100, height-20, "Time: " + getTime(System.currentTimeMillis() - startTime), 15);
        }else {
            OglUtils.drawStr2D(glDrawable, width -200, height-20, "Finished. Time: " + getTime(finishTime - startTime), 15);
        }

        OglUtils.drawStr2D(glDrawable, width - 590, 3, player.getLookString());
        OglUtils.drawStr2D(glDrawable, 3, height - 20, text);
        OglUtils.drawStr2D(glDrawable, 90, 3, " (c) Michal Dolenský 2019 | Textures:  © 2016 Sapix");
        OglUtils.drawStr2D(glDrawable, width - 590, 3, player.getLookString());
        OglUtils.drawStr2D(glDrawable, width / 2, height - 20, player.getDirection());


        // </editor-fold>
    }

    private String getTime(long mills) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(new Timestamp(mills - 3600 * 1000));
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
        ox = e.getX();
        oy = e.getY();
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        player.setDx(e.getX() - ox);
        player.setDy(e.getY() - oy);
        ox = e.getX();
        oy = e.getY();
        player.calculate();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (free) {
                player.moveForwardF();
            } else {
                curMaze.detectCollision(player.moveForwardC());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (free) {
                player.moveBackF();

            } else {
                curMaze.detectCollision(player.moveBackC());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (free) {
                player.moveLeftF();
            } else {
                curMaze.detectCollision(player.moveLeftC());
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (free) {
                player.moveRightF();
            } else {
                curMaze.detectCollision(player.moveRightC());
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
                    player.getPos().setY(curMaze.getPlayer().getCurLev() * curMaze.getHeightBetweenLevels() + AbstractMaze.PLAYER_OFFSET.getY() * curMaze.getSquareSize());
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
    }
    // </editor-fold>

}