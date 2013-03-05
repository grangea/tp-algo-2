package codagehuffman;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

public class CodageHuffman {
	/**
	 * @brief  Gere la creation d'arbre de huffman lorsque l'on souahite compresser un fichier
	 * Puis delegue a TraitementFichier
	 * @date   Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	private class PrioriteArbreHuffmanComparator implements
			Comparator<ArbreHuffman> {

		@Override
		public int compare(ArbreHuffman a1, ArbreHuffman a2) {
			if (a1.getPriorite() > a2.getPriorite())
				return 1;
			else if (a1.getPriorite() < a2.getPriorite())
				return -1;
			else
				return 0;
		}

	}

	private Comparator comparator = new PrioriteArbreHuffmanComparator();
	// PAS TOP DE METTRE UNE CAPACITE A REVOIR (jsuis daccord)
	private PriorityQueue<ArbreHuffman> fileArbres = new PriorityQueue<ArbreHuffman>(
			1000, comparator);
	private TraitementFichier tf = new TraitementFichier();
	private String nomFichier = "";
	private String nomFichierModifie = "";

	/*----------------------METHODES PRINCIPALES-----------------------*/
	public CodageHuffman(String nomFichier, String nomFichierModifie) {
		this.nomFichier = nomFichier;
		this.nomFichierModifie = nomFichierModifie;
	}

	public void compresserFichier() throws FileNotFoundException, IOException {
		ArbreHuffman<Integer> arbre = null;

                int nbCaracteresLus = tf.lireFichierACompresser(nomFichier);
                System.out.println("Nombre de caracteres lus : " + nbCaracteresLus);
                
                if(tf.tableauArbresHuffman.length > 0){
                    for(int i=0; i<tf.tableauArbresHuffman.length; i++){
                        if(tf.tableauArbresHuffman[i] != null){
                            fileArbres.add((ArbreHuffman)tf.tableauArbresHuffman[i]);
                        }
                    }

                    int nbCaracteresLusDifferents = fileArbres.size();
                    arbre = creationArbreHuffman();
                    tf.codageLettres = new String[256];
                    arbre.paroursTableauPrefixe(tf.codageLettres);
                    tf.ecrireFichierACompresser(nomFichier, nomFichierModifie, nbCaracteresLus, nbCaracteresLusDifferents);
                }

                /*if (!fileArbres.isEmpty()
                                && !tf.arbreHuffmanLettresFichier.isEmpty()) {
                        arbre = creationArbreHuffman();
                        if (arbre != null) {
                                String entete = arbre
                                                .remplissageTableauPrefixeRecursif(tf.codageLettres);
                                tf.ecrireFichierACompresser(nomFichier, nomFichierModifie,
                                                entete, nbCaracteresLus);
                        }
                }*/
	}

	public void decompresserFichier() throws FileNotFoundException, IOException {
			tf.lireEcrireFichierADecompresser(nomFichier, nomFichierModifie);
	}

	/*----------------------METHODES SUBORDONNEES-----------------------*/
	private ArbreHuffman creationArbreHuffman() {

		ArbreHuffman a1 = null;
		ArbreHuffman a2 = null;
		ArbreHuffman a3 = null;
		Noeud nvelleracine = null;

		// tant que la priorityqueue a plus qu'un element, fusion entre
		// les arbres
		while (!fileArbres.isEmpty()) {

			// On recupere l'element avec le poids le plus faible donc le
			// plus prioritaire
			a1 = fileArbres.poll();

			if (!fileArbres.isEmpty()) { // Il reste au moins un element
				a2 = fileArbres.poll();
				// On cree le nouvel arbre avec les deux arbres restants
				nvelleracine = new Noeud<Character>(null, a1.racine, a2.racine);
				a3 = new ArbreHuffman<Character>(nvelleracine, a1.getPriorite()
						+ a2.getPriorite());
				fileArbres.add(a3);
			} else { // Il ne reste aucun element
				break;
			}
		}
		return a1;
	}

}
