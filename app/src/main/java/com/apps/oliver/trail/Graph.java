package com.apps.oliver.trail;

import android.graphics.Canvas;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.Stack;

/**
 * Created by Oliver on 13/07/2014.
 */
public class Graph {

    public boolean generatingGraph;
    private boolean constructComplete;
    private boolean eulCircuit = true; // True if Euler circuit, false if non-circuit Euler trail
    private boolean vertexSelected = false;

    public int stageNo;
    public int gameMode;
    public int timerSecs;
    private int difficultyLevel; //Minimum value 1, maximum value 5
    private int vRows = 0; //Minimum value 3, maximum value provisionally 6
    private int vColumns = 0; //Minimum value 3, maximum value provisionally 6
    private int randNum;
    private int origin;
    private int limit;
    private int locked;
    private int panelWidth;
    private int panelHeight;
    private int centreHoriz;
    private int centreVert;
    private int vertexSpacing;
    private int initHoriz;
    private int initVert;
    private int edgeCount;
    private int activated = 0;
    private int penalty = 0; //Number of penalties incurred, for use in endless mode's scoring

    public Score score;
    private Timer timer;
    private Typeface tf;
    private Vertex selectedVertex; // Last selected vertex
    private Stack<Edge> selectedEdges = new Stack<Edge>(); // All edges that have been activated
    private ArrayList<Edge> edgeArrayList = new ArrayList<Edge>(); // All edges
    private Vertex[] vertexArray; // All vertices


    public Graph(int gameMode, int stageNo, int panelWidth, int panelHeight, Typeface tf) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.tf = tf;
        this.stageNo = stageNo;
        this.gameMode = gameMode;

        centreHoriz = panelWidth / 2;
        centreVert = panelHeight / 2;
        vertexSpacing = (panelWidth * 18) / 100;

