package br.odb.littlehelper3d;

public class GameSnapshot {
	
	final public GameSector[] sectors;
	final public GameActor[] actors;
	final public GameCamera camera;
	
	public GameSnapshot( final GameSector[] sectors, final GameActor[] actors, final GameCamera camera ) {
		this.sectors = sectors;
		this.actors = actors;
		this.camera = camera;
	}
}
