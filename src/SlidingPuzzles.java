/**--------------------------------------------------------------------------------
 * @author AKSHAYAN MOHANDAS - w1867142
 * Slinding puzzles - Check whether a directed graph is acyclic.
 * --------------------------------------------------------------------------------*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class SlidingPuzzles{
    private int nodes;
    private ArrayList<ArrayList<Integer>> adjList;

    //The readFile() method reads the graph description from a file and creates the adjacency list.
    public void readFile(String filename) {
        try {
            Scanner fileScanner = new Scanner(new File(filename));

            //get number of vertices in graph - first value in the file
            nodes = fileScanner.nextInt();
            System.out.println("\n------------------------------------------------------------");
            System.out.print("This graph has " + nodes + " vertices: ");

            int[] vertices = new int[nodes];
            for (int i = 0; i < nodes; i++) {
                vertices[i] = i;
                System.out.print(vertices[i] + " ");
            }
            System.out.println("\n------------------------------------------------------------");

            // Reads the number of vertices in the graph and creates an empty adjacency list for each vertex.
            adjList = new ArrayList<>(nodes);

            for (int i = 0; i < nodes; i++) {
                adjList.add(new ArrayList<>());
            }

            //While there are numbers left, read two and use them to add an edge
            while (fileScanner.hasNextInt()) {
                int src = fileScanner.nextInt();
                int dest = fileScanner.nextInt();

                if (src >= nodes || dest >= nodes) {
                    throw new InputMismatchException("Input file contains invalid vertex values.");
                }

                //add edge to the graph by connecting nodes
                connectNodes(src, dest);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Text file " + filename + " not found.");
            System.out.println("Do you want to enter another file name? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("y")) {
                System.out.println("Please enter the filename:");
                String newFilename = scanner.nextLine();
                readFile(newFilename);
            } else {
                System.exit(0);
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input in file: " + e.getMessage());
            System.out.println("Do you want to enter another file name? (y/n)");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("y")) {
                System.out.println("Please enter the filename:");
                String newFilename = scanner.nextLine();
                readFile(newFilename);
            } else {
                System.exit(0);
            }
        }
    }

    //Adds an edge between two vertices by adding the destination vertex to the adjacency list of the source vertex
    public void connectNodes(int src, int dest) {
        adjList.get(src).add(dest);
    }

    //Prints the graph by iterating through each source vertex and printing its adjacency list
    public void displayGraph() {
        for (int srcVertex = 0; srcVertex < nodes; srcVertex++) {
            //checks if the source vertex is not null
            if (adjList.get(srcVertex) != null) {
                for (int destVertex : adjList.get(srcVertex)) {
                    System.out.println(srcVertex + " -> " + destVertex);
                }
            }
        }
        System.out.println();
    }

    public void sinkElimination() {

        // variable for storing sinks
        int sinkVertex;

        // variable for the path
        int currentVertex;

        // counts how many null values are in the adjacency list
        int nullCounter = 0;

        for (int i = 0; i < adjList.size(); i++) {
            ArrayList<Integer> currentAdjacency = adjList.get(i);
            // skips null vertices
            if (currentAdjacency == null) {
                nullCounter++;
                continue;
            }
            // checks if the vertex is a sink
            if (currentAdjacency.isEmpty()) {
                sinkVertex = i;
                // set the sink to null and remove its edges from other vertices
                adjList.set(sinkVertex, null);
                for (int j = 0; j < adjList.size(); j++) {
                    ArrayList<Integer> updatedList = adjList.get(j);
                    if (updatedList != null) {
                        updatedList.remove((Integer) sinkVertex);
                    }
                }
                System.out.println("Sink " + sinkVertex + " has been found and eliminated.");
                System.out.println("------------------------------------------------------------");
                displayGraph();
                // recursively call this method until there are no more sinks
                sinkElimination();
                return;
            }
        }

        // if no sink is found, determine if the graph is acyclic or cyclic
        System.out.println("No more sinks found");
        System.out.println("------------------------------------------------------------");
        if (adjList.size() == nullCounter) {
            System.out.println("yes");
            System.out.println("The graph is acyclic");
            System.out.println("------------------------------------------------------------");
        } else {
            // find a vertex that is not null and start a cycle detection from it
            for (int i = 0; i < adjList.size(); i++) {
                ArrayList<Integer> currentAdjacency = adjList.get(i);
                if (currentAdjacency != null) {
                    currentVertex = i;
                    System.out.println("No");
                    System.out.println("The graph is cyclic");
                    System.out.println("------------------------------------------------------------");
                    String cycle = detectCycle(" ", + currentVertex);
                    System.out.println("Cycle is: " + cycle);
                    System.out.println("------------------------------------------------------------");
                    return;
                }
            }
        }
    }

    public String detectCycle(String currentPath, int currentNode) {

        //converts vertex to string
        String currentVertex = Integer.toString(currentNode);

        //splits the path of vertices by " -> "
        String[] nodes = currentPath.split(" -> ");

        //boolean to check if the cycle is found
        boolean isCycleFound = false;

        //Going through the vertices array
        for (int i = 0; i < nodes.length; i++) {
            //checks to find if the array of vertices has the "vertex"
            if (nodes[i].equals(currentVertex)) {
                isCycleFound = true;
                break;
            }
        }

        //When a vertex appears twice on the path - means cycle found
        if (isCycleFound) {
            String pathWithCycle = currentPath + currentVertex;
            //returns the cycle that was found from the getCycle
            return getCycle(pathWithCycle);
        } else {
            //Makes a random object
            Random randomGenerator = new Random();

            //gets any random index from the graph
            int nextIndex = Math.round(randomGenerator.nextInt(adjList.get(currentNode).size()));

            //randomly checks an index(vertices) in adjacency list
            int nextNode = adjList.get(currentNode).get(nextIndex);

            //the recursive detectCycle call adds the next vertex to the path
            return detectCycle(currentPath + currentVertex + " -> ", nextNode);
        }
    }

    private String getCycle(String currentPath) {
        //Splits the path vertices via " -> "
        String[] nodes = currentPath.split(" -> ");

        //Gets the last element in the array vertices
        String lastNode = nodes[nodes.length - 1];
        String detectedCycle = currentPath.substring(currentPath.indexOf(lastNode));
        //finds cycle of the given lastVertex then returns the cycle
        return detectedCycle;
    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("|------------------------------|");
        System.out.println("|  Welcome to Sliding Puzzles  |");
        System.out.println("|------------------------------|");
        while (true) {
            SlidingPuzzles graphAdjacencyList = new SlidingPuzzles();
            System.out.println("To check if your directed graph is acyclic, \nPlease enter the file name: ");
            String fileName = scanner.nextLine();


            long startTime = System.nanoTime(); // start measuring time

            graphAdjacencyList.readFile(fileName);
            graphAdjacencyList.displayGraph();
            graphAdjacencyList.sinkElimination();

            long endTime = System.nanoTime(); // stop measuring time
            long elapsedNanoSeconds = endTime - startTime;
            double elapsedSeconds = (double) elapsedNanoSeconds / 1_000_000_000.0;
            System.out.print("Total elapsed time: " + elapsedSeconds + " s");

            System.out.println("\nDo you want to enter another file name? (y/n)");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("n")) {
                break;
            }
        }
    }
}

