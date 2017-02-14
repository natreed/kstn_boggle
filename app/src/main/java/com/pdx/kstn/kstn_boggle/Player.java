package com.pdx.kstn.kstn_boggle;

import java.util.ArrayList;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class Player {

    private int score;
    private int scoreForCurrentRound;
    private int round;
    private ArrayList<String> foundWords;
    private ArrayList<String> foundWordsCurrentRound;

    Player() {
        this.score = 0;
        this.scoreForCurrentRound = 0;
        this.round = 1;
        this.foundWords = new ArrayList<String>();
        this.foundWordsCurrentRound = new ArrayList<String>();
    }

    public int getScore() { return this.score; }

    public int getScoreForCurrentRound() { return this.scoreForCurrentRound; }

    public int getRound() { return this.round; }

    public ArrayList<String> getFoundWords() { return this.foundWords; }

    public ArrayList<String> getFoundWordsCurrentRound() { return foundWordsCurrentRound; }


//    static public int getNewTimer(int timeLeft, int score) {
//        return timeLeft + score;
//    }
    public int getNumWordLastRound() {
        return foundWordsCurrentRound.size();
    }

    public void moveToNextRound() {
        this.scoreForCurrentRound = 0;
        round += 1;
        this.foundWordsCurrentRound = new ArrayList<String>();
    }

    /**
     * Update scores and word lists when found a new word
     * @param word
     */
    public boolean updateInfor(String word) {
        if (foundWordsCurrentRound.contains(word) == true)
            return false;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);

        switch (word.length()) {
            case 3:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 4:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 5:
                this.score += 2;
                this.scoreForCurrentRound += 2;
                break;
            case 6:
                this.score += 3;
                this.scoreForCurrentRound += 3;
                break;
            case 7:
                this.score += 4;
                this.scoreForCurrentRound += 4;
                break;
            default:
                this.score += 11;
                this.scoreForCurrentRound += 11;
                break;
        }

        return true;
    }



}