/**
 * Class representing a Bezier spline.
 * @author fabio
 */
public class BezierSpline extends Spline {
    /**
     * Bezier control points.
     * Each four control points defines a segment.
     */
	public Vec3[] controlPoints;
	
    /** 
     * Evaluates the spline uniformly for point positions.
     */
	public Vec3[] computePositions(int tesselationLevel) {
        throw new NotImplementedException();
	}
	
    /** 
     * Evaluates the spline uniformly for tangents.
     */
    public Vec3[] computeTangents(int tesselationLevel) {
        throw new NotImplementedException();
    }
    
    /**
     * Compute vertex position for t.
     * t should be in the 0..1 range, not in 0..N as in the notes.
     * This means that we have to pay attention to the number of segments.
     * The code adopts this convention for software engineering reasons.
     */
    public Vec3 computePosition(double t) {
        throw new NotImplementedException();
    }
    
    /**
     * Compute vertex tanget for t.
     * t should be in the 0..1 range, not in 0..N as in the notes.
     * This means that we have to pay attention to the number of segments.
     * The code adopts this convention for software engineering reasons.
     */
    public Vec3 computeTangent(double t) {
        throw new NotImplementedException();
    }
}
