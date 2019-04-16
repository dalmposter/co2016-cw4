import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.swing.JFrame;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.geometry.Box;

/**
 * co2016 cw4 java3d solar system.
 * Java3D looks really messy, making it hard to understand.
 * Throughout I group related statements under headings, beginning and ending with **********
 * Additionally many variable names are of the form [type]_[name] for easy identification
 * (Most notably groups of objects so it's easier when making the relations)
 */
public class Example3D extends JFrame
{
	/**
	 * Function to create a triangular based pyramid where all edges are the same length
	 * @param sideLength the length of any and all sides
	 * @return 
	 */
	Shape3D triangleBasePyramid(float sideLength)
	{
		//Worked out coordinates for a pyramid with side length 2 so just divide given sideLength by 2 and use it as a multiplier
		sideLength /= 2;
		//Points to construct a tetrahedron whose back face is parallel to the plane made by the X and Y axis
		Point3f bl = new Point3f(-sideLength, (float) (-sideLength/Math.sqrt(3)), (float) (-sideLength/Math.sqrt(6))); //Back left
		Point3f br = new Point3f(sideLength, (float) (-sideLength/Math.sqrt(3)), (float) (-sideLength/Math.sqrt(6))); //back right
		Point3f t = new Point3f(0, (float) ((2 * sideLength)/Math.sqrt(3)), (float) (-sideLength/Math.sqrt(6))); //top
		Point3f f = new Point3f(0, 0, (float) ((3 * sideLength)/Math.sqrt(6))); //front
		
		//Joining these points together in a series of triangles
		TriangleArray geometries = new TriangleArray(12, TriangleArray.COORDINATES);
		
		//Back face
		geometries.setCoordinate(0, bl);
		geometries.setCoordinate(1, t);
		geometries.setCoordinate(2, br);
		
		//Front left face
		geometries.setCoordinate(3, f);
		geometries.setCoordinate(4, t);
		geometries.setCoordinate(5, bl);
		
		//Front right face
		geometries.setCoordinate(6, br);
		geometries.setCoordinate(7, t);
		geometries.setCoordinate(8, f);
		
		//The base
		geometries.setCoordinate(9, f);
		geometries.setCoordinate(10, bl);
		geometries.setCoordinate(11, br);
		
		//Create the shape and return it
		GeometryInfo geometryInfo = new GeometryInfo(geometries);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);
		GeometryArray result = geometryInfo.getGeometryArray();
		Shape3D out = new Shape3D(result);
		
