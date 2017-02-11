package com.pdx.kstn.kstn_boggle;

/**
 * Created by kalebstriplin on 2/6/17.
 */

public class Tuple<String, Integer> {
    private String name;
    private Integer score;

    public Tuple(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return this.name;
    }

    public Integer getScore() {
        return this.score;
    }

}
