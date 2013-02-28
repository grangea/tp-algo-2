package codagehuffman;

import codagehuffman.CodageHuffman;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCodageHuffman {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		//String nomFichierDecompresse = "C:/Users/Lutine/Documents/fichierTexte1.txt";
		//String nomFichierModif1 = "C:/Users/Lutine/Documents/fichierTexteModif1.txt";
                String nomFichierOriginal = "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt";
                String nomFichierCompresse = "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt";
                String nomFichierDecompresse = "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteDecompresse.txt";
                
		CodageHuffman c1 = new CodageHuffman(nomFichierOriginal,nomFichierCompresse);
		c1.compresserFichier();
                
                CodageHuffman c2 = new CodageHuffman(nomFichierCompresse,nomFichierDecompresse);
		c2.decompresserFichier();

	}
}
