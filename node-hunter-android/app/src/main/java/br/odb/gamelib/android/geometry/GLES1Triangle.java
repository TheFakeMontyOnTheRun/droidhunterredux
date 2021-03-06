package br.odb.gamelib.android.geometry;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import br.odb.libstrip.GeneralTriangle;
import br.odb.gameutils.Color;
import br.odb.gameutils.math.Vec3;

import static android.opengl.GLES10.glColorPointer;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glVertexPointer;

/**
 * @author monty
 */
public class GLES1Triangle extends GeneralTriangle {

    private FloatBuffer textureBuffer;
    private FloatBuffer colorBuffer;
    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;

    private float[] color = new float[12];
    public int light = 0;

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void draw(GL10 gl) {

        gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, getVertexData().length / 3);

    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void draw() {

        glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
        glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        glDrawArrays(GL10.GL_TRIANGLES, 0, getVertexData().length / 3);
    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    @Override
    public void flush() {

        super.flush();

        float[] vertices = getVertexData();
        Vec3 normal = makeNormal();
        float[] normals = new float[ 3 ];
        float[] oneColor;
        float[] normalData;

        normals[ 0 ] = normal.x;
        normals[ 1 ] = normal.y;
        normals[ 2 ] = normal.z;

        if (material != null) {
            oneColor = material.mainColor.getFloatData();
        } else {
            oneColor = new Color(0xFFFFFFFF).getFloatData();
        }

        for (int c = 0; c < 3; ++c) {
            for (int d = 0; d < 4; ++d) {
                color[(c * 4) + (d)] = oneColor[d];
            }
        }

        normalData = new float[ 9 ];

        normalData[ 0 ] = normals[ 0 ];
        normalData[ 1 ] = normals[ 1 ];
        normalData[ 2 ] = normals[ 2 ];
        normalData[ 3 ] = normals[ 0 ];
        normalData[ 4 ] = normals[ 1 ];
        normalData[ 5 ] = normals[ 2 ];
        normalData[ 6 ] = normals[ 0 ];
        normalData[ 7 ] = normals[ 1 ];
        normalData[ 8 ] = normals[ 2 ];

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(normalData.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        normalBuffer.put(normalData);
        normalBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(color.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        colorBuffer = byteBuf.asFloatBuffer();
        colorBuffer.put(color);
        colorBuffer.position(0);

    }

    /**
     *
     */
    @Override
    public GLES1Triangle makeCopy() {
        GLES1Triangle t = new GLES1Triangle();
        t.material = material;
        t.x0 = x0;
        t.x1 = x1;
        t.x2 = x2;

        t.y0 = y0;
        t.y1 = y1;
        t.y2 = y2;

        t.z0 = z0;
        t.z1 = z1;
        t.z2 = z2;

        t.flush();

        return t;
    }

    public void drawGLES2(int vertexHandle, int colorHandle, int normalHandle, int textureHandle) {

        GLES20.glEnableVertexAttribArray(vertexHandle);
        GLES20.glEnableVertexAttribArray(colorHandle);

        GLES20.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT, false, 0, colorBuffer);


//        GLES20.glVertexAttribPointer(normalHandle, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);
//
//        GLES20.glEnableVertexAttribArray(normalHandle);


//        if (textureHandle != -1 && textureBuffer != null) {
//
//            textureBuffer.position(0);
//			GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT,
//                    false, 0, textureBuffer);
//
//			GLES20.glEnableVertexAttribArray(textureHandle);
//
//        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        GLES20.glDisableVertexAttribArray(vertexHandle);
        GLES20.glDisableVertexAttribArray(colorHandle);

    }


    // ------------------------------------------------------------------------------------------------------------

    public Vec3 makeNormal() {

        Vec3 v1;
        Vec3 v2;
        Vec3 vn;

        v1 = new Vec3(x1 - x0, y1 - y0, z1 - z0);
        v2 = new Vec3(x2 - x0, y2 - y0, z2 - z0);

        vn = v1.crossProduct(v2);

        return vn.normalized();
    }

    // ------------------------------------------------------------------------------------------------------------

    /**
     *
     */
    public void flatten(float z) {
        z0 = z1 = z2 = z;
    }

    public void flatten() {

        flatten(-1.2f);
    }

    public void multiplyColor(float factor) {
        for (int c = 0; c < color.length; ++c) {
            color[c] *= factor;
        }
    }

//    public void setTextureCoordenates(float[] fs) {
//
//		this.textureCoordinates = fs;
//
//		ByteBuffer byteBuf = ByteBuffer.allocateDirect(getVertexData().length * 4);
//		byteBuf = ByteBuffer.allocateDirect(color.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		textureBuffer = byteBuf.asFloatBuffer();
//		textureBuffer.put(textureCoordinates);
//		textureBuffer.position(0);
//
//    }

    public void clear() {
        color = null;
    }
}
