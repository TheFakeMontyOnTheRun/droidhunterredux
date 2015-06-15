package br.odb.droidhunter.command;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class UseCommand extends UserCommandLineAction {

	public UseCommand() {
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
	public void run(ConsoleApplication arg0, String arg1) throws Exception {
	}

	@Override
	public String toString() {
		return "use";
	}
}
