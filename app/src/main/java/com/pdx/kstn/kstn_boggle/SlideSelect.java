package com.pdx.kstn.kstn_boggle;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

/**
 * Created by nathanreed on 2/18/17.
 */

public class SlideSelect {
    LinearLayout boardLayout;
    Button[] BoardButton;

    SlideSelect(LinearLayout boardLayout, Button[] BoardButton) {
        this.boardLayout = boardLayout;
        this.BoardButton = BoardButton;
    }


    private boolean checkInterSection(View view, int rawX, int rawY) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int width = view.getWidth();
        int height = view.getHeight();
        //Check the intersection of point with rectangle achieved
        return (!(rawX < x || rawX > x + width || rawY < y || rawY > y + height));
    }

//    for(int i = 0; i < touchview.getChildCount(); i++){
//        if(checkInterSection(touchview.getChildAt(i), event.getRawX(), event.getRawY())){
//            if(checkInterSection(touchview.getChildAt(i), event.getRawX(), event.getRawY())){
//                ((Button)touchview.getChildAt(i)).setBackgroundColor(Color.BLUE);// Type casting may not be required
//            }else{
//                ((Button)touchview.getChildAt(i)).setBackgroundColor(Color.WHITE);
//            }
//            break;
//        }
//    }

    //returns index to boardButton array
    public int buttonFinder(Button[] boardButtons,
                              /*for prints statements*/LinearLayout boardLayout, int x, int y) {
        for (int i = 0; i < boardButtons.length; i++) {
            Button button = boardButtons[i];
            if (this.checkInterSection(button, x, y) == true) {
                return i;
            }
        }
        return -1;
    }


}