package utils;

import com.jogamp.opengl.GL2;

public class DS {

    public static void drawFlatSquare(GL2 gl, int ZS0, int XS0, int YPS, int ZSS, int XSS) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3d(ZS0, YPS, XS0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex3d(ZSS, YPS, XS0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex3d(ZSS, YPS, XSS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3d(ZS0, YPS, XSS);
        gl.glEnd();
    }
}
