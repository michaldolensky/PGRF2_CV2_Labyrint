package transforms;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * 3D point with homogeneous coordinates, immutable
 *
 * @author PGRF FIM UHK
 * @version 2014
 */

public class Point3D {
    private double x, y, z, w;

    /**
     * Creates a homogeneous point representing the origin
     */
    public Point3D() {
        x = y = z = 0.0;
        w = 1.0;
    }

    /**
     * Creates a homogeneous point representing a 3D point with the three given
     * affine coordinates
     *
     * @param x affine x coordinate
     * @param y affine y coordinate
     * @param z affine z coordinate
     */
    public Point3D(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1.0;
    }

    /**
     * Creates a point with the given homogeneous coordinates
     *
     * @param x homogeneous x coordinate
     * @param y homogeneous y coordinate
     * @param z homogeneous z coordinate
     * @param w homogeneous w coordinate
     */
    private Point3D(final double x, final double y, final double z, final double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Creates a homogeneous point representing a 3D affine point defined by the
     * given vector from origin
     *
     * @param v affine coordinates vector (vector from origin to the point)
     */
    @SuppressWarnings("unused")
    public Point3D(final Vec3D v) {
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
        this.w = 1.0;
    }

    /**
     * Creates a point by cloning the give one
     *
     * @param p homogeneous point to be cloned
     */
    @SuppressWarnings("unused")
    public Point3D(final Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.w = p.w;
    }

    /**
     * Creates a point by attaching the given z coordinate to the given 2D
     * homogeneous point coordinates
     *
     * @param p homogeneous 2D point whose x,y,w will be cloned
     * @param z z coordinate to be attached
     */
    @SuppressWarnings("unused")
    public Point3D(final Point2D p, final double z) {
        x = p.getX();
        y = p.getY();
        this.z = z;
        w = p.getW();
    }

    /**
     * Creates a point by extracting homogeneous coordinates from the given
     * array of doubles
     *
     * @param array double array of size 4 (asserted)
     */
    @SuppressWarnings("unused")
    public Point3D(final double[] array) {
        assert (array.length >= 4);
        x = array[0];
        y = array[1];
        z = array[2];
        w = array[3];
    }


    /**
     * Returns the homogeneous x coordinate
     *
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the homogeneous y coordinate
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the homogeneous z coordinate
     *
     * @return the z
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns the homogeneous w coordinate
     *
     * @return the w
     */
    public double getW() {
        return w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns a clone of this point with the homogeneous x coordinate replaced by the
     * given value
     *
     * @param x homogeneous x coordinate
     * @return new Point3D instance
     */
    @SuppressWarnings("unused")
    public Point3D withX(double x) {
        return new Point3D(x, this.getY(), this.getZ(), this.getW());
    }

    /**
     * Returns a clone of this point with the homogeneous y coordinate replaced by the
     * given value
     *
     * @param y homogeneous y coordinate
     * @return new Point3D instance
     */
    @SuppressWarnings("unused")
    public Point3D withY(double y) {
        return new Point3D(this.getX(), y, this.getZ(), this.getW());
    }

    /**
     * Returns a clone of this point with the homogeneous z coordinate replaced by the
     * given value
     *
     * @param z homogeneous z coordinate
     * @return new Point3D instance
     */
    @SuppressWarnings("unused")
    public Point3D withZ(double z) {
        return new Point3D(this.getX(), this.getY(), z, this.getW());
    }

    /**
     * Returns a clone of this point with the homogeneous w coordinate replaced by the
     * given value
     *
     * @param w homogeneous w coordinate
     * @return new Point3D instance
     */
    @SuppressWarnings("unused")
    public Point3D withW(double w) {
        return new Point3D(this.getX(), this.getY(), this.getZ(), w);
    }


    /**
     * Returns the result of element-wise summation with the given homogeneous
     * 3D point
     *
     * @param p homogeneous 3D point to sum
     * @return new Point3D instance
     */
    public void add(final Point3D p) {
        this.x += p.getX();
        this.y += p.getY();
        this.z += p.getZ();
        this.w += p.getW();
    }

    public void addX(final double px) {
        this.x += px;
    }

    @SuppressWarnings("unused")
    public void addY(final double py) {
        this.y += py;
    }

    public void addZ(final double pz) {
        this.z += pz;
    }


    public void sub(final Point3D p) {
        this.x -= p.getX();
        this.y -= p.getY();
        this.z -= p.getZ();
        this.w -= p.getW();
    }

    public void subX(final double px) {
        this.x -= px;
    }

    @SuppressWarnings("unused")
    public void subY(final double py) {
        this.y -= py;
    }

    public void subZ(final double pz) {
        this.z -= pz;
    }

    /**
     * Returns the result of point dehomogenization, i.e. the affine point
     * coordinates = x,y,z divided by w, in the form of a vector from origin to
     * the affine point if possible (the point is not in infinity), empty
     * Optional otherwise
     *
     * @return new Optional<Vec3D> instance
     */
    @SuppressWarnings("unused")
    public Optional<Vec3D> dehomog() {
        if (w == 0.0)
            return Optional.empty();
        return Optional.of(new Vec3D(x / w, y / w, z / w));
    }

    /**
     * Converts the homogeneous 3D point to 3D vector by ignoring the fourth
     * homogeneous coordinate w
     *
     * @return new Vec3D instance
     */
    @SuppressWarnings("unused")
    public Vec3D ignoreW() {
        return new Vec3D(x, y, z);
    }

    /**
     * Compares this object against the specified object.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the objects are the same; {@code false}
     * otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return (this == obj) || (obj instanceof Point3D)
                && (new Double(((Point3D) obj).getX()).equals(getX()))
                && (new Double(((Point3D) obj).getY()).equals(getY()))
                && (new Double(((Point3D) obj).getZ()).equals(getZ()))
                && (new Double(((Point3D) obj).getW()).equals(getW()));
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getX(), this.getY(), this.getZ(), this.getW());
    }

    /**
     * Compares this Point3D against the specified Point3D.
     *
     * @param point   the point to compare with.
     * @param epsilon the maximum epsilon between actual and specified value for
     *                which both numbers are still considered equal
     * @return {@code true} if the objects are considered equal; {@code false}
     * otherwise.
     */
    @SuppressWarnings("SameParameterValue")
    private boolean eEquals(Point3D point, double epsilon) {
        return (this == point) || (point != null)
                && Compare.eEquals(getX(), point.getX(), epsilon)
                && Compare.eEquals(getY(), point.getY(), epsilon)
                && Compare.eEquals(getZ(), point.getZ(), epsilon)
                && Compare.eEquals(getW(), point.getW(), epsilon);
    }

    /**
     * Compares this Point3D against the specified Point3D.
     *
     * @param point the point to compare with.
     * @return {@code true} if the objects are considered equal; {@code false}
     * otherwise.
     */
    @SuppressWarnings("unused")
    public boolean eEquals(Point3D point) {
        return eEquals(point, Compare.EPSILON);
    }

    /**
     * Returns String representation of this point
     *
     * @return comma separated floating-point values in parentheses
     */
    @Override
    public String toString() {
        return toString("%4.1f");
    }

    /**
     * Returns String representation of this point with coordinates formatted
     * according to the given format, see
     * {@link java.lang.String#format(String, Object...)}
     *
     * @param format String format applied to each coordinate
     * @return comma separated floating-point values in parentheses, useful in constructor
     */
    @SuppressWarnings("SameParameterValue")
    private String toString(final String format) {
        return String.format(Locale.US, "(" + format + "," + format + "," + format + "," + format + ")", x, y, z, w);
    }
}