package maze;

import blocks.AbstractBlock;
import blocks.Teleport;
import player.Player;
import transforms.Point2D;
import transforms.Point3D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AbstractMaze implements Serializable {

    private final static int COLLISION_SIZE = 5;
    public final static Point3D PLAYER_OFFSET = new Point3D(0.5, 0.5, 0.5);
    private final int squareSize = 40;
    private final int heightBetweenLevels = 100;
    private Point3D startPosition;
    private final List<AbstractBlock[][]> levels = new ArrayList<>();
    private final HashMap<Integer, String> textureUls = new HashMap<>();
    private final Player player = new Player();

    AbstractMaze() {
        //Finish
        getTextureUls().put(98, "textures/block/lime_shulker_box.png");
        getTextureUls().put(99, "textures/block/lime_shulker_box_side.png");

        //Default Hall Texture
        getTextureUls().put(101, "textures/block/cracked_stone_bricks.png");//texN
        getTextureUls().put(102, "textures/block/cracked_stone_bricks.png");//texE
        getTextureUls().put(103, "textures/block/cracked_stone_bricks.png");//texS
        getTextureUls().put(104, "textures/block/cracked_stone_bricks.png");//texW
        getTextureUls().put(105, "textures/block/light_blue_stained_glass.png");//texU
        getTextureUls().put(106, "textures/block/brown_terracotta.png");//texD

        //Teleport
        getTextureUls().put(111, "textures/block/piston_bottom.png");//texN
        getTextureUls().put(112, "textures/block/piston_bottom.png");//texE
        getTextureUls().put(113, "textures/block/piston_bottom.png");//texS
        getTextureUls().put(114, "textures/block/piston_bottom.png");//texW
        getTextureUls().put(115, "textures/block/piston_top.png");//texU
        getTextureUls().put(116, "textures/block/piston_inner.png");//texD
    }

    public void detectCollision(Point2D p) {
        double x = p.getX();
        double z = p.getY();
        double posX = x / squareSize;
        double posZ = z / squareSize;
        double curPosX = player.getPos().getX() / squareSize;
        double curPosZ = player.getPos().getZ() / squareSize;

        if (posZ - curPosZ > 0) {
            posZ = (z + COLLISION_SIZE) / squareSize;
        } else posZ = (z - COLLISION_SIZE) / squareSize;
        if (levels.get(player.getCurLev())[(int) posZ][(int) posX].isWalkable()) player.getPos().setZ(z);

        posZ = z / squareSize;
        if (posX - curPosX > 0) {
            posX = (x + COLLISION_SIZE) / squareSize;
        } else posX = (x - COLLISION_SIZE) / squareSize;
        if (levels.get(player.getCurLev())[(int) posZ][(int) posX].isWalkable()) player.getPos().setX(x);


        System.out.println("-----");
        System.out.println("posX - curPosX = " + (posX - curPosX));
        System.out.println("posZ - curPosZ = " + (posZ - curPosZ));
        System.out.println("posX = " + posX);
        System.out.println("posZ = " + posZ);
        System.out.println("curPosX = " + curPosX);
        System.out.println("curPosZ = " + curPosZ);
        System.out.println("curMaze.getLevels().get(0)[posZ][posX] = " + levels.get(0)[(int) posZ][(int) posX].getClass().getName());
        System.out.println("player.getCurLev() = " + player.getCurLev());
    }

    /**
     * @return Returns Block at player if is inside the maze, else returns null
     */
    public AbstractBlock getCurrentBlockAtPlayerLocation() {
        if (player.getPX() < 0 || player.getPZ() < 0 || player.getPX() > levels.get(player.getCurLev()).length * squareSize || player.getPZ() > levels.get(player.getCurLev()).length * squareSize)
            return null;
        else
            return levels.get(player.getCurLev())[(int) player.getPZ() / squareSize][(int) player.getPX() / squareSize];
    }

    /**
     * Resets Player to start Position
     */
    public void resetPlayer() {
        player.setCurrentLevel((int) getStartPosition().getY());

        player.setPos(calcPos(getStartPosition()));
    }

    private void movePlayer(Point3D pos) {
        player.setCurrentLevel((int) pos.getY());
        player.setPos(calcPos(pos));
    }

    Point3D calcPos(Point3D pos) {
        double px = squareSize * pos.getX() + PLAYER_OFFSET.getX() * squareSize;
        double py = (int) pos.getY() * heightBetweenLevels + PLAYER_OFFSET.getY() * squareSize;
        double pz = squareSize * pos.getZ() + PLAYER_OFFSET.getZ() * squareSize;
        return new Point3D(px, py, pz);
    }


    public List<AbstractBlock[][]> getLevels() {
        return levels;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getHeightBetweenLevels() {
        return heightBetweenLevels;
    }


    public HashMap<Integer, String> getTextureUls() {
        return textureUls;
    }


    Point3D getStartPosition() {
        return startPosition;
    }

    void setStartPosition(Point3D startPosition) {
        this.startPosition = startPosition;
    }

    public Player getPlayer() {
        return player;
    }

    public void checkForTeleport() {
        if (getCurrentBlockAtPlayerLocation() != null) {
            if ((getCurrentBlockAtPlayerLocation() instanceof Teleport)) {
                movePlayer(((Teleport) getCurrentBlockAtPlayerLocation()).getTeleportTo());
            }
        }
    }
}
