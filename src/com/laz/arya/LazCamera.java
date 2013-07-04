package com.laz.arya;

import com.laz.utils.Vector3;

import android.opengl.Matrix;

public class LazCamera {

	
	float[] view;
	float[] projection;
	float[] vp;
	
	Vector3 eye;
	Vector3 center;
	Vector3 up;
	
	/** the viewport width **/
	public float viewportWidth = 0;
	/** the viewport height **/
	public float viewportHeight = 0;
	
	public LazCamera(){
		
		
		eye = new Vector3(0, 0, 4);
		center = new Vector3(0, 0, 0);
		up = new Vector3(0, 1, 0);
		
		view = new float[16];
		projection = new float[16];
		vp = new float[16];
		
	}
	
	public void update(){
		Matrix.perspectiveM(projection, 0, 45f, viewportWidth / viewportHeight, 0.1f, 10000f);
		Matrix.setLookAtM(view, 0, eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z);
		Matrix.multiplyMM(vp, 0, projection, 0, view, 0);
	}
	
	public void setViewport(float width, float height){
		this.viewportWidth = width;
		this.viewportHeight = height;
	}
	
}
