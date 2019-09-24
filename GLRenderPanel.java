import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

/**
 * Provides rendering through OpenGL.
 * Uses jogl as the GL layer.
 * @author fabio
 */
public class GLRenderPanel extends JPanel implements GLEventListener {
    /** serialID */
    private static final long serialVersionUID = 132277839319035748L;
    
    /** view mode string used for ui */
    public static final String[] DRAWMODE_TABLE = { "solid", "line", "point"  };
    /** view mode solid */
    public static final int DRAWMODE_SOLID = 0;
    /** view mode line */
    public static final int DRAWMODE_LINE = 1;
    /** view mode point */
    public static final int DRAWMODE_POINT = 2;
    
    /** view mode string used for ui */
    public static final String[] LIGHTINGMODE_TABLE = { "none", "flat", "smooth"  };
    /** view mode solid */
    public static final int LIGHTINGMODE_NONE = 0;
    /** view mode line */
    public static final int LIGHTINGMODE_FLAT = 1;
    /** view mode point */
    public static final int LIGHTINGMODE_SMOOTH = 2;
    
    /** GLU object */
    private GLU glu;
    
	/**
	 * Scene
	 */
	protected Scene scene;
	
	/**
	 * GL drawing canvas
	 */
	protected GLCanvas canvas;
    
    /**
     * View mode
     */
    protected int viewMode;
    
    /**
     * Remove backfaces
     */
    protected boolean viewCull;
    
    /**
     * Orthographic camera
     */
    protected boolean viewPerspective;
	
    /**
     * Lighting mode
     */
    protected int viewLighting;
    
    /**
     * Use flattened hierarchy
     */
    protected boolean hierarchyFlattening;
    
	/**
	 * Default constructor
	 */
	public GLRenderPanel(Scene nScene) {
		scene = nScene;
        viewMode = DRAWMODE_SOLID;
        viewCull = true;
        viewPerspective = true;
        viewLighting = LIGHTINGMODE_SMOOTH;
        hierarchyFlattening = false;
		
		// Window size
		setSize(scene.camera.xResolution, scene.camera.yResolution);
		setPreferredSize(new Dimension(scene.camera.xResolution, scene.camera.yResolution));
		
		// Canvas
		GLCapabilities caps = new GLCapabilities();
		caps.setDoubleBuffered(true);
		canvas = new GLCanvas(caps);
		
		// Add canvas
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		
		// Link this object as a listener for the canvas
		canvas.addGLEventListener(this);
	}
	
	/**
	 * Set scene
	 */
	public void setScene(Scene nScene) {
		scene = nScene;
		redraw();
	}
    
    /**
     * Set view mode
     */
    public void setViewMode(int nViewMode) {
        viewMode = nViewMode;
        redraw();
    }
    
    /**
     * Set view cull
     */
    public void setViewCull(boolean nViewCull) {
        viewCull = nViewCull;
        redraw();
    }
	
    /**
     * Set view perspective
     */
    public void setViewPerspective(boolean nViewPerspective) {
        viewPerspective = nViewPerspective;
        redraw();
    }
    
    /**
     * Set view lighting
     */
    public void setViewLighting(int nViewLighting) {
        viewLighting = nViewLighting;
        redraw();
    }
    
    /**
     * Set hierarchy flattening
     */
    public void setHierarchyFlattening(boolean nHierarchyFlattening) {
        hierarchyFlattening = nHierarchyFlattening;
        redraw();
    }
    
	/**
	 * Redraw the view. Should be called after a change occurs in the scene.
	 */
	public void redraw() {
		canvas.repaint();
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
	}
	
	/**
	 * GL init code. Anything that will happen only ones should go here.
	 */
	public void init(GLAutoDrawable glD) {
		// get GL
		GL gl = glD.getGL();
		
		// set up GL flags
        
        // culling
        gl.glCullFace(GL.GL_BACK);
        
        // interpolation
        gl.glShadeModel(GL.GL_SMOOTH);
        
        // normalized normals
        gl.glEnable(GL.GL_NORMALIZE);
        
        // depth test
		gl.glEnable(GL.GL_DEPTH_TEST);
        
        // clear
		gl.glClearColor(0,0,0,0);
		gl.glClearDepth(1);
		
		// init glu
		glu = new GLU();
	}

	/**
	 * draw the current scene using GL
	 */
	public void display(GLAutoDrawable glD) {
		// get GL
		GL gl = glD.getGL();
		
		// clear
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        
        // set display mode
        switch(viewMode) {
            case DRAWMODE_SOLID:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                break;
            case DRAWMODE_LINE:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
                break;
            case DRAWMODE_POINT:
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_POINT);
                break;
            default:                
                gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
                break;
        }
		
        // set culling
        if(viewCull) {
            gl.glEnable(GL.GL_CULL_FACE);
        } else {
            gl.glDisable(GL.GL_CULL_FACE);            
        }
        
        // clear matrices
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // set lights
        drawLights(gl,glu,scene.lights);
        
		// set camera projection
		drawCamera(gl, glu, scene.camera);
        
        // flatten hierarchy if needed
        if(hierarchyFlattening) {
            scene.flatten();
        }
        
