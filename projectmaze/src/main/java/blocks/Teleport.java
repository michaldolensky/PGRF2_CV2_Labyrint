package blocks;

import transforms.Point3D;

public class Teleport extends Hall{
    private final Point3D teleportTo;

    public Teleport(int z, int y, int x) {
        this.teleportTo = new Point3D(x, y, z);
        this.texN = 111;
        this.texE = 112;
        this.texS = 113;
        this.texW = 114;
        this.texU = 115;
        this.texD = 116;
    }

    public Point3D getTeleportTo() {
        return teleportTo;
    }
}
