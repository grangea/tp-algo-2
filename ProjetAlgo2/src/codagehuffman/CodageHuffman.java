package codagehuffman;

import arbre.ArbreHuffman;
import arbre.Noeud;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

public class CodageHuffman {
	/**
	 * @brief  Gere la creation d'arbre de huffman lorsque l'on souahite compresser un fichier
	 * Puis delegue a TraitementFichier
	 * @date   Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	private class PrioriteArbreHuffmanComparator implements
			Comparator<ArbreHuffman<Integer>> {

		@Override
		public int compare(ArbreHuffman<Integer> a1, ArbreHuffman<Integer> a2) {
			if (a1.getPriorite() > a2.getPriorite()){
				return 1;
                        }else if (a1.getPriorite() < a2.getPriorite()){
				return -1;
                        }else{
				return 0;
                        }
		}

	}

	private PriorityQueue<ArbreHuffman> fileArbres;
	private String nomFichierOriginal = "";
	private String nomFichierModifie = "";

	public CodageHuffman(String nomFichier, String nomFichierModifie) {
		this.nomFichierOriginal = nomFichier;
		this.nomFichierModifie = nomFichierModifie;
	}

	public void compresserFichier() throws FileNotFoundException, IOException {
		ArbreHuffman<Integer> arbre;                
                TraitementFichier tf = new TraitementFichier();
                int nbCaracteresDifferentsLus = 0;

                // Lecture du fichier original afin d'obtenir un tableau d'associations (code ascii - arbres de huffman)
                int nbCaracteresLus = tf.lectureFichierOriginal(nomFichierOriginal);
                System.out.println("Nombre de caracteres lus : " + nbCaracteresLus);                
                
                if(tf.associationsCodeAsciiArbresHuffman.length > 0){ // Le fichier a ompresser n'est pas vide
                    Comparator comparator = new PrioriteArbreHuffmanComparator();
                    fileArbres = new PriorityQueue(tf.associationsCodeAsciiArbresHuffman.length, comparator);
                    
                    // Ajout de chaque arbre de huffman dans une file triée selon la priorité de l'arbre en quetion
                    for(int i = 0; i < tf.associationsCodeAsciiArbresHuffman.length; i++){
                        if(tf.associationsCodeAsciiArbresHuffman[i] != null){
                            fileArbres.add((ArbreHuffman<Integer>)tf.associationsCodeAsciiArbresHuffman[i]);
                            nbCaracteresDifferentsLus++;
                        }
                    }

                    // Création de l'arbre de Huffman final à partir des arbres de Huffman tries par priorite
                    arbre = creationArbreHuffman();
                    
                    // Parcours de l'arbre genere afin d'obtenir une liste chaînée d'associations (symbole - code de Huffma nassocié)
                    tf.associationsCodeAsciiCodeHuffman = new LinkedHashMap();
                    arbre.paroursPrefixe(tf.associationsCodeAsciiCodeHuffman);
                    
                    // Ecriture des caracteres du fichier original dans un fichier compresse (en les ayant traduit par leur code de Huffman associé)
                    tf.ecritureFichierCompresse(nomFichierOriginal, nomFichierModifie, nbCaracteresLus, nbCaracteresDifferentsLus);
                }
	}

	public void decompresserFichier() throws FileNotFoundException, IOException {
            TraitementFichier tf = new TraitementFichier();
            tf.lireEcrireFichierADecompresser(nomFichierOriginal, nomFichierModifie);
	}

	private ArbreHuffman<Integer> creationArbreHuffman() {
		ArbreHuffman<Integer> arbreLePlusPrioritaire = null;
		ArbreHuffman<Integer> secondArbreLePlusPrioritaire;
		ArbreHuffman<Integer> nouvelArbre;

		// tant que la file de priorite n'est pas vide, on fusionne les deux arbres les plus prioritaires
		while (!fileArbres.isEmpty()) {
			// On recupere l'element avec le poids le plus faible donc le plus prioritaire
			arbreLePlusPrioritaire = fileArbres.poll();

			if (!fileArbres.isEmpty()) { // Il reste au moins un element dans la file de priorite                                
				secondArbreLePlusPrioritaire = fileArbres.poll(); // On recupere l'element avec le poids le plus faible donc le plus prioritaire                                
				
				nouvelArbre = new ArbreHuffman<Integer>(new Noeud<Integer>(null, arbreLePlusPrioritaire.getRacine(), secondArbreLePlusPrioritaire.getRacine()), 
                                                                arbreLePlusPrioritaire.getPriorite() + secondArbreLePlusPrioritaire.getPriorite());
                                fileArbres.add(nouvelArbre); // On cree le nouvel arbre a partir des deux arbres les plus prioritaires dans la file
			} else { // Il ne reste plus d'elements dans la file de priorite
				break;
			}
		}
                
		return arbreLePlusPrioritaire;
	}

}
