package com.pdx.kstn.kstn_boggle;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by thanhhoang on 2/18/17.
 */

public class ResultAdapter extends BaseAdapter {
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    Activity activity;
    ArrayList<HashSet<String>> possible = new ArrayList<>();
//    ArrayList<String> possible;
    ArrayList<String> found;
    int score;

    public ResultAdapter(Activity activity, int score, ArrayList<String> possible, ArrayList<String> found) {
        super();
        this.activity = activity;
        this.score = score;
        this.found = found;

        HashSet<String> set = new HashSet<String>();
        int count = 0;
        for (String word: possible) {
            if (count >= 3) {
                this.possible.add(set);
                count = 0;
                set = new HashSet<String>();
            }
            count++;
            set.add(word);
        }
        this.possible.add(set);
    }

    @Override
    public int getCount() {
        return possible.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.items1, null);
            txtFirst = (TextView) view.findViewById(R.id.items_item1);
            txtSecond = (TextView) view.findViewById(R.id.items_item2);
            txtThird = (TextView) view.findViewById(R.id.items_item3);
        }

        HashSet<String> temp = possible.get(i);

        int count = 0;
        for (String str: temp) {
            count++;
            switch (count) {
                case 1:
                    txtFirst.setText(str);
                    if (found.contains(str)) {
                        txtFirst.setTextColor(Color.RED);
                    }
                    break;
                case 2:
                    txtSecond.setText(str);
                    if (found.contains(str)) {
                        txtSecond.setTextColor(Color.RED);
                    }
                    break;
                case 3:
                    txtThird.setText(str);
                    if (found.contains(str)) {
                        txtThird.setTextColor(Color.RED);
                    }
                    break;
            }

        }


//        int pos1, pos2, pos3;
//        pos1 = i*3 + 0;
//        pos2 = i*3 + 1;
//        pos3 = i*3 + 2;
//
//        if (pos1 < possible.size()) {
//            txtFirst.setText(pos1 + " " + possible.get(pos1));
//            txtFirst.setTextColor(Color.BLACK);
//            if (found.contains(possible.get(pos1))) {
//                txtFirst.setTextColor(Color.RED);
//            }
//        }
//
//        if (pos2 < possible.size()) {
//            txtSecond.setText(pos2 + " " + possible.get(pos2));
//            txtSecond.setTextColor(Color.BLACK);
//            if (found.contains(possible.get(pos2))) {
//                txtSecond.setTextColor(Color.RED);
//            }
//        }
//
//        if (pos3 < possible.size()) {
//            txtThird.setText(pos3 + " " + possible.get(pos3));
//            txtThird.setTextColor(Color.BLACK);
//            if (found.contains(possible.get(pos3))) {
//                txtThird.setTextColor(Color.RED);
//            }
//        }

        return view;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

}
