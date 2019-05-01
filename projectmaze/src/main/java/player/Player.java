package player;

import com.jogamp.opengl.glu.GLU;
import transforms.Point2D;
import transforms.Point3D;

public class Player {
    private Point3D pos = new Point3D();
    private int currentLevel;
    private int dx;
    private int dy;
    private float zenit = 0;
    private float azimut = 0;
    private double ex = 1, ey = 0, ez = 0, ux = 0, uy = 1, uz = 0;
    private double a_rad;
    private float trans = 0;

    public void look(GLU glu) {
        glu.gluLookAt(getPX(), getPY(), getPZ(), ex + getPX(), ey + getPY(), ez + getPZ(), ux, uy, uz);
    }

    public String getLookString() {
        return String.format("%f|%f|%f||%f|%f|%f||%f|%f|%f", getPX(), getPY(), getPZ(), ex + getPX(), ey + getPY(), ez + getPZ(), ux, uy, uz);
    }

    public void calculate() {
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
    }

    public String getDirection() {
        int az = (int) azimut;
        if (az > 0) {
            if (az >= 315 && az < 360 || az <= 45) {
                return "N";
            }
            if (az < 135) {
                return "W";
            }
            if (az < 225) {
                return "S";
            }
            if (az < 315) {
                return "E";
            }
        } else {
            az = Math.abs(az);
            if (az >= 315 && az < 360 || az <= 45) {
                return "N";
            }
            if (az < 135) {
                return "E";
            }
            if (az < 225) {
                return "S";
            }
            if (az < 315) {
                return "W";
            }
        }
        return "";
    }

    // <editor-fold defaultstate="collapsed" desc=" Getters and Setters ">
    public Point3D getPos() {
        return pos;
    }


    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    public int getCurLev() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public double getPX() {
        return pos.getX();
    }

    public double getPY() {
        return pos.getY();
    }

    public double getPZ() {
        return pos.getZ();
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setTrans(float trans) {
        this.trans = trans;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Player Movement ">

    public void moveForwardF() {
        pos.add(new Point3D(ex * trans, ey * trans, ez * trans));

    }

    public void moveBackF() {
        pos.sub(new Point3D(ex * trans, ey * trans, ez * trans));
    }

    public void moveLeftF() {
        pos.addX(Math.sin(a_rad - Math.PI / 2) * trans);
        pos.subZ(Math.cos(a_rad - Math.PI / 2) * trans);
    }

    public void moveRightF() {
        pos.subX(Math.sin(a_rad - Math.PI / 2) * trans);
        pos.addZ(Math.cos(a_rad - Math.PI / 2) * trans);
    }

    public Point2D moveForwardC() {
        double kpx = getPX() + ex * trans;
        double kpz = getPZ() + ez * trans;
        return new Point2D(kpx, kpz);
    }

    public Point2D moveBackC() {
        double kpx = getPX() - ex * trans;
        double kpz = getPZ() - ez * trans;
        return new Point2D(kpx, kpz);
    }

    public Point2D moveLeftC() {
        double kpx = getPos().getX() + Math.sin(a_rad - Math.PI / 2) * trans;
        double kpz = getPos().getZ() - Math.cos(a_rad - Math.PI / 2) * trans;
        return new Point2D(kpx, kpz);

    }

    public Point2D moveRightC() {
        double kpx = getPos().getX() - Math.sin(a_rad - Math.PI / 2) * trans;
        double kpz = getPos().getZ() + Math.cos(a_rad - Math.PI / 2) * trans;
        return new Point2D(kpx, kpz);
    }

    // </editor-fold>
}
