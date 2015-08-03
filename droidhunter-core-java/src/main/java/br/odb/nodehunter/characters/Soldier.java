package br.odb.nodehunter.characters;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Kind;
import br.odb.nodehunter.items.PlasmaRifle;

public class Soldier extends CharacterActor {

	public final PlasmaRifle riffle = new PlasmaRifle();
	
	public Soldier(String name, Kind kind) {
		super(name, kind);
	}
}
