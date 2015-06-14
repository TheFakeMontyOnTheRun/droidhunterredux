package br.odb.droidhunter;


public class DroidHunterRedux1D {

	public static void main( String[] args ) {
		App game = new App();
		
		game.setAppName("Droid Hunter")
        .setAuthorName("Daniel 'MontyOnTheRun' Monteiro")
        .setLicenseName("3-Clause BSD").setReleaseYear(2015);
		game.createDefaultClient();
		new Thread( game ).start();
	}
}
