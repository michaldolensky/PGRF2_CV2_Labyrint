package maze;

import java.util.ArrayList;
import java.util.List;

public class AbstractMaze {
    protected int squareSize = 20;
    protected int wallHeight = 100;
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
}
