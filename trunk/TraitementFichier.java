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
import java.util.Set;

public class TraitementFichier {

	// Hashtable permettant de stocker le couple (codeASCII d'un caractere,
	// nombre d'occurences de ce caractere)
	protected Hashtable<Integer, ArbreHuffman<Integer>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Integer>>();;
	protected Hashtable<Integer, String> codageLettres = new Hashtable<Integer,String>();
	
	/**
	 * Lit les caracteres du fichier a compresser 
	 * Ajoute chaque caractere dans la Hashtable si non present 
	 * Sinon modifie leur priorite
	 */
	public void lireFichierACompresser(String nomFichier) {
		try {
			// Permet de lire le fichier � compresser
			BufferedReader fichierTexte = new BufferedReader(new FileReader(
					new File(nomFichier)));
			// Code ASCII correspondant au caract�re lu
			int caractereLuCodeAscii;

			if (fichierTexte == null)
				throw new FileNotFoundException("Le fichier " + nomFichier
						+ " n'a pas ete trouve:");

			// Lecture des caract�res un par un dans le fichier
			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
				// this.texte += (char)caractereLuCodeAscii;

				// Si le caract�re n'a jamais �t� vu avant
				// On l'ajoute dans la Hashtable
				if (!arbreHuffmanLettresFichier
						.containsKey(caractereLuCodeAscii)) {
					arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
							new ArbreHuffman<Integer>(caractereLuCodeAscii));
					
				} else { // Si le caract�re a d�j� �t� vu et enregistr�
							// On augmente son nombre d'occurences dans la
							// hashtable � partir de son codeASCII
					ArbreHuffman<Integer> arbreHuffman = arbreHuffmanLettresFichier
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
	 * Puis le texte � l'aide des codes des caract�res que l'on a d�termin� 
	 * avec notre arbre de huffman
	 */
	public void ecrireFichierACompresser(String entete) { // A revoir
		File f = new File(
				"C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt");
				//"C:/Users/Lutine/Documents/TexteCompresse1.txt");
				DataOutputStream dos = null;
		
		String chaineAecrire = "";
		// Code ASCII correspondant � un caract�re
		int caractereLuCodeAscii;

		try {
			dos = new DataOutputStream(new FileOutputStream(f));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

                //Temporaire
                Set keySet = codageLettres.keySet();
                Object[] keys = keySet.toArray();
                for(int i=0; i < codageLettres.size(); i++){
                    Object key = keys[i];
                    System.out.println("cle : "+ key +" valeur : " + codageLettres.get(key));
                }
		/*
		String[] enteteElements = entete.split("\\|");
		Hashtable<String, String> ht = new Hashtable();

		for (int i = 0; i < enteteElements.length; i = i + 3) {
			ht.put(enteteElements[i + 1], enteteElements[i]);
		}
*/
		try {
			//TODO generer nouveau fichier avec ancien nom du fichier + "compresse"
			BufferedReader fichierTexte = new BufferedReader(
					new FileReader(
							new File(
							//"C:/Users/Lutine/Documents/TexteCompresse2.txt")));

									"C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt")));

			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
				chaineAecrire += codageLettres.get(caractereLuCodeAscii);
			}
		} catch (IOException e) {

		}

		chaineAecrire = entete + chaineAecrire;

		try {
			int conv;
			for (int i = 0; i < chaineAecrire.length(); i += 8) {
				conv = 0;
				// convertit 8 bits :
				for (int j = 0; j < 8; j++) {
					conv = conv << 1; // décale d'un bit vers la gauche ton
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
