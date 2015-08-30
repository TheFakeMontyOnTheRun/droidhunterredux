package br.odb.nodehunter;


public class NodeHunterCli {

	public static void main( String[] args ) {
		App game = new App();
		
		game.setAppName("Node Hunter")
        .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
        .setLicenseName("3-Clause BSD").setReleaseYear(2015);
		game.createDefaultClient();
		new Thread( game ).start();
	}
}
