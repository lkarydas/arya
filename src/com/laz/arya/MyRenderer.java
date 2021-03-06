package com.laz.arya;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class MyRenderer implements Renderer {

	Triangle mTriangle;
	Sphere mSphere;
	//private Camera3D camera;
	public LazCamera camera;

	@Override
	public void onDrawFrame(GL10 gl) {

		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		camera.update();
		mTriangle.draw(camera.vp);
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		camera.setViewport(width, height);
        GLES20.glViewport(0, 0, width, height);
		camera.update();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		camera = new LazCamera();

		// initialize a triangle
		mTriangle = new Triangle();

		mSphere = new Sphere(1f, (short) 10, (short) 10);

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	public static int loadShader(int type, String shaderCode){

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	public LazCamera getCamera(){
		return this.camera;
	}
	
}
