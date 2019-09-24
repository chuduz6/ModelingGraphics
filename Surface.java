/**
 * Base class for surface.
 * @author fabio
 */
public abstract class Surface extends HierarchyNode {
    /**
     * Material.
     */
    public Material             material;
	/**
	 * Tesselate geometry
	 */
	public Mesh                tesselatedMesh;
    /**
     * Flattened tesselated geometry
     */
    public Mesh                flattenedTesselatedMesh;

    /**
     * @see HierarchyNode#tesselate(int)
     */
    public abstract void tesselate(int tesselationLevel);   

    /**
     * @see HierarchyNode#flatten(Mat4)
     */
    public void flatten(Mat4 transform) {
        throw new NotImplementedException();
    }
}
