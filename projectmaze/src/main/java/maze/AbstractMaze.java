package maze;

import transforms.Point3D;

import java.util.ArrayList;
import java.util.List;

public class AbstractMaze {
    protected int squareSize = 40;
    protected int wallHeight = 100;
    protected Point3D startPosition;
    protected List<int[][]> levels = new ArrayList();


    public List<int[][]> getLevels() {
        return levels;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getWallHeight() {
        return wallHeight;
    }

    public Point3D getStartPosition() {
        return startPosition.mul(squareSize);
    }
}
