import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;

import java.sql.Array;
import java.util.Random;


// global variables
class Global {
    public static int rows = 96;
    public static int cols = 64;
    public static int sizeOfCell = 10;
    public static int screenWidth = 1000;
    public static int screenHeight = 800;
    public static boolean shouldPrintAnything = true;
    public static Sleeper sleeper = new Sleeper();
    public static Cell current;
}


public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI("maze solver", Global.screenWidth, Global.screenHeight);

//        Maze maze = new Maze();
//        maze.recursiveBacktrack(gui);
//        maze.setStart(gui);
//        maze.setEnd(gui);
//
//        maze.solveDFS(gui);
//        maze.solveBFS(gui);
//        maze.solveAStar(gui);
//        maze.shouldPrintMaze(false);


        //for statistics
        int numberOfRuns = 1;


        float[] pathLength = new float[numberOfRuns];
        float[] pointDistance = new float[numberOfRuns];
        float[] DFSVisit = new float[numberOfRuns];
        float[] BFSVisit = new float[numberOfRuns];
        float[] AStarVisit = new float[numberOfRuns];

        Random random = new Random();
        for (int i = 0; i < numberOfRuns; i++) {
            Maze m = new Maze();
            m.shouldPrintMaze(false);
            m.recursiveBacktrack(gui);
            //m.setStartFromPoint(random.nextInt(Global.rows), random.nextInt(Global.cols));
            //m.setEndFromPoint(random.nextInt(Global.rows), random.nextInt(Global.cols));

            m.solveDFS(gui);
            DFSVisit[i] = m.howManyVisited();
            m.solveBFS(gui);
            BFSVisit[i] = m.howManyVisited();
            m.solveAStar(gui);
            AStarVisit[i] = m.howManyVisited();
            pathLength[i] = m.pathLength();
            pointDistance[i] = m.distanceFromStartToEnd();
        }

        gui.close();

    }

}
