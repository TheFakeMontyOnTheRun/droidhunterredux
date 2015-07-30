package br.odb.robothunter.dalvik;

import br.odb.libstrip.AbstractTriangle;
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Mesh;
import br.odb.libstrip.MeshFactory;
import br.odb.utils.math.Vec3;

public class AndroidMeshFactory extends MeshFactory {

	@Override
	public IndexedSetFace makeTrig(float x, float y, float z, float x2,
			float y2, float z2, float x3, float y3, float z3, int color,
			Vec3 defaultLightVector) {
		
		return null;
	}

	@Override
	public Mesh emptyMeshNamed(String subToken) {
		
		return null;
	}

	@Override
	public AbstractTriangle[][] newTriangleGroups(int i) {
		
		return null;
	}

}
