package maze;

import blocks.*;
import transforms.Point3D;

public class TeleportMaze extends AbstractMaze {
    public TeleportMaze() {
        setStartPosition(new Point3D(1, 0, 1));
        getPlayer().setPos(calcPos(getStartPosition()));
        Hall Hall0 = new Hall(1, 2, 1, 2, 3, 0);
        Hall Hall1 = new Hall(5, 5, 5, 5, 6, 4);

        AbstractBlock[][] level0 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), Hall0, Hall0, new Teleport(1, 1, 3), new Wall(), Hall0, Hall0, new Teleport(1, 1, 7), new Wall(), Hall0, new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), Hall0, new Wall(), new Wall(), new Wall(), Hall0, new Wall()},
                {new Wall(), new Teleport(3, 1, 1), new Wall(), Hall0, new Wall(), new Teleport(3, 1, 5), new Wall(), new Teleport(3, 1, 7), new Wall(), new Teleport(9, 1, 3), new Wall()},
                {new Wall(), Hall0, new Wall(), Hall0, new Wall(), Hall0, new Wall(), Hall0, new Wall(), new Wall(), new Wall()},
                {new Wall(), new Teleport(5, 1, 1), new Wall(), Hall0, new Wall(), Hall0, new Wall(), Hall0, Hall0, Hall0, new Wall()},
                {new Wall(), Hall0, new Wall(), Hall0, new Wall(), Hall0, new Wall(), new Wall(), new Wall(), Hall0, new Wall()},
                {new Wall(), new Teleport(7, 1, 1), new Wall(), Hall0, new Wall(), Hall0, Hall0, Hall0, new Wall(), new Teleport(7, 1, 9), new Wall()},
                {new Wall(), Hall0, new Wall(), Hall0, new Wall(), new Wall(), new Wall(), Hall0, new Wall(), new Wall(), new Wall()},
                {new Wall(), Hall0, Hall0, new Teleport(9, 1, 3), new Wall(), Hall0, Hall0, Hall0, new Wall(), new Teleport(9, 1, 9), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},

        };
        getLevels().add(level0);
        AbstractBlock[][] level1 = new AbstractBlock[][]{
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), Hall1, Hall1, new Teleport(1, 0, 3), Hall1, Hall1, new Wall(), new Teleport(1, 0, 7), Hall1, Hall1, new Wall()},
                {new Wall(), new Wall(), new Wall(), Hall1, new Wall(), Hall1, new Wall(), new Wall(), new Wall(), Hall1, new Wall()},
                {new Wall(), new Teleport(3, 0, 1), Hall1, Hall1, new Wall(), new Teleport(3, 0, 5), new Wall(), new Teleport(3, 0, 7), Hall1, new Teleport(3, 0, 9), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), Hall1, new Wall()},
                {new Wall(), new Teleport(5, 0, 1), Hall1, Hall1, new Wall(), Hall1, new Wall(), Hall1, Hall1, Hall1, new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), Hall1, new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), new Teleport(7, 0, 1), new Wall(), Hall1, new Wall(), Hall1, Hall1, Hall1, Hall1, new Teleport(7, 0, 9), new Wall()},
                {new Wall(), Hall1, new Wall(), Hall1, new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
                {new Wall(), Hall1, new Wall(), new Teleport(9, 0, 3), Hall1, Hall1, Hall1, Hall1, new Finish(), new Teleport(9, 0, 9), new Wall()},
                {new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall(), new Wall()},
        };
        getLevels().add(level1);
        getPlayer().setCurrentLevel(0);
        //floor
        getTextureUls().put(0, "textures/block/stripped_jungle_log.png");
        getTextureUls().put(1, "textures/block/concrete/light_blue_concrete.png");
        getTextureUls().put(2, "textures/block/concrete/orange_concrete1.png");
        getTextureUls().put(3, "textures/block/dark_prismarine.png");

        getTextureUls().put(4, "textures/block/stripped_spruce_log.png");
        getTextureUls().put(5, "textures/block/stone_bricks.png");
        getTextureUls().put(6, "textures/block/white_wool.png");
    }
}