		// hierarchically draw using gl
        drawNode(gl, glu, scene.hierarchyRoot);
	}
    
    /**
     * GL view commands.
     */
    protected void drawCamera(GL gl, GLU glu, Camera c) {
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        if(viewPerspective) {
            glu.gluPerspective(2 * scene.camera.yfov * 180 / Math.PI, 
                    scene.camera.xfov/scene.camera.yfov, 0.01, 10000);
        } else {
            double ofov = Math.tan(scene.camera.yfov);
            gl.glOrtho(-2*ofov,2*ofov,-2*ofov,2*ofov,0.01,10000);
        }
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        glu.gluLookAt(c.origin.x,
                      c.origin.y,
                      c.origin.z,
                      c.origin.add(scene.camera.z.negate()).x,
                      c.origin.add(scene.camera.z.negate()).y,
                      c.origin.add(scene.camera.z.negate()).z,
                      c.y.x,
                      c.y.y,
                      c.y.z);        
    }
    
    /**
     * GL lighting command.
     */
    protected void drawLights(GL gl, GLU glu, Light[] lights) {
        if(viewLighting == LIGHTINGMODE_NONE) {
            // disable lighting
            gl.glDisable(GL.GL_LIGHTING);
        } else {
            // enable lighting
            gl.glEnable(GL.GL_LIGHTING);
            
            // set lights
            for(int i = 0; i < Math.min(lights.length,8); i ++) {
                gl.glEnable(GL.GL_LIGHT0+i);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_AMBIENT,
                        new float[]{0,0,0,1},0);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_DIFFUSE,
                        new float[]{(float)lights[i].intensity.r,(float)lights[i].intensity.g,(float)lights[i].intensity.b,1},0);
                gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_SPECULAR,
                       new float[]{(float)lights[i].intensity.r,(float)lights[i].intensity.g,(float)lights[i].intensity.b,1},0);
                if(lights[i] instanceof PointLight) {
                    gl.glLightfv(GL.GL_LIGHT0+i,GL.GL_POSITION,
                           new float[]{(float)((PointLight)lights[i]).position.x,
                                       (float)((PointLight)lights[i]).position.y,
                                       (float)((PointLight)lights[i]).position.z,1},0);
                }
            }
        }
    }
    
    /**
     * GL hierarchy commands.
     */
    protected void drawNode(GL gl, GLU glu, HierarchyNode n) {
        if(n instanceof Transform) {
            Transform t = (Transform)n;
            gl.glMatrixMode(GL.GL_MODELVIEW);
            gl.glPushMatrix();
            if(!hierarchyFlattening) {
                gl.glTranslated(t.translation.x, t.translation.y, t.translation.z);
                gl.glScaled(t.scale.x, t.scale.y, t.scale.z);
                gl.glRotated(t.rotation.x * 180 / Math.PI, 1, 0, 0);
                gl.glRotated(t.rotation.y * 180 / Math.PI, 0, 1, 0);
                gl.glRotated(t.rotation.z * 180 / Math.PI, 0, 0, 1);
            }
            for(int i = 0; i < t.children.length; i ++) {
                drawNode(gl,glu,t.children[i]);
            }
            gl.glPopMatrix();
        } else if(n instanceof Surface) {
            Mesh m;
            if(!hierarchyFlattening) {
                m = ((Surface)n).tesselatedMesh;
            } else {
                m = ((Surface)n).flattenedTesselatedMesh;
            }
            drawMaterial(gl,glu,((Surface)n).material);
            boolean useFaceNormal;
            if(viewLighting != LIGHTINGMODE_SMOOTH || m.vertexNormal == null) {
                useFaceNormal = true;
            } else {
                useFaceNormal = false;
            }
            for(int f = 0; f < m.getNumFaces(); f ++) {
                if(m.quads) {
                    gl.glBegin(GL.GL_QUADS);                                    
                } else {
                    gl.glBegin(GL.GL_TRIANGLES);
                }
                if(useFaceNormal) {
                    Vec3 v0 = m.vertexPos[m.getFaceVertexIndex(f,1)].sub(m.vertexPos[m.getFaceVertexIndex(f,0)]);
                    Vec3 v1 = m.vertexPos[m.getFaceVertexIndex(f,2)].sub(m.vertexPos[m.getFaceVertexIndex(f,0)]);
                    Vec3 faceN = v0.cross(v1).normalize();
                    gl.glNormal3d(faceN.x,faceN.y,faceN.z);
                }
                for(int v = 0; v < ((m.quads)?4:3); v ++) {
                    if(!useFaceNormal) {
                        Vec3 N = m.vertexNormal[m.getFaceVertexIndex(f, v)];
                        gl.glNormal3d(N.x,N.y,N.z);
                    }
                    Vec3 P = m.vertexPos[m.getFaceVertexIndex(f, v)];
                    gl.glVertex3d(P.x,P.y,P.z);
                }
                gl.glEnd();
            }
        } else {
            System.out.println("Should never have gotten here!");
        }
    }
    
    /**
     * GL material and color commands
     */
    public void drawMaterial(GL gl, GLU glu, Material m) {
        Phong p = m.getApproximatePhong();
        gl.glColor3d(p.diffuse.r, p.diffuse.g, p.diffuse.b);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_AMBIENT_AND_DIFFUSE, 
                new float[]{(float)p.diffuse.r, (float)p.diffuse.g, (float)p.diffuse.b, 0},0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SPECULAR, 
                new float[]{(float)p.specular.r, (float)p.specular.g, (float)p.specular.b, 0},0);
        gl.glMaterialfv(GL.GL_FRONT_AND_BACK,GL.GL_SHININESS, 
                new float[]{(float)p.exponent},0);
    }

}