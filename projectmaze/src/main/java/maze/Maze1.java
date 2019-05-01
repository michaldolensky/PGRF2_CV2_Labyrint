package maze;

import blocks.*;
import transforms.Point3D;

public class Maze1 extends AbstractMaze {

    public Maze1() {
        setStartPosition(new Point3D(1, 0, 1));
        getPlayer().setPos(calcPos(getStartPosition()));
        Teleport Tel1 = new Teleport(1, 1, 1);
        Hall hall_1 = new Hall(0, 0, 0, 0, 2, 1);

        AbstractBlock[][] level0 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, new Wall()},
                {new Wall(), Tel1, new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, new Wall(), new Wall(), hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, new Wall(), hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, new Wall(), hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, new Wall(), new Wall(), hall_1, new Wall(), new Wall(), new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level0);
        AbstractBlock[][] level1 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), hall_1, new Teleport(1, 0, 1), hall_1, hall_1, hall_1, hall_1, hall_1, new Teleport(1, 2, 1), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), hall_1, new Wall()},
                {new Wall(), new Wall(), new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), new Wall(), new Wall(), hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, new Wall(), hall_1, new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), hall_1, hall_1, hall_1, new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level1);
        AbstractBlock[][] level2 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), hall_1, new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Teleport(1, 0, 1), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, hall_1, new Wall()},
                {new Wall(), new Wall(), hall_1, new Wall(), new Wall(), new Wall(), new Wall(), hall_1, hall_1, new Wall()},
                {new Wall(), new Wall(), hall_1, new Wall(), hall_1, hall_1, new Wall(), hall_1, new Wall(), new Wall()},
                {new Wall(), new Wall(), hall_1, new Wall(), hall_1, hall_1, new Wall(), hall_1, hall_1, new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), hall_1, new Finish(), new Wall()},
                {new Wall(), new Wall(), hall_1, hall_1, hall_1, hall_1, new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level2);
        getPlayer().setCurrentLevel(0);
        getTextureUls().put(0, "textures/block/mossy_stone_bricks.png");
        getTextureUls().put(1, "textures/block/mossy_cobblestone.png");
        getTextureUls().put(2, "textures/block/redstone_lamp_on.png");
    }
}