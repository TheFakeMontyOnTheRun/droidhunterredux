package br.odb.droidhunter.characters;

import br.odb.droidhunter.items.PlasmaRifle;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Kind;

public class Soldier extends CharacterActor {

	public final PlasmaRifle riffle = new PlasmaRifle();
	
	public Soldier(String name, Kind kind) {
		super(name, kind);
	}
}
