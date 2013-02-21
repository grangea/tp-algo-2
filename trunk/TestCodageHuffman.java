package codagehuffman;

import codagehuffman.CodageHuffman;

public class TestCodageHuffman {

	public static void main(String[] args) {
		String nomFichierCompresse = "C:/Users/Lutine/Documents/fichierTexte.txt";
		CodageHuffman c1 = new CodageHuffman(nomFichierCompresse);
		c1.compresserFichier();

		// String nomFichierDecompresse="";
		// CodageHuffman c2 = new CodageHuffman(nomFichierDecompresse);
		// c2.decompresserFichier();

	}
}
