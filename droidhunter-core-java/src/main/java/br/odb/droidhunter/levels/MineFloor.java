package br.odb.droidhunter.levels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.odb.gameworld.Location;

public class MineFloor extends Location {
	public final List< HistoryPanel > intro = new ArrayList< HistoryPanel >();
	public final Set<Location> locations = new HashSet<Location>();
	
	public MineFloor(String name) {
		super(name);
	
	}

}
