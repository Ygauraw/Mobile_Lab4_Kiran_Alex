package com.example.trackit;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

//custom adapter class to use for expandable listview (basically the main UI content)
public class ExpandableAppsListAdapter extends BaseExpandableListAdapter {
	
    private Context mContext;
    
    //labels for app (productive, unproductive, unlabeled)
    private List<String> _labels;
    
    //map of apps by label, app name
    private HashMap<String, List<String>> _appsByLabel;
    
    //constructor
    public ExpandableAppsListAdapter(Context context, List<String> labels,
            HashMap<String, List<String>> appsByLabel) {
        this.mContext = context;
        this._labels = labels;
        this._appsByLabel = appsByLabel;
    }

    //get a specific app based on which label and app usage ranking
	@Override
	public Object getChild(int labelPosition, int appPosition) {
        return this._appsByLabel.get(this._labels.get(labelPosition))
                .get(appPosition);
	}

	//return the position of an app as long
	@Override
	public long getChildId(int labelPosition, int appPosition) {
		return appPosition;
	}

	//get the view a specific application
	@Override
	public View getChildView(int labelPosition, int appPosition, boolean isLastApp,
			View convertView, ViewGroup parent) {

        final String appName = (String) getChild(labelPosition, appPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.apps_lists, null);
        }
        
        TextView appsList = (TextView) convertView
                .findViewById(R.id.apps_lists);
 
        appsList.setText(appName);
        return convertView;
	}

	//get number of apps in a given label
	@Override
	public int getChildrenCount(int labelPosition) {
        return this._appsByLabel.get(this._labels.get(labelPosition))
                .size();
	}

	//get a group of apps from a specific label
	@Override
	public Object getGroup(int labelPosition) {
		return this._labels.get(labelPosition);
	}

	//get total number of labels
	@Override
	public int getGroupCount() {
		return this._labels.size();
	}

	//get the position of a given label as long
	@Override
	public long getGroupId(int labelPosition) {
		return labelPosition;
	}

	//get the view of all the labels
	@Override
	public View getGroupView(int labelPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

        String labelTitle = (String) getGroup(labelPosition);
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.app_list_groups, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.app_list_label_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(labelTitle);
 
        return convertView;	
    }

	//stable IDs?
	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int labelPosition, int appPosition) {
		return true;
	}

}
