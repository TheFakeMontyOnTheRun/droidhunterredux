package br.odb.gamelib.android.geometry;

import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.Material;
import br.odb.libstrip.builders.GeneralTriangleFactory;

public class GLES1TriangleFactory extends GeneralTriangleFactory {

	private static GLES1TriangleFactory instance;

	public static GLES1TriangleFactory getInstance() {

		if (instance == null)
			instance = new GLES1TriangleFactory();

		return instance;
	}

	public GLES1Triangle makeTrig(float x0, float y0, float z0, float x1,

	float y1, float z1, float x2, float y2, float z2, Material color) {
		GLES1Triangle toReturn = new GLES1Triangle();
		toReturn.x0 = x0;
		toReturn.x1 = x1;
		toReturn.x2 = x2;
		toReturn.y0 = y0;
		toReturn.y1 = y1;
		toReturn.y2 = y2;
		toReturn.z0 = z0;
		toReturn.z1 = z1;
		toReturn.z2 = z2;

        toReturn.material = color;
		
		toReturn.flush();
		return toReturn;
	}

	public GLES1Triangle makeTrigFrom(GeneralTriangle gt) {

		GLES1Triangle toReturn = makeTrig( gt.x0, gt.y0, gt.z0, gt.x1, gt.y1, gt.z1, gt.x2, gt.y2, gt.z2, gt.material);
		toReturn.setTextureCoordinates( gt.getTextureCoordinates() );
		toReturn.hint = gt.hint;

		return toReturn;
	}
}
