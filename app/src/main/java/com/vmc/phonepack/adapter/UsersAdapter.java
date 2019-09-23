package com.vmc.phonepack.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vmc.phonepack.activity.MainActivity;
import com.vmc.phonepack.model.User;
import com.vmc.phonepack.view.CustomTypefaceSpan;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {

    private static Typeface mTypeface_ThickFont;

    public UsersAdapter(Context context, List<User> object){
        super(context,0, object);
        mTypeface_ThickFont = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_bold.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView =  LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
        User user = getItem(position);

        String text1 = user.getName();
        textView.setText(getSpannableString(text1));

        String text2 = user.getCategory();
        // textView2.setText(getSpannableString(text2));
        return convertView;
    }

    private static SpannableString getSpannableString(String s){
        if(!TextUtils.isEmpty(MainActivity.mQuery) && getStartIndexOf(s) != -1) {
            SpannableString text = new SpannableString(s);
            int start = getStartIndexOf(s);
            int end = getEndIndexOf(s);
            CustomTypefaceSpan customTypefaceSpan = new CustomTypefaceSpan("", mTypeface_ThickFont);
            text.setSpan(customTypefaceSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);        //text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return text;
        }else{
            return new SpannableString(s);
        }
    }

    private static int getStartIndexOf(String s){
        int index = s.toLowerCase().indexOf(MainActivity.mQuery.toLowerCase());
        return index;
    }

    private static int getEndIndexOf(String string){
        int end = getStartIndexOf(string) + MainActivity.mQuery.length();
        if (end > string.length()) end = string.length();
        return end;
    }
}