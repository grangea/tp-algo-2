package codagehuffman;

import codagehuffman.CodageHuffman;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCodageHuffman {
	
	//Variables pour gerer les fichiers
	static String nomFichierOriginal = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexte1.txt";
	static String nomFichierCompresse = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexteCompresse1.txt";
	static String nomFichierDecompresse = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexteDecompresse1.txt";
	
	 /*
	 * static String nomFichierCompresse =
	 * "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt"
	 * ; 
	 * static String nomFichierDecompresse =
	 * "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteDecompresse.txt"
	 * ;
	 */
	
	// Variables pour les temps d'exécution
	static long start;
	static long duree;
	
	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		System.out.println("_____________________FICHIER TEXTE 1_____________________");
		start = System.nanoTime();
		testCompression();
		duree = System.nanoTime() - start;	
		System.out.println("Duree d'éxécution pour la compression du fichier: "+duree+" ns");
		
		start = System.nanoTime();
		testDecompression();	
		duree = System.nanoTime() - start;
		System.out.println("Duree d'éxécution pour la décompression du fichier: "+duree+" ns");		

	}
	
	private static void testCompression() throws FileNotFoundException, IOException{
		System.out.println("********************COMPRESSION FICHIER*******************");
		CodageHuffman c1 = new CodageHuffman(nomFichierOriginal,
				nomFichierCompresse);
		// Compression du fichier original
		c1.compresserFichier();	
	}
	
	private static void testDecompression() throws FileNotFoundException, IOException{
		System.out.println("*******************DECOMPRESSION FICHIER******************");
		CodageHuffman c2 = new CodageHuffman(nomFichierCompresse,
				nomFichierDecompresse);
		// Decompression du fichier que l'on a compressé
		c2.decompresserFichier();
	}
}
