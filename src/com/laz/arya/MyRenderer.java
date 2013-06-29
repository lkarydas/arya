package com.laz.arya;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;

public class MyRenderer implements Renderer {

	Triangle mTriangle;
	Sphere mSphere;

	private final float[] mProjMatrix = new float[16];
	// Declare as volatile because we are updating it from another thread
	public volatile float mAngle;

	private final float[] cameraPosition = new float[3];

	Camera3D camera;

	public void moveCamera(float step){
		cameraPosition[2] += step*10;
	}

	@Override
	public void onDrawFrame(GL10 gl) {

		// Redraw background color
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		camera.update();
		
		camera.printViewMatrix();
		
		mTriangle.draw(camera.combined);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		// This projection matrix is applied to object coordinates
		// in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		camera = new Camera3D();

		// initialize a triangle
		mTriangle = new Triangle();

		mSphere = new Sphere(1f, (short) 10, (short) 10);

		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
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

}
