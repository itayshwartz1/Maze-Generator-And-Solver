import java.util.Comparator;


//this class store data about the runs of the maze. This class help me to compare the searches.
public class Data implements Comparable<Data> {
    float pointDistance;
    float pathLength;
    float DFSVisit;
    float BFSVisit;
    float AStarVisit;


    public Data(float pointDistance, float pathLength, float DFSVisit, float BFSVisit, float AStarVisit) {
        this.pointDistance = pointDistance;
        this.pathLength = pathLength;
        this.DFSVisit = DFSVisit;
        this.BFSVisit = BFSVisit;
        this.AStarVisit = AStarVisit;
    }

    public Data() {
        this.pointDistance = 0;
        this.pathLength = 0;
        this.DFSVisit = 0;
        this.BFSVisit = 0;
        this.AStarVisit = 0;
    }

    @Override
    public int compareTo(Data o) {
        if ((this.pointDistance - o.pointDistance) > 0)
            return 1;
        if((this.pointDistance - o.pointDistance) == 0)
            return 0;
        return -1;
    }
}