        // Creating score associated with this mode's graphs
        score = new Score(tf, panelWidth, panelHeight);
    }

    // Pauses the timer in Timed mode
    public void pauseTimer() {
        if (gameMode == 0) {
            timer.pauseTimer();
        }
    }

    // Resumes the timer in Timed mode
    public void resumeTimer() {
        if (gameMode == 0) {
            timer.resumeTimer();
        }
    }

    // Tries to construct a new stage dependent on game mode
    public void constructStage() {

        selectedEdges.clear(); // Clear array of selected edges in case it is still full
        constructComplete = false; // Flags if construction of graph has been successful
        vertexSelected = false;
        generatingGraph = true; // Flags if graph is still being generated
        activated = 0;
        penalty = 0;

        while(!constructComplete) {
            edgeArrayList.clear();
            if (gameMode == 0) {
                timedMode();
            }
            if (gameMode == 1) {
                endlessMode();
            }
        }
        generatingGraph = false;
    }

    public void timedMode() {

        // Switch for difficulty curve
        switch (stageNo) {
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

        // Switch for what each difficulty entails for the graph (probability of Euler circuit, size)
        switch (difficultyLevel) {
            case 1:
                vRows = 3;
                vColumns = 3;
                eulCircuit = true;
                break;
            case 2:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 3;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 3;
                }
                eulCircuit = true;
                break;
            case 3:
                vRows = 4;
                vColumns = 4;
                eulCircuit = true;
                break;
            case 4:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 5;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 5;
                }
                eulCircuit = true;
                break;
            case 5:
                vRows = 5;
                vColumns = 5;
                eulCircuit = true;
                break;
        }

        constructVertices();
        constructCornerEdges();
        constructSideEdges();

        if(!constructInnerEdges()) {
            return;
        }

        // If not a circuit, add random extra edge to make it so
        if(!eulCircuit) {
            addNonCircuitEdge();
        }

        edgeCount = edgeArrayList.size(); // Number of edges
        timerSecs = vRows * vColumns * 3; // The timer length, determined by graph size

        // Check for connectedness
        if(!traverseGraph()) {
            return;
        }

        timer = new Timer(timerSecs, tf, panelWidth, panelHeight); // Instantiate and start timer
        constructComplete = true; // Construction successful
    }

    public void endlessMode() {

        // Difficulty curve
        if(stageNo <= 10)
            difficultyLevel = 1;
        else if (stageNo <= 20)
            difficultyLevel = 2;
        else if (stageNo <= 30)
            difficultyLevel = 3;
        else if (stageNo <= 40)
            difficultyLevel = 4;
        else
            difficultyLevel = 5;

        // Switch for what each difficulty entails for the graph (probability of Euler circuit, size)
        switch (difficultyLevel) {
            case 1:
                vRows = 3;
                vColumns = 3;
                eulCircuit = true;
                break;
            case 2:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 3;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 3;
                }
                if(randInt(1, 100) <= 5) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 3:
                vRows = 4;
                vColumns = 4;
                if(randInt(1, 100) <= 10) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 4:
                randNum = randInt(1,2);
                if(randNum == 1) {
                    vRows = 5;
                    vColumns = 4;
                }
                else {
                    vRows = 4;
                    vColumns = 5;
                }
                if(randInt(1, 100) <= 15) eulCircuit = false;
                else eulCircuit = true;
                break;
            case 5:
                vRows = 5;
                vColumns = 5;
                eulCircuit = true;
                if(randInt(1, 100) <= 20) eulCircuit = false;
                else eulCircuit = true;
                break;
        }

        constructVertices();
        constructCornerEdges();
        constructSideEdges();

        if(!constructInnerEdges()) {
            return;
        }

        // If not a circuit, add random extra edge to make it so
        if(!eulCircuit) {
            addNonCircuitEdge();
        }

        edgeCount = edgeArrayList.size(); // Number of edges

        // Check for connectedness
        if(!traverseGraph()) {
            return;
        }

        constructComplete = true; // Construction successful
    }

    public void draw(Canvas canvas) {

        // Only draw graph if finished being constructed
        if(constructComplete) {
            try {
                // Draw each edge
                for (Edge edge : edgeArrayList) {
                    edge.draw(canvas);
                }

                // Draw each vertex
                for (Vertex vertex : vertexArray) {
                    vertex.draw(canvas);
                }
            }

            // Very rare possibility of concurrent modification and attempt to draw from arrays
            catch(ConcurrentModificationException e) {
                e.printStackTrace();
            }

            // If Timed mode, draw timer
            if(gameMode == 0) {
                timer.draw(canvas);
            }

            score.draw(canvas);
        }
    }

    // Generate random integer between (and inclusive of) two integers
    public int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    // Construct vertices dependent on number of rows and columns expected
    private void constructVertices() {

        if(vColumns % 2 != 0) initHoriz = centreHoriz - (vertexSpacing * (vColumns / 2));
        else initHoriz = centreHoriz + (vertexSpacing / 2) - (vertexSpacing * (vColumns / 2));
        if(vRows % 2 != 0) initVert = centreVert + ((panelHeight * 5) / 100) - (vertexSpacing * (vRows / 2));
        else initVert = centreVert + (vertexSpacing / 2) + ((panelHeight * 5) / 100) - (vertexSpacing * (vRows / 2));

        vertexArray = new Vertex[vRows*vColumns];
        for (int i = 0; i < vRows; i++) {
            for (int j = 0; j < vColumns; j++) {
                int vertexNo = (i * vColumns) + j;
                vertexArray[vertexNo] = new Vertex(initHoriz+(j * vertexSpacing),initVert+(i * vertexSpacing), panelWidth, panelHeight);
            }
        }
    }

    // Construct any edges coming from vertices in the corners of the graph
    private void constructCornerEdges() {
        // Draw random combination of two edges then lock top left vertex
        randNum = randInt(0, 2);
        origin = 0;
        switch (randNum) {
            case 0:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                vertexArray[origin].setLocked();
                break;
            case 1:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                vertexArray[origin].setLocked();
                break;
            case 2:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+vColumns+1]));
                vertexArray[origin].setLocked();
                break;
        }

        // Draw random combination of two edges then lock top right vertex
        randNum = randInt(0, 2);
        origin = vColumns - 1;
        switch (randNum) {
            case 0:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                vertexArray[origin].setLocked();
                break;
            case 1:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                vertexArray[origin].setLocked();
                break;
            case 2:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin*2]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[(origin*2)+1]));
                vertexArray[origin].setLocked();
                break;
        }

        // Draw random combination of two edges then lock bottom left vertex
        randNum = randInt(0, 2);
        origin = (vRows - 1) * vColumns;
        switch (randNum) {
            case 0:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                vertexArray[origin].setLocked();
                break;
            case 1:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                vertexArray[origin].setLocked();
                break;
            case 2:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns+1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin+1]));
                vertexArray[origin].setLocked();
                break;
        }

        // Draw random combination of two edges then lock bottom right vertex
        randNum = randInt(0, 2);
        origin = (vRows * vColumns) - 1;
        switch (randNum) {
            case 0:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                vertexArray[origin].setLocked();
                break;
            case 1:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns-1]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                vertexArray[origin].setLocked();
                break;
            case 2:
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-vColumns]));
                edgeArrayList.add(new Edge(vertexArray[origin], vertexArray[origin-1]));
                vertexArray[origin].setLocked();
                break;
        }
    }

    // Construct edges coming from vertices on the sides of the graph
    private void constructSideEdges() {

        // Draw random combination of two/four edges then lock vertices along top side
        for (int i = 1; i < vColumns - 1; i++) {
            origin = i;
            // Array of adjacent vertices
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin + vColumns - 1],
                    vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1],
                    vertexArray[origin + 1]};
            constructSideEdge(origin, adjVertices);
            vertexArray[origin].setLocked();
        }

        // Draw random combination of two/four edges then lock vertices along left side
        for (int i = 1; i < vRows - 1; i++) {
            origin = vColumns * i;
            // Array of adjacent vertices
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1],
                    vertexArray[origin + 1], vertexArray[origin + vColumns],
                    vertexArray[origin + vColumns + 1]};
            constructSideEdge(origin, adjVertices);
            vertexArray[origin].setLocked();
        }

        // Draw random combination of two/four edges then lock vertices along right side
        for (int i = 1; i < vRows - 1; i++) {
            origin = (vColumns * (i + 1)) - 1;
            // Array of adjacent vertices
            Vertex[] adjVertices = {vertexArray[origin - vColumns], vertexArray[origin - vColumns - 1],
                    vertexArray[origin - 1], vertexArray[origin + vColumns - 1],
                    vertexArray[origin + vColumns]};
            constructSideEdge(origin, adjVertices);
            vertexArray[origin].setLocked();
        }

        // Draw random combination of two/four edges then lock vertices along bottom side
        for (int i = (vColumns * (vRows - 1)) + 1; i < (vColumns * vRows) - 1; i++) {
            origin = i;
            // Array of adjacent vertices
            Vertex[] adjVertices = {vertexArray[origin - 1], vertexArray[origin - vColumns - 1],
                    vertexArray[origin - vColumns], vertexArray[origin - vColumns + 1],
                    vertexArray[origin + 1]};
            constructSideEdge(origin, adjVertices);
            vertexArray[origin].setLocked();
        }

    }

    private void constructSideEdge(int origin, Vertex[] adjVertices) {

        // Determine number of adjacent vertices that are locked
        locked = 0;
        for (int v = 0; v < 5; v++) {
            if (adjVertices[v].isLocked()) {
                locked++;
            }
        }
        // Either 2 or 4 edges wanted for Euler path
        while (true) {
            randNum = (randInt(1, 2)) * 2;
            if ((5 - locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                break;
        }
        // Want 2 or 4 edges minus the number of existing edges to be generated
        limit = randNum - vertexArray[origin].numConnected();
        for (int j = 0; j < limit; j++) {
            // Repeat until successfully draw edge
            while (true) {
                // Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                int randNum2 = randInt(0, 4);
                if (!(adjVertices[randNum2].isLocked())) {
                    if (!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                        // Doesn't exist already, so create new edge
                        edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                        break;
                    }
                }
            }
        }
    }

    private boolean constructInnerEdges() {
        int locked;
        for (int i = 1; i < vRows - 1; i++) {
            for (int j = 1; j < vColumns - 1; j++) {
                origin = (i * vColumns) + j;
                // Array of adjacent vertices
                Vertex[] adjVertices = {vertexArray[origin - vColumns - 1], vertexArray[origin - vColumns],
                        vertexArray[origin - vColumns + 1], vertexArray[origin - 1],
                        vertexArray[origin + 1], vertexArray[origin + vColumns - 1],
                        vertexArray[origin + vColumns], vertexArray[origin + vColumns + 1]};
                // Determine number of adjacent vertices that are locked
                locked = 0;
                for (int v = 0; v < 8; v++) {
                    if(adjVertices[v].isLocked()) {
                        locked++;
                    }
                }
                // If impossible to make into circuit, break out of graph construction to start again
                if(vertexArray[origin].numConnected() == 0 &&  locked > 6) {
                    return false;
                }
                // Either 2, 4, 6 or 8 edges wanted for Euler path
                while(true) {
                    randNum = (randInt(1,4)) * 2;
                    if((8 - locked) + vertexArray[origin].numConnected() >= randNum && vertexArray[origin].numConnected() <= randNum)
                        break;
                }

                limit = randNum - vertexArray[origin].numConnected();
                for (int k = 0; k < limit; k++) {
                    while (true) {
                        //Choose random element from adjacent vertices array and check if an edge to that vertex already exists
                        int randNum2 = randInt(0, 7);
                        if (!(adjVertices[randNum2].isLocked())) {
                            if (!adjVertices[randNum2].isConnectedTo(vertexArray[origin])) {
                                // Doesn't exist already, so create new edge
                                edgeArrayList.add(new Edge(vertexArray[origin], adjVertices[randNum2]));
                                break;
                            }
                        }
                    }
                }
                vertexArray[origin].setLocked();
            }
        }
        return true;
    }

    // Add a random edge to a graph with an Eulerian circuit
    private void addNonCircuitEdge() {

        outerloop:
        while(true) {
            //Find random vertex
            randNum = randInt(0, (vRows * vColumns) - 1);
            //If there aren't already 8 connections to that random vertex, proceed to attempt drawing an edge to random adjacent vertex
            if(vertexArray[randNum].numConnected() != 8) {
                while (true) {
                    //Top left corner
                    if(randNum == 0) {
                        int[] adjVertices = new int[] {
                                1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Top right corner
                    else if(randNum == vColumns - 1) {
                        int[] adjVertices = new int[] {
                                - 1,
                                vColumns - 1,
                                vColumns
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Bottom left corner
                    else if(randNum == vColumns * (vRows - 1)) {
                        int[] adjVertices = new int[] {
                                - vColumns,
                                - vColumns + 1,
                                1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Bottom right corner
                    else if(randNum == (vColumns * vRows) - 1) {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 2);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Left side vertex
                    else if(randNum % vColumns == 0) {
                        int[] adjVertices = new int[] {
                                - vColumns,
                                - vColumns + 1,
                                1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 4);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    //Right side vertex
                    else if((randNum + 1) % vColumns == 0) {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - 1,
                                vColumns - 1,
                                vColumns
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 4);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }
                    else {
                        int[] adjVertices = new int[] {
                                - vColumns - 1,
                                - vColumns,
                                - vColumns + 1,
                                - 1,
                                1,
                                vColumns - 1,
                                vColumns,
                                vColumns + 1
                        };
                        //Find random adjacent vertex
                        int randNum2 = randInt(0, 7);

                        //If the 'adjacent' vertex is an existing vertex, proceed
                        if (randNum + adjVertices[randNum2] > 0 && randNum + adjVertices[randNum2] < vRows * vColumns) {
                            //If there isn't already a connection, create edge
                            if (!vertexArray[randNum].isConnectedTo(vertexArray[randNum + adjVertices[randNum2]])) {
                                edgeArrayList.add(new Edge(vertexArray[randNum], vertexArray[randNum + adjVertices[randNum2]]));
                                break outerloop;
                            }
                        }
                    }

                }
            }
        }
    }

    // Traverse the graph to find out if all vertices are connected with edges
    private boolean traverseGraph() {
        int noVertices = vRows * vColumns;
        //ArrayList for all the visited vertices
        ArrayList<Integer> visited = new ArrayList<Integer>();
        visited.add(new Integer(0));
        //Stack for parents to get children of
        Stack<Integer> parents = new Stack<Integer>();
        parents.add(new Integer(0));
        //An array of the integer modifiers for adjacent vertices
        int[] adjVertices = new int[] {
                - vColumns - 1,
                - vColumns,
                - vColumns + 1,
                - 1,
                1,
                vColumns - 1,
                vColumns,
                vColumns + 1
        };

        //While the parents stack still has children to check, carry on
        while(!parents.empty()) {
            //Pop parent off the stack(in int form)
            origin = parents.pop().intValue();
            //For all adjacent vertices (always size 8)
            for(int i = 0; i < 8; i++) {
                //If the parent is connected to the adjacent vertex, proceed
                if(connectedTo(origin + adjVertices[i])) {
                    //Else if child has not yet been visited, add it to parents stack
                    if(!visited.contains(new Integer(origin + adjVertices[i]))) {
                        parents.add(new Integer(origin + adjVertices[i]));
                        visited.add(new Integer(origin + adjVertices[i]));
                        if(visited.size() == noVertices) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    // Check if two vertices are connected by an edge
    private boolean connectedTo(int vertInt) {
        try {
            if(vertexArray[origin].isConnectedTo(vertexArray[vertInt]))
                return true;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    // Check if touch event is within a rectangular grid of vertices
    private boolean vertexSelection(Vertex vertex1, Vertex vertex2, int eventX, int eventY) {
        if((eventX >= (vertex1.getX() - vertex1.getR() - vertex1.getH()) &&
                (eventX <= (vertex2.getX() + vertex2.getR() + vertex2.getH())))
                && (eventY >= (vertex1.getY() - vertex1.getR() - vertex1.getH()) &&
                (eventY <= vertex2.getY() + vertex2.getR() + vertex2.getH()))) {
            return true;
        }
        return false;
    }

    // Check if touch event is within that vertex
    private boolean vertexSelection(Vertex vertex, int eventX, int eventY) {
        if((eventX >= (vertex.getX() - vertex.getR() - vertex.getH()) &&
                (eventX <= (vertex.getX() + vertex.getR() + vertex.getH())))
                && (eventY >= (vertex.getY() - vertex.getR() - vertex.getH()) &&
                (eventY <= vertex.getY() + vertex.getR() + vertex.getH()))) {
            return true;
        }
        return false;
    }

    // Reset all the activations and variables tracking activations
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
        penalty += 5;
    }

    // Check if game mode has finished
    public boolean modeFinished() {
        if(gameMode == 0)
            if(stageNo > 10)
                return true;

        return false;
    }

    // Handle touch down presses for the graph
    public void handleActionDown(int eventX, int eventY) {
        //Check if touch is in the bounds of the graph (and there aren't other activated elements)
        if(activated == 0) {
            if (vertexSelection(vertexArray[0], vertexArray[(vRows*vColumns)-1], eventX, eventY)) {
                //Check the column that has been touched
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH())
                            && eventX <= (initHoriz + (i * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        // Check the row that has been touched
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH())
                                    && eventY <= (initVert + (j * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                // Since vertex is being touched, activate and set as last selected
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

        // If touching the last selected vertex, reselect so moving touch gesture works
        else if(vertexSelection(selectedVertex, eventX, eventY)) {
            vertexSelected = true;
        }
    }

    // Handle touch move gestures for the graph
    public void handleActionMove(int eventX, int eventY) {
        // Check if there are more edges to select
        if (edgeCount > 0) {
            // Check if a vertex is selected
            if (vertexSelected) {
                // Check the column that has been touched
                for (int i = 0; i < vColumns; i++) {
                    if (eventX >= (initHoriz + (i * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH())
                            && eventX <= (initHoriz + (i * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                        // Check the row that has been touched
                        for (int j = 0; j < vRows; j++) {
                            if (eventY >= (initVert + (j * vertexSpacing) - vertexArray[0].getR() - vertexArray[0].getH())
                                    && eventY <= (initVert + (j * vertexSpacing) + vertexArray[0].getR() + vertexArray[0].getH())) {
                                // Check for adjacency to currently selected vertex
                                if (selectedVertex.isConnectedTo(vertexArray[(j * vColumns) + i])) {
                                    // Check if vertex being selected is not already activated
                                    if (!selectedVertex.getEdge(vertexArray[(j * vColumns) + i]).isActivated()) {
                                        // Since vertex is select-able and being touched, activate and set as last selected
                                        // Also set edge connecting last vertex selected and this one as activated
                                        // and the last selected.
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

                                    // If selecting the last vertex drawn from, deselect currently
                                    // selected vertex and edge adjoining.
                                    else if(selectedEdges.peek() == selectedVertex.getEdge(vertexArray[(j * vColumns) + i])) {
                                        selectedEdges.peek().toggleActivation(false);
                                        selectedEdges.pop().lastSelected(false);
                                        // If there are edges left after deselection, set activation and last selected
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
                                        // Add penalty for calculating score in Endless mode
                                        penalty++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Check if stage has finished and take relevant action
    public boolean stageFinished() {
        // If there are no more selected edges but a selected vertex,
        // deselect and deactivate the vertex
        if(selectedEdges.empty()) {
            if(vertexSelected) {
                selectedVertex.lastSelected(false);
                selectedVertex.toggleActivation(false);
                activated = 0;
            }
        }

        vertexSelected = false;

        // If all edges have been selected, stage is complete
        if(edgeCount == 0) {
            stageNo++;
            // Tally score based on timer for Timed mode
            if(gameMode == 0) {
                if(timer.getTimeLeft() > 0) {
                    score.addToScore((long) timer.getTimeLeft() * 100);
                }
            }
            // Tally score based on penalties incurred for Endless mode
            else if(gameMode == 1) {
                if((vRows * vColumns) - penalty > 0) {
                    score.addToScore((long) ((vRows * vColumns) - penalty) * 100);
                }
            }

            constructComplete = false;
            return true;
        }
        return false;
    }
}


