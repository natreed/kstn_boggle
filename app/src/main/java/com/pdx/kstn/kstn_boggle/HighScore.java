package com.pdx.kstn.kstn_boggle;

import android.widget.ListView;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class HighScore {
    private ArrayList<Tuple> scores;
    private ListView myList;

    HighScore () {
        loadScores();
    }

    public void loadScores() {
        // load scores from text file, update if player reaches new high scores
        // and update text file
        Resources res = getResources();
        Scanner s = new Scanner(res.openRawResource(R.raw.scores));
        scores = new ArrayList<Tuple>();
        String tempString;
        Integer tempInt;
        while(s.hasNext()) {
            s.useDelimiter(":");
            tempString = s.next();
            s.skip(":");
            s.useDelimiter("\n");
            tempInt = s.nextInt();
            scores.add(new Tuple(tempString, tempInt));
        }
    }

    public boolean updateScore(int score) {
        return false;
    }

    private void writeBack() {
        return;
    }
}
