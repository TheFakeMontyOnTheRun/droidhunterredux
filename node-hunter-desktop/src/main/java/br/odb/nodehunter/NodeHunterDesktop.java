/**
 * 
 */
package br.odb.nodehunter;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.GroupSector;
import br.odb.libscene.LightNode;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.libstrip.TriangleMesh;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;
import br.odb.utils.math.Vec3;
import br.odb.vintage.GameEngine;
import br.odb.vintage.ScenePresenter;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author monty
 *
 */
public class NodeHunterDesktop {
	
	private static final int CANVAS_WIDTH = 640;
	private static final int CANVAS_HEIGHT = 480;
	private static final int FPS = 60;
	
	public static World loadMap(String filename)
			throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException {
		
		FileInputStream fis = new FileInputStream(
				System.getProperty( "user.home" ) + filename );
		 
		World world = WorldLoader.build(fis);
		 
		return world;
	}	

	
	public static void main(String[] args) {
		final GameView3D canvas = new GameView3D();

		new Thread(new Runnable() {
			private World world;

			@Override
			public void run() {
				try {					
					world = NodeHunterDesktop.loadMap( "/prison.opt.xml" );
					canvas.tesselator.generateSubSectorQuadsForWorld(world);
					initDefaultActorModel();

					//canvas.applyDecalToSector("/title.bin", Direction.FLOOR, "Cube.002_Cube.112" );
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ScenePresenter presenter = new ScenePresenter( canvas );
				GameEngine engine = new GameEngine();
				engine.makeNewSessionFor( null, world, presenter);				
				createScene(canvas);
				new Thread( engine ).start();
				
				System.out.println( "loaded " + canvas.polysToRender.size() + " polys" );
				
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH,
						CANVAS_HEIGHT));

				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

				final JFrame frame = new JFrame();
				frame.getContentPane().add(canvas);

				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {

						new Thread() {
							@Override
							public void run() {
								if (animator.isStarted())
									animator.stop();
								System.exit(0);
							}
						}.start();
					}
				});
				frame.setTitle( "3D View" );
				frame.pack();
				frame.setVisible(true);
				animator.start(); // start the animation loop
			}

			void initDefaultActorModel() throws FileNotFoundException {
				WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
				List<Material> mats = matLoader.parseMaterials(new FileInputStream(
						System.getProperty("user.home") + "/gargoyle.mtl"));

				WavefrontOBJLoader loader = new WavefrontOBJLoader(
						new GeneralTriangleFactory());
				ArrayList<TriangleMesh> mesh = (ArrayList<TriangleMesh>) loader
						.loadMeshes(new FileInputStream(System.getProperty("user.home")
								+ "/gargoyle.obj"), mats);

				canvas.setDefaultMeshForActor( mesh.get( 0 ) );
			}
			
			private void createScene(GameView3D canvas) {
			
				GroupSector sr = (GroupSector) world.masterSector.getChild( "Cube" );
				canvas.getCurrentCameraNode().setPositionFromGlobal( ( sr.getAbsolutePosition().add( new Vec3( sr.size.x / 2.0f, sr.size.y / 2.0f, sr.size.z / 2.0f ) ) ) );
				LightNode light0 = new LightNode( "light0" );
				light0.intensity = 0.5f;
				
				light0.setPositionFromGlobal( canvas.getCurrentCameraNode().getAbsolutePosition() );
				canvas.addLight( light0 );
				//canvas.spawnDefaultActor( canvas.getCurrentCameraNode().localPosition.add( new Vec3( 5.0f, 0.0f, 5.0f ) ), 0.0f );
				//canvas.spawnDefaultActor( new Vec3( 0.0f, 0.0f, 0.0f ), 0.0f );
			}
		}).start();
	}
}
