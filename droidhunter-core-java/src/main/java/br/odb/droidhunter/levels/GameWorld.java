package br.odb.droidhunter.levels;

import br.odb.droidhunter.characters.Node;
import br.odb.droidhunter.characters.Soldier;
import br.odb.gameworld.Kind;
import br.odb.gameworld.Location;
import br.odb.utils.Direction;

public class GameWorld {
	public MineFloor[] floors;
	Node[] nodes;
	final public Soldier jane37 = new Soldier( "jane37", new Kind( "Soldier" ) );
	
	public GameWorld() {
		 
		floors = new MineFloor[ 1 ];
		floors[ 0 ] = new MineFloor( "floor1" );
		
		Location entryWay = new Location( "entryway" );
		Location centerHall = new Location( "center-hall" );
		Location northRoom = new Location( "north-room" );
		Location westRoom = new Location( "west-room" );
		Location eastRoom = new Location( "east-room" );
		
		floors[ 0 ].locations.add( entryWay );
		floors[ 0 ].locations.add( centerHall );
		floors[ 0 ].locations.add( northRoom );
		floors[ 0 ].locations.add( westRoom );
		floors[ 0 ].locations.add( eastRoom );
				
		centerHall.setConnected( Direction.S, entryWay );
		centerHall.setConnected( Direction.N, northRoom );
		centerHall.setConnected( Direction.W, westRoom );
		centerHall.setConnected( Direction.E, eastRoom );

		
		entryWay.setConnected( Direction.N, entryWay );
		northRoom.setConnected( Direction.S, northRoom );
		westRoom.setConnected( Direction.E, westRoom );
		eastRoom.setConnected( Direction.W, eastRoom );
		
		entryWay.addCharacter( jane37 );
	}
}
