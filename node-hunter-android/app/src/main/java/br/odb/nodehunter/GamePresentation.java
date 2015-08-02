package br.odb.nodehunter;

import android.util.Log;
import android.view.KeyEvent;

import br.odb.libscene.World;
import br.odb.vintage.ScenePresenter;
import br.odb.vintage.SceneRenderer;

public class GamePresentation extends ScenePresenter {

    public GamePresentation(SceneRenderer renderer) {
        super(renderer);
    }

//    final public Map<LightSource, GroupSector> lightsForPlace = new HashMap<LightSource, GroupSector>();
//    LightSource light0 = new LightSource(new Vec3(), 128);
//    final public ArrayList<LightSource> lightSources = new ArrayList<LightSource>();
//    public void lit(GroupSector s, LightSource ls) {
//
//        for (GeneralTriangle isf : s.mesh.faces) {
//            //         ( (GLES1Triangle ) isf ).light = ls.intensity;
//        }
//    }
//
//    public void findPlaceForLightSource(LightSource ls, World world) {
//        for (SceneNode sr : world.getAllRegionsAsList()) {
//            if (sr instanceof GroupSector) {
//                if (((GroupSector) sr).isInside(ls.position)) {
//                    lightsForPlace.put(ls, (GroupSector) sr);
//                    return;
//                }
//            }
//        }
//    }
//
//    public void processLights(World world) {
//        for (LightSource ls : lightSources) {
//            findPlaceForLightSource(ls, world);
//        }
//    }
//
//    public void spawnActor(Vec3 v, float angleXZ) {
//        ActorSceneNode actor = new ActorSceneNode( "actor@" + v.toString() );
//        actor.localPosition.set( v );
//        actor.angleXZ = angleXZ;
//        renderer.actors.add( actor );
//    }

    int polyCount = 0;

    public void setScene(World scene) {
        renderer.setScene( scene );
    }
}

