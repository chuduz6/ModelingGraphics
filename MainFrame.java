import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComponent;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Main application frame.
 * @author fabio
 */
public class MainFrame extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
    /** serialID */
    private static final long serialVersionUID = -8076636996215470577L;

    /** max tesselation allowed */
    protected static final int MAX_TESSELATION_LEVEL = 5;
    
	/**
	 * Scene
	 */
	protected Scene scene;
    
    /**
     * Current level of tesselation
     */
    protected int tesselationLevel;
	
	/**
	 * Scene filename.
	 */
	protected String sceneFilename;
	
	/**
	 * Render panel
	 */
	protected GLRenderPanel renderPanel;
    
    /**
     * Draw modes combo box
     */
    protected JComboBox viewModesCombo;
    
    /**
     * View cull combo box
     */
    protected JComboBox viewCullCombo;
    
    /**
     * View cull combo box
     */
    protected JComboBox viewPerspectiveCombo;
    
    /**
     * Lighting enabled
     */
    protected JComboBox viewLightingCombo;
    
    /**
     * Flattening level
     */
    protected JComboBox hierarchyFlatteningCombo;
    
    /**
     * Tesselation level
     */
    protected JComboBox tesselationLevelCombo;
    
    /**
     * UI panel
     */
    protected JPanel uiPanel;
    
    /**
     * Mouse down position - for camera manipulation.
     */
    protected Point mouseBegin;
	
    /**
     * Translation at mouseDown - for camera manipulation.
     */
    protected Vec3 mouseBeginTranslation;
    
    /**
     * Rotation at mouseDown - for camera manipulation.
     */
    protected Vec3 mouseBeginRotation;
    
    /**
     * Scaling at mouseDown - for camera manipulation.
     */
    protected Vec3 mouseBeginScale;
    
	/**
	 * Default constructor
	 */
	public MainFrame(String nFilename) {
        tesselationLevel = 0;
        
		// load scene
		loadScene(nFilename);

		// close by exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// do not rescale
		setResizable(false);
		
		// create GL render window
		renderPanel = new GLRenderPanel(scene);
        renderPanel.canvas.addMouseListener(this);
        renderPanel.canvas.addMouseMotionListener(this);
        
        // create the ui
        initUI();
		
		// add to the main window
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(renderPanel, BorderLayout.CENTER);
        getContentPane().add(uiPanel, BorderLayout.EAST);
		
		// finishing up
		pack();
        
        // init camera manipulation
        mouseBegin = new Point();
        mouseBeginTranslation = new Vec3();
        mouseBeginRotation = new Vec3();
        mouseBeginScale = new Vec3(1,1,1);
	}
    
    /**
     * Creates the ui components
     */
    protected void initUI() {
        // create
        uiPanel = new JPanel();
        viewModesCombo = new JComboBox();
        viewCullCombo = new JComboBox();
        viewPerspectiveCombo = new JComboBox();
        viewLightingCombo = new JComboBox();
        hierarchyFlatteningCombo = new JComboBox();
        tesselationLevelCombo = new JComboBox();
        
        // init controls
        uiPanel.setPreferredSize(new Dimension(250,512));
        uiPanel.setLayout(new java.awt.FlowLayout(FlowLayout.LEFT));
        for(int i = 0; i < GLRenderPanel.DRAWMODE_TABLE.length; i ++) {
            viewModesCombo.addItem(GLRenderPanel.DRAWMODE_TABLE[i]);
        }
        viewModesCombo.setSelectedIndex(renderPanel.viewMode);
        viewModesCombo.addActionListener(this);
        viewCullCombo.addItem("false");
        viewCullCombo.addItem("true");
        viewCullCombo.setSelectedIndex((renderPanel.viewCull)?1:0);
        viewCullCombo.addActionListener(this);
        viewPerspectiveCombo.addItem("false");
        viewPerspectiveCombo.addItem("true");
        viewPerspectiveCombo.setSelectedIndex((renderPanel.viewPerspective)?1:0);
        viewPerspectiveCombo.addActionListener(this);
        for(int i = 0; i < GLRenderPanel.LIGHTINGMODE_TABLE.length; i ++) {
            viewLightingCombo.addItem(GLRenderPanel.LIGHTINGMODE_TABLE[i]);
        }
        viewLightingCombo.setSelectedIndex(renderPanel.viewLighting);
        viewLightingCombo.addActionListener(this);
        for(int i = 0; i < MAX_TESSELATION_LEVEL; i ++) {
            tesselationLevelCombo.addItem(new Integer(i).toString());
        }
        tesselationLevelCombo.setSelectedIndex(tesselationLevel);
        tesselationLevelCombo.addActionListener(this);
        hierarchyFlatteningCombo.addItem("false");
        hierarchyFlatteningCombo.addItem("true");
        hierarchyFlatteningCombo.setSelectedIndex((renderPanel.hierarchyFlattening)?1:0);
        hierarchyFlatteningCombo.addActionListener(this);
        
        // add controls to panels
        uiPanel.add(createLabelledComponent("View mode", viewModesCombo));
        uiPanel.add(createLabelledComponent("Backface cull", viewCullCombo));
        uiPanel.add(createLabelledComponent("Perspective", viewPerspectiveCombo));
        uiPanel.add(createLabelledComponent("Lighting", viewLightingCombo));
        uiPanel.add(createLabelledComponent("Tesselation", tesselationLevelCombo));
        uiPanel.add(createLabelledComponent("Flattening", hierarchyFlatteningCombo));
    }
    
    /**
     * Create a panel with a label and the given component
     */
    protected JPanel createLabelledComponent(String s, JComponent c) { 
        JPanel p = new JPanel();
        JLabel l = new JLabel(s);
        l.setPreferredSize(new Dimension(100,20));
        c.setPreferredSize(new Dimension(100,20));
        p.add(l);
        p.add(c);
        return p;
    }
    
	/**
	 * Load a scene from file.
	 */
	public void loadScene(String nFilename) {
		sceneFilename = nFilename;
		scene = null;
		if(sceneFilename == null) return;
		try {
            FileFormat parser = new FileFormat();
			scene = parser.parseXMLScene(sceneFilename);
		} catch(Exception e) {
			System.out.println("Problem parsing file: " + sceneFilename);
			System.out.println(e.toString());
		}
        
        scene.tesselate(tesselationLevel);
	}

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        
        if(src == viewModesCombo) {
            renderPanel.setViewMode(viewModesCombo.getSelectedIndex());
        }
        
        if(src == viewCullCombo) {
            renderPanel.setViewCull(viewCullCombo.getSelectedIndex() == 1);
        }
        
        if(src == viewPerspectiveCombo) {
            renderPanel.setViewPerspective(viewPerspectiveCombo.getSelectedIndex() == 1);
        }
        
        if(src == viewLightingCombo) {
            renderPanel.setViewLighting(viewLightingCombo.getSelectedIndex());
        }
        
        if(src == hierarchyFlatteningCombo) {
            renderPanel.setHierarchyFlattening(hierarchyFlatteningCombo.getSelectedIndex() == 1);
        }
        
        if(src == tesselationLevelCombo) {
            tesselationLevel = tesselationLevelCombo.getSelectedIndex();
            scene.tesselate(tesselationLevel);
        }
        
        renderPanel.redraw();
    }

    /**
     * Mouse dragging listener.
     * Implements camera movement.
     */
    public void mouseDragged(MouseEvent e) {
        //System.out.println("Mouse dragging: " + MouseEvent.getModifiersExText(e.getModifiersEx()));
        boolean leftButton = (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;
        boolean middleButton = (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0;
        boolean rightButton = (e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0;
        
        Point mouseCurrent = e.getPoint();
        Point mouseDelta = new Point((int)(mouseCurrent.getX()-mouseBegin.getX()),
                                     (int)(mouseCurrent.getY()-mouseBegin.getY()));
        
        // rotation
        if(leftButton) {
            scene.hierarchyRoot.rotation.x = mouseBeginRotation.x - mouseDelta.getY()*0.01;
            scene.hierarchyRoot.rotation.y = mouseBeginRotation.y + mouseDelta.getX()*0.01;
        }
        
        // scale
        if(rightButton) {
            scene.hierarchyRoot.scale.set(mouseBeginScale.scale(1+mouseDelta.getY()*0.01));
            if(scene.hierarchyRoot.scale.length() < 0.01 || scene.hierarchyRoot.scale.x < 0) {
                scene.hierarchyRoot.scale.set(new Vec3(0.01,0.01,0.01));
            }
        }
        
        // panning
        if(middleButton) {
            System.out.println("panning not implemented yet");            
        }
        
        // redraw
        renderPanel.redraw();
    }

    /**
     * Mouse motion listener. Unused.
     */
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Mouse click event.
     * Unused.
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Mouse click event.
     * Stores mouse coordinate for manipulation.
     */
    public void mousePressed(MouseEvent e) {
        mouseBegin = e.getPoint();
        mouseBeginTranslation.set(scene.hierarchyRoot.translation);
        mouseBeginRotation.set(scene.hierarchyRoot.rotation);
        mouseBeginScale.set(scene.hierarchyRoot.scale);
    }

    /**
     * Mouse release event.
     * Unused.
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Mouse entered event.
     * Unused.
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Mouse exited event.
     * Unused.
     */
    public void mouseExited(MouseEvent e) {
    }
	
}
