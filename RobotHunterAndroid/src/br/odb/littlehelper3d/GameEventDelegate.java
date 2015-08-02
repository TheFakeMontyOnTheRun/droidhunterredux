package br.odb.littlehelper3d;

public interface GameEventDelegate {
	GameEventResponse onGameEvent( GameEvent event, GameSnapshot snapshot );
}
