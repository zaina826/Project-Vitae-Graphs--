import java.util.*;
import javax.xml.parsers.*;
import java.io.*;
import org.w3c.dom.*;

// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {
        try {
            List<Molecule> hMol = new ArrayList<>();
            List<Molecule> vMol = new ArrayList<>();

            //Initialize the DocumentBuilderFactory
            //Defines a factory API that enables applications to obtain a parser that produces DOM object trees from XML documents.
            //Making it easier to read the XML function in sections

            File xmlFile = new File(filename);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);

            doc.getDocumentElement().normalize();

            //Getting a list of the molecules:
            NodeList molList = doc.getElementsByTagName("Molecule");
            for (int i = 0; i < molList.getLength(); i++) {
                //For each molecule:
                Element mol = (Element) molList.item(i);

                //Find its id, strength and bonds
                String id = mol.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(mol.getElementsByTagName("BondStrength").item(0).getTextContent());
                NodeList bondList = mol.getElementsByTagName("Bonds");
                List<String> bonds = new ArrayList<>();

                //Now for every node, find the bonds
                for (int bond = 0; bond < bondList.getLength(); bond++) {
                    Element bondElement = (Element) bondList.item(bond);
                    NodeList moleculeIdList = bondElement.getElementsByTagName("MoleculeID");
                    for (int k = 0; k < moleculeIdList.getLength(); k++) {
                        //Iterate over bonds, add bonded molecules to a list called bonds, which will be used to create a molecule in the nect step.
                        Element moleculeIdElement = (Element) moleculeIdList.item(k);
                        String bondId = moleculeIdElement.getTextContent();
                        bonds.add(bondId);
                    }
                }

                //Now we've collected all the info for our molecule, make that molecule
                Molecule molecule = new Molecule(id, bondStrength, bonds);

                //Add this molcules to human/vitales depending on its parent tag name
                String parentTagName = mol.getParentNode().getNodeName();
                if ("HumanMolecularData".equals(parentTagName)) {
                    hMol.add(molecule);
                } else if ("VitalesMolecularData".equals(parentTagName)) {
                    vMol.add(molecule);
                }

                //Now set the molecular data objects for human and vitales.
                molecularDataHuman= new MolecularData(hMol);
                molecularDataVitales = new MolecularData(vMol);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
