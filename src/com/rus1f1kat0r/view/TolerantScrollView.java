/*******************************************************************************
 * Copyright 2012 Kirill Kharkov
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
package com.rus1f1kat0r.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;
/**
 * The simple scroll view implementation that doesn't intercept all
 * the touch events, but dispatches them to it's children in order 
 * they could process touch events such as horizontal scrolling.
 * 
 * <p>The {@link TolerantScrollView} intercepts only touch events 
 * responsible for vertical scrolling (when there is no horizontal touch
 * event enough for scrolling horizontally but there is a touch event,
 * enough for vertical scrolling)
 *  
 * @author Rus1f1Kat0R
 *
 */
public class TolerantScrollView extends ScrollView {

	private int mLastX;
	private int mLastY;
	
	private int distanceX;
	private int distanceY;
	
	private int mTouchSlop;
	
	public TolerantScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TolerantScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TolerantScrollView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		/**
		 * define touch slop according to the display
		 */
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
	}
	/**
	 * Processes every touch event through the {@link #onTouchEvent(MotionEvent)}
	 * and intercepts if only we have restricted vertical scrolling.
	 * <p>
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		onTouchEvent(ev);
		int dy = 0;
		int dx = 0;
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			distanceX = 0;
			distanceY = 0;
			mLastX = (int) ev.getX();
			mLastY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			dx = Math.abs((int) (mLastX - ev.getX()));
			mLastX = (int) ev.getX();
			distanceX += dx;
			dy = Math.abs((int) (mLastY - ev.getY()));
			mLastY = (int) ev.getY();
			distanceY += dy;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			// define whether we have vertical scrolling 
			// without horizontal one and intercept if so
			if (distanceY > mTouchSlop && distanceX < mTouchSlop){
				Log.d(VIEW_LOG_TAG, "intercepted");
				return true;
			}		
			distanceX = 0;
			distanceY = 0;
			break;
		}
		return false;
	}
}
