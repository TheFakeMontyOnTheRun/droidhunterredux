package br.odb.nodehunter.command;

import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;

public class ToggleShieldCommand extends UserCommandLineAction {

	public ToggleShieldCommand() {
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
	public void run(ConsoleApplication arg0, String arg1) throws Exception {
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "toggle-shield";
	}
}
