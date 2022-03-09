import biuoop.DrawSurface;
import biuoop.GUI;



// global variables
class Global {
    public static int rows = 48;
    public static int cols = 32;
    public static int sizeOfCell = 20;
    public static int screenWidth = 1040;
    public static int screenHeight = 800;
    public static boolean shouldPrintAnything = true;

    public static Cell current;
}


public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI("maze solver", Global.screenWidth, Global.screenHeight);

        float numDFS = 0, numBFS = 0, numAStar = 0;
        int numberOfRuns = 1;
        for(int i = 0; i < numberOfRuns; i++) {

            Maze maze = new Maze();
            maze.recursiveBacktrack(gui);

            maze.solveDFS(gui);
            numDFS += (float) maze.howManyVisited() / numberOfRuns;
            maze.solveBFS(gui);
            numBFS += (float) maze.howManyVisited() / numberOfRuns;
            maze.solveAStar(gui);
            numAStar += (float) maze.howManyVisited() / numberOfRuns;
        }
        System.out.println(numDFS);
        System.out.println(numBFS);
        System.out.println(numAStar);

        gui.close();

    }

}
