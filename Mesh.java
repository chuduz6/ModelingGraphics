/**
 * A collection of triangles or quads. This data structure is the simplest mesh
 * representation and is suitable for efficient drawing. Mesh operation often
 * require better data structure for efficiency.
 * 
 * @author fabio
 * 
 */
public class Mesh extends Surface {
    /**
     * Vertex positions
     */
    public Vec3[] vertexPos;

    /**
     * Vertex normals - might be null, in which case face normals are used
     */
    public Vec3[] vertexNormal;

    /**
     * Faces indices pointing to the vertex ones
     */
    public int[] faceIds;

    /**
     * Quads faces - otherwise triangles
     */
    public boolean quads;

    /**
     * Create an empty mesh
     */
    public Mesh() {
    }

    /**
     * Create a mesh of nV vertices and nF faces, either quads or triangles
     */
    public Mesh(int nV, int nF, boolean quads) {
        allocate(nV, nF, quads);
    }

    /**
     * Create a mesh of nV vertices and nF faces, either quads or triangles
     */
    protected void allocate(int nV, int nF, boolean quads) {
        this.quads = quads;
        vertexPos = new Vec3[nV];
        vertexNormal = new Vec3[nV];
        for (int i = 0; i < nV; i++) {
            vertexPos[i] = new Vec3();
            vertexNormal[i] = new Vec3();
        }
        faceIds = new int[nF * verticesPerFace()];
    }

    /**
     * Make a copy. Not robust! Assumes the mesh is contructed through the above
     * contructor and only copies vertex list in the faces.
     */
    public Mesh copy() {
        if (faceIds.length == 0) {
            return new Mesh(3, 0, false);
        }

        Mesh m = new Mesh(getNumVertices(), faceIds.length, quads);

        for (int v = 0; v < getNumVertices(); v++) {
            m.vertexPos[v] = new Vec3(vertexPos[v]);
            m.vertexNormal[v] = new Vec3(vertexNormal[v]);
        }
        m.faceIds = (int[]) faceIds.clone();
        return m;
    }

    /**
     * Set the given face with the proper vertex indices. The face is a number
     * from 0 to number of faces - 1. This function allows to forget about the
     * packing rule and should be used to create new triangle faces.
     */
    public void setTriangleFace(int face, int v0, int v1, int v2) {
        if (quads) {
            throw new Error("Cannot create a triangle face in a quad mesh");
        }

        faceIds[face * verticesPerFace() + 0] = v0;
        faceIds[face * verticesPerFace() + 1] = v1;
        faceIds[face * verticesPerFace() + 2] = v2;
    }

    /**
     * Set the given face with the proper vertex indices. The face is a number
     * from 0 to number of faces - 1. This function allows to forget about the
     * packing rule and should be used to create new quad faces.
     */
    public void setQuadFace(int face, int v0, int v1, int v2, int v3) {
        if (!quads) {
            throw new Error("Cannot create a quad face in a triangle mesh");
        }

        faceIds[face * verticesPerFace() + 0] = v0;
        faceIds[face * verticesPerFace() + 1] = v1;
        faceIds[face * verticesPerFace() + 2] = v2;
        faceIds[face * verticesPerFace() + 3] = v3;
    }

    /**
     * Retrieve the vertex associated with the given face. This function should
     * be used to indiex vertices from faces since it allows to forget about
     * packing.
     */
    public int getFaceVertexIndex(int face, int vertex) {
        return faceIds[face * verticesPerFace() + vertex];
    }

    /**
     * Returns the number of vertices per face.
     */
    public int verticesPerFace() {
        if (quads)
            return 4;
        return 3;
    }

    /**
     * Returns the number of faces
     */
    public int getNumFaces() {
        return faceIds.length / verticesPerFace();
    }

    /**
     * Returns the number of vertices
     */
    public int getNumVertices() {
        return vertexPos.length;
    }

    /**
     * Tesselate.
     */
    public void tesselate(int tesselationLevel) {
        throw new NotImplementedException();
    }
    
    public void initFromParser() {
        if(vertexNormal != null) {
            for(int i = 0; i < vertexNormal.length; i ++) {
                vertexNormal[i].setToNormalize();
            }
        }
    }    
}
