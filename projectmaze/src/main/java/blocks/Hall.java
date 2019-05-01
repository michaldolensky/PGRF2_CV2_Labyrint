package blocks;

public class Hall extends AbstractBlock {

    public Hall() {
        this.texN = 101;
        this.texE = 102;
        this.texS = 103;
        this.texW = 104;
        this.texU = 105;
        this.texD = 106;
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
