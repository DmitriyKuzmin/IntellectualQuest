package com.kdp.quest.util;

import android.opengl.GLES20;

/**
 * Shader utilities
 *
 * @author DmitriyKuzmin <i>dmitriy.kuzmin910@gmail.com</i>
 * @version 1.0
 */
public class ShaderUtil {

    /**
     * Creating OpenGL program and loading vertex and fragment shader in her
     *
     * @param vertexSource   - vertex shader source
     * @param fragmentSource - fragment shader source
     * @return id created program
     */
    public static int createProgram(String vertexSource, String fragmentSource) {

        int vertexShader = ShaderUtil.loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        int fragmentShader = ShaderUtil.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);

        int shaderProgramId = GLES20.glCreateProgram();

        GLES20.glAttachShader(shaderProgramId, vertexShader);
        GLES20.glAttachShader(shaderProgramId, fragmentShader);
        GLES20.glLinkProgram(shaderProgramId);

        return shaderProgramId;
    }

    /**
     * Loading and compiling shader
     *
     * @param type         - shader type (GL_VERTEX_SHADER, GL_FRAGMENT_SHADER)
     * @param shaderSource - shader source
     * @return compiled shader
     */
    private static int loadShader(int type, String shaderSource) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderSource);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
