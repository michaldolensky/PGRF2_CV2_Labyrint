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

    public void setTEXNESWUD(int texN, int texE, int texS, int texW, int texU, int texD) {
        this.texN = texN;
        this.texE = texE;
        this.texS = texS;
        this.texW = texW;
        this.texU = texU;
        this.texD = texD;
    }

    public int getTexRepXY() {
        return texRepeatXY;
    }

    public void setTexRepXY(int texRepeatXY) {
        this.texRepeatXY = texRepeatXY;
    }

    public void setTexN(int texN) {
        this.texN = texN;
    }

    public void setTexE(int texE) {
        this.texE = texE;
    }

    public void setTexS(int texS) {
        this.texS = texS;
    }

    public void setTexW(int texW) {
        this.texW = texW;
    }

    public void setTexU(int texU) {
        this.texU = texU;
    }

    public void setTexD(int texD) {
        this.texD = texD;
    }
}
