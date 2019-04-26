package blocks;

import com.jogamp.opengl.util.texture.Texture;

public class AbstractBlock{
    Texture texTop;
    boolean collision = false;


    public boolean hasCollision() {
        return collision;
    }


}
