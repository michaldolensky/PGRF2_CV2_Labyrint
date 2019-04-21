package blocks;

import com.jogamp.opengl.GL2;
import maze.AbstractMaze;
import transforms.Vec3D;

public class Wall extends AbstractBlock {


    private GL2 gl;
    private AbstractMaze maze;
    private int size;
    private int[][] level;
    private int x;
    private int y;
    private int z;


    public Wall init(GL2 gl, AbstractMaze maze, int[][] level, Vec3D position) {
        this.gl = gl;
        this.maze = maze;
        this.x = (int) position.getX();
        this.y = (int) position.getY();
        this.z = (int) position.getZ();
        this.size = maze.getSquareSize();
        this.level = level;
        return this;
    }

    public void draw() {
//        int absAzi = Math.abs((int) azimut);
        //cyan
//                            if (!(absAzi >= 270 - fovy / 2 && absAzi <= 270 + fovy / 2)) {
        if (x + 1 < level.length && level[x + 1][z] != 1) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(0.0f, 1.0f, 1.0f);
            gl.glVertex3i(size * x + size, y, z * size);
            gl.glVertex3i(x * size + size, y, z * size + size);
            gl.glVertex3i(x * size + size, y + size, z * size + size);
            gl.glVertex3i(size * x + size, y + size, z * size);
            gl.glEnd();

        }
//                            }
        //magenta
//                            if (!(absAzi >= 90 - fovy / 2 && absAzi <= 90 + fovy / 2)) {
        if (x - 1 > -1 && level[x - 1][z] != 1) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1.0f, 0.0f, 1.0f);
            gl.glVertex3i(x * size, y + size, z * size);
            gl.glVertex3i(x * size, y + size, z * size + size);
            gl.glVertex3i(x * size, y, z * size + size);
            gl.glVertex3i(x * size, y, z * size);
            gl.glEnd();
        }
//                            }
        //green
//                            if (!(absAzi >= -fovy / 2 && absAzi <= fovy / 2)) {
        if (z - 1 > -1 && level[x][z - 1] != 1) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(0.0f, 1.0f, 0.0f);
            gl.glVertex3i(x * size, y, z * size);
            gl.glVertex3i(size * x + size, y, z * size);
            gl.glVertex3i(size * x + size, y + size, z * size);
            gl.glVertex3i(x * size, y + size, z * size);
            gl.glEnd();

        }
//                            }
        //yellow
//                            if (!(absAzi >= 180 - fovy / 2 && absAzi <= 180 + fovy / 2)) {
        if (z + 1 < level.length && level[x][z + 1] != 1) {
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1.0f, 1.0f, 0.0f);
            gl.glVertex3i(x * size, y + size, z * size + size);
            gl.glVertex3i(x * size + size, y + size, z * size + size);
            gl.glVertex3i(x * size + size, y, z * size + size);
            gl.glVertex3i(x * size, y, z * size + size);
            gl.glEnd();

        }
//                            }
        //top of wall
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3i(x * size, y + size, z * size);
        gl.glVertex3i(size * x + size, y + size, z * size);
        gl.glVertex3i(x * size + size, y + size, z * size + size);
        gl.glVertex3i(x * size, y + size, z * size + size);
        gl.glEnd();
    }
}
