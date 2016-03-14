package br.odb.nodehunter;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST; // GL constants
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_TRIANGLES;
import static javax.media.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import br.odb.libscene.CameraNode;
import br.odb.libscene.DirectedSceneNode;
import br.odb.libscene.GroupSector;
import br.odb.libscene.LightNode;
import br.odb.libscene.MeshNode;
import br.odb.libscene.SceneNode;
import br.odb.libscene.Sector;
import br.odb.libscene.SpaceRegion;
import br.odb.libscene.World;
import br.odb.libscene.util.SceneTesselator;
import br.odb.libstrip.Decal;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.TriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;
import br.odb.gameutils.Color;
import br.odb.gameutils.Direction;
import br.odb.gameutils.math.Vec3;
import br.odb.vintage.GameEngine;
// GL2 constants
import br.odb.vintage.SceneRenderer;
import br.odb.vintage.actor.ActorSceneNode;

public class GameView3D extends GLCanvas implements GLEventListener,
		KeyListener, SceneRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3207880799571393702L;

	final List<GeneralTriangle> polysToRender = new ArrayList<>();
	final List<GeneralTriangle> defaultActorMesh = new ArrayList<>();
	final List<LightNode> lights = new ArrayList<>();
	final List<ActorSceneNode> actors = new ArrayList<>();

	float[] colorWhite = { 1.0f, 1.0f, 1.0f, 1.0f };

	public final CameraNode defaultCameraNode = new CameraNode("default");
	private GLU glu;

	private SpaceRegion currentSector;

	SceneTesselator tesselator;

	private List<LaserBeam> lasers = new ArrayList<>();

	public GameView3D() {
		this.addGLEventListener(this);
		this.addKeyListener(this);

		tesselator = new SceneTesselator(new GeneralTriangleFactory());
	}

	public GeneralTriangle changeHue(GeneralTriangle trig) {
		trig.material = Material.makeWithColor( new Color(trig.material.mainColor));

		 switch (trig.hint) {
			 case W:
			 trig.material.mainColor.multiply(0.1f);
			 break;
			 case E:
			 trig.material.mainColor.multiply(0.4f);
			 break;
			 case N:
			 trig.material.mainColor.multiply(0.2f);
			 break;
			 case S:
			 trig.material.mainColor.multiply(0.6f);
			 break;
			 case FLOOR:
			 trig.material.mainColor.multiply(0.9f);
			 break;
			 case CEILING:
			 trig.material.mainColor.multiply(0.3f);
			 break;
		 }

		Vec3 normal = trig.makeNormal();

		trig.nx = normal.x;
		trig.ny = normal.y;
		trig.nz = normal.z;

		trig.material.mainColor.a = 255;

		return trig;

	}

	public void addLight(LightNode ln) {
		lights.add(ln);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		glu = new GLU();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glEnable(gl.GL_LIGHTING);
		gl.glEnable(gl.GL_COLOR_MATERIAL);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	}

	void applyDecalToSector(Decal decal, GroupSector target) {
		decal.scale(target.size);
		MeshNode meshNode = new MeshNode( decal.name, decal );
		target.addChild( meshNode );
		decal.translate(target.getAbsolutePosition());
	}

	void applyDecalToSector(String decalFilename, Direction direction,
			String targetSectorName) throws FileNotFoundException, IOException {
		FileInputStream fis;
		fis = new FileInputStream(System.getProperty("user.home")
				+ decalFilename);
		Decal decal = Decal.loadDecal("gun", fis, direction);
		applyDecalToSector(decal,
				(GroupSector) world.masterSector.getChild(targetSectorName));
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		GL2 gl = drawable.getGL().getGL2();

		if (height == 0)
			height = 1;

		float aspect = (float) width / height;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0, aspect, 0.1, 10000.0f);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public synchronized void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		gl.glRotatef( defaultCameraNode.angleXZ, 0.0f, 1.0f, 0.0f);
		gl.glTranslatef(-defaultCameraNode.localPosition.x,
				-defaultCameraNode.localPosition.y,
				-defaultCameraNode.localPosition.z);

		Color c;

		float[] raw;
		Vec3 absPos;

		for (LightNode ln : lights) {

			gl.glEnable(gl.GL_LIGHT1);
			raw = new float[4];
			absPos = ln.getAbsolutePosition();
			raw[0] = absPos.x;
			raw[1] = absPos.y;
			raw[2] = absPos.z;
			raw[3] = 1.0f;
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_POSITION, raw, 0);
			gl.glLightfv(gl.GL_LIGHT1, gl.GL_DIFFUSE, colorWhite, 0);
			gl.glLightf(gl.GL_LIGHT1, gl.GL_CONSTANT_ATTENUATION, 0.8f);
		}		

		synchronized (actors) {
			for (ActorSceneNode p : actors) {
				synchronized( p ) {
					drawCube(gl, p.localPosition, p.angleXZ );
				}
			}
		}
		
		synchronized( lasers ) {
			for ( LaserBeam beam : lasers ) {
				drawBeam( gl, beam );
			}
		}
		
		gl.glBegin(GL_TRIANGLES);
		for (GeneralTriangle poly : this.polysToRender) {

			c = poly.material.mainColor;

			gl.glColor4f(c.r / 255.0f, c.g / 255.0f, c.b / 255.0f, c.a / 255.0f);
			gl.glNormal3f(poly.nx, poly.ny, poly.nz);
			gl.glVertex3f(poly.x0, poly.y0, poly.z0);
			gl.glNormal3f(poly.nx, poly.ny, poly.nz);
			gl.glVertex3f(poly.x1, poly.y1, poly.z1);
			gl.glNormal3f(poly.nx, poly.ny, poly.nz);
			gl.glVertex3f(poly.x2, poly.y2, poly.z2);

		}
		gl.glEnd();
		
	}

	private void drawBeam(GL2 gl, LaserBeam beam) {
		gl.glBegin( gl.GL_TRIANGLES );
		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f );		
		gl.glVertex3f(beam.target.x, beam.target.y, beam.target.z);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.5f );
		gl.glVertex3f(beam.origin.x + 0.025f, beam.origin.y - 0.01f, beam.origin.z);
		gl.glVertex3f(beam.origin.x + 0.025f, beam.origin.y + 0.01f, beam.origin.z);

		gl.glColor4f(1.0f, 0.0f, 0.0f, 1.0f );
		gl.glVertex3f(beam.target.x, beam.target.y, beam.target.z);
		gl.glColor4f(1.0f, 0.0f, 0.0f, 0.5f );
		gl.glVertex3f(beam.origin.x + 0.01f + 0.025f, beam.origin.y, beam.origin.z);
		gl.glVertex3f(beam.origin.x - 0.01f + 0.025f, beam.origin.y, beam.origin.z);
		
		gl.glEnd();
	}

	private void drawCube(GL2 gl, Vec3 p, float angleXZ) {
		gl.glTranslatef( p.x, p.y, p.z);
		gl.glRotatef( -angleXZ, 0, 1, 0);
		gl.glBegin(GL_TRIANGLES);
		for (GeneralTriangle poly : this.defaultActorMesh) {

			gl.glColor4f(poly.material.mainColor.r / 255.0f,
					poly.material.mainColor.g / 255.0f,
					poly.material.mainColor.b / 255.0f,
					poly.material.mainColor.a / 255.0f);

			gl.glVertex3f(poly.x0, poly.y0, poly.z0 );
			gl.glVertex3f(poly.x1, poly.y1, poly.z1 );
			gl.glVertex3f(poly.x2, poly.y2, poly.z2 );

		}
		gl.glEnd();
		gl.glRotatef( angleXZ, 0, 1, 0);
		gl.glTranslatef( -p.x, -p.y, -p.z);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		float scale = 10.0f;

		switch (keyCode) {
		case KeyEvent.VK_R:
			recalculateVisibility();
			break;
		case KeyEvent.VK_A:
			defaultCameraNode.localPosition.y += scale / 2.0f;
			break;
		case KeyEvent.VK_Z:
			defaultCameraNode.localPosition.y -= scale / 2.0f;
			break;

		case KeyEvent.VK_P:

			actors.add(new ActorSceneNode(defaultCameraNode));
			this.repaint();
			break;
		case KeyEvent.VK_LEFT:
			defaultCameraNode.angleXZ -= 10.0f;
			break;
			
		case KeyEvent.VK_SPACE:
			startShooting();
			break;
			
		case KeyEvent.VK_RIGHT:
			defaultCameraNode.angleXZ += 10.0f;
			break;
		case KeyEvent.VK_UP:
			defaultCameraNode.localPosition.x += scale
					* Math.sin(defaultCameraNode.angleXZ * (Math.PI / 180.0f));
			defaultCameraNode.localPosition.z -= scale
					* Math.cos(defaultCameraNode.angleXZ * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_DOWN:
			defaultCameraNode.localPosition.x -= scale
					* Math.sin(defaultCameraNode.angleXZ * (Math.PI / 180.0f));
			defaultCameraNode.localPosition.z += scale
					* Math.cos(defaultCameraNode.angleXZ * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_COMMA:
			defaultCameraNode.localPosition.x += scale
					* Math.sin((defaultCameraNode.angleXZ - 90.0f) * (Math.PI / 180.0f));
			defaultCameraNode.localPosition.z -= scale
					* Math.cos((defaultCameraNode.angleXZ - 90.0f) * (Math.PI / 180.0f));
			break;
		case KeyEvent.VK_PERIOD:
			defaultCameraNode.localPosition.x += scale
					* Math.sin((defaultCameraNode.angleXZ + 90.0f) * (Math.PI / 180.0f));
			defaultCameraNode.localPosition.z -= scale
					* Math.cos((defaultCameraNode.angleXZ + 90.0f) * (Math.PI / 180.0f));
			break;

		case KeyEvent.VK_ESCAPE:
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}

	}

	private void startShooting() {
		
		ActorSceneNode target = GameEngine.checkForPointedElements( getCurrentCameraNode(),  this.actors, world );
		
		if ( target != null ) {
			addLaser( getCurrentCameraNode().getAbsolutePosition().add( new Vec3( 0.0f, -0.1f, 0.0f ) ), target.getAbsolutePosition().add( new Vec3( 0.0f, -0.1f, 0.0f ) ), 5250 );
		}		
	}

	private void addLaser(Vec3 origin, Vec3 target, int decayInMS ) {
		lasers .add( new LaserBeam( origin, target, decayInMS ) );		
	}

	private void clearMeshesOn(GroupSector sector ) {
		
		List< MeshNode > nodes = new ArrayList<MeshNode>();
		
		for ( SceneNode sn : sector.getSons() ) {
			if ( sn instanceof MeshNode ) {
				nodes.add( (MeshNode) sn );
			} else if ( sn instanceof GroupSector ) {
				clearMeshesOn( (GroupSector) sn );
			}
		}
		
		for ( MeshNode node : nodes ) {
			sector.removeChild( node );
		}
	}
	
	private void recalculateVisibility() {
		currentSector = world.masterSector
				.pick(this.defaultCameraNode.localPosition);

		if (currentSector != null) {

			Sector visitingSon = (Sector) currentSector;
			GroupSector visitingParent = (GroupSector) visitingSon.parent;
			this.polysToRender.clear();
			clearMeshesOn( this.world.masterSector );

			while (visitingSon != null) {

				if (visitingSon.parent != visitingParent) {
					visitingParent = (GroupSector) visitingSon.parent;
					tesselator.generateSubSectorMeshForSector(visitingParent);
				}

				visitingSon = (Sector) world.masterSector
						.getChild(visitingSon.links[0]);
			}
		}
	}

	private World world;

	public void setScene(World world) {
		this.polysToRender.clear();
		this.world = world;
		world.masterSector.size.set(1024.0f, 1024.0f, 1024.0f);
	}

	@Override
	public void spawnDefaultActor(Vec3 pos, float angleXZ) {
		ActorSceneNode dsn = new ActorSceneNode("actor");
		dsn.localPosition.set(pos);
		dsn.angleXZ = angleXZ;
		actors.add(dsn);
	}

	@Override
	public CameraNode getCurrentCameraNode() {
		return this.defaultCameraNode;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultMeshForActor(TriangleMesh mesh) {
		this.defaultActorMesh.addAll( mesh.faces );
		
	}

	@Override
	public void addTriangleToStaticScene(GeneralTriangle gt) {
		this.polysToRender.add( changeHue( gt ) );		
	}

	@Override
	public synchronized void addActor(ActorSceneNode actor) {
		this.actors.add( actor );		
	}

	@Override
	public synchronized void clearActors() {
		this.actors.clear();	
	}

	@Override
	public void update(long step) {
		
		List< LaserBeam > toRemove = new ArrayList<>();
		
		for ( LaserBeam beam : lasers ) {
			beam.update( step );
			
			if ( beam.decayInMS <= 0 ) {
				toRemove.add( beam );
			}
		}		
		
		for ( LaserBeam beam : toRemove ) {
			lasers.remove( beam );
		}
	}
}