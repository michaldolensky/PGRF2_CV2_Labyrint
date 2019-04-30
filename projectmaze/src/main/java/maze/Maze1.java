package maze;

import blocks.AbstractBlock;
import blocks.Hall;
import blocks.Teleporter;
import blocks.Wall;
import transforms.Point3D;

public class Maze1 extends AbstractMaze {

    public Maze1() {
        setStartPosition(new Point3D(1, 0, 1));
        getPlayer().setPos(calcPos(getStartPosition()));
        AbstractBlock[][] level0 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Teleporter(1, 1, 1), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Wall(), new Hall(), new Wall(), new Wall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level0);
        AbstractBlock[][] level1 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Hall(), new Teleporter(1, 0, 1), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Teleporter(1, 2, 1), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Hall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level1);
        AbstractBlock[][] level2 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Hall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Teleporter(1, 0, 1), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Hall(), new Wall(), new Wall(), new Wall(), new Wall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Hall(), new Wall(), new Hall(), new Hall(), new Wall(), new Hall(), new Wall(), new Wall()},
                {new Wall(), new Wall(), new Hall(), new Wall(), new Hall(), new Hall(), new Wall(), new Hall(), new Hall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Hall(), new Hall(), new Hall()},
                {new Wall(), new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Hall(), new Hall(), new Hall(), new Hall(), new Hall()},
        };
        getLevels().add(level2);
        getPlayer().setCurrentLevel(0);
        //floor
        getTextureUls().add("textures/block/anvil.png");//0
        getTextureUls().add("textures/block/acacia_planks.png");//1
        getTextureUls().add("textures/block/bricks.png");//2
        getTextureUls().add("textures/block/piston_side.png");//3
        getTextureUls().add("textures/block/concrete_powder/blue_concrete_powder.png");//4
        getTextureUls().add("textures/block/water_still.png");//5
        getTextureUls().add("textures/block/clay.png");//6
        getTextureUls().add("textures/block/piston_inner.png");//7
        getTextureUls().add("textures/block/crafting_table_top.png");//8
    }

}