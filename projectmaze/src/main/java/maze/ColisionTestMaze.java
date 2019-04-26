package maze;

import transforms.Point3D;

public class ColisionTestMaze extends AbstractMaze {
    Object[][] level0 = new Object[][]{
            {1,1,1,1,1},
            {1,0,0,0,1},
            {1,0,1,0,1},
            {1,0,0,0,1},
            {1,1,1,1,1}
    };
    public ColisionTestMaze() {
        getLevels().add(level0);
        setStartPosition(new Point3D(1, 0, 1));
    }
}
