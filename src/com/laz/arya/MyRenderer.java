package com.laz.arya;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView.Renderer;
//import android.os.SystemClock;

public class MyRenderer implements Renderer {

	Triangle mTriangle;
    
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
	
	@Override
	public void onDrawFrame(GL10 gl) {
		
		


	    // Draw triangle
	    mTriangle.draw(mMVPMatrix);
		
	      // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        

		// Set the camera position (View Matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        
        
		// Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
        
        
	    // Create a rotation transformation for the triangle
	    //long time = SystemClock.uptimeMillis() % 4000L;
	    //mAngle = 0.090f * ((int) time);
	    Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

	    // Combine the rotation matrix with the projection and camera view
	    Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
        
        // Draw shape
        mTriangle.draw(mMVPMatrix);

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
		
		
	    // initialize a triangle
	    mTriangle = new Triangle();
		
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
