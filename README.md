# Maze-Generator-And-Solver
## About The Project
Imagine that you wake up inside a maze. What strategy should you take to visit as few cells as possible to find the exit? 
In this project I created a program that randomly creates a maze, and then solves it according to different algorithms and try to decide which is better.
In addition I run the program 10,000 time and the analysis of the runs appears under the Analysis section

## Built With
The project was built in JAVA combined with a GUI library called biuoop-1.4.jar

## Getting Started
First you need to download this project to your choosen file. 
After thet you need to run the program via some IDE or CMD. The file that needs to be run is called Main.java and it is inside the src folder.

## Demonstration
https://user-images.githubusercontent.com/71848366/183287045-acbef8cc-533d-4d3d-bb26-d6bb30032a67.mp4

## Analysis
I ran 10000 times creation of a maze with 10000 cells and solving by DFS, BFS and A*, with a random start and end point to get an accurate avarage of the data. and here are the results (all data appears at the end of the video) :
1. When the end point is close to the start (in the quarter of the maze closest to you) you should choose BFS, that solves the maze quickly without a lot of unnecessary cell visits.
2. If the distance to the finish is relatively large (far from the quarter closest to you) you should choose DFS or A * when surprisingly I did not find a significant difference in the number of cells each visited.
3. If you do not know the distance from the end point, as can be seen in the first pie graph - you should choose A * or DFS that have visited significantly fewer cells than the BFS.
4. When the end point is in the center of the maze - it is not worth choosing BFS - that manages to "beat" its opponents in the number of visits at a rate of 11%! Therefore, you should choose DFS or A*. 
5. Even in a relatively small maze (500 cells) the ratio between the searches is maintained (This can be seen in the attached second graph).


![1](https://user-images.githubusercontent.com/71848366/183287031-3d12b69a-e434-42fa-9fc0-3b542878b301.jpeg)

![2](https://user-images.githubusercontent.com/71848366/183287037-cd5a9663-5606-43b9-a525-9dee6bd21026.png)

![3](https://user-images.githubusercontent.com/71848366/183287041-88d7a8b5-bde4-4feb-8870-93736f94c871.jpeg)

![4](https://user-images.githubusercontent.com/71848366/183287042-d2a58859-c545-4e68-92f4-cdb12ab1c93a.png)
