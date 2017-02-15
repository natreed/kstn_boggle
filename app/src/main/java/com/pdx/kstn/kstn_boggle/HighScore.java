package com.pdx.kstn.kstn_boggle;

import android.widget.ListView;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static com.pdx.kstn.kstn_boggle.R.raw.dictionary;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class HighScore {
    private ArrayList<String> scores = new ArrayList<String>();

    public ArrayList<String> getScores() {
        return this.scores;
    }


    public void loadScores(InputStream in) {
        // load scores from text file

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
            in.close();
        } catch (Exception e) {

        }
    }

    //Check list of scores and update if new high score is available
    public boolean updateScore(String name, int score) {
        /*Integer newScore = score;
        Iterator<Tuple> tupleIterator = scores.iterator();
        while(tupleIterator.hasNext()) {
            Tuple<String, Integer> entry = tupleIterator.next();
            if(entry.getScore() < newScore) {
                tupleIterator.remove();
                scores.add(new Tuple(name, score));
                return true;
            }
            if(entry.getScore() == newScore) {
                scores.add(new Tuple(name, score));
                return true;
            }
        }*/
        return false;
    }

    //write the scores back to the file
    // format is name: score\n
    private void writeBack() {
        /*try {
            File file = new File("res/raw/scores.txt");
            FileOutputStream fos = new FileOutputStream(file, false);
            Iterator<Tuple> tupleIterator = scores.iterator();
            while(tupleIterator.hasNext()) {
                Tuple<String, Integer> current = tupleIterator.next();
                String tempString = current.getName();
                Integer tempInt = current.getScore();
                tempString = tempString + ": " + tempInt.toString() +"\n";
                byte[] content = tempString.getBytes();
                fos.write(content);
            }
        } catch (Exception e) {

        }*/
        return;
    }
}
