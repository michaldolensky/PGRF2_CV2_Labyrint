package blocks;

import transforms.Point3D;

public class Teleporter extends AbstractBlock{
    private Point3D teleportTo;

    public Teleporter(int z, int y, int x) {
        createWall = true;
        this.teleportTo = new Point3D(x, y, z);
    }

    public Point3D getTeleportTo() {
        return teleportTo;
    }
}
