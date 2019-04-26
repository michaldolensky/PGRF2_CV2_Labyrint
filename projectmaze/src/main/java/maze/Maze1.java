package maze;

import blocks.Teleporter;
import transforms.Point3D;

public class Maze1 extends AbstractMaze {

    public Maze1() {
        setStartPosition(new Point3D(1, 0, 1));
        getPlayer().setPos(calcPos(getStartPosition()));
        Object[][] level0 = new Object[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, new Teleporter(new Point3D(1,1,1)), 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        getLevels().add(level0);
        Object[][] level1 = new Object[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 1, 1, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        getLevels().add(level1);
        Object[][] level2 = new Object[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 0, 1, 1, 1, 1, 0, 0, 1},
                {1, 1, 0, 1, 0, 0, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 0, 1, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
        };
        getLevels().add(level2);
        getPlayer().setCurrentLevel(0);
        //floor
        getTextureUls().add("textures/block/black_wool.png");
        getTextureUls().add("textures/block/acacia_planks.png");
        getTextureUls().add("textures/block/bricks.png");
        getTextureUls().add("textures/block/cobblestone.png");
        getTextureUls().add("textures/block/concrete_powder/blue_concrete_powder.png");
        getTextureUls().add("textures/block/water_still.png");
        getTextureUls().add("textures/block/clay.png");
        getTextureUls().add("textures/block/piston_inner.png");
    }

}