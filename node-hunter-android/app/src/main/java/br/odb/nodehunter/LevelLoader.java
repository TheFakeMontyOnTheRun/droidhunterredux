package br.odb.nodehunter;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.List;

import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.libscene.GroupSector;
import br.odb.libscene.SceneNode;
import br.odb.libscene.World;
import br.odb.libscene.builders.WorldLoader;
import br.odb.libscene.util.SceneTesselator;
import br.odb.utils.FileServerDelegate;
import br.odb.vintage.GameEngine;
import br.odb.vintage.ScenePresenter;

/**
 * Created by monty on 8/1/15.
 */
public class LevelLoader extends AsyncTask<Void, Void, Void> {

    private final ScenePresenter presenter;
    private final FileServerDelegate fileServer;
    private final String mapName;
    GameEngine engine;
    World world;

    public LevelLoader( GameEngine engine, ScenePresenter presenter, FileServerDelegate fileServer, String mapName ) {
        this.engine = engine;
        this.presenter = presenter;
        this.fileServer = fileServer;
        this.mapName = mapName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            InputStream fileInput = fileServer.openAsset( mapName );

            if ( mapName.endsWith( ".xml" ) ) {
                world = WorldLoader.build(fileInput);
            } else if ( mapName.endsWith( ".ser" ) ) {
                world = (World) new ObjectInputStream(fileInput).readObject();
            }

            SceneTesselator tesselator = new SceneTesselator(new GLES1TriangleFactory());

            tesselator.generateSubSectorQuadsForWorld(world);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        engine.makeNewSessionFor(null, world, presenter);
//
//        for ( SceneNode sn : world.getAllRegionsAsList() ) {
//            if ( sn instanceof GroupSector) {
//                ((GroupSector) sn ).clearMeshes();
//            }
//        }

        presenter.renderer.getCurrentCameraNode().angleXZ = 180.0f;

        final List<SceneNode> srs = world.getAllRegionsAsList();

        int size = srs.size();
        int index = 0;

        new Thread( engine ).start();

        for (index = size - 1; index >= 0; --index) {
            if (srs.get(index) instanceof GroupSector) {

                presenter.renderer.getCurrentCameraNode().localPosition
                        .set(((GroupSector) srs.get(index)).getAbsoluteCenter());

//                Vec3 pos = new Vec3(presenter.renderer.getCurrentCameraNode().localPosition);
//                view.spawnActor(pos.add(new Vec3(10.0f, 0.0f, 10.0f)), 180.0f);
//                view.spawnActor(pos.add(new Vec3(30.0f, 0.0f, 30.0f)), 0.0f);
//                view.spawnActor(pos.add(new Vec3(20.0f, 0.0f, 20.0f)), 90.0f);
                presenter.renderer.setAsReady();
                return;
            }
        }
    }
}