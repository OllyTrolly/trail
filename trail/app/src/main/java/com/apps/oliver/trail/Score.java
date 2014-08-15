package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Score {

    private long scoreValue;
    private String scoreName;

    public Score() {
        scoreValue = 0;
        scoreName = "Player";
    }

    public void addToScore(long score) {
        scoreValue += score;
    }

    public void nameScore(String inputName) {
        scoreName = inputName;
    }

    public void addToBoard() {
        //Write the score with its name to SQLite
        //Learn to use SQLite of course
    }
}
