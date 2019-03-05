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
import com.sun.j3d.utils.geometry.Box;

/**
 * Main class for java 3d universe for co2016 cw4.
 */
public class Example3D extends JFrame
{

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

		// creating a branch group
		BranchGroup objRoot = new BranchGroup();

		// creating a transform group
		TransformGroup mainTG = new TransformGroup();
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// creating a rotation interpolator for cubeTG0
		TransformGroup cubeTG0 = new TransformGroup();
		cubeTG0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// Create a new behavior object that will
		// rotate cube c (defined below)
		// 135 degrees (3pi/4 radians) around the locale y axis
		// ----
		// LOOK UP Alpha in the API !!!
		// 18000 is the time for the rotation
		// 1 is the loop count
		Alpha rotationAlpha0 = new Alpha(-1, 2000);
		// ----
		// LOOK UP RotationInterpolator in the API !!!
		// RotationInterpolator always rotates around the axis
		// specified by yAxis below. This is a transform.
		// If yAxis is the identity transform (matrix) then rotation
		// occurs around the y axis.
		Transform3D yAxis = new Transform3D();
		RotationInterpolator rotator0 = new RotationInterpolator(rotationAlpha0, cubeTG0, yAxis, 0.0f,
				(float) Math.PI * 2.0f);
		rotator0.setSchedulingBounds(bounds);

		// creating another transform group (new cubeTG1, from t)
		// first creating a transformation t
		Transform3D t = new Transform3D();
		t.setScale(new Vector3d(2.0, 2.0, 2.0));
		t.setTranslation(new Vector3d(0.0, 0.0, -5));
		Transform3D helperT3D = new Transform3D();
		helperT3D.rotZ(Math.PI);
		t.mul(helperT3D);
		helperT3D.rotX(Math.PI / 2);
		t.mul(helperT3D);
		TransformGroup cubeTG1 = new TransformGroup(t);

		// creating a rotation interpolator for a new cubeTG2
		TransformGroup cubeTG2 = new TransformGroup();
		cubeTG2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// Create a new behavior object that will
		// rotate cube c (defined below)
		// repeatedly around the local y axis = axis through CYAN color
		// ----
		// -1 means indefinite loop count
		Alpha rotationAlpha2 = new Alpha(-1, 4000);
		// ----
		Transform3D yAxis2 = new Transform3D();
		// RotationInterpolator always rotates around y axis by default
		// *** see scene graph example 1 for a change to the z axis ***
		// *** but try this out yAxis2.rotZ(Math.PI/2); ***
		RotationInterpolator rotator2 = new RotationInterpolator(rotationAlpha2, cubeTG2, yAxis2, 0.0f,
				(float) Math.PI * (2.0f));
		rotator2.setSchedulingBounds(bounds);

		// creating an appearance (for a box)
		Appearance greenApp = new Appearance();
		Color3f greenColor = new Color3f(0.0f, 1.0f, 0.0f);
		ColoringAttributes greenCA = new ColoringAttributes();
		greenCA.setColor(greenColor);
		greenApp.setColoringAttributes(greenCA);

		// create a box
		Box b = new Box(0.8f, 0.8f, .1f, greenApp);

		// creating 3D shapes: colorcubes
		// try altering the ".5" sizing parameter
		ColorCube c = new ColorCube(.5);
		ColorCube co = new ColorCube(.5);

		// make edge relations between the scene graph nodes
		// cube c is transformed by cubeTG2 then cubeTG1
		// cube co and box b at the origin
		objRoot.addChild(mainTG);
		mainTG.addChild(cubeTG0);
		cubeTG0.addChild(cubeTG1);
		cubeTG1.addChild(cubeTG2);
		cubeTG2.addChild(c);
		cubeTG0.addChild(rotator0);
		cubeTG2.addChild(rotator2);
		mainTG.addChild(b);
		mainTG.addChild(co);

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
		//increase the max render distance beyond 10m, we're dealing with cosmic scales now. Well not really, but it is bigger.
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