		return out;
	}
	
	/**
	 * Create an alien spaceship inspired by the sci-fi series stargate-SG1.
	 * (A square based pyramid with a cool star-like 'ring' around it, designed to land on and dock with the Egyptian pyramids) 
	 * I spent way too long on this, got a bit carried away but it does look cool.
	 * @param scale A multiplier to change size, default size has a pyramid side length of 8
	 * @param pyramidAppearance the appearance of the central pyramid
	 * @param ringAppearance the appearance of the outer star shaped ring
	 * @return
	 */
	Group alienMothership(float scale, Appearance pyramidAppearance, Appearance ringAppearance)
	{
		Shape3D pyramid = squareBasePyramid(scale * 8f, scale * 6f);

		//(right, up, front) coordinate quick reference
		
		//I plot all the points but only make one quarter of the overall object 
		//and just make 4 copies of that and rotate each by 90 degrees
		//Outline square
		Point3f s_fl = new Point3f(-scale * 6f, -scale * 2f, scale * 6f); //Front left point in a square
		Point3f s_bl = new Point3f(-scale * 6f, -scale * 2f, -scale * 6f); //Back left
		Point3f s_br = new Point3f(scale * 6f, -scale * 2f, -scale * 6f); //Back right
		Point3f s_fr = new Point3f(scale * 6f, -scale * 2f, scale * 6f); //Front right
		
		//Outline octagon
		Point3f o_lf = new Point3f(-scale * 7f, -scale * 2f, scale * 2f); //Left most front point in an octagon
		Point3f o_lb = new Point3f(-scale * 7f, -scale * 2f, -scale * 2f); //Left most back
		Point3f o_bl = new Point3f(-scale * 2f, -scale * 2f, -scale * 7f); //Back most left
		Point3f o_br = new Point3f(scale * 2f, -scale * 2f, -scale * 7f); //Back most right
		Point3f o_rb = new Point3f(scale * 7f, -scale * 2f, -scale * 2f); //Right most back
		Point3f o_rf = new Point3f(scale * 7f, -scale * 2f, scale * 2f); //Right most front
		Point3f o_fr = new Point3f(scale * 2f, -scale * 2f, scale * 7f); //Front most right
		Point3f o_fl = new Point3f(-scale * 2f, -scale * 2f, scale * 7f); //Front most left
		
		//Upper square that touches the square based pyramid
		Point3f us_fl = new Point3f(-scale * 2.5f, -scale * 1f, scale * 2.5f); //Front left
		Point3f us_bl = new Point3f(-scale * 2.5f, -scale * 1f, -scale * 2.5f); //Back left
		Point3f us_br = new Point3f(scale * 2.5f, -scale * 1f, -scale * 2.5f); //Back right
		Point3f us_fr = new Point3f(scale * 2.5f, -scale * 1f, scale * 2.5f); //Front right
		
		//Lower square that touches the pyramid
		Point3f ls_fl = new Point3f(-scale * 4f, -scale * 3f, scale * 4f); //Front left
		Point3f ls_bl = new Point3f(-scale * 4f, -scale * 3f, -scale * 4f); //Back left
		Point3f ls_br = new Point3f(scale * 4f, -scale * 3f, -scale * 4f); //Back right
		Point3f ls_fr = new Point3f(scale * 4f, -scale * 3f, scale * 4f); //Front right
		
		//Upper inline square
		Point3f uis_fl = new Point3f(-scale * 4f, -scale * 1f, scale * 4f); //Front left
		Point3f uis_bl = new Point3f(-scale * 4f, -scale * 1f, -scale * 4f); //Back left
		Point3f uis_br = new Point3f(scale * 4f, -scale * 1f, -scale * 4f); //Back right
		Point3f uis_fr = new Point3f(scale * 4f, -scale * 1f, scale * 4f); //Front right
		
		//Lower inline square
		Point3f lis_fl = new Point3f(-scale * 4.5f, -scale * 3f, scale * 4.5f); //Front left
		Point3f lis_bl = new Point3f(-scale * 4.5f, -scale * 3f, -scale * 4.5f); //Back left
		Point3f lis_br = new Point3f(scale * 4.5f, -scale * 3f, -scale * 4.5f); //Back right
		Point3f lis_fr = new Point3f(scale * 4.5f, -scale * 3f, scale * 4.5f); //Front right
		
		//Upper Inline octagon
		Point3f uio_lf = new Point3f(-scale * 4f, -scale * 1f, scale * 2f); //Left most front point in an octagon
		Point3f uio_lb = new Point3f(-scale * 4f, -scale * 1f, -scale * 2f); //Left most back
		Point3f uio_bl = new Point3f(-scale * 2f, -scale * 1f, -scale * 4f); //Back most left
		Point3f uio_br = new Point3f(scale * 2f, -scale * 1f, -scale * 4f); //Back most right
		Point3f uio_rb = new Point3f(scale * 4f, -scale * 1f, -scale * 2f); //Right most back
		Point3f uio_rf = new Point3f(scale * 4f, -scale * 1f, scale * 2f); //Right most front
		Point3f uio_fr = new Point3f(scale * 2f, -scale * 1f, scale * 4f); //Front most right
		Point3f uio_fl = new Point3f(-scale * 2f, -scale * 1f, scale * 4f); //Front most left
		
		//Lower Inline octagon
		Point3f lio_lf = new Point3f(-scale * 4f, -scale * 3f, scale * 2f); //Left most front point in an octagon
		Point3f lio_lb = new Point3f(-scale * 4f, -scale * 3f, -scale * 2f); //Left most back
		Point3f lio_bl = new Point3f(-scale * 2f, -scale * 3f, -scale * 4f); //Back most left
		Point3f lio_br = new Point3f(scale * 2f, -scale * 3f, -scale * 4f); //Back most right
		Point3f lio_rb = new Point3f(scale * 4f, -scale * 3f, -scale * 2f); //Right most back
		Point3f lio_rf = new Point3f(scale * 4f, -scale * 3f, scale * 2f); //Right most front
		Point3f lio_fr = new Point3f(scale * 2f, -scale * 3f, scale * 4f); //Front most right
		Point3f lio_fl = new Point3f(-scale * 2f, -scale * 3f, scale * 4f); //Front most left
		
		//Upper octagon to anchor (be the inner points for) star-like shapes made by the outline square and outline octagon
		//(upper anchor octagon)
		Point3f uao_lf = new Point3f(-scale * 4.5f, -scale * 1f, scale * 4f); //Left most front
		Point3f uao_lb = new Point3f(-scale * 4.5f, -scale * 1f, -scale * 4f); //Left most back
		Point3f uao_bl = new Point3f(-scale * 4f, -scale * 1f, -scale * 4.5f); //Back most left
		Point3f uao_br = new Point3f(scale * 4f, -scale * 1f, -scale * 4.5f); //Back most right
		Point3f uao_rb = new Point3f(scale * 4.5f, -scale * 1f, -scale * 4f); //Right most back
		Point3f uao_rf = new Point3f(scale * 4.5f, -scale * 1f, scale * 4f); //Right most front
		Point3f uao_fr = new Point3f(scale * 4f, -scale * 1f, scale * 4.5f); //Front most right
		Point3f uao_fl = new Point3f(-scale * 4f, -scale * 1f, scale * 4.5f); //Front most left
		
		//Lower octagon to anchor (be the inner points for) star-like shapes made by the outline square and outline octagon
		//(lower anchor octagon)
		Point3f lao_lf = new Point3f(-scale * 4.5f, -scale * 3f, scale * 4f); //Left most front
		Point3f lao_lb = new Point3f(-scale * 4.5f, -scale * 3f, -scale * 4f); //Left most back
		Point3f lao_bl = new Point3f(-scale * 4f, -scale * 3f, -scale * 4.5f); //Back most left
		Point3f lao_br = new Point3f(scale * 4f, -scale * 3f, -scale * 4.5f); //Back most right
		Point3f lao_rb = new Point3f(scale * 4.5f, -scale * 3f, -scale * 4f); //Right most back
		Point3f lao_rf = new Point3f(scale * 4.5f, -scale * 3f, scale * 4f); //Right most front
		Point3f lao_fr = new Point3f(scale * 4f, -scale * 3f, scale * 4.5f); //Front most right
		Point3f lao_fl = new Point3f(-scale * 4f, -scale * 3f, scale * 4.5f); //Front most left
		
		//Joining the points together in a series of triangles
		TriangleArray geometries = new TriangleArray(90, TriangleArray.COORDINATES);

		//Upper front left part of star pattern
		geometries.setCoordinate(0, uio_fl);
		geometries.setCoordinate(1, uao_fl);
		geometries.setCoordinate(2, o_fl);

		geometries.setCoordinate(3, uao_fl);
		geometries.setCoordinate(4, uis_fl);
		geometries.setCoordinate(5, s_fl);

		geometries.setCoordinate(6, uis_fl);
		geometries.setCoordinate(7, uio_fl);
		geometries.setCoordinate(8, uao_fl);

		geometries.setCoordinate(9, uio_fl);
		geometries.setCoordinate(10, us_fl);
		geometries.setCoordinate(11, uis_fl);
		
		geometries.setCoordinate(12, us_fl);
		geometries.setCoordinate(13, uio_lf);
		geometries.setCoordinate(14, uis_fl);

		geometries.setCoordinate(15, uis_fl);
		geometries.setCoordinate(16, uao_lf);
		geometries.setCoordinate(17, s_fl);

		geometries.setCoordinate(18, uao_lf);
		geometries.setCoordinate(19, uio_lf);
		geometries.setCoordinate(20, o_lf);
		
		geometries.setCoordinate(21, uio_lf);
		geometries.setCoordinate(22, uis_fl);
		geometries.setCoordinate(23, uao_lf);
		
		geometries.setCoordinate(24, uio_lf);
		geometries.setCoordinate(25, us_fl);
		geometries.setCoordinate(26, uio_lb);
		
		geometries.setCoordinate(27, uio_lb);
		geometries.setCoordinate(28, us_fl);
		geometries.setCoordinate(29, us_bl);
		
		geometries.setCoordinate(30, uio_lf);
		geometries.setCoordinate(31, uio_lb);
		geometries.setCoordinate(32, lio_lf);
		
		geometries.setCoordinate(33, lio_lb);
		geometries.setCoordinate(34, lio_lf);
		geometries.setCoordinate(35, uio_lb);
		
		geometries.setCoordinate(36, uis_fl);
		geometries.setCoordinate(37, uio_lf);
		geometries.setCoordinate(38, uao_lf);
		
		geometries.setCoordinate(39, uis_fl);
		geometries.setCoordinate(40, uao_fl);
		geometries.setCoordinate(41, uio_fl);
		
		geometries.setCoordinate(42, lis_fl);
		geometries.setCoordinate(43, lao_lf);
		geometries.setCoordinate(44, lio_lf);
		
		geometries.setCoordinate(45, lis_fl);
		geometries.setCoordinate(46, lio_fl);
		geometries.setCoordinate(47, lao_fl);
		
		//lower front left part of star pattern
		geometries.setCoordinate(48, lio_fl);
		geometries.setCoordinate(49, o_fl);
		geometries.setCoordinate(50, lao_fl);

		geometries.setCoordinate(51, lao_fl);
		geometries.setCoordinate(52, s_fl);
		geometries.setCoordinate(53, lis_fl);

		geometries.setCoordinate(54, lis_fl);
		geometries.setCoordinate(55, lao_fl);
		geometries.setCoordinate(56, lio_fl);

		geometries.setCoordinate(57, lio_fl);
		geometries.setCoordinate(58, lis_fl);
		geometries.setCoordinate(59, ls_fl);
		
		geometries.setCoordinate(60, ls_fl);
		geometries.setCoordinate(61, lis_fl);
		geometries.setCoordinate(62, lio_lf);

		geometries.setCoordinate(63, lis_fl);
		geometries.setCoordinate(64, s_fl);
		geometries.setCoordinate(65, lao_lf);

		geometries.setCoordinate(66, lao_lf);
		geometries.setCoordinate(67, o_lf);
		geometries.setCoordinate(68, lio_lf);
		
		geometries.setCoordinate(69, lio_lf);
		geometries.setCoordinate(70, lao_lf);
		geometries.setCoordinate(71, lis_fl);
		
		//Now to join the top to the bottom with more triangles
		//front left join upper-lower
		geometries.setCoordinate(72, uao_fl);
		geometries.setCoordinate(73, s_fl);
		geometries.setCoordinate(74, lao_fl);
		
		geometries.setCoordinate(75, s_fl);
		geometries.setCoordinate(76, uao_lf);
		geometries.setCoordinate(77, lao_lf);
		
		geometries.setCoordinate(78, uao_fl);
		geometries.setCoordinate(79, lao_fl);
		geometries.setCoordinate(80, o_fl);
		
		geometries.setCoordinate(81, uio_fl);
		geometries.setCoordinate(82, o_fl);
		geometries.setCoordinate(83, lio_fl);
		
		geometries.setCoordinate(84, o_lf);
		geometries.setCoordinate(85, uio_lf);
		geometries.setCoordinate(86, lio_lf);
		
		geometries.setCoordinate(87, o_lf);
		geometries.setCoordinate(88, lao_lf);
		geometries.setCoordinate(89, uao_lf);

		GeometryInfo geometryInfo = new GeometryInfo(geometries);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);

		GeometryArray result = geometryInfo.getGeometryArray();

		//The geometry array makes 1 corner, so I need to make 4 of them and rotate each by 90 degrees more than the previous in the Y axis
		Transform3D t = new Transform3D();
		t.rotY(Math.PI / 2);
		TransformGroup group0 = new TransformGroup();
		TransformGroup group1 = new TransformGroup(t);
		TransformGroup group2 = new TransformGroup(t);
		TransformGroup group3 = new TransformGroup(t);
		
		//Create 4 copies of this corner
		Shape3D corner0 = new Shape3D(result, ringAppearance);
		Shape3D corner1 = new Shape3D(result, ringAppearance);
		Shape3D corner2 = new Shape3D(result, ringAppearance);
		Shape3D corner3 = new Shape3D(result, ringAppearance);
		
		//Each group rotates by 90 degrees and contains 1 corner and the next group
		group0.addChild(corner0);
		group0.addChild(group1);
		group1.addChild(corner1);
		group1.addChild(group2);
		group2.addChild(corner2);
		group2.addChild(group3);
		group3.addChild(corner3);
		
		//Set appearance
		pyramid.setAppearance(pyramidAppearance);
		Group out = new Group();
		//Combine the pyramid and ring pattern into one group and return it
		out.addChild(group0);
		out.addChild(pyramid);

		return out;
	}
	
	/**
	 * Create a square based pyramid, its base will be on the XZ plane
	 * @param baseSideLength length of any and all sides in the base square
	 * @param height the height of the peak
	 * @return
	 */
	Shape3D squareBasePyramid(float baseSideLength, float height)
	{
		//Centered at 0,0,0 so divide given arguments by 2 and use the new values to position corners
		baseSideLength /= 2;
		height /= 2;
		
		//Points to construct a square based pyramid with the centre at 0, 0, 0 and the base parallel with the XZ plane (facing down)
		Point3f fl = new Point3f(-baseSideLength, -height, -baseSideLength); //Front left
		Point3f fr = new Point3f(baseSideLength, -height, -baseSideLength); //Front right
		Point3f bl = new Point3f(-baseSideLength, -height, baseSideLength); //Back left
		Point3f br = new Point3f(baseSideLength, -height, baseSideLength); //Back right
		Point3f t = new Point3f(0, height, 0); //top
		
		//Joining the points together in a series of triangles
		TriangleArray geometries = new TriangleArray(18, TriangleArray.COORDINATES);
		
		//Right face
		geometries.setCoordinate(0, br);
		geometries.setCoordinate(1, fr);
		geometries.setCoordinate(2, t);
		
		//Front face
		geometries.setCoordinate(3, fr);
		geometries.setCoordinate(4, fl);
		geometries.setCoordinate(5, t);
		
		//Left face
		geometries.setCoordinate(6, fl);
		geometries.setCoordinate(7, bl);
		geometries.setCoordinate(8, t);
		
		//Back face
		geometries.setCoordinate(9, bl);
		geometries.setCoordinate(10, br);
		geometries.setCoordinate(11, t);
		
		//Front-right half of base
		geometries.setCoordinate(12, br);
		geometries.setCoordinate(13, fl);
		geometries.setCoordinate(14, fr);
		
		//Back-left half of base
		geometries.setCoordinate(15, fl);
		geometries.setCoordinate(16, br);
		geometries.setCoordinate(17, bl);
		
		//Generate the shape and return it
		GeometryInfo geometryInfo = new GeometryInfo(geometries);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(geometryInfo);
		GeometryArray result = geometryInfo.getGeometryArray();
		Shape3D out = new Shape3D(result);
		
		return out;
	}

	public BranchGroup createSceneGraph()
	{
		// Approximate representation of what I'm trying to achieve for my reference
		// (Where Ps orbit the SUN and Ms orbit Ps) Note this doesn't try to represent any objects starting positions, only their orbits around the sun
		
		//							(M1)
		//							 |
		//    (SUN) = = (P1) = (S1)-(P2) = = (P3)-(M3..MX)
		//							 |
		//							(M2)
		
		// P = planet, M = moon, S = satellite, X = some arbitrary integer with the goal of making an asteroid field of moons around P3
		
		/*To summarise in words the goal:
		 * A sun (sphere) in the centre of the system which has a star texture map and emits white light in all directions
		 * A large alien mothership, ship2, orbits the sun in the absolute X axis
		 * 
		 * A small planet (p1) nearest the sun which is red and has some sort of spaceship (rocket1) going between itself and the sun
		 * When the ship reaches the planet, the planet becomes bright yellow and a powerful ambient light is turned on,
		 * when it reaches the sun the planet becomes red again and the light turns off
		 * Perhaps some intergalactic warlord is syphoning off the suns energy via space freighter and using it to fuel a scout outpost on mars (p1)
		 * p1 also has a spaceship, ship1, (inspired by stargate) orbiting it but the orbit reverses on collision with rocket1
		 * Maybe the warlord has this ship patrolling the planet and defending the route of rocket1
		 * but has to move it out of the way of the supply rocket (rocket1)
		 * 
		 * A medium sized planet (p2) with an earth texture map a bit further from the sun
		 * It has 2 moons (m1 and m2) orbiting up and over it and a space station/satellite (s1) orbiting around it
		 * 
		 * A larger brown-ish planet (p3) is farthest from the sun
		 * It has rings of randomly sized spheres (asteroids) orbiting around it with a slight tilt (to the orbit)
		 * p3 also has an alien mothership, ship3, sitting above it, rotating slowly and shining a green light downwards
		 * A blue alien rocket (triangular based pyramid) makes trips between the surface of p3 and ship3
		 * 
		 * All moons/asteroids are grey
		 * 
		 * There is also a very low intensity ambient light so things are still vaguely visible (not just silhouettes) when eclipsing the sun
		 * 
		 * 
		 * For the best viewing angles I suggest (from the initial position) zooming in and out
		 * In addition you can get a nice view by rotating the system such that you are looking down on the planets orbits
		 * The collision events surrounding rocket1 toggle quite a potent ambient light so one can see the lighting/shadows in effect better (when ambient light is off)
		 * Or one can see the materials and textures a bit better (when the ambient light is on)
		 */
		
		
		//Field to store any transforms I need without wasting memory since we just create then immediately use them and never need them again
		Transform3D t;
		
		//Random number generator to add some variety
		Random rand = new Random();
		
		//setting the bounds of the universe
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000);

		//creating a branch group
		BranchGroup objRoot = new BranchGroup();

		//creating a main transform group
		TransformGroup mainTG = new TransformGroup();
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		
		// ******************* CREATING A GROUP FOR ROTATION AROUND THE SUN ******************************
		//Will attach almost all objects to this directly/indirectly to have them orbiting the sun
		TransformGroup group_orbitSun = new TransformGroup();
		group_orbitSun.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Alpha infinite16000 = new Alpha(-1, 16000);
		// Rotating around the y axis (sun is at 0,0,0)
		RotationInterpolator rotator_orbitSun = new RotationInterpolator(infinite16000, group_orbitSun, new Transform3D(), 0.0f, (float) Math.PI * 2.0f);
		rotator_orbitSun.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF P1 ***********************************
		//p1 is the closest planet to the sun
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, 15));
		TransformGroup group_p1Position = new TransformGroup(t);
		// ***********************************************************************************************
		

		// ******************* CREATING A GROUP FOR ROTATION AROUND P2 ***********************************
		//group for the moons oribiting p2
		TransformGroup group_orbitP2 = new TransformGroup();
		group_orbitP2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis (which gets pretty warped by group_Orbitp2Orientation)
		Alpha infinite8000 = new Alpha(-1, 8000);
		RotationInterpolator rotator_orbitP2 = new RotationInterpolator(infinite8000, group_orbitP2,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_orbitP2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING ANOTHER GROUP FOR ROTATION AROUND P2 *****************************
		//A second group for orbiting p2, is not a child of group_Orbitp2Orientation
		//so is a different orbit to the above group
		TransformGroup group_orbitP2no2 = new TransformGroup();
		group_orbitP2no2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis
		Alpha infinite6000 = new Alpha(-1, 6000);
		RotationInterpolator rotator_orbitP2no2 = new RotationInterpolator(infinite6000, group_orbitP2no2,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_orbitP2no2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE ORIENTATION OF THE GROUP ORBITING P2 *************
		//I wanted to change the starting rotation of the moons around P2 without affecting P2
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, 0.0));
		Transform3D helperT3D = new Transform3D();
		helperT3D.rotZ(Math.PI);
		t.mul(helperT3D);
		helperT3D.rotX(Math.PI / 2);
		t.mul(helperT3D);
		TransformGroup group_Orbitp2Orientation = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF P2 ***********************************
		//p2 is the second closest planet from the sun
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, -20));
		helperT3D = new Transform3D();
		TransformGroup group_p2Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION AROUND P3 ***********************************
		//group for orbiting p3 (used for the asteroid rings)
		TransformGroup group_orbitP3 = new TransformGroup();
		group_orbitP3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis (slightly tilted by parent transform groups)
		Alpha infinite2000 = new Alpha(-1, 2000);
		RotationInterpolator rotator_orbitP3 = new RotationInterpolator(infinite2000, group_orbitP3,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_orbitP3.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION OF THE SUN **********************************
		//rotate the sun pretty fast as our actual sun rotates quite fast
		TransformGroup group_rotateSun = new TransformGroup();
		group_rotateSun.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis
		Alpha infinite1000 = new Alpha(-1, 2000);
		RotationInterpolator rotator_rotateSun = new RotationInterpolator(infinite1000, group_rotateSun,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateSun.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION OF P2 ***************************************
		//rotate p2 quite slowly
		TransformGroup group_rotateP2 = new TransformGroup();
		group_rotateP2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis
		Alpha alpha_rotateP2 = new Alpha(-1, 10000);
		RotationInterpolator rotator_rotateP2 = new RotationInterpolator(alpha_rotateP2, group_rotateP2,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateP2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION OF P3 ***************************************
		//rotate p3 at a moderate speed (not too visible due to the texture applied to p3)
		TransformGroup group_rotateP3 = new TransformGroup();
		group_rotateP3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local y axis
		Alpha alpha_rotateP3 = new Alpha(-1, 6000);
		RotationInterpolator rotator_rotateP3 = new RotationInterpolator(alpha_rotateP3, group_rotateP3,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateP3.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF P3 ***********************************
		//p3 is the farthest planet from the sun
		t = new Transform3D();
		t.setTranslation(new Vector3d(35, 0, 0));
		helperT3D = new Transform3D();
		//Give the position and thus orbits around p3 a slight tilt
		//(make it easier to have a tilted asteroid belt without additional groups)
		helperT3D.rotX(Math.PI / 8);
		t.mul(helperT3D);
		TransformGroup group_p3Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF M1 ***********************************
		//Position is relative to that of p2, m1 orbits p2
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, 4));
		TransformGroup group_m1Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF S1 ***********************************
		//Position is relative to that of p2, s1 is a space station that orbits p2
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, 3));
		TransformGroup group_s1Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE POSITION OF M2 ***********************************
		//Position is relative to that of p2, m2 orbits p2
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, -4.5));
		TransformGroup group_m2Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE START POSITION OF ROCKET1 ************************
		//Position is relative to that of p1, start close to the surface of p1 facing the sun
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, -1.4f));
		helperT3D = new Transform3D();
		helperT3D.rotX(Math.PI * 1.5);
		t.mul(helperT3D);
		TransformGroup group_rocket1Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE START POSITION OF SHIP1 **************************
		//Position is relative to that of p1, ship1 orbits p1
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 0.0, -4));
		TransformGroup group_ship1Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION OF SHIP1 ************************************
		//I wanted the ship to not rotate due to it's orbitP1 rotator, so they share the same alpha.
		//(note it will still rotate due to its orbit of the sun but this is intentional)
		TransformGroup group_rotateShip1 = new TransformGroup();
		group_rotateShip1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		t = new Transform3D();
		helperT3D = new Transform3D();
		//Need to flip the Y axis upside down to rotate in the opposite direction
		helperT3D.rotX(Math.PI);
		t.mul(helperT3D);
		Alpha alpha_ship1Orbit1 = new Alpha(1, 6000);
		alpha_ship1Orbit1.setStartTime(Long.MAX_VALUE);
		RotationInterpolator rotator_rotateShip1 = new RotationInterpolator(alpha_ship1Orbit1, group_rotateShip1,
				t, 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateShip1.setSchedulingBounds(bounds);
		
		//Second rotator for when the orbit reverses (this rotation will need to reverse)
		Alpha alpha_ship1Orbit2 = new Alpha(1, 6000);
		//Delay it's start time forever (until it's changed by a collision with rocket1)
		alpha_ship1Orbit2.setStartTime(Long.MAX_VALUE);
		RotationInterpolator rotator_rotateShip1no2 = new RotationInterpolator(alpha_ship1Orbit2, group_rotateShip1,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateShip1no2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATION AROUND P1 ***********************************
		//orbiting p1 (shares alpha with group above as these 2 work together to have a ship orbit p1
		//but not be rotated by its orbit
		TransformGroup group_orbitP1 = new TransformGroup();
		group_orbitP1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		RotationInterpolator rotator_orbitP1 = new RotationInterpolator(alpha_ship1Orbit1, group_orbitP1,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_orbitP1.setSchedulingBounds(bounds);
		
		//Second rotator for the reverse orbit (when ship1 hits rocket1 it's orbit should reverse)
		t = new Transform3D();
		helperT3D = new Transform3D();
		//Need to flip the Y axis upside down to rotate in the opposite direction
		helperT3D.rotX(Math.PI);
		t.mul(helperT3D);
		RotationInterpolator rotator_orbitP1no2 = new RotationInterpolator(alpha_ship1Orbit2, group_orbitP1,
				t, 0.0f, (float) Math.PI * (2.0f));
		rotator_orbitP1no2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR MOVING ROCKET1 ***************************************
		//Rocket1 moves between p1 and the sun, toggling a powerful ambient light on collision with either
		TransformGroup group_rocket1Movement = new TransformGroup();
		group_rocket1Movement.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Transform3D rocketAxis = new Transform3D();
		rocketAxis.rotZ(Math.PI / 2);
		//Define alpha for rockets movement
		Alpha rocketAlpha = new Alpha(-1, 6000);
		//Have the rocket move in one direction and then back
		rocketAlpha.setDecreasingAlphaDuration(6000);
		rocketAlpha.setMode(Alpha.DECREASING_ENABLE + Alpha.INCREASING_ENABLE);
		//Have the rocket stay at p1 and the sun for some time (to deposit/collect energy)
		rocketAlpha.setAlphaAtOneDuration(3000);
		rocketAlpha.setAlphaAtZeroDuration(3000);
		PositionInterpolator positioner_moveRocket1 = new PositionInterpolator(rocketAlpha, group_rocket1Movement,
				rocketAxis, 7.5f, -0.25f);
		positioner_moveRocket1.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING GROUPS FOR THE POSITION OF P3s MOONS *****************************
		//The aim here is to create a large amount of small spheres that orbit p3 in rings,
		//creating rings similar to those of Saturn
		
		//Create list to store transformations to position all the moons/asteroids that orbit p3
		ArrayList<TransformGroup> p3MoonPositionGroups = new ArrayList<>();
		
		//Loop to create vertically layered rings (increase y ordinate)
		for(float y = -0.2f; y <= 0.2f; y+=0.4f)
		{
			//Loop to make rings of asteroids with loop-determined radius
			for(float radius = 3; radius <= 5.5; radius+=0.5)
			{
				//Loop to make 1 ring of asteroids, the asteroids have a spacing of 0.25 in the (local) x dimension
				//I could make more asteroids per ring by changing the increment in this loop but I don't really know how this would affect performance
				for(float x = -radius; x <= radius; x+=0.25)
				{
					//equation for a circle: x^2 + z^2 = radius^2
					//hence z = sqrt(radius^2 - x^2)
					t = new Transform3D();
					//Since I'm increasing x by the same amount each time then calculating z, the spacing between asteroids increases around x = -radius and x = radius
					//I can use this to make an interesting pattern by effectively rotating the rings by 90 degrees with each new radius size
					if(radius * 10 % 2 == 0) t.setTranslation(new Vector3d(x, y, Math.sqrt((radius*radius) - (x*x))));
					else t.setTranslation(new Vector3d(Math.sqrt((radius*radius) - (x*x)), y, x));
					TransformGroup p3MoonPosition = new TransformGroup(t);
					p3MoonPositionGroups.add(p3MoonPosition);
					
					//Since we're square rooting there's actually 2 answers
					//same as above but with the negative answer
					t = new Transform3D();
					if(radius * 10 % 2 == 0) t.setTranslation(new Vector3d(x, y, -Math.sqrt((radius*radius) - (x*x))));
					else t.setTranslation(new Vector3d(-Math.sqrt((radius*radius) - (x*x)), y, x));
					p3MoonPosition = new TransformGroup(t);
					p3MoonPositionGroups.add(p3MoonPosition);
				}
			}
		}
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE START POSITION OF SHIP2 **************************
		//ship2 should be position above the sun and rotate around it in the X axis
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 9, 0.0));
		TransformGroup group_ship2Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE ORBIT OF SHIP2 ***********************************
		//ship2 should orbit the sun in the x axis
		TransformGroup group_ship2Orbit = new TransformGroup();
		group_ship2Orbit.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Rotate around local x axis
		Alpha alpha_ship2Orbit = new Alpha(-1, 10000);
		t = new Transform3D();
		helperT3D = new Transform3D();
		helperT3D.rotZ(Math.PI / 2);
		//Rotate such that the local Y axis is the absolute X axis
		t.mul(helperT3D);
		RotationInterpolator rotator_ship2Orbit = new RotationInterpolator(alpha_ship2Orbit, group_ship2Orbit,
				t, 0.0f, (float) Math.PI * (2.0f));
		rotator_ship2Orbit.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE START POSITION OF ROCKET2 ************************
		//position is relative to p3, rocket2 starts on the surface of p3
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 2, 0.0));
		//as well as position it, I need to rotate it such that one of its faces is facing p3
		//(triangleBasePyramid() creates tetrahedrons where a face is facing the XY plane)
		//so 90 degrees clockwise in x axis should do the trick
		helperT3D = new Transform3D();
		helperT3D.rotX(Math.PI * 1.5f);
		t.mul(helperT3D);
		TransformGroup group_rocket2Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR MOVING ROCKET2 ***************************************
		//Rocket2 should take off and land from p3 vertically, docking with ship3
		TransformGroup group_rocket2Movement = new TransformGroup();
		group_rocket2Movement.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		t = new Transform3D();
		//The parents of this group have rotated it so we need to account for that
		//to get the ship to take off and land vertically
		t.rotY(Math.PI * 1.5f);
		PositionInterpolator positioner_moveRocket2 = new PositionInterpolator(rocketAlpha, group_rocket2Movement,
				t, 0, 2);
		positioner_moveRocket2.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR THE START POSITION OF SHIP3 **************************
		//position is relative to p3, ship3 hovers just above p3 shining a green spot light down
		t = new Transform3D();
		t.setTranslation(new Vector3d(0.0, 4.5f, 0.0));
		TransformGroup group_ship3Position = new TransformGroup(t);
		// ***********************************************************************************************
		
		
		// ******************* CREATING A GROUP FOR ROTATING SHIP3 ***************************************
		//Ship3 should rotate slowly while hovering above p3
		TransformGroup group_rotateShip3 = new TransformGroup();
		group_rotateShip3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//Slow alpha
		Alpha alpha_rotateShip3 = new Alpha(-1, 15000);
		//rotate in local y axis so just use new Transform3D
		RotationInterpolator rotator_rotateShip3 = new RotationInterpolator(alpha_rotateShip3, group_rotateShip3,
				new Transform3D(), 0.0f, (float) Math.PI * (2.0f));
		rotator_rotateShip3.setSchedulingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATING THE SUN **********************************************************
		//Composed of a large, yellow/orange sphere and a white point light
		// Set up the texture map for the sun
	    TextureLoader loader = new TextureLoader("images/2k_sun.jpg", "RGB", new Container());
	    Texture texture = loader.getTexture();
	    texture.setBoundaryModeS(Texture.WRAP);
	    texture.setBoundaryModeT(Texture.WRAP);

	    //Setup the texture
	    TextureAttributes texAttr = new TextureAttributes();
	    //Replace incoming colour with texture as there is minimal light shining on the sun
	    texAttr.setTextureMode(TextureAttributes.REPLACE);
	    Appearance sunAppearance = new Appearance();
	    sunAppearance.setTexture(texture);
	    sunAppearance.setTextureAttributes(texAttr);
	    
	    int primflags = Primitive.GENERATE_NORMALS
		        + Primitive.GENERATE_TEXTURE_COORDS;
	    
	    //Increasing the number of 'divisions' (polygons) in the sun as its quite large and didn't really look like a sphere
	    Sphere sun = new Sphere(6f, primflags, 50, sunAppearance);
	    
		//Creating the light in the sun
		PointLight light_sun = new PointLight();
		light_sun.setInfluencingBounds(bounds);
		sun.setName("sun");
		light_sun.setName("light_sun");
		// ***********************************************************************************************
		
		
		// ******************* CREATING AMBIENT LIGHTING *************************************************
		//Low intensity white, just to light things up slightly
		AmbientLight light_ambient = new AmbientLight(true, new Color3f(0.2f, 0.2f, 0.2f));
		light_ambient.setInfluencingBounds(bounds);
		// ***********************************************************************************************
		
		
		// ******************* CREATE TEXTURE FOR P2 *****************************************************
		// Set up the texture map (looks like earth)
	    loader = new TextureLoader("images/earthmap1k.jpg", "RGB", new Container());
	    texture = loader.getTexture();
	    texture.setBoundaryModeS(Texture.WRAP);
	    texture.setBoundaryModeT(Texture.WRAP);

	    //Setup the texture
	    texAttr = new TextureAttributes();
	    texAttr.setTextureMode(TextureAttributes.MODULATE);
	    Appearance p2Appearance = new Appearance();
	    p2Appearance.setTexture(texture);
	    p2Appearance.setTextureAttributes(texAttr);

	    //create a material (diffuses white light as the textures image contains the colours)
	    Material p2Material = new Material();
	    p2Material.setAmbientColor(new Color3f(.5f, .5f, .5f));
	    p2Material.setDiffuseColor(new Color3f(.8f, .8f, .8f));
	    p2Material.setSpecularColor(new Color3f(.2f, .2f, .2f));
	    p2Appearance.setMaterial(p2Material);
		// ***********************************************************************************************
	    
	    
		// ******************* CREATE TEXTURE FOR P3 *****************************************************
		// Set up the texture map (looks like saturn)
		loader = new TextureLoader("images/2k_saturn.jpg", "RGB", new Container());
		texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);

		// Setup the texture
		texAttr = new TextureAttributes();
		texAttr.setTextureMode(TextureAttributes.MODULATE);
		//Aplly via new appearance
		Appearance p3Appearance = new Appearance();
		p3Appearance.setTexture(texture);
		p3Appearance.setTextureAttributes(texAttr);
		//create a material (mostly white as the colors come from the texture)
		Material p3Material = new Material();
		p3Material.setAmbientColor(new Color3f(.4f, .4f, .4f));
		p3Material.setDiffuseColor(new Color3f(.6f, .6f, .6f));
		p3Material.setSpecularColor(new Color3f(.2f, .2f, .2f));
		p3Appearance.setMaterial(p3Material);
		// ***********************************************************************************************
	    
		
		// ******************* CREATING MATERIALS ********************************************************
		//P1 material (red planet)
		Material p1Material = new Material();
		p1Material.setAmbientColor(new Color3f(0.4f, 0.0f, 0.0f));
		p1Material.setDiffuseColor(new Color3f(0.6f, 0.3f, 0.3f));
		p1Material.setSpecularColor(new Color3f(0.1f, 0.1f, 0.1f));
		//Apply material via new appearance
		Appearance p1Appearance = new Appearance();
		p1Appearance.setMaterial(p1Material);
		
		//P1 material 2, yellow (p1 turns from red to yellow on collision with rocket1)
		Material p1Material2 = new Material();
		p1Material2.setAmbientColor(new Color3f(.7f, .7f, .4f));
		p1Material2.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.2f));
		p1Material2.setSpecularColor(new Color3f(.6f, .6f, .2f));
		p1Material2.setEmissiveColor(new Color3f(0.7f, 0.7f, 0.4f));
		Appearance p1Appearance2 = new Appearance();
		p1Appearance2.setMaterial(p1Material2);
		
		//M1/2 material (grey moon)
		Material m1Material = new Material();
		m1Material.setAmbientColor(new Color3f(.4f, .4f, .4f));
		m1Material.setDiffuseColor(new Color3f(.4f, .4f, .4f));
		m1Material.setSpecularColor(new Color3f(.1f, .1f, .1f));
		//Apply material via new appearance
		Appearance m1Appearance = new Appearance();
		m1Appearance.setMaterial(m1Material);
		
		//S1 material (yellow space station)
		Material s1Material = new Material();
		s1Material.setAmbientColor(new Color3f(.8f, .6f, .2f));
		Appearance s1Appearance = new Appearance();
		s1Appearance.setMaterial(s1Material);
		
		//Rocket1 material (blue rocket)
		Material rocket1Material = new Material();
		rocket1Material.setAmbientColor(new Color3f(0, 0, .2f));
		rocket1Material.setDiffuseColor(new Color3f(0, .5f, 1));
		rocket1Material.setSpecularColor(new Color3f(0, .5f, 1));
		Appearance rocket1Appearance = new Appearance();
		rocket1Appearance.setMaterial(rocket1Material);
		
		//Material for the pyramids within alien motherships
		//A kind of gold-yellow-brown shiny material
		Material pyramidMaterial = new Material();
		pyramidMaterial.setAmbientColor(new Color3f(.3f, .3f, 0));
		pyramidMaterial.setDiffuseColor(new Color3f(.6f, .6f, .2f));
		pyramidMaterial.setSpecularColor(new Color3f(.5f, .5f, .2f));
		Appearance pyramidAppearance = new Appearance();
		pyramidAppearance.setMaterial(pyramidMaterial);
		
		//Material for the pattern around the pyramid in alien motherships
		//A dark black/grey
		Material ringMaterial = new Material();
		ringMaterial.setAmbientColor(new Color3f(.15f, .15f, .15f));
		ringMaterial.setDiffuseColor(new Color3f(.15f, .15f, .15f));
		ringMaterial.setSpecularColor(new Color3f(.1f, .1f, .1f));
		Appearance ringAppearance = new Appearance();
		ringAppearance.setMaterial(ringMaterial);
		// ***********************************************************************************************
		
		
		// ******************* CREATING 3D SHAPES ********************************************************
	    //Giving objects names for debugging purposes, maybe they can be used for selective collisions or something else later...
	    //Increase the number of divisions/polygons in some larger objects to make them look nicer
		Sphere p1Red = new Sphere(1, Sphere.GENERATE_NORMALS, 20, null);
		p1Red.setName("p1Red");
		p1Red.setAppearance(p1Appearance);
		
		Sphere p1Yellow = new Sphere(1, Sphere.GENERATE_NORMALS, 20, null);
		p1Yellow.setName("p1Yellow");
		p1Yellow.setAppearance(p1Appearance2);
		
		Sphere p2 = new Sphere(1.75f, primflags, 25, p2Appearance);
		p2.setName("p2");
		
		Sphere m1 = new Sphere(.4f);
		m1.setName("m1");
		m1.setAppearance(m1Appearance);
		
		Sphere m2 = new Sphere(.5f);
		m2.setName("m2");
		m2.setAppearance(m1Appearance);
		
		Sphere p3 = new Sphere(2f, primflags, 30, p3Appearance);
		p3.setName("p3");
		
		Box s1 = new Box(.25f, .25f, .25f, null);
		s1.setName("s1");
		s1.setAppearance(s1Appearance);
		
		Cone rocket1 = new Cone(0.4f, 0.8f);
		rocket1.setName("rocket1");
		rocket1.setAppearance(rocket1Appearance);
		
		Shape3D rocket2 = triangleBasePyramid(1f);
		rocket2.setName("rocket2");
		rocket2.setAppearance(rocket1Appearance);
		
		//Create a switch to hold both appearances of p1, to be toggled by collisions
		Switch p1Switch = new Switch();
		//Give p1 an ambient light which is powered by energy brought from the sun via space freighter
		//(when the ship reaches p1, the light goes on, when it reaches the sun the light goes off)
		AmbientLight light_p1On = new AmbientLight(true, new Color3f(0.7f, 0.7f, 0.7f));
		light_p1On.setInfluencingBounds(bounds);
		Group p1On = new Group();
		p1On.addChild(p1Yellow);
		p1On.addChild(light_p1On);
		p1Switch.setCapability(Switch.ALLOW_SWITCH_WRITE);
		p1Switch.setCapability(Switch.ALLOW_SWITCH_READ);
		p1Switch.addChild(p1Red);
		p1Switch.addChild(p1On);
		p1Switch.setWhichChild(1);
		p1Switch.setName("p1Switch");
		// ***********************************************************************************************
		
		
		// ******************* CREATING ALIEN MOTHERSHIPS ************************************************
		Group ship1 = alienMothership(0.1f, pyramidAppearance, ringAppearance);
		Group ship2 = alienMothership(0.25f, pyramidAppearance, ringAppearance);
		
		//ship3 has a directional light shining downwards out of its base
		//Slightly tilted with a small angle of spread to make it move slightly across the surface of p3 as ship3 rotates
		Group ship3 = alienMothership(0.2f, pyramidAppearance, ringAppearance);
		//spotLight(lightOn, colour, position, attenuation, direction, spreadAngle, concentration)
		//Green light to make it look alien
		SpotLight light_ship3 = new SpotLight(true, new Color3f(0, 1, 0), new Point3f(0, 0, 0),
				new Point3f(1, 0, 0), new Vector3f(0, -1, .2f), (float) (Math.PI / 12f), 0f);
		light_ship3.setInfluencingBounds(bounds);
		light_ship3.setName("light_ship3");
		// ***********************************************************************************************
		
		
		//Group used to position a light for test viewing objects which I replaced the sun with for testing purposes
		t = new Transform3D();
		t.setTranslation(new Vector3f(0, 10, 20));
		TransformGroup group_testLightPosition = new TransformGroup(t);
		
		// ******************** MAKE RELATIONS IN SCENE GRAPH ********************************************
		//Indentation helps show the hierarchy
		objRoot.addChild(mainTG);
			mainTG.addChild(group_orbitSun);
				//P1 and related nodes
				group_orbitSun.addChild(group_p1Position);
					group_p1Position.addChild(p1Switch);
					group_p1Position.addChild(group_orbitP1);
						group_orbitP1.addChild(group_ship1Position);
							group_ship1Position.addChild(group_rotateShip1);
								group_rotateShip1.addChild(ship1);
								group_rotateShip1.addChild(rotator_rotateShip1);
								group_rotateShip1.addChild(rotator_rotateShip1no2);
						group_orbitP1.addChild(rotator_orbitP1);
						group_orbitP1.addChild(rotator_orbitP1no2);
					group_p1Position.addChild(group_rocket1Position);
						group_rocket1Position.addChild(group_rocket1Movement);
							group_rocket1Movement.addChild(rocket1);
							group_rocket1Movement.addChild(positioner_moveRocket1);
				//P2 and related nodes
				group_orbitSun.addChild(group_p2Position);
					group_p2Position.addChild(group_rotateP2);
						group_rotateP2.addChild(p2);
						group_rotateP2.addChild(rotator_rotateP2);
					group_p2Position.addChild(group_Orbitp2Orientation);
						group_Orbitp2Orientation.addChild(group_orbitP2);
							group_orbitP2.addChild(group_m1Position);
								group_m1Position.addChild(m1);
							group_orbitP2.addChild(group_m2Position);
								group_m2Position.addChild(m2);
							group_orbitP2.addChild(rotator_orbitP2);
					group_p2Position.addChild(group_orbitP2no2);
						group_orbitP2no2.addChild(group_s1Position);
							group_s1Position.addChild(s1);
						group_orbitP2no2.addChild(rotator_orbitP2no2);
				//P3 and related nodes
				group_orbitSun.addChild(group_p3Position);
					group_p3Position.addChild(group_rotateP3);
						group_rotateP3.addChild(p3);
						group_rotateP3.addChild(rotator_rotateP3);
					group_p3Position.addChild(group_rocket2Position);
						group_rocket2Position.addChild(group_rocket2Movement);
							group_rocket2Movement.addChild(rocket2);
							group_rocket2Movement.addChild(positioner_moveRocket2);
					group_p3Position.addChild(group_ship3Position);
						group_ship3Position.addChild(group_rotateShip3);
							group_rotateShip3.addChild(light_ship3);
							group_rotateShip3.addChild(ship3);
							group_rotateShip3.addChild(rotator_rotateShip3);
					group_p3Position.addChild(group_orbitP3);
						//Loop to add an asteroid belt to p3 (a LOT of moons orbiting it in several circles)
						for(int i = 0; i < p3MoonPositionGroups.size(); i++)
						{
							//This list contains transform groups to position moons around p3
							group_orbitP3.addChild(p3MoonPositionGroups.get(i));
							//Create a new 'moon' for each group and attach it to that group
							//Spice things up by randomising the size of each asteroid
							float size = (((float) rand.nextInt(5)) + 8f) / 100f;
							Sphere p3Moon = new Sphere(size);
							p3Moon.setAppearance(m1Appearance);
							p3MoonPositionGroups.get(i).addChild(p3Moon);
						}
						group_orbitP3.addChild(rotator_orbitP3);
				//Rotator that causes everything to orbit the sun
				group_orbitSun.addChild(rotator_orbitSun);
			//Add the sun and its rotator
			mainTG.addChild(group_rotateSun);
				group_rotateSun.addChild(sun);
				group_rotateSun.addChild(rotator_rotateSun);
			mainTG.addChild(light_sun);
			//Add ship2 which orbits the sun
			mainTG.addChild(group_ship2Orbit);
				group_ship2Orbit.addChild(group_ship2Position);
					group_ship2Position.addChild(ship2);
					group_ship2Position.addChild(rotator_ship2Orbit);
			//mainTG.addChild(group_testLightPosition);			//Create a differently positioned point light for testing purposes
				//group_testLightPosition.addChild(light_sun);
			//mainTG.addChild(testPy);							//Statement to add some test object to the center
		//Add low intensity ambient light
		objRoot.addChild(light_ambient);
		// ***********************************************************************************************
		
		
		// ******************** SETUP COLLISION BEHAVIOUR FOR ROCKET1 ************************************
		//Make p1 collidable so the rocket can 'dock' with it
		p1Switch.setCollidable(true);
		p1Switch.setCollisionBounds(new BoundingSphere(new Point3d(0, 0, 0), .5f));
		//Make the sun collidable so the rocket can 'dock' with it
		sun.setCollisionBounds(new BoundingSphere(new Point3d(0, 0, 0), 6));
		sun.setCollidable(true);
		//Make the rocket collidable so it can participate
		rocket1.setCollisionBounds(new BoundingSphere(new Point3d(0, 10, 0), 0.1f));
		rocket1.setCollidable(true);
		
		//Setup toggling the switch that is p1 when the rocket collides with the sun or p1 itself
		ToggleSwitchOnCollision rocketHitsP1 = new ToggleSwitchOnCollision(p1Switch, p1Switch, bounds, 1, 16000);
		ToggleSwitchOnCollision rocketHitsTheSun = new ToggleSwitchOnCollision(sun, p1Switch, bounds, 0, 16000);
		mainTG.addChild(rocketHitsTheSun);
		mainTG.addChild(rocketHitsP1);
		// ***********************************************************************************************
		
		
		// ******************** SETUP COLLISION BEHAVIOUR FOR SHIP1 ************************************
		//The aim here is to have ship1 orbit p1 until it collides with rocket1, at which point it's rotation/orbit will reverse
		//The collisions bounds will be a bit larger than the actual objects to make it look like it's avoiding the collision
		//Make ship1 collidable
		ship1.setCollidable(true);
		ship1.setCollisionBounds(new BoundingSphere(new Point3d(0, 0, 0), .325f));
			
		//Setup dis/enabling alphas used to make ship1 orbit p1 (one for clockwise, one for counter-clockwise)
		Alpha[] ship1Alphas = {alpha_ship1Orbit2, alpha_ship1Orbit1};
		//Setting min time between switches (maxSwitchRate) to be the time it takes to complete 1 orbit
		//so it doesn't snap into position unnaturally if a collision occurs before it's completed 1 orbit
		ToggleMovementOnCollision shipHitsRocket = new ToggleMovementOnCollision(ship1, ship1Alphas, bounds, 6000);
		mainTG.addChild(shipHitsRocket);
		// ***********************************************************************************************


		// ******************** CREATE MOUSE BEHAVIOUR ***************************************************
		// Create the rotate behaviour
		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTG);
		objRoot.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		// Create the zoom behaviour
		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTG);
		objRoot.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

		// Create the translate behaviour
		MouseTranslate behavior3 = new MouseTranslate();
		behavior3.setTransformGroup(mainTG);
		objRoot.addChild(behavior3);
		behavior3.setSchedulingBounds(bounds);
		// ***********************************************************************************************

		objRoot.compile();
		return objRoot;
	}

	/**
	 * Constructor, creates the universe using a SimpleUniverse.
	 * Sets up the window and camera
	 */
	public Example3D()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		cp.add("Center", c);
		BranchGroup scene = createSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);
		
		//Increase max render distance to keep more of the scene visible
		u.getViewer().getView().setBackClipDistance(1000);

		// ******************* SETUP THE CAMERA **********************************************************
		TransformGroup cameraTG = u.getViewingPlatform().getViewPlatformTransform();
		// starting position of the viewing platform
		Vector3f translate = new Vector3f();
		Transform3D T3D = new Transform3D();
		// move the camera towards the screen for better viewing
		translate.set(0.0f, 0.0f, 100.0f);
		T3D.setTranslation(translate);
		cameraTG.setTransform(T3D);
		// ***********************************************************************************************
		
		setTitle("co2016-cw4");
		setSize(700, 700);
		setVisible(true);
	}

	/**
	 * Main method, just create an instance of this class
	 */
	public static void main(String[] args) { Example3D main = new Example3D(); }
}

