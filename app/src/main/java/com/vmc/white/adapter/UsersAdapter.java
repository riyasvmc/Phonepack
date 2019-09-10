package com.vmc.white.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vmc.white.R;
import com.vmc.white.model.User;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    public UsersAdapter(Context context, List<User> object){
        super(context,0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        // TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
        User user = getItem(position);
        textView.setText(user.getName() + " " + user.getPhone());
        //textView2.setText(user.getCategory() + " : " + user.getPhone());
        return convertView;
    }

}