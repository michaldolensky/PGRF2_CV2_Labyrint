package blocks;

public class Hall extends AbstractBlock {

    public Hall() {
        this.texN = 1;
        this.texE = 3;
        this.texS = 2;
        this.texW = 4;
        this.texU = 6;
        this.texD = 0;
    }

    public Hall(int texN, int texE, int texS, int texW, int texU, int texD) {
        this.texN = texN;
        this.texE = texE;
        this.texS = texS;
        this.texW = texW;
        this.texU = texU;
        this.texD = texD;
    }
}
