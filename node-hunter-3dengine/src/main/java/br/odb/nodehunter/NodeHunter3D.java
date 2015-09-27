package br.odb.nodehunter;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.Play;
import br.odb.libscene.World;
import br.odb.utils.FileServerDelegate;
import br.odb.vintage.GameEngine;
import br.odb.vintage.ScenePresenter;


public class NodeHunter3D extends GameEngine implements ApplicationClient {

	NodeHunter3D(boolean multiplayerEnabled) {
		super(multiplayerEnabled);
	}

	App game = new App();
	
	public void makeNewSessionFor(Play play, World scene,
			ScenePresenter presenter) {
		
		super.makeNewSessionFor(play, scene, presenter);
		
		game.setAppName("Node Hunter")
        .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
        .setLicenseName("3-Clause BSD").setReleaseYear(2015);
		game.setApplicationClient( this );
	}
	
	public void fire() {
		game.sendData("fire");
	}
	
	public void walkTo( String locationName ) {
		game.sendData( "move " + locationName );
	}
	

	@Override
	public void alert(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int chooseOption(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FileServerDelegate getFileServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String openHTTP(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void playMedia(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printError(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printNormal(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printVerbose(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printWarning(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendQuit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientId(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}
}
