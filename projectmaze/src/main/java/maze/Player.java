package maze;

import transforms.Point3D;

public class Player {
    private Point3D pos = new Point3D();
    private int currentLevel;

    public Point3D getPos() {
        return pos;
    }

    public void setPos(Point3D pos) {
        this.pos = pos;
    }

    public int getCurrentLevel() {
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

}
