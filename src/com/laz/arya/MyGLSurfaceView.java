package com.laz.arya;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

class MyGLSurfaceView extends GLSurfaceView {
	private static final String TAG = "AryaGL";
	public MyGLSurfaceView(Context context){
		super(context);

		// Set the Renderer for drawing on the GLSurfaceView
		Log.d(TAG, "Setting renderer...");
		setEGLContextClientVersion(2);
		setRenderer(new MyRenderer());
		// Render the view only when there is a change in the drawing data
		//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}
}