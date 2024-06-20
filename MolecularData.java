import java.util.*;
import javax.xml.parsers.*;
import java.io.*;
// Class representing molecular data
public class MolecularData {
    //We have two of this object, one for humans one for vitales
    //It's just a list of nodes belonging to that species, each node has its id, strength, and a list of nodes bonded to it

    int numOfStructures=0;
    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }
    //Gets all the molecules for that species.

    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        ArrayList<MolecularStructure> structures = new ArrayList<>();
        //First we start by constructing our graph:
        //We will iterate over each moledcule, and store the graph in a hash map, where the key is the molecule
        //and the value is a list of molecules that it is bonded to, this is also called an Adjacency list
        HashMap<Molecule, List<Molecule>> graph = new HashMap<>();

        for (Molecule molecule: molecules){
            graph.putIfAbsent(molecule,new ArrayList<>());
            for (String bonded: molecule.getBonds()){
                Molecule bondedMol= findbyid(bonded);
                graph.get(molecule).add(bondedMol);
                graph.putIfAbsent(bondedMol,new ArrayList<>());
                graph.get(bondedMol).add(molecule);
            }
        }

        //Now that the graph is set, we have to do dfs to find the connected componenets, these connected components are the
        //MolecularStructures.
        List<Molecule> visited = new ArrayList<>();
        for (Molecule molecule: molecules) {
            if (!visited.contains(molecule)){
                numOfStructures++;
                MolecularStructure currComp = new MolecularStructure();
                DFS(molecule, graph, currComp, visited);
                structures.add(currComp);
            }
        }

        return structures;
    }

    // Method to print given molecular structures
    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(numOfStructures+" molecular structures have been discovered in "+ species+".");
        int count=1;
        for (MolecularStructure structure : molecularStructures){
            System.out.println("Molecules in Molecular Structure "+ count+": "+ structure.toString());
            count++;
        }
    }

    // Method to identify anomalies given a source and target molecular structure
    // Returns a list of molecular structures unique to the targetStructure only
    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> sourceStructures, List<MolecularStructure> targeStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();
        for (MolecularStructure VitalesStructure: targeStructures) {
            boolean unique= true;
            for (MolecularStructure humanStructure : sourceStructures) {
                if (VitalesStructure.equals(humanStructure)){
                    unique=false;
                }
            }
            if (unique){
                anomalyList.add(VitalesStructure);
            }
        }
        if (anomalyList.isEmpty()) {
            System.out.println("Vitales aren't special, go find another way to make humans immortal.");
        } else {
            System.out.println("Molecular structures unique to Vitales individuals:");
            for (MolecularStructure anomaly : anomalyList) {
                System.out.println(anomaly.toString());
            }
        }

        return anomalyList;
    }

    // Method to print Vitales anomalies
    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        /* YOUR CODE HERE */ 

    }


    //Finding a molecule from its id
    public Molecule findbyid(String id){
        for (Molecule molecule:molecules){
            if (Objects.equals(molecule.getId(), id)){
                return molecule;
            }
        }
        return null;
    }

    //DFS to find connected components:
    public void DFS(Molecule current, HashMap<Molecule, List<Molecule>> adjacencyList, MolecularStructure cc, List<Molecule> visited){
        visited.add(current);
        cc.addMolecule(current);
        for (Molecule neighbor: adjacencyList.get(current)){
            if (!visited.contains(neighbor)){
                DFS(neighbor, adjacencyList,cc,visited);
            }
        }
    }
}
