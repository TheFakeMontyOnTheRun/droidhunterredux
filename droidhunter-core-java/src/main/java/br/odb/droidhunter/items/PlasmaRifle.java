package br.odb.droidhunter.items;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.utils.Direction;

public final class PlasmaRifle extends Item {

	public PlasmaRifle() {
		super( "plasma-riffle");
	}

	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {

		super.use(user);
		
		user.getLocation().addItem( new PlasmaPellet( user.getLocation(), Direction.N ) );
	}
}
