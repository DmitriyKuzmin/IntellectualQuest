package com.kdp.quest.renderer;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.kdp.quest.TrackerRenderer;
import com.kdp.quest.util.ShaderUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ImageRender extends BaseRenderer {
    private static final String VERTEX_SHADER_SRC =
            "attribute vec4 a_position;\n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform mat4 u_mvpMatrix;\n" +
                    "void main()							\n" +
                    "{										\n" +
                    "	gl_Position = u_mvpMatrix * a_position;\n" +
                    "	v_texCoord = a_texCoord; 			\n" +
                    "}										\n";

    private static final String FRAGMENT_SHADER_SRC =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D u_texture;\n" +

                    "void main(void)\n" +
                    "{\n" +
                    "	gl_FragColor = texture2D(u_texture, v_texCoord);\n" +
                    "}\n";


    private static final float[] VERTEX_BUF = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };

    private static final short[] INDEX_BUF = {
            1, 0, 3, 3, 2, 1
    };

    private static final float[] TEXTURE_COORD_BUF = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };

    public ImageRender() {
        super();
        ByteBuffer bb = ByteBuffer.allocateDirect(VERTEX_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(VERTEX_BUF);
        vertexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(INDEX_BUF.length * Integer.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        indexBuffer = bb.asShortBuffer();
        indexBuffer.put(INDEX_BUF);
        indexBuffer.position(0);

        bb = ByteBuffer.allocateDirect(TEXTURE_COORD_BUF.length * Float.SIZE / 8);
        bb.order(ByteOrder.nativeOrder());
        textureCoordBuff = bb.asFloatBuffer();
        textureCoordBuff.put(TEXTURE_COORD_BUF);
        textureCoordBuff.position(0);

        shaderProgramId = ShaderUtil.createProgram(VERTEX_SHADER_SRC, FRAGMENT_SHADER_SRC);

        positionHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_position");
        textureCoordHandle = GLES20.glGetAttribLocation(shaderProgramId, "a_texCoord");
        mvpMatrixHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_mvpMatrix");
        textureHandle = GLES20.glGetUniformLocation(shaderProgramId, "u_texture");

        textureNames = new int[1];

        GLES20.glGenTextures(1, textureNames, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(shaderProgramId);

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false,
                0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(textureCoordHandle, 2, GLES20.GL_FLOAT, false,
                0, textureCoordBuff);
        GLES20.glEnableVertexAttribArray(textureCoordHandle);

        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.multiplyMM(modelMatrix, 0, translation, 0, rotation, 0);
        Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scale, 0);
        Matrix.multiplyMM(modelMatrix, 0, transform, 0, modelMatrix, 0);


        Matrix.multiplyMM(localMvpMatrix, 0, projectionMatrix, 0, modelMatrix, 0);
        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, localMvpMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureHandle, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, INDEX_BUF.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(textureCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    public void setImage(Bitmap image) {
        Log.d(TrackerRenderer.class.getSimpleName(), "size: " + image.getWidth() + "x" + image.getHeight());
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, image, 0);
    }
}