/**
 * Class to move back and forth with the change in direction occurring due to a collision.
 */
class ToggleMovementOnCollision extends Behavior
{
	WakeupCriterion hit;
	Group collidingObject;
	Alpha[] movement;
	int nextAlpha;
	long maxSwitchRate;
	long lastSwitch;
	
	/**
	 * Creates a new behaviour which on collision enter, initiates the next in a wrapped list of movement alphas
	 * @param collidingObject the object to detect collisions on
	 * @param bounds the scheduling bounds
	 * @param maxSwitchRate the number of milliseconds to wait after a switch before allowing the next
	 */
	public ToggleMovementOnCollision(Group collidingObject, Alpha[] alphas, Bounds bounds, long maxSwitchRate)
	{
		this.collidingObject = collidingObject;
		setSchedulingBounds(bounds);
		movement = alphas;
		nextAlpha = 0;
		this.maxSwitchRate = maxSwitchRate;
	}
	
	@Override
	public void initialize()
	{
		//Only trigger at the start of collisions
		hit = new WakeupOnCollisionEntry(collidingObject);
		wakeupOn(hit);
	}

	@Override
	public void processStimulus(Enumeration criteria)
	{
		//Check if we would be switching faster than maxSwitchRate allows
		if(System.currentTimeMillis() >= lastSwitch + maxSwitchRate)
		{
			//Look through the wakeup criterion
			while(criteria.hasMoreElements())
			{
				WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
				//If we find a collision enter we're in business
				if(theCriterion instanceof WakeupOnCollisionEntry)
				{
					//Start the next alpha
					movement[nextAlpha].setStartTime(System.currentTimeMillis());
					//Increment and/or wrap the counter for the alpha to start next time
					if(++nextAlpha >= movement.length) nextAlpha = 0;
					
					//log this switches time
					lastSwitch = System.currentTimeMillis();
				}
				
				wakeupOn(hit);
			}
		}
		
		wakeupOn(hit);
	}
}

