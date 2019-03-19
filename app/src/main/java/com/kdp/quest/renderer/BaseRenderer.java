package com.kdp.quest.renderer;

import android.opengl.Matrix;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 */
abstract class BaseRenderer {
    /**
     *
     */
    float[] localMvpMatrix = new float[16];
    /**
     *
     */
    float[] projectionMatrix = new float[16];
    /**
     *
     */
    float[] modelMatrix = new float[16];
    /**
     *
     */
    float[] translation = new float[16];
    /**
     *
     */
    float[] scale = new float[16];
    /**
     *
     */
    float[] rotation = new float[16];
    /**
     * Location Texture
     */
    float[] transform = new float[16];

    /**
     *
     */
    int shaderProgramId = 0;
    /**
     *
     */
    int positionHandle;
    /**
     *
     */
    int textureCoordHandle;
    /**
     *
     */
    int mvpMatrixHandle;
    /**
     *
     */
    int textureHandle;

    /**
     *
     */
    int[] textureNames;
    /**
     *
     */
    int[] textureHandles;

    /**
     *
     */
    FloatBuffer vertexBuffer;
    /**
     *
     */
    ShortBuffer indexBuffer;
    /**
     *
     */
    FloatBuffer textureCoordBuff;

    /**
     *
     */
    BaseRenderer() {
        Matrix.setIdentityM(localMvpMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(translation, 0);
        Matrix.setIdentityM(scale, 0);
        Matrix.setIdentityM(rotation, 0);
        Matrix.setIdentityM(transform, 0);
    }

    /**
     *
     * @param projectionMatrix - projection Matrix
     */
    public void setProjectionMatrix(float[] projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    /**
     *
     * @param x - x
     * @param y - y
     * @param z - z
     */
    public void setScale(float x, float y, float z) {
        Matrix.setIdentityM(scale, 0);
        Matrix.scaleM(scale, 0, x, y, z);
    }

    /**
     *
     * @param x - x
     * @param y - y
     * @param z - z
     */
    public void setTranslate(float x, float y, float z) {
        Matrix.setIdentityM(translation, 0);
        Matrix.translateM(translation, 0, x, y, z);
    }

    /**
     *
     * @param angle - angle
     * @param x - x
     * @param y - y
     * @param z - z
     */
    public void setRotation(float angle, float x, float y, float z) {
        Matrix.setIdentityM(rotation, 0);
        Matrix.rotateM(rotation, 0, angle, x, y, z);
    }

    /**
     *
     * @param transform - transform
     */
    public void setTransform(float[] transform) {
        System.arraycopy(transform, 0, this.transform, 0, transform.length);
    }

    /**
     *
     */
    public void draw() {
    }
}
