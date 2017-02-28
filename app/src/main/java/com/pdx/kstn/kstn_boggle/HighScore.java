package com.pdx.kstn.kstn_boggle;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import static com.pdx.kstn.kstn_boggle.R.raw.dictionary;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class HighScore {
    private ArrayList<String> scores = new ArrayList<String>();

    public ArrayList<String> getScores() {
        return this.scores;
    }


    public void loadScores(FileInputStream fis) {
        // load scores from text file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
            fis.close();
        } catch (Exception e) {
            System.out.println("Load scores fail\n");
        }
    }

    //Check list of scores and update if new high score is available
    //Always call writeBack after calling update scores for persistence
    public boolean updateScore(String name, int score) {
        Iterator<String> it = scores.iterator();
        StringBuilder sb = new StringBuilder();
        int track = 0;
        while (it.hasNext()) {
            String temp = it.next();
            int len = temp.length();
            /* this for loops is necessary to extract the score section from
            // the entire string stored in the txt file. The String is of form
            // "name: 00" name is of arbitrary so the loops looks through the
            // String for digits.
             */
            for (int i = 0; i < len; i++) {
                char c = temp.charAt(i);
                if (Character.isDigit(c)) sb.append(c);
            }
            //Int form of the score we found in the list
            Integer currentScore = Integer.decode(sb.toString());
            /*
            // if the score is higher than our current score in the list
            // remove current score and add it at the current index.
             */
            if (score >= currentScore) {
                StringBuilder newSb = new StringBuilder(name);
                newSb.append(": ");
                newSb.append(score);
                //it.remove();
                scores.add(track, newSb.toString());
                scores.remove(scores.size()-1);
                return true;
            }
            track++;
        }
        return false;
    }


    //write the scores back to the file
    // format is name: score\n
    public void writeBack(FileOutputStream fos) {
        try {
            Iterator<String> it = scores.iterator();
            while(it.hasNext()) {
                fos.write(it.next().toString().getBytes());
                fos.write('\n');
            }
            fos.close();
        } catch (Exception e) {
            System.out.println("Write back failure\n");
        }
        return;
    }
}
