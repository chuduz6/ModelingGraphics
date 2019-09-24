/**
 * Base class for trasnformations in a hierarchy.
 * @author fabio
 */
public class Transform extends HierarchyNode {
    /**
     * Children of the transform node.
     */
    public HierarchyNode[] children;
    
    /**
     * Translation
     */
    public Vec3 translation;
    
    /**
     * Rotation
     */
    public Vec3 rotation;
    
    /**
     * Scale
     */
    public Vec3 scale;
    
    /**
     * Empty transform
     */
    public Transform() {
        children = new HierarchyNode[0];
        translation = new Vec3(0,0,0);
        rotation = new Vec3(0,0,0);
        scale = new Vec3(1,1,1);
    }
    
    /**
     * Tesselate all children.
     */
    public void tesselate(int tesselationLevel) {
        throw new NotImplementedException();
    }
    
    /**
     * Flattens the hierarchy.
     * The previous transform and its inverse-transpose are passed as arguments.
     * @see Scene#flatten()
     */
    public void flatten(Mat4 transform) {
        throw new NotImplementedException();
    }
}
