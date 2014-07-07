/*
 * Copyright (C) 2011 Patrik �kerfeldt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bld.adapter;

import java.util.ArrayList;

import com.bld.data.DataBuilder;
import com.bld.widget.CircleFlowIndicator;
import com.bld.widget.ViewFlow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ViewFlowImageAdapter extends BaseAdapter {

	public LayoutInflater mInflater;
	public static final ArrayList<View> viewList = new ArrayList<View>();
	Integer tasksOver=0;
	private Context context;
	ViewFlow viewFlow;
	CircleFlowIndicator Indic;
	public ViewFlowImageAdapter(Context context) {
		context=context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		
	}
	
	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	
	

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView=viewList.get(position);
		return convertView;
	}

}
