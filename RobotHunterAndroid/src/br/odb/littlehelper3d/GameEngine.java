package br.odb.littlehelper3d;

import java.util.ArrayList;

import br.odb.libstrip.MeshFactory;
import br.odb.utils.FileServerDelegate;

public class GameEngine {
	
	final ArrayList< GameActor > actors = new ArrayList<GameActor>();
	final GameCamera currentCamera = new GameCamera();
	GameWorld world;
	GameRenderDelegate renderDelegate;
	GameEventDelegate eventDelegate;
			
	
	public void setRenderDelegate( GameRenderDelegate renderDelegate ) {
		this.renderDelegate = renderDelegate;		
	}

	public void setGameEventDelegate( GameEventDelegate gameEventDelegate) {
		this.eventDelegate = gameEventDelegate;
	}
	
	public void update( long timeInMS ) {
		
	}

	public void render() {
		GameSnapshot gameSnapshot = buildGameSnapshot();
		renderDelegate.render( gameSnapshot );
		
	}

	private GameSnapshot buildGameSnapshot() {
		
		GameSnapshot gameSnapshot = new GameSnapshot( world.sectors, actors.toArray( new GameActor[ 1 ] ), currentCamera );
		
		return gameSnapshot;
	}	
	
	public static GameEngine buildToPlayLevel(String levelName, FileServerDelegate fsd, MeshFactory meshFactory ) {
		
		GameEngine toReturn;
		
		toReturn = new GameEngine();
		
		GameWorldBuilder gwBuilder= new GameWorldBuilder();
		toReturn.world = gwBuilder.getFor( levelName, fsd, meshFactory );
				
		return toReturn;
	}
	
}
