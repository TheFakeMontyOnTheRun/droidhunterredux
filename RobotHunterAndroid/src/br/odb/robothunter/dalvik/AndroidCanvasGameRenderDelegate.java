package br.odb.robothunter.dalvik;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.View;
import br.odb.gameworld.Direction;
import br.odb.littlehelper3d.GameRenderDelegate;
import br.odb.littlehelper3d.GameSector;
import br.odb.littlehelper3d.GameSnapshot;

public class AndroidCanvasGameRenderDelegate extends View implements GameRenderDelegate {

	GameSnapshot lastSnapshotAvailable;
	
	public AndroidCanvasGameRenderDelegate(Context context) {
		super(context);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
	
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setStyle( Style.FILL );
		
		if ( lastSnapshotAvailable != null ) {
			
			for ( GameSector gs : lastSnapshotAvailable.sectors ) {
				paint.setColor( gs.color[ Direction.CEILING.ordinal() ].getARGBColor() );
				canvas.drawRect( gs.p0.x, gs.p0.z, gs.p1.x, gs.p1.z, paint );
			}
			
			paint.setColor( 0xFF0000FF );
			
//			for ( GameActor ga : lastSnapshotAvailable.actors ) {
//				canvas.drawRect( ga.position.x, ga.position.z, ga.position.x + 5, ga.position.z + 5, paint );
//			}
		}
	}

	@Override
	public void render(GameSnapshot snapshot) {
		this.lastSnapshotAvailable = snapshot;		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
	
		return super.onKeyUp(keyCode, event);
	}
}