/**
 * Class to switch between 2 child nodes in a switch on collision start.
 */
class ToggleSwitchOnCollision extends Behavior
{
	public WakeupOnCollisionEntry hit;
	Group collidingObject;
	Switch target;
	int child;
	long maxSwitchRate;
	long lastSwitch;
	
	/**
	 * Creates a new behaviour which on collision enter, toggles between 2 children of a switch.
	 * @param collidingObject the object to detect collisions on
	 * @param target switch to toggle
	 * @param bounds the scheduling bounds
	 * @param child the child to make visible when this behaviour triggers
	 */
	public ToggleSwitchOnCollision(Group collidingObject, Switch target, Bounds bounds, int child, long maxSwitchRate)
	{
		this.collidingObject = collidingObject;
		setSchedulingBounds(bounds);
		this.target = target;
		this.child = child;
		this.maxSwitchRate = maxSwitchRate;
	}
	
	@Override
	public void initialize()
	{
		//Only trigger at the start of collisions
		hit = new WakeupOnCollisionEntry(collidingObject);
		wakeupOn(hit);
	}

	@Override
	public void processStimulus(Enumeration criteria)
	{
		//Check if we would be switching faster than maxSwitchRate allows
		if(System.currentTimeMillis() >= lastSwitch + maxSwitchRate)
		{
			//look through wakeup criterion
			while(criteria.hasMoreElements())
			{
				WakeupCriterion theCriterion = (WakeupCriterion) criteria.nextElement();
				//if we find collision enter we're in business
				if(theCriterion instanceof WakeupOnCollisionEntry)
				{
					//do the switch
					target.setWhichChild(child);
					//log this switches time
					lastSwitch = System.currentTimeMillis();
				}
				
				wakeupOn(hit);
			}
		}
		
		wakeupOn(hit);
	}

}

