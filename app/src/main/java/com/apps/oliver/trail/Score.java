package com.apps.oliver.trail;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Xml;
import android.view.WindowManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Score {

    private static final String TAG = Score.class.getSimpleName();
    private long scoreValue;
    private String scoreName;
    private Typeface robotoLight;
    private int panelWidth;
    private int panelHeight;
    private int highScoreNumber = 10;
    private ArrayList<ScoreBoardXmlParser.Score> scores = new ArrayList<ScoreBoardXmlParser.Score>();
    private Paint textPaint;
    private Context context;
    private int gameMode;
    private String xmlName;

    public Score(Typeface robotoLight, int panelWidth, int panelHeight, Context context, int gameMode) {
        this.gameMode = gameMode;
        if (gameMode == 0)
            xmlName = "timedBoard";
        else if (gameMode == 1)
            xmlName = "flawlessBoard";
        this.context = context;
        this.robotoLight = robotoLight;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        scoreValue = 0;
        scoreName = "Player";
        textPaint = new Paint();
    }

    public long getHighScore() {
        ScoreBoardXmlParser parser = new ScoreBoardXmlParser(context);
        try {
            scores = parser.parse(gameMode);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return scores.get(0).scoreValue;
    }

    public long getValue() {
        return scoreValue;
    }

    public void setScoreValue(long scoreValue) {
        this.scoreValue = scoreValue;
    }

    public boolean isHighScore() {
        ScoreBoardXmlParser parser = new ScoreBoardXmlParser(context);
        try {
            scores = parser.parse(gameMode);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (scores.size() == 0) {
            return true;
        }

        else {
            ScoreBoardXmlParser.Score tempScore = scores.get(0);

            if (scoreValue > tempScore.scoreValue) {
                return true;
            }
            else
                Log.d(TAG, "Score " + scoreValue + " is no bigger than current high score " + tempScore.scoreValue);
        }

        return false;
    }

    public void addToBoard() {

        ScoreBoardXmlParser parser = new ScoreBoardXmlParser(context);
        try {
            scores = parser.parse(gameMode);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        //Just sort arraylist (look up algorithm) and shorten to ten elements
        ScoreBoardXmlParser.Score newScore = new ScoreBoardXmlParser.Score(scoreName, scoreValue);
        scores.add(scores.size(), newScore);

        int swaps = 0;
        ScoreBoardXmlParser.Score tempScore;
        while(true) {
            for (int i = 0; i < scores.size() - 1; i++) {
                if (scores.get(i).scoreValue < scores.get(i + 1).scoreValue) {
                    // Swap scores
                    tempScore = scores.get(i);
                    scores.set(i, scores.get(i + 1));
                    scores.set(i + 1, tempScore);
                    swaps++;
                }
            }
            if (swaps == 0) {
                break;
            }
            swaps = 0;
        }

        if(scores.size() == 11) {
            scores.remove(10);
        }

        int i = 0;
        for (ScoreBoardXmlParser.Score score : scores) {
            if (score == newScore) {
                highScoreNumber = i;
            }
            i++;
        }

        try {
            FileOutputStream fileos = context.openFileOutput(xmlName + ".xml", Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, xmlName);
            for (ScoreBoardXmlParser.Score score : scores) {
                xmlSerializer.startTag(null, "score");
                xmlSerializer.startTag(null, "scoreName");
                xmlSerializer.text(score.scoreName);
                xmlSerializer.endTag(null, "scoreName");
                xmlSerializer.startTag(null, "scoreValue");
                xmlSerializer.text(String.valueOf(score.scoreValue));
                xmlSerializer.endTag(null, "scoreValue");
                xmlSerializer.endTag(null, "score");
            }
            xmlSerializer.endTag(null, xmlName);
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
