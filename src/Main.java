import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;

import java.io.*;
import java.sql.Array;
import java.util.Arrays;
import java.util.Random;
import java.io.FileOutputStream;


// global variables
class Global {
    public static int rows = 100;
    public static int cols = 100;
    public static int sizeOfCell = 10;
    public static int screenWidth = 1000;
    public static int screenHeight = 800;
    public static boolean shouldPrintAnything = true;
    public static Sleeper sleeper = new Sleeper();
    public static Cell current;
}


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        GUI gui = new GUI("maze solver", Global.screenWidth, Global.screenHeight);




        //for statistics
        int numberOfRuns = 10000;


        Data[] allTheData = new Data[numberOfRuns];

        int DFSWins = 0;
        int BFSWins = 0;
        int AStarWins = 0;

        Random random = new Random();
        for (int i = 0; i < numberOfRuns; i++) {
            //System.out.println(i);
            allTheData[i] = new Data();
            Maze m = new Maze();
            m.shouldPrintMaze(false);
            m.recursiveBacktrack(gui);
            m.setStartFromPoint(random.nextInt(Global.rows), random.nextInt(Global.cols));
            m.setEndFromPoint(random.nextInt(Global.rows), random.nextInt(Global.cols));

            m.solveDFS(gui);
            allTheData[i].DFSVisit = m.howManyVisited();
            m.solveBFS(gui);
            allTheData[i].BFSVisit = m.howManyVisited();
            m.solveAStar(gui);
            allTheData[i].AStarVisit = m.howManyVisited();

            allTheData[i].pathLength = m.pathLength();
            allTheData[i].pointDistance = m.distanceFromStartToEnd();

            if(allTheData[i].BFSVisit < allTheData[i].DFSVisit && allTheData[i].BFSVisit < allTheData[i].AStarVisit)
                BFSWins++;
            else if (allTheData[i].DFSVisit < allTheData[i].AStarVisit && allTheData[i].DFSVisit < allTheData[i].BFSVisit)
                DFSWins++;
            else if(allTheData[i].AStarVisit < allTheData[i].DFSVisit && allTheData[i].AStarVisit < allTheData[i].BFSVisit)
                AStarWins++;

        }
        Arrays.sort(allTheData);

        System.out.println("pointDistance, pathLength, DFSVisit, BFSVisit, AStarVisit");
        for (int i = 0; i < numberOfRuns; i++) {
            System.out.println(allTheData[i].pointDistance + "," + allTheData[i].pathLength + "," + allTheData[i].DFSVisit
                    + "," +allTheData[i].BFSVisit + "," + allTheData[i].AStarVisit);
        }

        System.out.println(DFSWins);
        System.out.println(BFSWins);
        System.out.println(AStarWins);
        gui.close();


    }

}
