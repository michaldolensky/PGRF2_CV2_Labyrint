package maze;

import blocks.AbstractBlock;
import blocks.Hall;
import blocks.Teleporter;
import blocks.Wall;
import transforms.Point3D;

public class CollisionTestMaze extends AbstractMaze {
    AbstractBlock[][] level0 = new AbstractBlock[][]{
            {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
            {new Wall(), new Hall(), new Teleporter(2, 0, 1), new Hall(), new Wall()},
            {new Wall(), new Hall(), new Wall(), new Hall(), new Wall()},
            {new Wall(), new Hall(), new Hall(), new Hall(), new Wall()},
            {new Wall(), new Wall(), new Wall(), new Wall(), new Wall()}
    };

    public CollisionTestMaze() {
        setStartPosition(new Point3D(1, 0, 2));
        getPlayer().setPos(calcPos(getStartPosition()));
        getLevels().add(level0);
        getPlayer().setCurrentLevel(0);
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
