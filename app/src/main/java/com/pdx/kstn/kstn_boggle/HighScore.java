package com.pdx.kstn.kstn_boggle;

import android.widget.ListView;
import android.content.res.Resources;

import java.io.FileOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
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
        // load scores from text file
        Scanner s = new Scanner(("res/raw/scores.txt"));
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
        s.close();
    }

    //Check list of scores and update if new high score is available
    public boolean updateScore(String name, int score) {
        Integer newScore = score;
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
        }
        return false;
    }

    //write the scores back to the file
    // format is name: score\n
    private void writeBack() {
        try {
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

        }
        return;
    }
}
