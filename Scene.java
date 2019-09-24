/**
 * Raytracing scene.
 * @author fabio
 */
public class Scene {
	/**
	 * Camera.
	 */
	public Camera				camera;
	/**
	 * Object surfaces.
	 */
	public Transform		    hierarchyRoot;
	/**
	 * Lights.
	 */
	public Light[]				lights;
	
	/**
	 * Default constructor.
	 * Scene is initialized in the Loader.
	 */
	public Scene() {
		camera = new Camera();
        hierarchyRoot = new Transform();
		lights = new Light[0];
	}
    
    /**
     * Tesselate all object in a scene and store 
     * tesselated surfaces in the surface data structure.
     * @see HierarchyNode#tesselate(int)
     */
    public void tesselate(int level) {
        hierarchyRoot.tesselate(level);
    }

    /**
     * Apply the transformations to all tesselated vertices in the hierarchy
     * such when drawing with these and without any transform, the same picture 
     * gets generated.
     * @see HierarchyNode#flatten()
     */
    public void flatten() {
        Mat4 currentTransform = new Mat4();
        hierarchyRoot.flatten(currentTransform);
    }
}
