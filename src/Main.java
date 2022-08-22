import biuoop.*;

// global variables
class Global {
    public static int rows = 96;
    public static int cols = 64;
    public static int sizeOfCell = 10;
    public static int screenWidth = 1000;
    public static int screenHeight = 800;
    public static boolean shouldPrint = true;
    public static Sleeper sleeper = new Sleeper();
}


public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI("maze solver", Global.screenWidth, Global.screenHeight);
        Maze maze = new Maze();

        maze.recursiveBacktrack(gui);
        maze.setStart(gui);
        maze.setEnd(gui);
        maze.solveDFS(gui);
        maze.solveBFS(gui);
        maze.solveAStar(gui);

        gui.close();
    }
}
