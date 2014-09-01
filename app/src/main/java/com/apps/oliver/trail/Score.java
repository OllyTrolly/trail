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
    private ArrayList<ScoreBoardXmlParser.Score> scores = new ArrayList<ScoreBoardXmlParser.Score>();
    private ScoreBoardXmlParser.Score[] scoresArray = new ScoreBoardXmlParser.Score[10];
    private Paint textPaint;
    private Context context;
    private static final String ns = null;

    public Score(Typeface robotoLight, int panelWidth, int panelHeight, Context context) {
        this.context = context;
        this.robotoLight = robotoLight;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        scoreValue = 0;
        scoreName = "Player";
        textPaint = new Paint();
    }

    public void setTypeface(Typeface tf) {
        textPaint.setTypeface(tf);
    }

    public void addToScore(long score) {
        scoreValue += score;
    }

    public void nameScore(String inputName) {
        scoreName = inputName;
    }

    public void draw(Canvas canvas) {

        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(45);
        //Draw text
        canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 90) / 100, textPaint);
    }

    public void drawFinal(Canvas canvas) {
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.LTGRAY);
        textPaint.setTypeface(robotoLight);
        textPaint.setTextSize(60);
        //Draw text
        //canvas.drawText("Final score: ", panelWidth / 2, (panelHeight * 45) / 100, textPaint);
        //canvas.drawText(scoreValue + "", panelWidth / 2, (panelHeight * 55) / 100, textPaint);
        canvas.drawText("Scoreboard", panelWidth / 2, (panelHeight * 10) / 100, textPaint);

        textPaint.setTextSize(40);
        int i = 1;
        for(ScoreBoardXmlParser.Score score : scores) {
            canvas.drawText(i + ". " + score.scoreName + " - " + score.scoreValue, panelWidth / 2, (panelHeight * (10 + (i * 8))) / 100, textPaint);
            i++;
            if (i > 10) {
                break;
            }
        }
    }

    public boolean isHighScore() {
        ScoreBoardXmlParser parser = new ScoreBoardXmlParser(context);
        try {
            scores = parser.parse();
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

        if (scores.size() < 10) {
            return true;
        }

        else {
            ScoreBoardXmlParser.Score tempScore = scores.get(scores.size() - 1);

            if (scoreValue <= tempScore.scoreValue) {
                return false;
            }
        }

        return true;
    }

    public void addToBoard() {

        final String xmlFile = context.getFilesDir() + "/scoreBoard.xml";
        ScoreBoardXmlParser parser = new ScoreBoardXmlParser(context);
        try {
            scores = parser.parse();
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

        for (ScoreBoardXmlParser.Score score : scores) {
            Log.d(TAG, "Score name is: " + score.scoreName);
            Log.d(TAG, "Score value is: " + score.scoreValue);
        }

        scores.add(scores.size(), new ScoreBoardXmlParser.Score(scoreName, scoreValue));

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

        try {
            FileOutputStream fileos = context.openFileOutput("scoreBoard.xml", Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
            xmlSerializer.startTag(null, "scoreBoard");
            for (ScoreBoardXmlParser.Score score : scores) {
                xmlSerializer.startTag(null, "score");
                xmlSerializer.startTag(null, "scoreName");
                Log.d(TAG, "Score is called:" + score.scoreName);
                xmlSerializer.text(score.scoreName);
                xmlSerializer.endTag(null, "scoreName");
                xmlSerializer.startTag(null, "scoreValue");
                Log.d(TAG, "Score has value: " + score.scoreValue);
                xmlSerializer.text(String.valueOf(score.scoreValue));
                xmlSerializer.endTag(null, "scoreValue");
                xmlSerializer.endTag(null, "score");
            }
            xmlSerializer.endTag(null, "scoreBoard");
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
