package com.pdx.kstn.kstn_boggle;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;


import static com.pdx.kstn.kstn_boggle.R.raw.dictionary;
import static com.pdx.kstn.kstn_boggle.R.raw.scores;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class HighScore {
    public ArrayList<String> scores = new ArrayList<String>();

    public Context activity_context;

    HighScore(Context context) {
        this.activity_context = context;
        try {
            FileOutputStream test = activity_context.openFileOutput("scores.txt", Context.MODE_APPEND);
            test.close();
        } catch (Exception e) { e.printStackTrace(); }
        loadScores();
    }

    public int lowestScore() {
        if(scores.isEmpty()) return 0;
        String lowestScore = scores.get(scores.size() -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lowestScore.length(); i++) {
            char c = lowestScore.charAt(i);
            if (Character.isDigit(c)) sb.append(c);
        }
        return Integer.decode(sb.toString());
    }

    private void loadScores() {
        // load scores from text file
        try {
            FileInputStream fis = activity_context.openFileInput("scores.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                scores.add(line);
            }
            System.out.println(scores);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Check list of scores and update if new high score is available
    //Always call writeBack after calling update scores for persistence
    public boolean updateScore(String name, int score) throws IOException{
        if(scores.isEmpty()) {
            scores.add(name + ": " + score);
            writeBack();
            return true;
        }
        Iterator<String> it = scores.iterator();
        int track = 0;

        while (it.hasNext()) {
            String currentString = "";
            String temp = it.next();
            System.out.println(temp);
            int len = temp.length();
            // this for loops is necessary to extract the score section from
            //// the entire string stored in the txt file. The String is of form
            //// "name: 00" name is of arbitrary so the loops looks through the
            //// String for digits.
            //
            for (int i = 0; i < len; i++) {
                char c = temp.charAt(i);
                if (Character.isDigit(c)) currentString = currentString + c;
            }
            //Int form of the score we found in the list
            Integer currentScore = Integer.decode(currentString);
            //
            //// if the score is higher than our current score in the list
            //// remove current score and add it at the current index.
            //
            System.out.println("score = " + score + " currentscore = " + currentScore);
            if (score >= currentScore) {
                System.out.println("Found High Score\n");
                scores.add(track, name + ": " + score);
                if (scores.size() > 5) scores.remove(scores.size() - 1);
                writeBack();
                return true;
            }
            track++;
        }
        if(scores.size() < 5) {
            scores.add(name + ": " + score);
            writeBack();
            System.out.println("High Score less than five");
        }
        return false;

    }

    public void resetScores() throws IOException {
        scores.clear();
        writeBack();
        return;
    }


    //write the scores back to the file
    // format is name: score\n
    private void writeBack() throws IOException {
        try {
            FileOutputStream fos = activity_context.openFileOutput("scores.txt", Context.MODE_PRIVATE);
            Iterator<String> it = scores.iterator();
            while(it.hasNext()) {
                fos.write(it.next().toString().getBytes());
                fos.write('\n');
            }
            System.out.println(scores);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }
}
