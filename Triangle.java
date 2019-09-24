/**
 * Triangle.
 * @author fabio
 */
public class Triangle extends Surface {
	/**
	 * Vertex 0.
	 */
	public Vec3					v0;
	/**
	 * Vertex 1.
	 */
	public Vec3					v1;
	/**
	 * Vertex 2.
	 */
	public Vec3					v2;
	/**
	 * Normal.
	 */
	public Vec3					normal;
	
	/**
	 * Tesselate surface
	 */
	public void tesselate(int tesselationLevel) {
        // Initialize by copy
		tesselatedMesh = new Mesh(3,1,false);
        
        tesselatedMesh.vertexPos[0].set(v0);
        tesselatedMesh.vertexNormal[0].set(normal);
        tesselatedMesh.vertexPos[1].set(v1);
        tesselatedMesh.vertexNormal[1].set(normal);
        tesselatedMesh.vertexPos[2].set(v2);
        tesselatedMesh.vertexNormal[2].set(normal);
        tesselatedMesh.setTriangleFace(0,0,1,2);        
	}

    /**
     * Set the normal as orthogonal to the triangle plane.
     * Use by the parser.
     */
    public void initFromParser() {
        normal = (v1.sub(v0)).cross(v2.sub(v0)).normalize();
    }
    
}
