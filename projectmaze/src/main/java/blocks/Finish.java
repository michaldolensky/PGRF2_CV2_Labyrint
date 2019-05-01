package blocks;

public class Finish extends Hall {

    public Finish() {
        texRepeatXY = 1;
        this.texU = 101;
        this.texD = 102;
        this.texE = 103;
        this.texN = 104;
        this.texW = 105;
        this.texS = 106;
    }

    public Finish(int texN, int texE, int texS, int texW, int texU, int texD) {
        super(texN, texE, texS, texW, texU, texD);
    }
}
