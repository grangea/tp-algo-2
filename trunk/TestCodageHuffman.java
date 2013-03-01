package codagehuffman;

import codagehuffman.CodageHuffman;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCodageHuffman {
	/**
	 * @brief Teste differents fichiers a compresser et a decompresser
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	private static int NB_FICHIERS = 3;

	public static void main(String[] args) {
		double start;
		double duree;
		CodageHuffman[] ch = new CodageHuffman[NB_FICHIERS];

		try {
			for (int i = 0; i < NB_FICHIERS; i++) {
				ch[i] = new CodageHuffman("Texte" + i + ".txt", "Texte" + i
						+ "Compresse.txt");
				System.out.println("_____________________FICHIER TEXTE " + i
						+ "_____________________");
				start = System.nanoTime();
				ch[i].compresserFichier();
				duree = System.nanoTime() - start;
				System.out
						.println("**********************COMPRESSION*********************");
				System.out.println("Duree d'éxécution : " + (duree*0.000000001) + " s");
				System.out
						.println("*********************DECOMPRESSION*********************");
				ch[i] = new CodageHuffman("Texte" + i + "Compresse.txt",
						"Texte" + i + "Decompresse.txt");
				start = System.nanoTime();
				ch[i].decompresserFichier();
				duree = System.nanoTime() - start;
				System.out.println("Duree d'éxécution : " + (duree*0.000000001) + " s");
				System.out
						.println("_________________________________________________________");
				System.out.println("");
			}

		} catch (FileNotFoundException e) {
			System.out.println("Le fichier est introuvable!");
		} catch (IOException e) {
			System.out.println("------------" + e + "------------");
		}
	}
}
