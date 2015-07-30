package br.odb.littlehelper3d;


public class GameWorld {
	
	final public GameSector[] sectors;
	

	
	public GameWorld( GameSector[] sectors ) {
		this.sectors = sectors;
	}
	

	public int getTotalMasterSectors() {
		int total = 0;

		for (GameSector s : sectors) {
			if (s.isMaster()) {
				++total;
			}
		}

		return total;
	}	
}
