/*
 * Copyright (C) 2012 Rasmus Zweidorff Iversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package dk.rasive.androidMoveListViewElements;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SwapListViewAdapter<T> extends ArrayAdapter<T>
{
	private Context mContext;
	private int mResource;
	private int mTextViewResourceId;
	private int mSlideViewResourceId;

	public SwapListViewAdapter (Context context, int resource,
			int textViewResourceId, int slideViewResourceId, List<T> objects)
	{
		super(context, textViewResourceId, objects);
		mContext = context;
		mResource = resource;
		mTextViewResourceId = textViewResourceId;
		mSlideViewResourceId = slideViewResourceId;
	}

	@Override
	public View getView (final int position, View convertView, ViewGroup parent)
	{
		// Inflate the view if it's null
		if (convertView == null)
		{
			LayoutInflater layoutInflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(mResource, null);
		}

		// Set the view´s text to be that from our list according to the
		// position
		((TextView) convertView.findViewById(mTextViewResourceId))
				.setText(getItem(position).toString());

		final ImageView ivSlide = (ImageView) convertView
				.findViewById(mSlideViewResourceId);

		ivSlide.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch (View v, MotionEvent event)
			{

				// If we move outside the scope, or lift our finger, then call
				// swapView()
				if (event.getAction() == MotionEvent.ACTION_CANCEL
						|| event.getAction() == MotionEvent.ACTION_UP)
				{
					// Half the height of ivSlide minus where our finger is on
					// ivSlide according to the event. If it's greater than
					// zero, we are moving our finger downwards, otherwise
					// upwards. This indicate in which direction we are
					// swapping.
					int swapPosition = position + 1;
					if (ivSlide.getHeight() / 2 - event.getY() > 0)
						swapPosition = position - 1;

					// A context can also be an activity, in this case it is
					// true
					ListView listView = ((ListActivity) mContext).getListView();

					// Select the view to be swaped with, relative to the view
					// we are pressing
					//
					// We need to substract the position of the first visible
					// position in the ListView to get the right swapView when
					// the user has scrolled
					View swapView = listView.getChildAt(swapPosition
							- listView.getFirstVisiblePosition());

					// Swap the two selected views with each other
					// getParent() is used because we need to select the row,
					// not just ivSlide
					swapViews((View) v.getParent(),
							swapView,
							position,
							swapPosition);

					return false;
				}

				return true;
			}
		});

		return convertView;
	}

	private void swapViews (final View view, final View swapView,
			final int position, final int swapPosition)
	{
		// If we weren't able to get the swapView - most likely because it
		// didn't exist because the first or last view was selected - then
		// return the method
		if (swapView == null)
			return;

		int duration = 300;
		int direction = -(view.getHeight());
		int swapDirection = swapView.getHeight();

		// Switch the directions if we are swapping downwards
		if (swapPosition > position)
		{
			direction = -direction;
			swapDirection = -swapDirection;
		}

		Animation animation = new TranslateAnimation(0, 0, 0, direction);
		animation.setDuration(duration);

		animation.setAnimationListener(new Animation.AnimationListener()
		{

			@Override
			public void onAnimationStart (Animation animation)
			{}

			@Override
			public void onAnimationRepeat (Animation animation)
			{}

			@Override
			public void onAnimationEnd (Animation animation)
			{
				// This is to remove the flicker which would otherwise occur
				view.clearAnimation();
				// Now we swap the elements in the actual list
				swapElements(position, swapPosition);
			}
		});

		Animation animationSwap = new TranslateAnimation(0, 0, 0, swapDirection);
		animationSwap.setDuration(duration);
		animationSwap.setAnimationListener(new Animation.AnimationListener()
		{

			@Override
			public void onAnimationStart (Animation animation)
			{}

			@Override
			public void onAnimationRepeat (Animation animation)
			{}

			@Override
			public void onAnimationEnd (Animation animation)
			{
				// This is to remove the flicker which would otherwise occur
				swapView.clearAnimation();
			}
		});

		view.startAnimation(animation);
		swapView.startAnimation(animationSwap);
	}

	private void swapElements (int position, int swapPosition)
	{
		// If swapPosition is greater than the last
		// element of our list, or less than 0, then return the method
		if (swapPosition > getCount() - 1 || swapPosition < 0)
			return;

		T element = getItem(position);
		T swapElement = getItem(swapPosition);

		remove(element);
		remove(swapElement);

		// If we are swapping downwards, then...
		//
		// Also, remember, the order here is pretty important
		if (swapPosition > position)
		{
			insert(swapElement, position);
			insert(element, swapPosition);
		}
		else
		{
			insert(element, swapPosition);
			insert(swapElement, position);
		}
	}
}
