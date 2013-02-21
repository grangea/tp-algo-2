package codagehuffman;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

public class TraitementFichier {

	// Hashtable permettant de stocker le couple (codeASCII d'un caractere,
	// nombre d'occurences de ce caractere)
	protected Hashtable<Integer, ArbreHuffman<Character>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Character>>();;

	/**
	 * Lit les caracteres du fichier a compresser 
	 * Ajoute chaque caractere dans la Hashtable si non present 
	 * Sinon modifie leur priorite
	 */
	public void lireFichierACompresser(String nomFichier) {
		try {
			// Permet de lire le fichier à compresser
			BufferedReader fichierTexte = new BufferedReader(new FileReader(
					new File(nomFichier)));
			// Code ASCII correspondant au caractère lu
			int caractereLuCodeAscii;

			if (fichierTexte == null)
				throw new FileNotFoundException("Le fichier " + nomFichier
						+ " n'a pas ete trouve:");

			// Lecture des caractères un par un dans le fichier
			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
				// this.texte += (char)caractereLuCodeAscii;

				// Si le caractère n'a jamais été vu avant
				// On l'ajoute dans la Hashtable
				if (!arbreHuffmanLettresFichier
						.containsKey(caractereLuCodeAscii)) {
					arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
							new ArbreHuffman((char) caractereLuCodeAscii));
				} else { // Si le caractère a déjà été vu et enregistré
							// On augmente son nombre d'occurences dans la
							// hashtable à partir de son codeASCII
					ArbreHuffman<Character> arbreHuffman = arbreHuffmanLettresFichier
							.get(caractereLuCodeAscii);
					arbreHuffman.incrementerPriorite();
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
	
	/**
	 * Ecrit l'entete contenant chaque symbole avec son code 
	 * Puis le texte à l'aide des codes des caractères que l'on a déterminé 
	 * avec notre arbre de huffman
	 */
	public void ecrireFichierACompresser(String entete) { // A revoir
		File f = new File(
				"C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt");
				//"C:/Users/Lutine/Documents/TexteCompresse.txt");
				DataOutputStream dos = null;
		
		String chaineAecrire = "";
		// Code ASCII correspondant à un caractère
		int caractereLuCodeAscii;

		try {
			dos = new DataOutputStream(new FileOutputStream(f));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		String[] enteteElements = entete.split("\\|");
		Hashtable<String, String> ht = new Hashtable();

		for (int i = 0; i < enteteElements.length; i = i + 3) {
			ht.put(enteteElements[i + 1], enteteElements[i]);
		}

		try {
			BufferedReader fichierTexte = new BufferedReader(
					new FileReader(
							new File(
									"C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt"))); // A
																																		// changer
																																		// pour
																																		// le
																																		// nom
																																		// du
																																		// fichier

			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
				chaineAecrire += ht.get((char) caractereLuCodeAscii);
			}
		} catch (IOException e) {
			// blabla
		}

		chaineAecrire = entete + chaineAecrire;

		try {
			int conv;
			for (int i = 0; i < chaineAecrire.length(); i += 8) {
				conv = 0;
				// convertit 8 bits :
				for (int j = 0; j < 8; j++) {
					conv = conv << 1; // dÃ©cale d'un bit vers la gauche ton
										// nombre convertit (revient a
										// multiplier par 2!)

					// conversion du bit
					conv = conv + (chaineAecrire.charAt(i + j) - '0');
				}

				// ecriture dans le fichier :
				dos.write(conv);
			}

			dos.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void lireFichierADecompresser(String nomFichier) {

	}

}
