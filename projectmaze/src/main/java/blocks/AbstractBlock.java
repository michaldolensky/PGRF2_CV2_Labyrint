package blocks;

import com.jogamp.opengl.util.texture.Texture;

public class AbstractBlock{
    Texture texTop;
    boolean colide = false;


    public boolean isColide() {
        return colide;
    }


}
