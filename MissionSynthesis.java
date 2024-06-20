import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();
        List<Bond> candidateBonds= new ArrayList<>();

        //Getting an initial list of all the candidate bonds:
        for (MolecularStructure structHuman: humanStructures){
            for (MolecularStructure structVitales: diffStructures){
                Molecule humanMol= structHuman.getMoleculeWithWeakestBondStrength();
                double humanWeight= humanMol.getBondStrength();
                Molecule vitalesMol=  structVitales.getMoleculeWithWeakestBondStrength();
                double vitalesWeight= vitalesMol.getBondStrength();
                Bond curr = new Bond(humanMol, vitalesMol,(humanWeight+vitalesWeight)/2);
                candidateBonds.add(curr);
            }
        }

        //Now we'll do Kruskal's
        MST tree = new MST();
        for (Bond bond : candidateBonds) {
            tree.addMolecule(bond.getFrom().getId());
            tree.addMolecule(bond.getTo().getId());
        }

        // Kruskal's algorithm to find the minimum spanning tree
        candidateBonds.sort(Comparator.comparingDouble(Bond::getWeight));
        for (Bond currentBond : candidateBonds) {
            String root1 = tree.find(currentBond.getFrom().getId());
            String root2 = tree.find(currentBond.getTo().getId());
            //If they are not in the same group, connect them
            if (!root1.equals(root2)) {
                serum.add(currentBond);
                tree.connect(root1, root2);
            }
        }


        /* YOUR CODE HERE */

        return serum;
    }

    // Method to print the synthesized bonds
    public void printSynthesis(List<Bond> serum) {
        double sum=0.00;
        System.out.print("Typical human molecules selected for synthesis: [");
        if (!humanStructures.isEmpty()) {
            for (int i = 0; i < humanStructures.size(); i++) {
                MolecularStructure structure = humanStructures.get(i);
                System.out.print(structure.getMoleculeWithWeakestBondStrength().getId());
                if (i < humanStructures.size() - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println("]");
        System.out.print("Vitales molecules selected for synthesis: [");
        if (!diffStructures.isEmpty()) {
            for (int i = 0; i < diffStructures.size(); i++) {
                MolecularStructure structure = diffStructures.get(i);
                System.out.print(structure.getMoleculeWithWeakestBondStrength().getId());
                if (i < diffStructures.size() - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println("]");
        System.out.println("Synthesizing the serum...");
        for (Bond jamesBond:serum){
            System.out.print("Forming a bond between "+jamesBond.getFrom().getId()+" to "+jamesBond.getTo().getId()+" with strength " );
            System.out.printf("%.2f\n", jamesBond.getWeight());

            sum+=jamesBond.getWeight();

        }
        System.out.print("The total serum bond strength is ");
        System.out.printf("%.2f\n", sum);


    }
    class MST {
        private Map<String, String> parent = new HashMap<>();
        private Map<String, Integer> rank = new HashMap<>();

        //Initializing an id for a molecule
        public void addMolecule(String id) {
            parent.put(id, id);
            rank.put(id, 0);
        }

        public String find(String id) {
            if (!id.equals(parent.get(id))) {
                parent.put(id, find(parent.get(id)));
            }
            return parent.get(id);
        }

        public void connect(String x, String y) {
            parent.put(find(x), find(y));

        }

    }
}
