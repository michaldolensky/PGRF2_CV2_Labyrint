package blocks;

public class AbstractBlock{
    int texN;
    int texE;
    int texS;
    int texW;
    int texU;
    int texD;
    int texRepeatXY = 1;

    boolean walkable = true;


    public boolean isWalkable() {
        return walkable;
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

    public int getTexRepXY() {
        return texRepeatXY;
    }

}
