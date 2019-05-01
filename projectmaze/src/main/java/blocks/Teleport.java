package blocks;

import transforms.Point3D;

public class Teleport extends Hall{
    private final Point3D teleportTo;

    public Teleport(int z, int y, int x) {
        this.teleportTo = new Point3D(x, y, z);
        this.texD = 7;
        this.texU = 7;
    }

    public Point3D getTeleportTo() {
        return teleportTo;
    }
}
