import biuoop.DrawSurface;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Cell implements Comparable<Cell> {
    int i;
    int j;

    int realI;
    int realJ;
    int size;

    Color color;

    //this fields represent if there are walls on the edge of the cell
    boolean upWall = true;
    boolean downWall = true;
    boolean leftWall = true;
    boolean rightWall = true;

    boolean visited = false;
    Cell father;

    double g = Double.MAX_VALUE;
    double f = Double.MAX_VALUE;

    /**
     * This is the constructor of the Cell object.
     *
     * @param i
     * @param j
     * @param size
     * @param color
     */
    Cell(int i, int j, int size, Color color) {
        this.i = i;
        this.j = j;
        this.size = size;
        this.color = color;

        // we calculate the real position of the cell on the drawSurface.
        realI = i * size + 20;
        realJ = j * size + 135;
    }

    /**
     * This method draw the cell on a drawSurface without the walls.
     *
     * @param d - DrawSurface.
     */
    public void drawCell(DrawSurface d) {
        int realI = i * size + 20;
        int realJ = j * size + 135;
        // draw the cell with it color.
        d.setColor(this.color);
        d.fillRectangle(realI, realJ, size, size);

    }

    /**
     * this method draw the cell walls on the drawSurface.
     *
     * @param d - DrawSurface.
     */
    public void drawCellWalls(DrawSurface d) {
        d.setColor(Color.BLACK);
        if (upWall)
            d.drawLine(realI, realJ, realI + size, realJ);
        if (downWall)
            d.drawLine(realI, realJ + size, realI + size, realJ + size);
        if (leftWall)
            d.drawLine(realI, realJ, realI, realJ + size);
        if (rightWall)
            d.drawLine(realI + size, realJ, realI + size, realJ + size);
    }


    /**
     * This method check if the location is inside the maze.
     *
     * @param i
     * @param j
     * @return - true or false
     */
    public Boolean isIndexValid(int i, int j) {
        if (i < 0 || j < 0 || i >= Global.rows || j >= Global.cols) {
            return false;
        }
        return true;
    }

    /**
     * This method return random unvisited neighbor.
     *
     * @param array
     * @return - random neighbors.
     */
    public Cell getRandomNeighborWallsDontMatters(Cell[][] array) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();

        if (isIndexValid(i, j - 1) && !array[i][j - 1].visited) {
            neighbors.add(array[i][j - 1]);
        }

        if (isIndexValid(i, j + 1) && !array[i][j + 1].visited) {
            neighbors.add(array[i][j + 1]);
        }

        if (isIndexValid(i - 1, j) && !array[i - 1][j].visited) {
            neighbors.add(array[i - 1][j]);
        }

        if (isIndexValid(i + 1, j) && !array[i + 1][j].visited) {
            neighbors.add(array[i + 1][j]);
        }

        // pick one neighbors.
        if (neighbors.size() > 0) {
            Random random = new Random();
            int r = random.nextInt(neighbors.size());
            return neighbors.get(r);
        }
        return null;
    }

    public Cell getRandomNeighbor(Cell[][] array) {
        ArrayList<Cell> neighbors = new ArrayList<Cell>();

        if (isIndexValid(i, j - 1) && !array[i][j - 1].visited && !upWall) {
            neighbors.add(array[i][j - 1]);
        }

        if (isIndexValid(i, j + 1) && !array[i][j + 1].visited && !downWall) {
            neighbors.add(array[i][j + 1]);
        }

        if (isIndexValid(i - 1, j) && !array[i - 1][j].visited && !leftWall) {
            neighbors.add(array[i - 1][j]);
        }

        if (isIndexValid(i + 1, j) && !array[i + 1][j].visited && !rightWall) {
            neighbors.add(array[i + 1][j]);
        }

        // pick one neighbors.
        if (neighbors.size() > 0) {
            Random random = new Random();
            int r = random.nextInt(neighbors.size());
            return neighbors.get(r);
        }
        return null;
    }

    public double calculateHeuristic(Cell target) {
        return Math.sqrt(Math.pow(this.i - target.i,2) + Math.pow(this.j - target.j,2));
    }


    //need for the Astar search...
    @Override
    public int compareTo(Cell n) {
        return Double.compare(this.f, n.f);
    }
}
