package codagehuffman;

import codagehuffman.CodageHuffman;

public class TestCodageHuffman {

	public static void main(String[] args) {
		String nomFichierDecompresse = "C:/Users/Lutine/Documents/fichierTexte1.txt";
		String nomFichierModif1 = "C:/Users/Lutine/Documents/fichierTexteModif1.txt";
        //String nomFichierCompresse = "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt";
		CodageHuffman c1 = new CodageHuffman(nomFichierDecompresse,nomFichierModif1);
		c1.compresserFichier();

		String nomFichierCompresse="C:/Users/Lutine/Documents/fichierTexte2.txt";
		String nomFichierModif2 = "C:/Users/Lutine/Documents/fichierTexteModif2.txt";
		CodageHuffman c2 = new CodageHuffman(nomFichierCompresse,nomFichierModif2);
		c2.decompresserFichier();

	}
}
