import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import java.awt.*;
import java.util.*;

public class Maze {
    Cell[][] array = new Cell[Global.rows][Global.cols];
    Cell start;
    Cell finish;
    Color background = new Color(255, 220, 199);
    private Boolean shouldPrint = true;


    Maze() {
        // fill array with Cells.
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                array[i][j] = new Cell(i, j, Global.sizeOfCell, Color.white);
            }
        }
        // set default start and finish.
        start = array[0][0];
        start.color = Color.GREEN;
        finish = array[Global.rows - 1][Global.cols - 1];
        finish.color = Color.RED;
    }

    public void resetMaze() {
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                if (array[i][j] != start && array[i][j] != finish) {
                    array[i][j].visited = false;
                    array[i][j].color = Color.white;
                }
            }
        }
    }

    public void shouldPrintMaze(Boolean b){
        this.shouldPrint = b;
    }

    public float distanceFromStartToEnd(){
        return (float) Math.sqrt(Math.pow(start.i - finish.i, 2) + Math.pow(start.j - finish.j,2));
    }


    public int howManyVisited() {
        int result = 0;
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                if (array[i][j].visited)
                    result++;
            }
        }
        return result;
    }

    public int pathLength(){
        int result =0;
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                if (array[i][j].color == Color.MAGENTA || array[i][j] == start || array[i][j] == finish)
                    result++;
            }
        }
        return result;
    }


    /**
     * This method draw the maze on drawSurface.
     **/
    public void drawMaze(GUI gui, int x, int y, String string, int fontSize) {
        if (!shouldPrint)
            return;

        DrawSurface d = gui.getDrawSurface();
        d.setColor(background);
        //draw the cells, and only after the walls - to prevent drawing cells on top the walls/
        d.fillRectangle(0, 0, Global.screenWidth, Global.screenHeight);
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                array[i][j].drawCell(d);
            }
        }
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                array[i][j].drawCellWalls(d);
            }
        }
        d.drawText(x,y,string,fontSize);
        gui.show(d);
        sleepFor(10);
    }

    private void sleepFor(long millisecond){
        if (!shouldPrint)
            return;
        Global.sleeper.sleepFor(millisecond);
    }

    /**
     * This method build the maze - remove random walls.
     *
     * This algorithm, also known as the "recursive backtracker" algorithm, is a randomized version of the
     * depth-first search algorithm.
     * Frequently implemented with a stack, this approach is one of the simplest ways to generate a maze using a
     * computer. Consider the space for a maze being a large grid of cells (like a large chess board), each cell
     * starting with four walls. Starting from a random cell, the computer then selects a random neighbouring cell
     * that has not yet been visited. The computer removes the wall between the two cells and marks the new cell as
     * visited, and adds it to the stack to facilitate backtracking. The computer continues this process, with a cell
     * that has no unvisited neighbours being considered a dead-end. When at a dead-end it backtracks through the path
     * until it reaches a cell with an unvisited neighbour, continuing the path generation by visiting this
     * new, unvisited cell (creating a new junction). This process continues until every cell has been visited, causing
     * the computer to backtrack all the way back to the beginning cell. We can be sure every cell is visited.
     */
    public void recursiveBacktrack(GUI gui) {
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();

        while (!keyboardSensor.isPressed("enter") && shouldPrint) {
            drawMaze(gui, 275, 85, "To start press enter" , 50);
        }
        //to separate the enters
        sleepFor(300);
        boolean skipDrawing = false;

        ArrayList<Cell> stack = new ArrayList<>();
        stack.add(array[0][0]);
        while (stack.size() != 0) {
            long startTime = System.currentTimeMillis();

            if (keyboardSensor.isPressed("enter")) {
                skipDrawing = true;
            }

            Cell current = stack.get(stack.size() - 1);
            Color realColor = current.color;
            current.color = Color.MAGENTA;

            if (!skipDrawing) {
                drawMaze(gui, 275, 85, "To start press enter" , 50);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 45 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleepFor(milliSecondLeftToSleep);
                }
            }
            current.color = realColor;
            Cell next = stack.get(stack.size() - 1).getRandomNeighborWallsDontMatters(array);


            // if the current cell (on th top of the stack) have unvisited neighbors.
            if (next != null) {
                removeWalls(stack.get(stack.size() - 1), next);
                stack.add(next);
                next.visited = true;
            } else {
                stack.remove(stack.size() - 1);
            }
        }

        // in the end we mark all the cell as unvisited.
        for (int i = 0; i < Global.rows; i++) {
            for (int j = 0; j < Global.cols; j++) {
                array[i][j].visited = false;
            }
        }
    }

    /**
     * This method remove the walls between two neighbors cells.
     *
     * @param current .
     * @param next .
     */
    public void removeWalls(Cell current, Cell next) {
        int x = current.i - next.i;
        if (x == 1) {
            current.leftWall = false;
            next.rightWall = false;
        }
        if (x == -1) {
            current.rightWall = false;
            next.leftWall = false;
        }
        int y = current.j - next.j;
        if (y == 1) {
            current.upWall = false;
            next.downWall = false;
        }
        if (y == -1) {
            current.downWall = false;
            next.upWall = false;
        }
    }

    public void setStartFromPoint(int i, int j){
        if( i < 0 || i > Global.rows - 1 || j < 0 || j > Global.cols -1)
            return;
        start.color = Color.white;
        start = array[i][j];
        start.color = Color.GREEN;
    }

    public void setEndFromPoint(int i, int j){
        if( i < 0 || i > Global.rows - 1 || j < 0 || j > Global.cols -1)
            return;
        finish.color = Color.white;
        finish = array[i][j];
        finish.color = Color.RED;
    }

    /**
     * This method set the start of the maze.
     *
     * @param gui .
     */
    public void setStart(GUI gui) {
        if(!shouldPrint)
            return;
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        // sleep for a little time to separate the enters.
        sleepFor(300);

        while (true) {
            //we make sure that we won't color the end in different color.
            finish.color = Color.RED;
            long startTime = System.currentTimeMillis();
            drawMaze(gui, 75, 85, "Use the arrows to change the starting position", 40 );

            if (keyboardSensor.isPressed("up") && start.j > 0) {
                start.color = Color.white;
                start = array[start.i][start.j - 1];
                start.color = Color.GREEN;
            }
            if (keyboardSensor.isPressed("down") && start.j < Global.cols - 1) {
                start.color = Color.white;
                start = array[start.i][start.j + 1];
                start.color = Color.GREEN;
            }
            if (keyboardSensor.isPressed("left") && start.i > 0) {
                start.color = Color.white;
                start = array[start.i - 1][start.j];
                start.color = Color.GREEN;
            }
            if (keyboardSensor.isPressed("right") && start.i < Global.rows - 1) {
                start.color = Color.white;
                start = array[start.i + 1][start.j];
                start.color = Color.GREEN;
            }
            if (keyboardSensor.isPressed("enter")) {
                break;
            }

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = 40 - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleepFor(milliSecondLeftToSleep);
            }
        }
    }

    /**
     * This method set the exit from the maze.
     * I make two different method because the logic is slight different - we need to change here the end and before
     * only the start. it can't be setCell (change location of given cell) - because it is relevant only to unique cells
     *
     * @param gui
     */
    public void setEnd(GUI gui) {
        if(!shouldPrint)
            return;
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        sleepFor(300);
        while (true) {
            // make sure that we won't color the start of the maze.
            start.color = Color.GREEN;

            long startTime = System.currentTimeMillis();
            drawMaze(gui, 75, 85, "Use the arrows to change the finishing position", 40 );
            if (keyboardSensor.isPressed("up") && finish.j > 0) {
                finish.color = Color.white;
                finish = array[finish.i][finish.j - 1];
                finish.color = Color.RED;
            }
            if (keyboardSensor.isPressed("down") && finish.j < Global.cols - 1) {
                finish.color = Color.white;
                finish = array[finish.i][finish.j + 1];
                finish.color = Color.RED;
            }
            if (keyboardSensor.isPressed("left") && finish.i > 0) {
                finish.color = Color.white;
                finish = array[finish.i - 1][finish.j];
                finish.color = Color.RED;
            }
            if (keyboardSensor.isPressed("right") && finish.i < Global.rows - 1) {
                finish.color = Color.white;
                finish = array[finish.i + 1][finish.j];
                finish.color = Color.RED;
            }
            if (keyboardSensor.isPressed("enter")) {
                break;
            }

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = 40 - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleepFor(milliSecondLeftToSleep);
            }
        }
    }


    /**
     * This method solved the Maze with BFS.
     *
     * @param - gui
     */
    public void solveBFS(GUI gui) {
        resetMaze();
        // sleep to separate the enters
        sleepFor(300);
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();

        LinkedList<Cell> queue = new LinkedList<>();
        queue.add(start);
        boolean skip = false;
        while (queue.size() != 0) {
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }
            long startTime = System.currentTimeMillis();
            Cell current = queue.poll();
            current.visited = true;

            // if we find the exit we recover the path from the end to the start.
            if (current == finish) {
                recoverPath(gui);
                return;
            }

            // add all the unvisited neighbors
            if (!current.upWall && !array[current.i][current.j - 1].visited) {
                queue.add(array[current.i][current.j - 1]);
                array[current.i][current.j - 1].father = current;
                array[current.i][current.j - 1].color = Color.YELLOW;
            }

            if (!current.downWall && !array[current.i][current.j + 1].visited) {
                queue.add(array[current.i][current.j + 1]);
                array[current.i][current.j + 1].father = current;
                array[current.i][current.j + 1].color = Color.YELLOW;
            }

            if (!current.leftWall && !array[current.i - 1][current.j].visited) {
                queue.add(array[current.i - 1][current.j]);
                array[current.i - 1][current.j].father = current;
                array[current.i - 1][current.j].color = Color.YELLOW;
            }

            if (!current.rightWall && !array[current.i + 1][current.j].visited) {
                queue.add(array[current.i + 1][current.j]);
                array[current.i + 1][current.j].father = current;
                array[current.i + 1][current.j].color = Color.YELLOW;
            }

            if (!skip) {
                drawMaze(gui, 280, 85, "BFS: To skip press enter", 40);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 20 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleepFor(milliSecondLeftToSleep);
                }
            }
        }
    }


    public void solveDFS(GUI gui) {
        resetMaze();
        // sleep to separate the enters
        sleepFor(300);
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        boolean skip = false;
        Cell current = start;
        Cell newNeighbor = null;

        while (current != finish) {
            start.color = Color.GREEN;

            current.visited = true;
            current.color = Color.YELLOW;
            long startTime = System.currentTimeMillis();
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }
            //the algorithm:
            newNeighbor = current.getRandomNeighbor(array);
            // if there is no neighbors
            if (newNeighbor == null) {
                if (current.father == null) {
                    int i = 0;
                    break;
                }
                current = current.father;

            } else {
                newNeighbor.father = current;
                current = newNeighbor;
            }


            if (!skip) {
                drawMaze(gui, 280, 85, "DFS: To skip press enter", 40);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 30 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleepFor(milliSecondLeftToSleep);
                }
            }
        }
        recoverPath(gui);
    }

    public void solveAStar(GUI gui) {
        resetMaze();
        // sleep to separate the enters
        sleepFor(300);
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        boolean skip = false;


        PriorityQueue<Cell> closedList = new PriorityQueue<>();
        PriorityQueue<Cell> openList = new PriorityQueue<>();

        start.f = start.g + (int) start.calculateHeuristic(finish);
        openList.add(start);

        while (!openList.isEmpty()) {
            start.color = Color.GREEN;
            Cell current = openList.peek();

            current.visited = true;
            current.color = Color.YELLOW;

            long startTime = System.currentTimeMillis();
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }

            if (current == finish) {
                recoverPath(gui);
                return;
            }

            ArrayList<Cell> neighbors = createNeighbors(current);


            for (Cell cell : neighbors) {
                double totalWeight = current.g + 1;

                if (!openList.contains(cell) && !closedList.contains(cell)) {
                    cell.father = current;
                    cell.g = totalWeight;
                    cell.f = cell.g + cell.calculateHeuristic(finish);
                    openList.add(cell);
                } else {
                    if (totalWeight < cell.g) {
                        cell.father = current;
                        cell.g = totalWeight;
                        cell.f = cell.g + cell.calculateHeuristic(finish);

                        if (closedList.contains(cell)) {
                            closedList.remove(cell);
                            openList.add(cell);
                        }
                    }
                }
                if (!skip) {
                    drawMaze(gui, 300, 85, "A*: To skip press enter", 40);
                    long usedTime = System.currentTimeMillis() - startTime;
                    long milliSecondLeftToSleep = 40 - usedTime;
                    if (milliSecondLeftToSleep > 0) {
                        sleepFor(milliSecondLeftToSleep);
                    }
                }
            }

            openList.remove(current);
            closedList.add(current);
        }
    }

    private ArrayList<Cell> createNeighbors(Cell current) {
        ArrayList<Cell> arrayList = new ArrayList();
        if (!current.upWall)
            arrayList.add(array[current.i][current.j - 1]);
        if (!current.downWall)
            arrayList.add(array[current.i][current.j + 1]);
        if (!current.leftWall)
            arrayList.add(array[current.i - 1][current.j]);
        if (!current.rightWall)
            arrayList.add(array[current.i + 1][current.j]);
        Collections.shuffle(arrayList);
        return arrayList;
    }


    /**
     * This method recover the path from the end to the start.
     * it go from the end to his father and on until it reach to the start.
     *
     * @param gui
     */
    private void recoverPath(GUI gui) {
        Cell current = finish;
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();

        // sleep to separate the enters
        sleepFor(300);
        boolean skip = false;

        while (current != start) {
            start.color = Color.GREEN;
            finish.color = Color.RED;
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }
            current.color = Color.MAGENTA;

            if (!skip) {
                long startTime = System.currentTimeMillis();
                drawMaze(gui, 210, 85, "recover path: To skip press enter", 40);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 40 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleepFor(milliSecondLeftToSleep);
                }
            }
            current = current.father;
        }
        // sleep to separate the enters.
        sleepFor(300);
        drawMaze(gui, 210, 85, "recover path: To exit press enter", 40);
        while (!keyboardSensor.isPressed("enter") && shouldPrint) {
            sleepFor(100);
        }
    }
}