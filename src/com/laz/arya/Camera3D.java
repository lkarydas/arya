


	
	/*******************************************************************************
	 * Copyright 2011 See AUTHORS file.
	 * 
	 * Licensed under the Apache License, Version 2.0 (the "License");
	 * you may not use this file except in compliance with the License.
	 * You may obtain a copy of the License at
	 * 
	 *   http://www.apache.org/licenses/LICENSE-2.0
	 * 
	 * Unless required by applicable law or agreed to in writing, software
	 * distributed under the License is distributed on an "AS IS" BASIS,
	 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	 * See the License for the specific language governing permissions and
	 * limitations under the License.
	 ******************************************************************************/


	/** Base class for {@link OrthographicCamera} and {@link PerspectiveCamera}.
	 * @author mzechner */

package com.laz.arya;

import java.util.Locale;

import android.opengl.Matrix;
import android.util.Log;

import com.laz.utils.*;

	public class Camera3D {
		/** the position of the camera **/
		public final Vector3 position = new Vector3();
		/** the unit length direction vector of the camera **/
		public final Vector3 direction = new Vector3(0, 0, -1);
		/** the unit length up vector of the camera **/
		public final Vector3 up = new Vector3(0, 1, 0);

		/** the projection matrix **/
		public final Matrix4 projection = new Matrix4();
		/** the view matrix **/
		public final Matrix4 view = new Matrix4();
		/** the combined projection and view matrix **/
		public final Matrix4 combined = new Matrix4();
		/** the inverse combined projection and view matrix **/
		public final Matrix4 invProjectionView = new Matrix4();

		/** the near clipping plane distance, has to be positive **/
		public float near = 0.1f;
		/** the far clipping plane distance, has to be positive **/
		public float far = 1000;

		/** the viewport width **/
		public float viewportWidth = 0;
		/** the viewport height **/
		public float viewportHeight = 0;



		private final Vector3 tmpVec = new Vector3();
		private final Vector3 tmp = new Vector3();

		
		public void setViewport(float width, float height){
			this.viewportWidth = width;
			this.viewportHeight = height;
		}
		
		public Camera3D(){
			position.set(0, 0, 10);
			
			Log.i("3d_stuff", "position: " + position.toString());
			Log.i("3d_stuff", "direction: " + direction.toString());
			
		}
		
		public void update(){
			float aspect = viewportWidth / viewportHeight;
			//TODO: Field of view has to be parameter
			// Set projection matrix
			projection.setToProjection(Math.abs(near), Math.abs(far), 67, aspect);
			
			projection.print("Projection matrix");
			// Set view matrix
			Log.i("3d_stuff", "position: " + position.toString());
			Log.i("3d_stuff", "direction: " + direction.toString());
			view.setToLookAt(position, tmp.set(position).add(direction), up);
			combined.set(projection);
			Matrix4.mul(combined.val, view.val);
		}



		/** Recalculates the direction of the camera to look at the point (x, y, z).
		 * @param x the x-coordinate of the point to look at
		 * @param y the x-coordinate of the point to look at
		 * @param z the x-coordinate of the point to look at */
		public void lookAt (float x, float y, float z) {
			direction.set(x, y, z).sub(position).nor();
			normalizeUp();
		}
		
		/** Recalculates the direction of the camera to look at the point (x, y, z).
		 * @param target the point to look at */
		public void lookAt (Vector3 target) {
			direction.set(target).sub(position).nor();
			normalizeUp();
		}

		/** Normalizes the up vector by first calculating the right vector via a cross product between direction and up, and then
		 * recalculating the up vector via a cross product between right and direction. */
		public void normalizeUp () {
			tmpVec.set(direction).crs(up).nor();
			up.set(tmpVec).crs(direction).nor();
		}

		/** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
		 * will not be orthogonalized.
		 * 
		 * @param angle the angle
		 * @param axisX the x-component of the axis
		 * @param axisY the y-component of the axis
		 * @param axisZ the z-component of the axis */
		public void rotate (float angle, float axisX, float axisY, float axisZ) {
			direction.rotate(angle, axisX, axisY, axisZ);
			up.rotate(angle, axisX, axisY, axisZ);
		}

		/** Rotates the direction and up vector of this camera by the given angle around the given axis. The direction and up vector
		 * will not be orthogonalized.
		 * 
		 * @param axis
		 * @param angle the angle */
		public void rotate (Vector3 axis, float angle) {
			direction.rotate(axis, angle);
			up.rotate(axis, angle);
		}

		/** Rotates the direction and up vector of this camera by the given rotation matrix. The direction and up vector
		 * will not be orthogonalized.
		 * 
		 * @param transform The rotation matrix */
		public void rotate(final Matrix4 transform) {
			direction.rot(transform);
			up.rot(transform);
		}
		
		/** Rotates the direction and up vector of this camera by the given {@link Quaternion}. The direction and up vector
		 * will not be orthogonalized.
		 * 
		 * @param quat The quaternion */
		public void rotate(final Quaternion quat) {
			quat.transform(direction);
			quat.transform(up);
		}

		/** Rotates the direction and up vector of this camera by the given angle around the given axis, with the axis attached to given
		 * point. The direction and up vector will not be orthogonalized.
		 * 
		 * @param point
		 * @param axis
		 * @param angle the angle */
		public void rotateAround (Vector3 point, Vector3 axis, float angle) {
			tmpVec.set(point);
			tmpVec.sub(position);
			translate(tmpVec);
			rotate(axis, angle);
			tmpVec.rotate(axis, angle);
			translate(-tmpVec.x, -tmpVec.y, -tmpVec.z);
		}
		
		/** Transform the position, direction and up vector by the given matrix
		 * 
		 * @param transform The transform matrix */
		public void transform(final Matrix4 transform) {
			position.mul(transform);
			rotate(transform);
		}

		/** Moves the camera by the given amount on each axis.
		 * @param x the displacement on the x-axis
		 * @param y the displacement on the y-axis
		 * @param z the displacement on the z-axis */
		public void translate (float x, float y, float z) {
			position.add(x, y, z);
		}

		/** Moves the camera by the given vector.
		 * @param vec the displacement vector */
		public void translate (Vector3 vec) {
			position.add(vec);
		}

		public void printViewMatrix() {
			Log.i("3D_stuff", "View Matrix:");
			int j = 0;
			String line;
			for (int i = 0; i < 4; i++){
				line = String.format(Locale.US,"%.2f %.2f %.2f %.2f", view.val[j++], view.val[j++], view.val[j++], view.val[j++]);
				Log.i("3D_stuff", line);
			}
			
		}
	} 	

