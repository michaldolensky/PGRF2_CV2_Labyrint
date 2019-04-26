package blocks;

import transforms.Point3D;

public class Teleporter extends AbstractBlock{
    private Point3D teleportTo;

    public Teleporter(Point3D teleportTo) {
        this.teleportTo = teleportTo;
    }

    public Point3D getTeleportTo() {
        return teleportTo;
    }
}
