package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.RatingBar;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Graph {

    private int difficultyLevel; //Minimum value 1, maximum value 5
    private int vRows; //Minimum value 3, maximum value provisionally 6
    private int vColumns; //Minimum value 3, maximum value provisionally 6
    private boolean eulCircuit; //True if Euler circuit, false if non-circuit Euler trail
    private ArrayList<Edge> edgeArrayList = new ArrayList<Edge>();
    private Vertex[] vertexArray;
    private int edgeCount;
    private Score score;
    private Timer timer;
    public int stageNo;
    public int gameMode;
    private int spacing;
    private int randNum;
    private int origin;
    private int limit;
    private int locked;
    private int initHoriz;
    private int initVert;
    private Vertex selectedVertex;
    private Stack<Edge> selectedEdges = new Stack<Edge>();
    private boolean vertexSelected;
    private int activated = 0;
    public int timerSecs;
    private static final String TAG = Graph.class.getSimpleName(); //Define the tag for logging

    public Graph(int gameMode, int stageNo) {
        this.stageNo = stageNo;
        this.gameMode = gameMode;
        //score.addToScore(0); //Add as display element later
        switch (gameMode){ //Switch statement
            case 0: timedMode();
                break;
            case 1: endlessMode();
                break;
        }
    }

    public void timedMode() {

        edgeArrayList.clear();
        activated = 0;

        switch (stageNo) { //difficultyLevel switch should make it easy to modify difficulty curve later on and add or take away number of stages
            case 1:
                difficultyLevel = 1;
                break;
            case 2:
                difficultyLevel = 1;
                break;
            case 3:
                difficultyLevel = 2;
                break;
            case 4:
                difficultyLevel = 2;
                break;
            case 5:
                difficultyLevel = 3;
                break;
            case 6:
                difficultyLevel = 3;
                break;
            case 7:
                difficultyLevel = 4;
                break;
            case 8:
                difficultyLevel = 4;
                break;
            case 9:
                difficultyLevel = 5;
                break;
            case 10:
                difficultyLevel = 5;
                break;
        }

        switch (difficultyLevel) {
            case 1:
                vRows = 3;
                vColumns = 3;
                eulCircuit = true;
                break;
            case 2:
                vRows = randInt(3,4);
                vColumns = randInt(3,4);
                eulCircuit = true;
                break;
            case 3:
                vRows = randInt(4,5);
                vColumns = randInt(4,5);
                eulCircuit = true;
                break;
            case 4:
                vRows = randInt(5,6);
                vColumns = randInt(5,6);
                eulCircuit = true;
                break;
            case 5:
                vRows = 6;
                vColumns = 6;
                eulCircuit = true;
                break;
        }

        constructVertices();

        constructCornerEdges();

        constructSideEdges();

        constructInnerEdges();

        edgeCount = edgeArrayList.size();

        timerSecs = vRows*vColumns*3;
    }

    private void endlessMode() {

    }

    public Vertex[] getVertices() {
        return vertexArray;
    }

    public void draw(Canvas canvas) {

        //Draw edges
        for (Edge edge : edgeArrayList) {
            edge.draw(canvas);
        }

        //Draw vertices
        for (Vertex vertex : vertexArray) {
            vertex.draw(canvas);
        }



    }

    private boolean isEulCircuit(int weighting) {
        return true;
    }

    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private void constructVertices() {
        if(vColumns % 2 != 0) initHoriz = 360-(100*(vColumns/2));
        else initHoriz = 360+50-(100*(vColumns/2));
        if(vRows % 2 != 0) initVert = 500-(100*(vRows/2));
        else initVert = 500+50-(100*(vRows/2));

        vertexArray = new Vertex[vRows*vColumns];
        for (int i = 0; i < vRows; i++) {
            for (int j = 0; j < vColumns; j++) {
                int vertexNo = (i * vColumns) + j;
                vertexArray[vertexNo] = new Vertex(initHoriz+(j*100),initVert+(i*100));
            }
        }
    }

    private void constructCornerEdges() {
        //First corner
        randNum = randInt(0, 2);
        origin = 0;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Second corner
        randNum = randInt(0, 2);
        origin = vColumns-1;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin*2));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + ((origin*2)+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Third corner
        randNum = randInt(0, 2);
        origin = (vRows-1)*vColumns;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin+1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }

        //Fourth corner
        randNum = randInt(0, 2);
        origin = (vRows*vColumns)-1;
        switch (randNum) {
            case 0:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 1:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
            case 2:
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-vColumns));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                Log.d(TAG, "Drawing edge from " + origin + " to " + (origin-1));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
                break;
        }
    }

    private void constructSideEdges() {
        int origin;

        //Top sides
        for (int i = 1; i < vColumns - 1; i++) {
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1], vertexArray[origin + 1]};
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Left sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = vColumns*i;
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
            //Either 2 or 4 edges wanted for Euler path
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Right sides
        for (int i = 1; i < vRows - 1; i++) {
            origin = (vColumns*i)+(vRows-1);
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns - 1], vertexArray[origin - 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns]};
            //Either 2 or 4 edges wanted for Euler path
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            //Either 2 or 4 edges wanted for Euler path
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

        //Bottom sides
        for (int i = (vColumns*(vRows - 1)) + 1; i < (vColumns*vRows) - 1; i++) {
            origin = i;
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin + 1]};
            locked = 0;
            for (int v = 0; v < 5; v++) {
                if(adjVertices[v].isLocked()) {
                    locked++;
                }
            }
            if(vertexArray[origin].numConnected() == 0) {
                Log.d(TAG, "Regenerating graph due to no connections to bottom vertex");
                timedMode();
            }
            while(true) {
                randNum = (randInt(1,2)) * 2;
                if((5-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                    break;
            }
            Log.d(TAG, "randNum is " + randNum);
            //Want 2 or 4 edges minus the number of existing edges to be generated
            Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
            limit = randNum - vertexArray[origin].numConnected();
            Log.d(TAG, "Limit is " + limit);
            for (int j = 0; j < limit; j++) {
                Log.d(TAG, "On iteration " + j);
                while (true) {
                    //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                    int randNum2 = randInt(0, 4);
                    if (!(adjVertices[randNum2].isLocked())) {
                        if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                            Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                            edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                            break;
                        }

                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                        }
                    }
                    else {
                        Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                    }
                }
            }
            Log.d(TAG, "Locking vertex "+ origin);
            vertexArray[origin].setLocked();
        }

    }

    private void constructInnerEdges() {
        int origin;
        int locked;
        if (vRows == 3 || vColumns == 3)
            return;
        for (int i = 1; i < vRows-1; i++) {
            for (int j = 1; j < vColumns-1; j++) {
                origin = (i*vColumns) + j;
                Vertex[] adjVertices = {vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1], vertexArray[origin - 1],
                                        vertexArray[origin + 1], vertexArray[origin + vColumns - 1], vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
                locked = 0;
                for (int v = 0; v < 8; v++) {
                    if(adjVertices[v].isLocked()) {
                        locked++;
                    }
                }
                if(locked == 8 && !eulCircuit) {
                    //Draw completely random line (just make sure it doesn't already exist)
                    //TO BE COMPLETED
                    while(true) {
                        randNum = randInt(0,(vRows*vColumns)-1);
                        if(vertexArray[randNum].numConnected() != 8) {
                            while(true) {
                                int randNum2;
                            }
                        }
                    }
                }
                while(true) {
                    randNum = (randInt(1,4)) * 2;
                    if((8-locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                        break;
                }
                Log.d(TAG, "Number of vertices connected to vertex " + origin + " is " + vertexArray[origin].numConnected());
                limit = randNum - vertexArray[origin].numConnected();
                Log.d(TAG, "Limit is " + limit);
                for (int k = 0; k < limit; k++) {
                    Log.d(TAG, "On iteration " + k);
                    while (true) {
                        //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                        int randNum2 = randInt(0, 7);
                        if (!(adjVertices[randNum2].isLocked())) {
                            if(!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                                Log.d(TAG, "Drawing edge from " + origin + " to " + randNum2);
                                edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                                break;
                            }

                            else {
                                Log.d(TAG, "Adjacent vertex " + randNum2 + " was connected already");
                            }
                        }
                        else {
                            Log.d(TAG, "Adjacent vertex " + randNum2 + " was locked already");
                        }
                    }
                }
                Log.d(TAG, "Locking vertex "+ origin);
                vertexArray[origin].setLocked();
            }
        }
    }

    private boolean vertexSelection(Vertex vertex1, Vertex vertex2, int eventX, int eventY) {
        if((eventX >= (vertex1.getX() - vertex1.getR() - vertex1.getH()) &&
                (eventX <= (vertex2.getX() + vertex2.getR() + vertex2.getH())))
                && (eventY >= (vertex1.getY() - vertex1.getR() - vertex1.getH()) &&
                (eventY <= vertex2.getY() + vertex2.getR() + vertex2.getH()))) {
            return true;
        }
        return false;
    }

    private boolean vertexSelection(Vertex vertex, int eventX, int eventY) {
        if((eventX >= (vertex.getX() - vertex.getR() - vertex.getH()) &&
                (eventX <= (vertex.getX() + vertex.getR() + vertex.getH())))
                && (eventY >= (vertex.getY() - vertex.getR() - vertex.getH()) &&
                (eventY <= vertex.getY() + vertex.getR() + vertex.getH()))) {
            return true;
        }
        return false;
    }

    public void reset() {
        for (Edge edge : edgeArrayList) {
            edge.toggleActivation(false);
            edge.lastSelected(false);
        }

        for (Vertex vertex : vertexArray) {
            vertex.toggleActivation(false);
            vertex.lastSelected(false);
        }

        activated = 0;
        vertexSelected = false;
        selectedEdges.clear();
        edgeCount = edgeArrayList.size();
    }

    public void handleActionDown(int eventX, int eventY) {
        //Check if touch is in the bounds of the graph
        if(activated == 0) {
            if (vertexSelection(vertexArray[0], vertexArray[(vRows*vColumns)-1], eventX, eventY)) {
                //Check the row that has been touched
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * 100) - vertexArray[0].getR() - vertexArray[0].getH()) && eventX <= (initHoriz + (i * 100) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        //Log.d(TAG, "Touched vertex in column " + (i + 1));
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * 100) - vertexArray[0].getR() - vertexArray[0].getH()) && eventY <= (initVert + (j * 100) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                //Log.d(TAG, "Touched vertex in row " + (j + 1));
                                Log.d(TAG, "Selecting vertex");
                                vertexArray[(j * vColumns) + i].toggleActivation(true);
                                vertexArray[(j * vColumns) + i].lastSelected(true);
                                selectedVertex = vertexArray[(j * vColumns) + i];
                                vertexSelected = true;
                                activated++;
                                return;
                            }
                        }
                        return;
                    }
                }
                return;
            }
        }

        else if(vertexSelection(selectedVertex, eventX, eventY)) {
            vertexSelected = true;
        }
    }

    public void handleActionMove(int eventX, int eventY) {
        if (edgeCount > 0) {
            if (vertexSelected) {
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * 100) - vertexArray[0].getR() - vertexArray[0].getH()) && eventX <= (initHoriz + (i * 100) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        //Log.d(TAG, "Touched vertex in column " + (i + 1));
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * 100) - vertexArray[0].getR() - vertexArray[0].getH()) && eventY <= (initVert + (j * 100) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                //Check for adjacency to currently selected vertex
                                if (selectedVertex.isConnectedTo(vertexArray[(j * vColumns) + i])) {
                                    if (!selectedVertex.getEdge(vertexArray[(j * vColumns) + i]).isActivated()) {
                                        //Log.d(TAG, "Touched vertex in row " + (j + 1));
                                        Log.d(TAG, "Selecting vertex and previous edge");
                                        selectedVertex.lastSelected(false);
                                        if(!selectedEdges.empty()) {selectedEdges.peek().lastSelected(false);}
                                        selectedEdges.push(selectedVertex.getEdge(vertexArray[(j * vColumns) + i]));
                                        selectedVertex = vertexArray[(j * vColumns) + i];
                                        selectedEdges.peek().toggleActivation(true);
                                        selectedEdges.peek().lastSelected(true);
                                        selectedVertex.toggleActivation(true);
                                        selectedVertex.lastSelected(true);
                                        vertexSelected = true;
                                        edgeCount--;
                                    }

                                    else if(selectedEdges.peek() == selectedVertex.getEdge(vertexArray[(j * vColumns) + i])) {
                                        Log.d(TAG, "Deselecting vertex and previous edge");
                                        selectedEdges.peek().toggleActivation(false);
                                        selectedEdges.pop().lastSelected(false);
                                        if(!selectedEdges.empty()) {
                                            selectedEdges.peek().toggleActivation(true);
                                            selectedEdges.peek().lastSelected(true);
                                        }
                                        selectedVertex.lastSelected(false);
                                        if(!selectedVertex.hasActiveEdge()) selectedVertex.toggleActivation(false);
                                        selectedVertex = vertexArray[(j * vColumns) + i];
                                        selectedVertex.lastSelected(true);
                                        selectedVertex.toggleActivation(true);
                                        vertexSelected = true;
                                        edgeCount++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void handleActionUp(int eventX, int eventY) {
        vertexSelected = false;
        if(edgeCount == 0) {
            stageNo++;

            if(gameMode == 0) {
                timedMode();
            }

            else if(gameMode == 1) {
                endlessMode();
            }
        }
    }
}


