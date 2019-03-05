import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.swing.JFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Box;

/**
 * Main class for java 3d universe for co2016 cw4.
 */
public class Example3D extends JFrame
{

	//variables for changing the speed of effects and the scale of the scene. I might need these later.
	private final float SPEED = 1;
	private final float SCALE = 1;
	
	//flag to enable/disable console logging.
	private final boolean LOGGING = true;
	
	//method to print given string to the console if LOGGING is true.
	public void log(String msg)
	{
		if(LOGGING) System.out.println(msg);
	}
	
	/**
	 * Method to create the scene graph.
	 */
	public BranchGroup createSceneGraph()
	{
		// creating the bounds of the universe
		// see mouse behaviour below
		// API: The scheduling region defines a spatial volume
		// that serves to enable the scheduling of Behavior nodes.
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000);

		//create a branch group to be the object root
		BranchGroup objRoot = new BranchGroup();

		//create the main transform group (to contain everything so we can move it with the mouse)
		TransformGroup mainTG = new TransformGroup();
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		
		// *** make the star
		ColorCube sol = new ColorCube(5);
		mainTG.addChild(sol);
		
		// *** make planetoid 1 (p1)
		ColorCube p1 = new ColorCube();
		Transform3D t = new Transform3D();
		
		//p1 needs to move 10 units in the x (to not overlap the sun)
		t.set(1.0, new Vector3d(10.0, 0.0, 0.0));
		TransformGroup p1MoveInXTG = new TransformGroup(t);
		
		//p1 needs to rotate about y axis
		TransformGroup p1MoveInXTG = new TransformGroup(t);
		
		Alpha rotationAlphaP1 = new Alpha(-1, 5000);
		RotationInterpolator p1RotateInY = new RotationInterpolator(rotationAlphaP1, p1, new Transform3D(), 0.0f, (float) Math.PI * 2);
		p1RotateInY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		p1MoveInXTG.addChild(p1);
		
		mainTG.addChild(p1MoveInXTG);
		
		
		//add mainTG to object root
		objRoot.addChild(mainTG);

		// Create the rotate behavior node
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTG);
		objRoot.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		// Create the zoom behavior node
		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTG);
		objRoot.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

		// Create the translate behavior node
		MouseTranslate behavior3 = new MouseTranslate();
		behavior3.setTransformGroup(mainTG);
		objRoot.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);

		objRoot.compile();
		return objRoot;
	}

	/**
	 * create a "standard" universe using SimpleUniverse
	 */
	public Example3D()
	{
		// *** create universe using BranchGroup from createSceneGraph()
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		cp.add("Center", c);
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);

		// *** create a viewing platform
		TransformGroup cameraTG = u.getViewingPlatform().getViewPlatformTransform();
		// starting postion of the viewing platform
		Vector3f translate = new Vector3f();
		Transform3D T3D = new Transform3D();
		// move away from the screen [last arg] metres
		translate.set(0.0f, 0.0f, 10.0f);
		T3D.setTranslation(translate);
		cameraTG.setTransform(T3D);
		// increase the max render distance beyond 10m, we're dealing with cosmic scales
		// now. Well not really, but it is bigger.
		u.getViewer().getView().setBackClipDistance(100);

		// *** window properties
		setTitle("Hello, 3D world!");
		setSize(700, 700);
		setVisible(true);
	}

	public static void main(String[] args)
	{

		Example3D app = new Example3D();

	}

}