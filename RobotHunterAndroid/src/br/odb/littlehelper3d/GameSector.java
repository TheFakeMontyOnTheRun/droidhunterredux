package br.odb.littlehelper3d;

import br.odb.gameworld.Direction;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;

public class GameSector {
	
	public final Vec3 p0 = new Vec3();
	public final Vec3 p1 = new Vec3();
	private boolean masterSector = true;
	private int id = -1;
	public final Color[] color = new Color[ 6 ];
	final private int[] link = new int[ 6 ];
	private String info = "";
	public String name = "";

	
	public GameSector(float x0, float x1, float y0, float y1, float z0, float z1) {

		p0.set( x0, y0, z0 );
		p1.set( x1, y1, z1 );
		
		for ( int c = 0; c < 6; ++c ) {
			color[ c ] = new Color();
		}
		
	}

	public boolean isMaster() {

		return masterSector;
	}

	public void setIsMaster(boolean isMaster) {
		masterSector = isMaster;		
	}

	public void setId(int id) {
		this.id = id;		
	}

	public void setColor( Color color, int face ) {
		this.color[ face ] = color;		
	}

	public void setLinks( int nLink, int eLink, int sLink,
			int wLink, int fLink, int cLink) {
		
		link[ Direction.N.ordinal() ] = nLink;
		link[ Direction.E.ordinal() ] = eLink;
		link[ Direction.S.ordinal() ] = sLink;
		link[ Direction.W.ordinal() ] = wLink;
		link[ Direction.FLOOR.ordinal() ] = fLink;
		link[ Direction.CEILING.ordinal() ] = cLink;
		
	}

	public void setExtraInformation(String info) {
		this.info = info;		
	}

	public void setDecalAt(int face, String decalName) {
		// TODO Auto-generated method stub
		
	}
}
