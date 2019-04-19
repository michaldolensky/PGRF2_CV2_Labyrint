package maze;

import transforms.Point3D;

public class ColisionTestMaze extends AbstractMaze {
    int[][] level0 = new int[][]{
            {1,1,1,1,1},
            {1,0,0,0,1},
            {1,0,1,0,1},
            {1,0,0,0,1},
            {1,1,1,1,1}
    };
    public ColisionTestMaze() {
        getLevels().add(level0);
        startPosition = new Point3D(1.5,0.5,1.5);
    }
}
