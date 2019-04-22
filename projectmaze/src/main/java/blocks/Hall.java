package blocks;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import maze.AbstractMaze;
import transforms.Vec3D;

public class Hall extends AbstractBlock {
    GL2 gl;
    Texture texFloor;
    Texture texCeiling;
    int size;
    Vec3D position;
    Vec3D color;
    AbstractMaze maze;

    public Hall init(GL2 gl, AbstractMaze maze, Vec3D position, Vec3D color) {
        this.gl = gl;
        this.size = maze.getSquareSize();
        this.position = position;
        this.color = color;
        this.maze = maze;
        return this;
    }
    public void draw() {
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glActiveTexture(GL.GL_TEXTURE1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3d(position.getX() * size, position.getY(), position.getZ() * size);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3d(size * position.getX() + size, position.getY(), position.getZ() * size);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3d(position.getX() * size + size, position.getY(), position.getZ() * size + size);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3d(position.getX() * size, position.getY(), size * position.getZ() + size);
        gl.glEnd();
        gl.glDisable(GL.GL_TEXTURE_2D);
    }
}
