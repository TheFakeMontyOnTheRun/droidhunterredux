package br.odb.gamelib.android.geometry;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLESVertexArrayManager {

    private int capacity;
    private int length;
    private FloatBuffer colorBuffer;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
//    private FloatBuffer normalBuffer;
    private int numFaces;
    boolean ready;


    public GLESVertexArrayManager( int polys ) {

        this.numFaces = polys;

        Log.d("bzk3", "init VA manager with " + numFaces + " positions");

        // 3 verteces with 3 floats for coordinates. each of those take 4 bytes.
        capacity = numFaces * (3 * 3 * 4);

        vertexBuffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder()).asFloatBuffer();
//        normalBuffer = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder()).asFloatBuffer();
        colorBuffer = ByteBuffer.allocateDirect(4 * capacity / 3 ).order(ByteOrder.nativeOrder()).asFloatBuffer();
	    textureBuffer = ByteBuffer.allocateDirect(2 * capacity / 3 ).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void uploadToGPU() {

        vertexBuffer.position(0);
//        normalBuffer.position(0);
        colorBuffer.position(0);
	    textureBuffer.position(0);
        ready = true;
    }

    final public void draw(int vertexHandle, int colorHandle, int normalHandle, int textureHandle) {

        if ( !ready ) {
            return;
        }
        GLES20.glEnableVertexAttribArray(vertexHandle);
        GLES20.glEnableVertexAttribArray(colorHandle);
//        GLES20.glEnableVertexAttribArray(normalHandle);

        GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);
//        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);

        if (textureHandle != -1) {
            textureBuffer.position(0);
            GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
            GLES20.glEnableVertexAttribArray(textureHandle);
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, length / 12);

        if ( textureHandle != -1) {
            GLES20.glDisableVertexAttribArray(textureHandle);
        }

        GLES20.glDisableVertexAttribArray(vertexHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);
//        GLES20.glDisableVertexAttribArray(normalHandle);


    }

    final public void pushIntoFrameAsStatic(float[] vertexData, float[] normalData, float[] colorData, float[] textureData) {
        try {

            if ( length < capacity ) {

                vertexBuffer.put(vertexData);
                length += vertexData.length * 4;

                for (int c = 0; c < (vertexData.length / 3); ++c) {
                    colorBuffer.put(colorData);
//                    normalBuffer.put( normalData );
                }

	            textureBuffer.put( textureData );
            }
        } catch (BufferOverflowException e) {
            e.printStackTrace();
            Log.d("bzk3", "length: " + length + " capacity: " + capacity);
            throw e;
        }
    }
}
