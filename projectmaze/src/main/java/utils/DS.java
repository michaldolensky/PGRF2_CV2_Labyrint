package utils;

import com.jogamp.opengl.GL2;

public class DS {

    @SuppressWarnings("Duplicates")
    public static void drawCeiling(GL2 gl, int ZS0, int XS0, int YPS, int ZSS, int XSS, int rep) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0, 1);
        gl.glVertex3d(ZS0, YPS, XS0);
        gl.glTexCoord2f(rep, rep);
        gl.glVertex3d(ZSS, YPS, XS0);
        gl.glTexCoord2f(rep, 0);
        gl.glVertex3d(ZSS, YPS, XSS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3d(ZS0, YPS, XSS);
        gl.glEnd();
    }

    @SuppressWarnings("Duplicates")
    public static void drawFloor(GL2 gl, int ZS0, int XS0, int YPS, int ZSS, int XSS, int rep) {
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0, rep);
        gl.glVertex3d(ZS0, YPS, XSS);
        gl.glTexCoord2f(rep, rep);
        gl.glVertex3d(ZSS, YPS, XSS);
        gl.glTexCoord2f(rep, 0);
        gl.glVertex3d(ZSS, YPS, XS0);
        gl.glTexCoord2f(0, 0);
        gl.glVertex3d(ZS0, YPS, XS0);
        gl.glEnd();
    }
}
