package br.odb.nodehunter;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import br.odb.libscene.CameraNode;
import br.odb.utils.FileServerDelegate;
import br.odb.vintage.GameEngine;
import br.odb.vintage.ScenePresenter;

public class PlayGameActivity extends CardboardActivity implements FileServerDelegate {

    private static final String TAG = "MainActivity";
    private CardboardRenderer renderer;
    private ScenePresenter presenter;
    private CardboardView cardboardView;
    GameEngine engine;
    /**
     * Sets the view to our CardboardView and initializes the transformation matrices we will use
     * to render our scene.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.common_ui);
        cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        setCardboardView(cardboardView);

        initGameEngine( getIntent().getBooleanExtra( "VRMODE", false ), getIntent().getBooleanExtra( "MULTIPLAYER", false ) );
    }

    private void initGameEngine( boolean shouldUseVRMode, boolean runInMultiplayer ) {
        cardboardView.setVRModeEnabled( shouldUseVRMode );
        engine = new GameEngine( runInMultiplayer ? "192.168.1.70" : null );
        this.renderer = new CardboardRenderer( this );
        renderer.useVRMode = shouldUseVRMode;
        presenter = new GamePresentation( renderer );
        cardboardView.setRenderer( renderer );
        LevelLoader loader = new LevelLoader( engine, presenter, this, "prison.opt.ser" );
        loader.execute();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            finish();
            return true;
        }

        CameraNode cameraNode = presenter.renderer.getCurrentCameraNode();

        switch (keyCode) {

            case KeyEvent.KEYCODE_BUTTON_A:
                break;

            case KeyEvent.KEYCODE_BUTTON_L1:
                cameraNode.onStrafeLeft();
                break;

            case KeyEvent.KEYCODE_BUTTON_R1:
                cameraNode.onStrafeRight();
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                cameraNode.onLeft();
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                cameraNode.onRight();
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                cameraNode.onWalkForward();
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                cameraNode.onWalkBack();
                break;

            case KeyEvent.KEYCODE_COMMA:
                cameraNode.onStrafeLeft();
                break;
            case KeyEvent.KEYCODE_PERIOD:
                cameraNode.onStrafeRight();
                break;

            case KeyEvent.KEYCODE_A:
                cameraNode.onMoveUp();
                break;
            case KeyEvent.KEYCODE_Z:
                cameraNode.onMoveDown();
                break;
            case KeyEvent.KEYCODE_Q:
                System.exit(0);
                break;
            case KeyEvent.KEYCODE_BACK:
                return false;
        }
        this.cardboardView.invalidate();

        return true;
    }

    @Override
    public InputStream openAsInputStream(String s) throws IOException {
        return new FileInputStream( s );
    }

    @Override
    public InputStream openAsset(String s) throws IOException {
        return getAssets().open( s );
    }

    @Override
    public InputStream openAsset(int i) throws IOException {
        return openAsset( i );
    }

    @Override
    public OutputStream openAsOutputStream(String s) throws IOException {
        return new FileOutputStream( s );
    }

    @Override
    public void log(String s, String s1) {
        Log.d(s, s1);
    }
}
