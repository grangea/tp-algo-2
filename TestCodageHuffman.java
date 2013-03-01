package codagehuffman;

import codagehuffman.CodageHuffman;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCodageHuffman {
	/**
	 * @brief  Teste differents fichiers a compresser et a decompresser
	 * @date   Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	
	private static String nomFichierOriginal = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexte1.txt";
	private static String nomFichierCompresse = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexteCompresse1.txt";
	private static String nomFichierDecompresse = "C:/Users/Lutine/Documents/ExemplesTexteAlgo/fichierTexteDecompresse1.txt";
	
	 /*
	 * static String nomFichierCompresse =
	 * "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt"
	 * ; 
	 * static String nomFichierDecompresse =
	 * "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteDecompresse.txt"
	 * ;
	 */
	
	// Variables pour les temps d'exécution
	private static long start;
	private static long duree;
	
	public static void main(String[] args){

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
	
	private static void testCompression() {
		System.out.println("********************COMPRESSION FICHIER*******************");
		CodageHuffman c1 = new CodageHuffman(nomFichierOriginal,
				nomFichierCompresse);
		c1.compresserFichier();	
	}
	
	private static void testDecompression() {
		System.out.println("*******************DECOMPRESSION FICHIER******************");
		CodageHuffman c2 = new CodageHuffman(nomFichierCompresse,
				nomFichierDecompresse);
		c2.decompresserFichier();
	}
}
