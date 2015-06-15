package br.odb.droidhunter.command;

import br.odb.droidhunter.App;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameworld.Location;

public class MoveCommand extends UserCommandLineAction {

	public MoveCommand() {
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 1;
	}

	@Override
	public void run(ConsoleApplication app, String arg) throws Exception {
		App game = (App) app;
		Location current = game.gameWorld.jane37.getLocation();
		Location newPlace = game.gameWorld.floors[ 0 ].findLocationByName( arg );
		current.removeCharacter( game.gameWorld.jane37 );
		newPlace.addCharacter( game.gameWorld.jane37 );
		
		System.out.println( "moving from " + current.getName() + " to " + newPlace.getName() );
	}

	@Override
	public String toString() {
		return "move";
	}
}
