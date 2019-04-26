package blocks;

import com.jogamp.opengl.util.texture.Texture;

public class AbstractBlock{
    Texture texTop;
    boolean collision = false;
    boolean createWall = false;


    public boolean hasCollision() {
        return collision;
    }

    public boolean isCreateWall() {
        return createWall;
    }
}
