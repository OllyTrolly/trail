package com.apps.oliver.trail;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Score {

    private long scoreValue;
    private String scoreName;

    void Score() {
        scoreValue = 0;
        scoreName = "Player";
    }

    long addToScore(long score) {
        scoreValue += score;
        return scoreValue;
    }

    void nameScore(String inputName) {
        scoreName = inputName;
    }

    void addToBoard() {
        //Write the score with its name to SQLite
        //Learn to use SQLite of course
    }
}
