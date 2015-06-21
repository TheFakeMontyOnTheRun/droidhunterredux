package br.odb.droidhunter;

import br.odb.droidhunter.command.FireRiffleCommand;
import br.odb.droidhunter.command.MoveCommand;
import br.odb.droidhunter.levels.GameWorld;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;

public class App extends ConsoleApplication {

	final public GameWorld gameWorld = new GameWorld();

	@Override
	public ConsoleApplication init() {
			
		this.registerCommand( new FireRiffleCommand() );
		this.registerCommand( new MoveCommand() );
		
		return super.init();
	}
	
	@Override
	public void onDataEntered(String data) {
	
		super.onDataEntered(data);
		try {
			runCmd( data );
			gameWorld.update( 1000 );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public ConsoleApplication showUI() {
		
		super.showUI();
		getClient().printNormal( "Place:" );
		getClient().printNormal( gameWorld.jane37.getLocation().getName() );
		getClient().printNormal( "Present:" );
		for ( CharacterActor c : gameWorld.jane37.getLocation().getCharacters() ) {
			getClient().printNormal( c.getName() );	
		}
		getClient().printNormal( "Items:" );
		for ( Item i : gameWorld.jane37.getLocation().getCollectableItems() ) {
			getClient().printNormal( i.getName() );	
		}
		
		return this;
	}
	
	@Override
	public void log(String arg0, String arg1) {
		
	}

	@Override
	protected void doQuit() {
				
	}
}
