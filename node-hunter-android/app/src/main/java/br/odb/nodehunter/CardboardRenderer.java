package br.odb.nodehunter;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;

import br.odb.gamelib.android.geometry.GLES1Triangle;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamelib.android.geometry.GLESVertexArrayManager;
import br.odb.liboldfart.WavefrontMaterialLoader;
import br.odb.liboldfart.WavefrontOBJLoader;
import br.odb.libscene.CameraNode;
import br.odb.libscene.LightNode;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.Material;
import br.odb.libstrip.TriangleMesh;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;
import br.odb.vintage.SceneRenderer;
import br.odb.vintage.actor.ActorSceneNode;

/**
 * Created by monty on 7/2/15.
 */
public class CardboardRenderer implements CardboardView.StereoRenderer, SceneRenderer {

    public final CameraNode cameraNode = new CameraNode( "mainCamera" );

    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 1000.0f;
    private static final float CAMERA_Z = 0.01f;

    final HashMap<Material, GLESVertexArrayManager > managers = new HashMap<>();

    private static final String TAG = CardboardRenderer.class.getSimpleName();

    private final Context context;

    private int defaultProgram;

    private int positionParam;
    private int colorParam;
    private int modelViewProjectionParam;

    private br.odb.utils.math.Matrix camera = new br.odb.utils.math.Matrix( 4, 4 );
    private br.odb.utils.math.Matrix view = new br.odb.utils.math.Matrix( 4, 4 );
    private br.odb.utils.math.Matrix modelViewProjection = new br.odb.utils.math.Matrix( 4, 4 );

    private float[] forwardVector = new float[ 3 ];

    public final ArrayList<TriangleMesh> meshes = new ArrayList<>();
    public final TriangleMesh sampleEnemy = new TriangleMesh( "sample-enemy" );
    final public List<ActorSceneNode> actors = new ArrayList<>();
    final public List<LightNode> lights = new ArrayList<>();

    volatile boolean ready = false;
    public boolean useVRMode = false;
    final HashMap<Material, ArrayList< GLES1Triangle> > staticGeometryToAdd= new HashMap<>();
    private int polycount = 10000;
    private float headAngleXZ;
    private float headAngleYZ;
    private float headAngleXY;
    private int lightPositionParam;
    private int lightNumberParam;
    private int normalParam;

    public CardboardRenderer(Context context) {
        this.context = context;
    }

