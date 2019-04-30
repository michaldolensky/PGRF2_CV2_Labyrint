package blocks;

import com.jogamp.opengl.util.texture.Texture;

public class AbstractBlock{
    int texN;
    int texE;
    int texS;
    int texW;
    int texU;
    int texD;
    boolean collision = false;
    boolean createWall = false;


    public boolean hasCollision() {
        return collision;
    }

    public boolean isCreateWall() {
        return createWall;
    }

    public int getTexN() {
        return texN;
    }

    public int getTexE() {
        return texE;
    }

    public int getTexS() {
        return texS;
    }

    public int getTexW() {
        return texW;
    }

    public int getTexU() {
        return texU;
    }

    public int getTexD() {
        return texD;
    }
}
