package com.laz.arya;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener{
	
	private static final String LOG_TAG = "arya";

	private MyRenderer mRenderer;
	private float mPreviousX;
	private float mPreviousY;
	private final ScaleGestureDetector gestureDetector;

	private static boolean scalingInProgress;
	
	private static final String TAG = "AryaGL";
	public MyGLSurfaceView(Context context){
		super(context);

		// Set the Renderer for drawing on the GLSurfaceView
		Log.d(TAG, "Setting renderer...");
		setEGLContextClientVersion(2);
		mRenderer = new MyRenderer();
		setRenderer(mRenderer);
		// Render the view only when there is a change in the drawing data
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		
		scalingInProgress = false;
		gestureDetector = new ScaleGestureDetector(context, this);
		
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		gestureDetector.onTouchEvent(e);
		
		if (scalingInProgress)
			return true;

		// MotionEvent reports input details from the touch screen and other 
		// input controls. In this case, you are only interested in events where
		// touch position changed.
		
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()){
		case MotionEvent.ACTION_MOVE:
			
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			
			
			//mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR; // 180.f / 320
			mRenderer.getCamera().setYawAndPitch(dx, dy);
			
			Log.i("ACTION_MOVE","("+ dx+", "+dy+")");

			
			requestRender();
			

		}
		mPreviousX = x;
		mPreviousY = y;
		
		return true;

	}			

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
	}

}