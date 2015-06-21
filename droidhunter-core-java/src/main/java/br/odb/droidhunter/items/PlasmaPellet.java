package br.odb.droidhunter.items;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.utils.Direction;

public class PlasmaPellet extends Item {

	private Direction direction;

	public PlasmaPellet( Location location, Direction d ) {
		super( "pellet1" );
		
		
		this.location = location;
		this.direction = d;
	}

	@Override
	public void useWith(Item item1) throws ItemActionNotSupportedException {
		// TODO Auto-generated method stub
		super.useWith(item1);
	}

	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		// TODO Auto-generated method stub
		super.wasUsedOn(item1);
	}

	@Override
	public void update(long milisseconds) {
	
		super.update(milisseconds);
		
		for ( CharacterActor c : this.location.getCharacters() ) {
			c.setIsAlive( false );
			setIsDepleted( true );
			return;
		}
		
		Location possibleNewLocation = location.getConnections()[ direction.ordinal() ]; 
		
		if ( possibleNewLocation != null ) {
			location = possibleNewLocation;
		}
	}
}
