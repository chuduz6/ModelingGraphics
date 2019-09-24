/**
 * Base class for node in the hierarchy.
 * @author fabio
 */
public abstract class HierarchyNode {
    
    /**
     * Tesselate surfaces.
     * For each surface, results should be stored in the 
     * tesselatedMesh field.
     */
    public abstract void tesselate(int tesselationLevel);   

    /**
     * Flatten the hierarchy by transforming vertices in the
     * tesselatedMesh of each surface and store the result 
     * in the flattened tesselatedMesh field.
     */
    public abstract void flatten(Mat4 transform);
}
