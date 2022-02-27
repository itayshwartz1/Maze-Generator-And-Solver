import biuoop.DrawSurface;
import biuoop.GUI;



// global variables
class Global {
    public static int rows = 48;
    public static int cols = 32;
    public static int sizeOfCell = 20;
    public static int screenWidth = 1000;
    public static int screenHeight = 800;

    public static Cell current;
}


public class Main {
    public static void main(String[] args) {
        GUI gui = new GUI("maze solver", Global.screenWidth, Global.screenHeight);
        DrawSurface drawSurface = gui.getDrawSurface();

        Maze maze = new Maze();
        maze.recursiveBacktrack(gui);
        maze.setStart(gui);
        maze.setEnd(gui);
        maze.solveBFS(gui);

        gui.close();

    }

}
