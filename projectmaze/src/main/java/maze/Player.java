package maze;

import transforms.Point3D;

public class Player {
    private Point3D pos = new Point3D();
    private Point3D center = new Point3D();
    private Point3D up = new Point3D();
    private int currentLevel;

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

    public Point3D getCenter() {
        return center;
    }

    public void setCenter(Point3D center) {
        this.center = center;
    }

    public double getCX() {
        return center.getX();
    }

    public double getCY() {
        return center.getY();
    }

    public double getCZ() {
        return center.getZ();
    }


}
