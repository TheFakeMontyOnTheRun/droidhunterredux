package br.odb.robothunter.dalvik;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.odb.littlehelper3d.GameEngine;
import br.odb.utils.FileServerDelegate;

public class PlayLevelActivity extends Activity implements Runnable, FileServerDelegate {

	public static final String LEVEL_NAME_KEY = "level-name";
	GameEngine engine;
	Thread gameUpdateThread;
	AndroidCanvasGameRenderDelegate renderDelegate;
	AngstronDroidHunterGameEventDelegate gameEventDelegate;
	private long DEFAULT_TICK_TIME = 50;
	private AndroidMeshFactory meshFactory;
	
	/**
	 * Default entry point for Android Activity - just going to check for parameters and pass on...
	 * @see startLevel
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_level);
		
		Bundle bundle = getIntent().getExtras();
		String levelName = bundle.getString(LEVEL_NAME_KEY);
		
		if ( levelName != null ) {
			startLevel( levelName );
		} else {
			Toast.makeText( this, "A level must be selected", Toast.LENGTH_SHORT ).show();
			finish();
		}
	}
/**
 * startLevel - inits the game engine, sets the delegates and starts de level thread.
 * @param levelName
 */
	private void startLevel(String levelName) {
		meshFactory = new AndroidMeshFactory();
		
		engine = GameEngine.buildToPlayLevel( levelName, this, meshFactory );
		renderDelegate = new AndroidCanvasGameRenderDelegate( this );
		setContentView( renderDelegate );	
		gameUpdateThread = new Thread( this );
		engine.setRenderDelegate( renderDelegate );
		engine.setGameEventDelegate( gameEventDelegate );
	}

	
	/**
	 * finally starts the game and rendering thread
	 */
	@Override
	protected void onStart() {
	
		super.onStart();
		gameUpdateThread.start();
		Toast.makeText( this, "on start called", Toast.LENGTH_SHORT ).show();
	}
	
	@Override
	protected void onStop() {
		Toast.makeText( this, "on stop called", Toast.LENGTH_SHORT ).show();
		super.onStop();
	}
	
	/**
	 * Runs the game itself, by updating the engine and asking for a rendering
	 */
	@Override
	public void run() {
		
		while ( engine != null ) {
			
			engine.update( DEFAULT_TICK_TIME );
			engine.render();
		}
	}
	@Override
	public InputStream openAsInputStream(String filename) throws IOException {

		return openFileInput( filename );
	}
	
	@Override
	public OutputStream openAsOutputStream(String filename) throws IOException {
		return openFileOutput(filename, Context.MODE_PRIVATE );
	}
	
	@Override
	public InputStream openAsset(String filename) throws IOException {

		return getAssets().open(filename);
	}
	@Override
	public InputStream openAsset(int resId) throws IOException {
	
		return getResources().openRawResource( resId );
	}
	
	
	@Override
	public void log(String tag, String string) {
		Log.d(tag, string );		
	}
}
