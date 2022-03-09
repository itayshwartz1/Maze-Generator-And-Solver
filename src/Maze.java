import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.KeyboardSensor;
import biuoop.Sleeper;

import java.awt.*;
import java.util.*;

public class Maze {
    Cell[][] array = new Cell[Global.rows][Global.cols];
    Cell start;
    Cell finish;
    Color background = new Color(255, 220, 199);


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


    /**
     * This method draw the maze on drawSurface.
     *
     * @param d - DrawSurface
     */
    public void drawMaze(DrawSurface d) {
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
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

    }

    private void sleepFor(int millisecond){
        Sleeper sleeper = new Sleeper();
        sleeper.sleepFor(millisecond);
    }

    /**
     * This method build the maze - remove random walls.
     * <p>
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
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();

        while (!keyboardSensor.isPressed("enter")) {
            DrawSurface drawSurface = gui.getDrawSurface();
            drawMaze(drawSurface);
            drawSurface.drawText(275, 85, "To start press enter" , 50);
            gui.show(drawSurface);        }
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
                DrawSurface drawSurface = gui.getDrawSurface();
                drawMaze(drawSurface);
                drawSurface.drawText(275, 85, "To start press enter" , 50);
                gui.show(drawSurface);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 40 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleepFor((int) milliSecondLeftToSleep);
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
     * @param current
     * @param next
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

    /**
     * This method set the start of the maze.
     *
     * @param gui
     */
    public void setStart(GUI gui) {
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        // sleep for a little time to separate the enters.
        sleeper.sleepFor(300);

        while (true) {
            //we make sure that we won't color the end in different color.
            finish.color = Color.RED;
            long startTime = System.currentTimeMillis();
            DrawSurface drawSurface = gui.getDrawSurface();
            drawMaze(drawSurface);
            drawSurface.drawText(75, 85, "To skip press enter" , 40);
            gui.show(drawSurface);

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
            long milliSecondLeftToSleep = 45 - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
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
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        sleeper.sleepFor(500);
        while (true) {
            // make sure that we won't color the start of the maze.
            start.color = Color.GREEN;

            long startTime = System.currentTimeMillis();
            DrawSurface drawSurface = gui.getDrawSurface();
            drawMaze(drawSurface);
            drawSurface.drawText(75, 85, "Use the arrows to change the finishing position", 40);
            gui.show(drawSurface);

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
            long milliSecondLeftToSleep = 45 - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
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
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        // sleep to separate the enters
        sleeper.sleepFor(300);
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
                RecoverPath(gui);
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
                DrawSurface drawSurface = gui.getDrawSurface();
                drawMaze(drawSurface);
                drawSurface.drawText(320, 85, "To skip press enter", 40);
                gui.show(drawSurface);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 10 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleeper.sleepFor(milliSecondLeftToSleep);
                }
            }
        }
    }


    public void solveDFS(GUI gui) {
        resetMaze();
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        // sleep to separate the enters
        sleeper.sleepFor(300);
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        boolean skip = false;
        Cell current = start;

        Cell newNeighbor = null;


        while (current != finish) {

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
                DrawSurface drawSurface = gui.getDrawSurface();
                drawMaze(drawSurface);
                drawSurface.drawText(320, 85, "To skip press enter", 40);
                gui.show(drawSurface);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 10 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleeper.sleepFor(milliSecondLeftToSleep);
                }
            }
        }
        RecoverPath(gui);
    }

    public void solveAStar(GUI gui) {
        resetMaze();
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        // sleep to separate the enters
        sleeper.sleepFor(300);
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
        boolean skip = false;


        PriorityQueue<Cell> closedList = new PriorityQueue<>();
        PriorityQueue<Cell> openList = new PriorityQueue<>();

        start.f = start.g + (int) start.calculateHeuristic(finish);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.peek();

            current.visited = true;
            current.color = Color.YELLOW;

            long startTime = System.currentTimeMillis();
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }

            if (current == finish) {
                RecoverPath(gui);
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
                    DrawSurface drawSurface = gui.getDrawSurface();
                    drawMaze(drawSurface);
                    drawSurface.drawText(320, 85, "To skip press enter", 40);
                    gui.show(drawSurface);
                    long usedTime = System.currentTimeMillis() - startTime;
                    long milliSecondLeftToSleep = 10 - usedTime;
                    if (milliSecondLeftToSleep > 0) {
                        sleeper.sleepFor(milliSecondLeftToSleep);
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
    private void RecoverPath(GUI gui) {
        Cell current = finish;
        biuoop.Sleeper sleeper = new biuoop.Sleeper();
        KeyboardSensor keyboardSensor = gui.getKeyboardSensor();

        // sleep to separate the enters
        sleeper.sleepFor(300);
        boolean skip = false;

        while (current != start) {
            if (keyboardSensor.isPressed("enter")) {
                skip = true;
            }
            current.color = Color.MAGENTA;

            if (!skip) {
                long startTime = System.currentTimeMillis();
                DrawSurface drawSurface = gui.getDrawSurface();
                drawMaze(drawSurface);
                drawSurface.drawText(320, 85, "To skip press enter", 40);

                gui.show(drawSurface);
                long usedTime = System.currentTimeMillis() - startTime;
                long milliSecondLeftToSleep = 40 - usedTime;
                if (milliSecondLeftToSleep > 0) {
                    sleeper.sleepFor(milliSecondLeftToSleep);
                }
            }
            current = current.father;
        }
        // sleep to separate the enters.
        sleeper.sleepFor(300);
        DrawSurface drawSurface = gui.getDrawSurface();
        drawMaze(drawSurface);
        drawSurface.drawText(320, 85, "To exit press enter", 40);
        gui.show(drawSurface);
        while (!keyboardSensor.isPressed("enter")) {
            sleeper.sleepFor(100);
        }
    }
}