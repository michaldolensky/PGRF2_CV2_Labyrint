package maze;

import blocks.*;
import transforms.Point3D;

public class CollisionTestMaze extends AbstractMaze {

    public CollisionTestMaze() {
        setStartPosition(new Point3D(1, 0, 2));
        getPlayer().setPos(calcPos(getStartPosition()));
        AbstractBlock[][] level0 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Hall(), new Teleport(2, 0, 1), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Finish(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()}
        };
        getLevels().add(level0);
        getPlayer().setCurrentLevel(0);
    }
}
