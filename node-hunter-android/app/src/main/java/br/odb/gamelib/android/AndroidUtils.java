/**
 * 
 */
package br.odb.gamelib.android;

import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;


/**
 * @author monty
 *
 */
public class AndroidUtils {

	public static android.graphics.Rect toAndroidRect(Rect rect) {
		
		android.graphics.Rect androidRect = new android.graphics.Rect();
		
		androidRect.left = (int) rect.p0.x;
		androidRect.right = (int) rect.p1.x;
		androidRect.top = (int) rect.p0.y;
		androidRect.bottom = (int) rect.p1.y;
		
		return androidRect;
	}
	

	public static Rect toGameRect(
			android.graphics.Rect rect) {
		
		Rect gameRect = new Rect();
		
		gameRect.p0.x = rect.left;
		gameRect.p0.y = rect.top;
		gameRect.p1.x = rect.right;
		gameRect.p1.y = rect.bottom;
		
		return gameRect;
	}
}
