package maze;

import blocks.AbstractBlock;
import blocks.Hall;
import blocks.Teleporter;
import blocks.Wall;
import transforms.Point3D;

public class ColisionTestMaze extends AbstractMaze {
    AbstractBlock[][] level0 = new AbstractBlock[][]{
            {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
            {new Wall(), new Hall(), new Teleporter(2, 0, 1), new Hall(), new Wall()},
            {new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
            {new Wall(), new Hall(), new Hall(), new Hall(), new Wall()},
            {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()}
    };
    public ColisionTestMaze() {
        getLevels().add(level0);
        setStartPosition(new Point3D(1, 0, 1));
    }
}
