package application;

import codagehuffman.CodageHuffman;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TestCodageHuffman {
	/**
	 * @brief Teste differents fichiers a compresser et a decompresser, se trouvant dans le dossier "textes/"
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	private static int NB_FICHIERS = 1;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		/*double start;
		double duree;
		long txcompression;
		CodageHuffman[] ch = new CodageHuffman[NB_FICHIERS];

		try {
			for (int i = 0; i < NB_FICHIERS; i++) {
				ch[i] = new CodageHuffman("textes/texte" + i + ".txt", "textes/texte" + i
						+ "Compresse.txt");
				System.out.println("_____________________FICHIER TEXTE " + i
						+ "_____________________");
				start = System.nanoTime();
				ch[i].compresserFichier();
				duree = System.nanoTime() - start;
				txcompression=(new File("textes/texte" + i + "Compresse.txt").length())*100/((new File("textes/texte" + i + ".txt").length()));
				System.out
						.println("**********************COMPRESSION*********************");
				System.out.println("Duree d'execution : "
						+ (duree * 0.000000001) + " s");
				System.out.println("Taux de compression : " + txcompression + " %");
				System.out
						.println("*********************DECOMPRESSION*********************");
				ch[i] = new CodageHuffman("textes/texte" + i + "Compresse.txt",
						"Textes/Texte" + i + "Decompresse.txt");
				start = System.nanoTime();
				ch[i].decompresserFichier();
				duree = System.nanoTime() - start;
				System.out.println("Duree d'execution : "
						+ (duree * 0.000000001) + " s");
				System.out
						.println("_________________________________________________________");
				System.out.println("");
			}

		} catch (FileNotFoundException e) {
			System.out.println("Le fichier est introuvable!");
		} catch (IOException e) {
			System.out.println("------------" + e + "------------");
		}*/
                double start;
		double duree;
                start = System.nanoTime();
                CodageHuffman ch1 = new CodageHuffman("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt","C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt");
                ch1.compresserFichier();
                duree = System.nanoTime() - start;
                System.out.println(duree);
                start = System.nanoTime();
                CodageHuffman ch2 = new CodageHuffman("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt","C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteDecompresse.txt");
                ch2.decompresserFichier();
                duree = System.nanoTime() - start;
                System.out.println(duree);
	}
}
