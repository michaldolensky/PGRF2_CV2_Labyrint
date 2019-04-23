package maze;

import transforms.Point3D;

public class Maze1 extends AbstractMaze {

    public Maze1() {
        setStartPosition(new Point3D(1, 0, 1));
        getPlayer().setPos(calcPos(getStartPosition()));
        int[][] level0 = new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 3, 1, 1, 1, 1, 1, 1, 0, 1},
                {1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
                {1, 0, 1, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 1, 0, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        };
        getLevels().add(level0);
        int[][] level1 = new int[][]{
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
        int[][] level2 = new int[][]{
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