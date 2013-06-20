package com.laz.arya;

import android.content.Context;
import android.view.ScaleGestureDetector;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

class MyGLSurfaceView extends GLSurfaceView implements ScaleGestureDetector.OnScaleGestureListener{
	
	private static final String LOG_TAG = "arya";
	
	private MyRenderer mRenderer;
   	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float mPreviousX;
	private float mPreviousY;
	private final ScaleGestureDetector gestureDetector;
	
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
		
		gestureDetector = new ScaleGestureDetector(context, this);
		
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
			
		// MotionEvent reports input details from the touch screen and other 
		// input controls. In this case, you are only interested in events where
		// touch position changed.
		
		float x = e.getX();
		float y = e.getY();
		
		switch(e.getAction()){
		case MotionEvent.ACTION_MOVE:
			
			float dx = x - mPreviousX;
			float dy = y - mPreviousY;
			
			// reverse direction of rotation above the mid-line
			if (y > getHeight()/2 ){
				dx = dx * -1;
				
			}	
			
			// reverse direction of rotation to the left of the mid-line
			if (x < getWidth()/2){
				dy = dy * -1;
			}
			
			mRenderer.mAngle += (dx + dy) * TOUCH_SCALE_FACTOR; // 180.f / 320
			requestRender();
			

		}
		mPreviousX = x;
		mPreviousY = y;
		return gestureDetector.onTouchEvent(e);
		
		
	}			

	@Override
	public boolean onScale(ScaleGestureDetector detector) {
		Log.d(LOG_TAG, "scaling...");
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) {
		Log.d(LOG_TAG, "Scale start!");
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) {
			Log.d(LOG_TAG, "Scale ended!");
		
	}

}