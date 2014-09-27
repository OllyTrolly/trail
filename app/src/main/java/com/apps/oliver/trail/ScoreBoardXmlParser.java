package com.apps.oliver.trail;

import android.content.Context;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Oliver on 28/08/2014.
 */
public class ScoreBoardXmlParser {
    private String xmlName;
    private Context context;
    private static final String ns = null;
    private int gameMode;

    public ScoreBoardXmlParser(Context context) {
        this.context = context;
    }

    public ArrayList<Score> parse(int gameMode) throws XmlPullParserException, IOException, ParserConfigurationException {
        this.gameMode = gameMode;
        if (gameMode == 0)
            xmlName = "timedBoard";
        else if (gameMode == 1)
            xmlName = "flawlessBoard";

        FileInputStream in = null;
        File xmlFile = new File(context.getFilesDir(), xmlName + ".xml");
        if (!xmlFile.exists()) {
            FileOutputStream fileos = context.openFileOutput(xmlName + ".xml", Context.MODE_PRIVATE);
        }
        try {
            in = context.openFileInput(xmlName + ".xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readScoreBoard(parser);
        }
        finally {
            in.close();
        }
    }

    private ArrayList<Score> readScoreBoard(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Score> scores = new ArrayList<Score>();

        parser.require(XmlPullParser.START_TAG, ns, xmlName);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Starts by looking for the score tag
            if(name.equals("score")) {
                scores.add(readScore(parser));
            }
            else {
                skip(parser);
            }
        }

        return scores;
    }

    public static class Score {
        public final String scoreName;
        public final long scoreValue;

        public Score(String scoreName, long scoreValue) {
            this.scoreName = scoreName;
            this.scoreValue = scoreValue;
        }
    }

    //Parses the content of a score. If it encounters a scoreName or scoreValue tag, it hands them
    // to their respective read methods for processing, otherwise it skips the tag.
    private Score readScore(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "score");
        String scoreName = null;
        long scoreValue = 0;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("scoreName")) {
                scoreName = readScoreName(parser);
            } else if (name.equals("scoreValue")) {
                scoreValue = readScoreValue(parser);
            } else {
                skip(parser);
            }
        }
        return new Score(scoreName, scoreValue);
    }

    private String readScoreName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "scoreName");
        String scoreName = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "scoreName");
        return scoreName;
    }

    private long readScoreValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "scoreValue");
        String scoreValueString = readText(parser);
        long scoreValue = Long.parseLong(scoreValueString);
        parser.require(XmlPullParser.END_TAG, ns, "scoreValue");
        return scoreValue;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
