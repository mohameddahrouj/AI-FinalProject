# AI-FinalProject
This project tackles the notorious Rush Hour game using breadth first search, A* and simulated annealing algorithms.

1.	The main method for the project is in ./src/project/AppRunner.java
2.	A graphical user interface will be launched and is used to visualize, run search algorithms and view metrics
3.	Along side the GUI, a log of moves and and metrics are displayed in the console.

## Running the code:
4.	Simply select a parking lot orientation from the drop down list
5.	Select a search algorithm
6.	Select "Solve" to the solve using a desired algorithm
7.	To visualize the vehicle moves, select "Show"
8.	The metrics are constantly updated to reflect the parking orientation and algorithm chosen.

## The Algorithms:
Both the A* and simulated annealing algorithms use heuristics to process the viability of their successor nodes. Below is a brief description of each algorithm.

### Heuristics:
A* used a Manhattan distance heuristic while the simulated annealing search algorithm used a Euclidean distance heuristic.

### Note on Simulated Annealing:
For more complex parking lot orientations, SA is not expected to converge to an optimal solution by nature. The starting temperature and descent gradient are globally set in ./src/project/ControlPanel.java when initializing the Scheduler for the algorithm. 

| K Steps (Boltzmann constant) | Descent Gradient (Reduction Rate) | Initial Temperature (Maximum) |
|:----------------------------:|:---------------------------------:|:-----------------------------:|
|              20              |              0.00005              |           1,000,000           |

Table 1. Initial Scheduler values for simulated annealing search algorithm
