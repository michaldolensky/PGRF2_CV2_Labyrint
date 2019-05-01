package blocks;

public class Finish extends Hall {

    public Finish() {
        texRepeatXY = 1;
        this.texU = 98;
        this.texD = 99;
        this.texE = 99;
        this.texN = 98;
        this.texW = 99;
        this.texS = 99;
    }

    public Finish(int texN, int texE, int texS, int texW, int texU, int texD) {
        super(texN, texE, texS, texW, texU, texD);
    }
}
