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

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListAdapter;

public class AndroidMoveListViewElementsActivity extends ListActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		List<String> elements = new ArrayList<String>();
		elements.add("111");
		elements.add("222");
		elements.add("333");
		elements.add("444");
		elements.add("555");
		elements.add("666");
		elements.add("777");
		elements.add("888");
		elements.add("999");
		elements.add("101010");
		elements.add("111111");
		elements.add("121212");
		elements.add("131313");
		elements.add("141414");
		elements.add("151515");
		elements.add("161616");
		elements.add("171717");
		ListAdapter adapter = new SwapListViewAdapter<String>(this,
				R.layout.row, R.id.tvText, R.id.ivSlide, elements);

		setListAdapter(adapter);

	}
}