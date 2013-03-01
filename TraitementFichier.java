package codagehuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.TreeSet;

public class TraitementFichier {
	/**
	 * @brief Traite un fichier qui doit compresser ou decompresser
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	/*
	 * private class LongueurStringeyComparator implements Comparator<String> {
	 * 
	 * @Override public int compare(String s1, String s2) { if (s1.length() >
	 * s2.length()) return 1; else return -1; }
	 * 
	 * } private Comparator comparator = new LongueurStringeyComparator();
	 */
	protected Hashtable<Integer, ArbreHuffman<Integer>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Integer>>();;
	/**
	 * Hashtable permettant de stocker le couple (codeASCII d'un caractere,
	 * nombre d'occurences de ce caractere)
	 */

	protected Hashtable<Integer, String> codageLettres = new Hashtable<Integer, String>();
	/**
	 * Hastable permettant de stocker le couple(Cle:code ascii du caractere,
	 * code binaire)
	 */

	private TreeMap<String, Integer> decodageLettres = new TreeMap<String, Integer>();
	/**
	 * Treemap permettant de stocker le couple(Cle:code binaire, code ascii du
	 * caractere)
	 */

	/**
	 * Lit les caracteres du fichier a compresser Ajoute chaque caractere dans
	 * la Hashtable si non present Sinon modifie leur priorite
	 */
	protected void lireFichierACompresser(String nomFichierOriginal)
			throws FileNotFoundException, IOException {
		// Permet de lire le fichier a compresser
		BufferedReader fichierTexte = new BufferedReader(new FileReader(
				new File(nomFichierOriginal)));
		// Code ASCII correspondant au caractere lu
		int caractereLuCodeAscii;
		int nbCaracteresLus=0;

		// Lecture des caracteres un par un dans le fichier
		while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
			// this.texte += (char)caractereLuCodeAscii;
			nbCaracteresLus++;
			// Si le caractere n'a jamais ete vu avant
			// On l'ajoute dans la Hashtable
			if (!arbreHuffmanLettresFichier.containsKey(caractereLuCodeAscii)) {
				arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
						new ArbreHuffman<Integer>(caractereLuCodeAscii));

			} else { // Si le caractere a deja ete vu et enregistre
						// On augmente son nombre d'occurences dans la
						// hashtable a partir de son codeASCII
				ArbreHuffman<Integer> arbreHuffman = arbreHuffmanLettresFichier
						.get(caractereLuCodeAscii);
				arbreHuffman.incrementerPriorite();
			}
		}
		System.out.println("Nombre de caracteres lus:" + nbCaracteresLus);
	}

	/**
	 * Ecrit l'entete contenant chaque symbole avec son code Puis le texte a
	 * l'aide des codes des caractEres que l'on a determine avec notre arbre de
	 * huffman
	 */
	protected void ecrireFichierACompresser(String nomFichierOriginal,
			String nomFichierCompresse, String entete)
			throws FileNotFoundException, IOException { // A revoir
		int caractereLuCodeAscii;
		char caractereLuBit;
		String codeCaractere;

		BufferedReader fichierTexte = new BufferedReader(new FileReader(
				new File(nomFichierOriginal)));
		FileOutputStream fichierTexteCompresse = new FileOutputStream(
				nomFichierCompresse);

		int offset = 0;
		int bits = 0;

		// Ecriture de l'entete
		for (int i = 0; i < entete.length() - 1; i++) {
			fichierTexteCompresse.write(entete.charAt(i));
		}
		fichierTexteCompresse.write(';');

		// Ecriture du corps
		while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
			// Recuperation du code correspond au caractere lu
			codeCaractere = codageLettres.get(caractereLuCodeAscii);

			// Lecture du code bit par bit
			for (int i = 0; i < codeCaractere.length(); i++) {
				caractereLuBit = codeCaractere.charAt(i);

				if (caractereLuBit == '0') {
					bits <<= 1;
				} else {
					bits = bits << 1 | 1;
				}
				offset++;
				if (offset == 8) {
					fichierTexteCompresse.write(bits);
					bits = 0;
					offset = 0;
				}
			}
		}

		fichierTexte.close();
		fichierTexteCompresse.close();

		System.out
				.println("Nombre de caracteres differents : "
						+ codageLettres.size());
	}

	/**
	 * Lit l'entete du texte qui a ete compresse Recree les couples de
	 * cle-valeur (code binaire, caracterere en ascii) Lit le texte compresse
	 * puis ecrit le texte decode dans le nouveau fichier
	 */
	protected void lireEcrireFichierADecompresser(String nomFichierCompresse,
			String nomFichierDecompresse) throws FileNotFoundException,
			IOException {

		FileInputStream fichierCompresse = new FileInputStream(
				nomFichierCompresse);
		FileOutputStream fichierDecompresse = new FileOutputStream(
				nomFichierDecompresse);

		// ----- Lecture de l'entete et generation de la hashtable------
		boolean estEntete = true;
		boolean entreeEntiere = false;
		String codeAsciiCaractere = "";
		String codeCaractere = "";
		int caractereLuCodeAscii;
		char caractereLuChar;

		while (estEntete) {
			caractereLuCodeAscii = fichierCompresse.read();
			caractereLuChar = (char) caractereLuCodeAscii; // A voir si c crade
															// ou pas
			if (caractereLuChar == ';') {
				estEntete = false;
				decodageLettres.put(codeCaractere,
						Integer.parseInt(codeAsciiCaractere));
				codeCaractere = "";
			} else if ((char) caractereLuCodeAscii == '|') {
				if (entreeEntiere) {
					decodageLettres.put(codeCaractere,
							Integer.parseInt(codeAsciiCaractere));
					codeAsciiCaractere = "";
					codeCaractere = "";
					entreeEntiere = false;
				} else {
					entreeEntiere = true;
				}
			} else {
				if (entreeEntiere) { // On a fini de trouver la valeur de
										// l'entree
					codeAsciiCaractere += caractereLuChar;
				} else { // On a fini de trouver la cle de l'entree
					codeCaractere += caractereLuChar;
				}
			}
		}
		// ----- Fin de la lecture de l'entete ------------------------------

		// ----- Lecture du texte code et decompression ---------------------
		int bits = 0;
		int mask = 0;
		int maskTmp = 0;
		int bitsTmp = 0;
		while (true) {
			maskTmp = mask;

			if (maskTmp == 0) {
				bitsTmp = fichierCompresse.read();
				if (bitsTmp == -1)
					break;

				bits = bitsTmp;
				maskTmp = 0x80;
			} else {
				bitsTmp = bits;
			}

			// On cherche a savoir si on a le bit 0 ou 1
			if ((bitsTmp & maskTmp) == 0) {
				mask = maskTmp >> 1;
				codeCaractere += '0';
			} else {
				mask = maskTmp >> 1;
				codeCaractere += '1';
			}

			// On cherche a savoir si le code correspond à un code de la
			// hashtable
			if (decodageLettres.containsKey(codeCaractere)) {
				fichierDecompresse.write(decodageLettres.get(codeCaractere));
				codeCaractere = "";
			}
		}
		// ----- Fin de la lecture du texte code ----------------------------

		fichierCompresse.close();
		fichierDecompresse.close();

	}

}