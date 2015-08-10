package br.odb.nodehunter.command;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.nodehunter.App;

public class FireRiffleCommand extends UserCommandLineAction {

	public FireRiffleCommand() {
	}

	@Override
	public String getHelp() {
		return null;
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public void run(ConsoleApplication app, String arg1) throws Exception {
		App game = (App) app;
		game.gameWorld.jane37.riffle.use( game.gameWorld.jane37 );
	}

	@Override
	public String toString() {
		return "fire";
	}
}
