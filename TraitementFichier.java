package codagehuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

abstract class TraitementFichier {

	/** Lit les caracteres du fichier a compresser
	 *  Ajoute chaque caractere dans la Hashtable si non pr�sent
	 *  Sinon modifie leur priorite*/
            public static Hashtable<Integer, ArbreHuffman<Character>> lire(
			String nomFichier) {
		try {
			BufferedReader fichierTexte = new BufferedReader(new FileReader(
					new File(nomFichier)));
			int caractereLuCodeAscii;
			Hashtable<Integer, ArbreHuffman<Character>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Character>>();

			if (fichierTexte == null)
				throw new FileNotFoundException("Le fichier " + nomFichier
						+ " n'a pas ete trouve:");

			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
				if (!arbreHuffmanLettresFichier
						.containsKey(caractereLuCodeAscii)) {
					arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
							new ArbreHuffman((char) caractereLuCodeAscii));
				} else {
					ArbreHuffman<Character> arbreHuffman = arbreHuffmanLettresFichier
							.get(caractereLuCodeAscii);
					arbreHuffman.incrementerPriorite();
				}
			}

			return arbreHuffmanLettresFichier;

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());

			return null;
		} catch (IOException e) {
			System.out.println(e.getMessage());

			return null;
		}

	}

	public void ecrire() {

	}
}
