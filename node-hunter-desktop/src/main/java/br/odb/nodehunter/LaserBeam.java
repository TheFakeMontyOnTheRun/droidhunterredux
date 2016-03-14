package br.odb.nodehunter;

import br.odb.gameutils.math.Vec3;

public class LaserBeam {

	final public Vec3 origin = new Vec3();
	final public Vec3 target = new Vec3();
	public long decayInMS;

	public LaserBeam(Vec3 origin, Vec3 target, int decayInMS) {
		this.origin.set( origin );
		this.target.set( target );
		this.decayInMS = decayInMS;
	}
	
	public void update( long step ) {
		decayInMS -= step;
	}
}