    /**
     * Converts a raw text file, saved as a resource, into an OpenGL ES shader.
     *
     * @param type The type of shader we will be creating.
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The shader object handler.
     */
    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     */
    private static void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }


    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    /**
     * Creates the buffers we use to store information about the 3D world.
     *
     * <p>OpenGL doesn't use Java arrays, but rather needs data in a format it can understand.
     * Hence we use ByteBuffers.
     *
     * @param config The EGL configuration used when creating the surface.
     */
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well.

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, R.raw.light_vertex);
        int passthroughShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, R.raw.passthrough_fragment);

        defaultProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(defaultProgram, vertexShader);
        GLES20.glAttachShader(defaultProgram, passthroughShader);
        GLES20.glLinkProgram(defaultProgram);
        GLES20.glUseProgram(defaultProgram);

        positionParam = GLES20.glGetAttribLocation(defaultProgram, "a_Position");
        colorParam = GLES20.glGetAttribLocation(defaultProgram, "a_Color");
        lightPositionParam = GLES20.glGetAttribLocation(defaultProgram, "l_lights");
        lightNumberParam = GLES20.glGetAttribLocation(defaultProgram, "numLightsUsed");
        modelViewProjectionParam = GLES20.glGetUniformLocation(defaultProgram, "u_MVP");
        normalParam = GLES20.glGetAttribLocation(defaultProgram, "a_Normal");

        GLES20.glEnableVertexAttribArray(positionParam);
        GLES20.glEnableVertexAttribArray(colorParam);
        GLES20.glEnableVertexAttribArray(normalParam);

        // Object first appears directly in front of user.
        checkGLError("onSurfaceCreated");

        try {
            initDefaultMeshForActor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a raw text file into a string.
     *
     * @param resId The resource ID of the raw text file about to be turned into a shader.
     * @return The context of the text file, or null in case of error.
     */
    private String readRawTextFile(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prepares OpenGL ES before we draw a frame.
     *
     * @param headTransform The head transformation in the new frame.
     */
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        // Build the camera matrix and apply it to the ModelView.
        Matrix.setLookAtM(camera.values, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        checkGLError("onReadyToDraw");

        headAngleXZ = extractAngleXZFromHeadtransform(headTransform);
        headAngleYZ = extractAngleYZFromHeadtransform(headTransform);
        headAngleXY = extractAngleXYFromHeadtransform(headTransform);

        if ( this.useVRMode ) {
            cameraNode.angleXZ = headAngleXZ;

            while( cameraNode.angleXZ < 0 ) {
                cameraNode.angleXZ += 360.0;
            }

            while( cameraNode.angleXZ > 360 ) {
                cameraNode.angleXZ -= 360.0;
            }
        }
    }

    private float extractAngleXYFromHeadtransform(HeadTransform headTransform) {
        headTransform.getEulerAngles(forwardVector, 0);
        return  360.0f - ((float)( forwardVector[ 2 ] * ( 180 / Math.PI ) ));
    }

    private float extractAngleYZFromHeadtransform(HeadTransform headTransform) {
        headTransform.getEulerAngles(forwardVector, 0);
        return  360.0f - ((float)( forwardVector[ 0 ] * ( 180 / Math.PI ) ));
    }

    private float extractAngleXZFromHeadtransform(HeadTransform headTransform) {
        headTransform.getEulerAngles(forwardVector, 0);
        return  360.0f - ((float)( forwardVector[ 1 ] * ( 180 / Math.PI ) ));
    }

    public void initDefaultMeshForActor() throws IOException {
        TriangleMesh enemy;
        WavefrontMaterialLoader matLoader = new WavefrontMaterialLoader();
        List<Material> mats = matLoader.parseMaterials( context.getAssets().open( "gargoyle.mtl" ) );

        WavefrontOBJLoader loader = new WavefrontOBJLoader( new GLES1TriangleFactory() );
        ArrayList<TriangleMesh> mesh = (ArrayList<TriangleMesh>) loader.loadMeshes( context.getAssets().open("gargoyle.obj"), mats );

        enemy = mesh.get( 0 );

        for ( GeneralTriangle gt : enemy.faces ) {
            sampleEnemy.faces.add(GLES1TriangleFactory.getInstance().makeTrigFrom(gt));
        }
    }

    /**
     * Draws a frame for an eye.
     *
     * @param eye The eye to render. Includes all required transformations.
     */
    @Override
    public synchronized void onDrawEye(Eye eye) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        if ( ready ) {
            // Build the ModelView and ModelViewProjection matrices
            float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);

            GLES20.glUseProgram(defaultProgram);
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            // Apply the eye transformation to the camera.
            Matrix.multiplyMM(view.values, 0, eye.getEyeView(), 0, camera.values, 0);

            if (!useVRMode ) {
                Matrix.rotateM(view.values, 0, -headAngleXZ, 0, 1.0f, 0);
                Matrix.rotateM(view.values, 0, -headAngleYZ, 1.0f, 0.0f, 0);
                Matrix.rotateM(view.values, 0, -headAngleXY, 0.0f, 0.0f, 1.0f);
                Matrix.rotateM(view.values, 0, cameraNode.angleXZ, 0, 1.0f, 0);
            }

            Matrix.translateM(view.values, 0, -cameraNode.localPosition.x, -cameraNode.localPosition.y, -cameraNode.localPosition.z);
            Matrix.multiplyMM(modelViewProjection.values, 0, perspective, 0, view.values, 0);

            // Set the ModelViewProjection matrix in the shader.
            GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection.values, 0);

            float[] lightsBuffer = new float[ lights.size() * 4 ];

            int index = 0;
            Vec3 pos;
            for ( LightNode light : this.lights ) {
                pos = light.getAbsolutePosition();
                lightsBuffer[ index + 0 ] = pos.x;
                lightsBuffer[ index + 1 ] = pos.y;
                lightsBuffer[ index + 2 ] = pos.z;
                lightsBuffer[ index + 3 ] = 1;
                index += 4;
            }

            GLES20.glUniform1i( lightNumberParam, lights.size() );
            GLES20.glUniform4fv( lightPositionParam, lights.size(), lightsBuffer, 0 );

            drawPerMaterialStaticMesh();
            drawMeshes(eye);
        }
    }

    public void transform( Eye eye, float angleXZ, Vec3 trans) {
        float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
        Matrix.multiplyMM(view.values, 0, eye.getEyeView(), 0, camera.values, 0);

        if (!useVRMode ) {
            Matrix.rotateM(view.values, 0, -headAngleXZ, 0, 1.0f, 0);
            Matrix.rotateM(view.values, 0, -headAngleYZ, 1.0f, 0.0f, 0);
            Matrix.rotateM(view.values, 0, -headAngleXY, 0.0f, 0.0f, 1.0f);
            Matrix.rotateM(view.values, 0, cameraNode.angleXZ, 0, 1.0f, 0);
        }

        Matrix.translateM(view.values, 0, -cameraNode.localPosition.x, -cameraNode.localPosition.y, -cameraNode.localPosition.z);
        Matrix.translateM(view.values, 0, trans.x, trans.y, trans.z);
        Matrix.multiplyMM(modelViewProjection.values, 0, perspective, 0, view.values, 0);
    }

    private void drawMeshes( Eye eye ) {
        synchronized (meshes) {
            for ( ActorSceneNode actor : actors ) {

                transform( eye, actor.angleXZ, actor.localPosition );
                GLES20.glUniformMatrix4fv(modelViewProjectionParam, 1, false, modelViewProjection.values, 0);
                drawMeshGLES2(sampleEnemy);
            }
        }
    }

    /**
     * @param mesh
     */
    private void drawMeshGLES2(TriangleMesh mesh) {
        synchronized ( mesh ) {
            for (GeneralTriangle face : mesh.faces) {
                ((GLES1Triangle) face).drawGLES2(positionParam, colorParam, normalParam, -1);
            }
        }
    }

    private void drawPerMaterialStaticMesh() {

        GLESVertexArrayManager manager;

        for ( Material mat : managers.keySet() ) {

            manager = managers.get( mat );
            manager.draw(positionParam, colorParam, normalParam, -1);
        }
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void spawnDefaultActor(Vec3 vec3, float v) {

    }

    @Override
    public CameraNode getCurrentCameraNode() {
        return cameraNode;
    }

    @Override
    public void setDefaultMeshForActor(TriangleMesh generalTriangleMesh) {

    }

    public void addToVA(GLES1Triangle face) {

        if ( !staticGeometryToAdd.containsKey( face.material ) ) {
            staticGeometryToAdd.put(face.material, new ArrayList<GLES1Triangle>());

        }

        staticGeometryToAdd.get( face.material ).add(face);
    }

    @Override
    public void setAsReady() {
        ready = true;
    }

    public GLES1Triangle changeHue(GLES1Triangle trig) {
        trig.material = new Material(null, new Color(trig.material.mainColor), null, null );

        switch (trig.hint) {
            case W:
                trig.material.mainColor.multiply(0.8f);
                break;
            case E:
                trig.material.mainColor.multiply(0.6f);
                break;
            case N:
                trig.material.mainColor.multiply(0.4f);
                break;
            case S:
                trig.material.mainColor.multiply(0.2f);
                break;
            case FLOOR:
                trig.material.mainColor.multiply(0.9f);
                break;
            case CEILING:
                trig.material.mainColor.multiply(0.1f);
                break;
        }

        return trig;
    }

    void initManagerForMaterial( Material mat, int polys ) {

        GLESVertexArrayManager manager = new GLESVertexArrayManager( polys );
        managers.put( mat, manager );
    }

    @Override
    public void flush() {

        for ( Material m : staticGeometryToAdd.keySet() ) {
            initManagerForMaterial( m, staticGeometryToAdd.get( m ).size() );
            for ( GLES1Triangle t : staticGeometryToAdd.get( m ) ) {
                addToVAForReal( t );
                t.clear();
            }
            staticGeometryToAdd.get( m ).clear();
            staticGeometryToAdd.remove( staticGeometryToAdd.get( m ) );
        }

        for ( GLESVertexArrayManager manager : managers.values() ) {
            manager.uploadToGPU();
        }

        staticGeometryToAdd.clear();
        Log.i( "Renderer", "Flushed" );
    }

    @Override
    public synchronized void clearActors() {
        this.actors.clear();
    }

    @Override
    public void addActor(ActorSceneNode actorSceneNode) {
        this.actors.add( actorSceneNode );

        LightNode light = new LightNode( "light" + lights.size() );
        light.setPositionFromGlobal( actorSceneNode.getAbsolutePosition());
        addLight( light );
    }

    @Override
    public void addTriangleToStaticScene(GeneralTriangle generalTriangle) {
        GLES1Triangle glestrig = changeHue( GLES1TriangleFactory.getInstance().makeTrigFrom( generalTriangle ) );
        this.addToVA( glestrig );
    }

    @Override
    public void addLight(LightNode lightNode) {
        this.lights.add( lightNode );
    }

    private void addToVAForReal(GLES1Triangle face) {

        GLESVertexArrayManager manager;

        if ( !managers.containsKey( face.material ) ) {
            initManagerForMaterial( face.material, polycount / 10 );
        }

        manager = managers.get(face.material );
        Vec3 normal = face.makeNormal();
        float[] normalData = new float[]{ normal.x, normal.y, normal.z };
        manager.pushIntoFrameAsStatic(face.getVertexData(), normalData, face.material.mainColor.getFloatData());
    }
}
