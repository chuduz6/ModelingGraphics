/**
 * Base class for all 3d parametric splines.
 * @author fabio
 */
public abstract class Spline {
    /** 
     * Evaluates the spline uniformly for point positions.
     */
    public abstract Vec3[] computePositions(int tesselationLevel);

    /** 
     * Evaluates the spline uniformly for point tangents.
     */
    public abstract Vec3[] computeTangents(int tesselationLevel);   
}
