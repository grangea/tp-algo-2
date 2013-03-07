package codagehuffman;

import arbre.ArbreBinaire;
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
    
        /**
        * Comparateur utilise pour la comparaison de deux arbres de Huffman dans la file de priorite;
        * La comparaison est faite sur la priorite de l'arbre de Huffman.
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

        /**
        * File de priorite utilisee pour stocker des arbres de Huffman de maniere triee selon
        * la priorite contenue par l'arbre.
        */
	private PriorityQueue<ArbreHuffman<Integer>> fileArbres;
        
        /**
        * Nom du fichier à modifier (qui va etre compresse ou decompresse)
        */
	private String nomFichierAModifier = "";
        
        /**
        * Nom du fichier modifie (qui est issu d'une compression ou d'une decompression)
        */
	private String nomFichierModifie = "";
        
        /**
        * Permet des traitements sur les fichiers (tels que l'ecriture ou la lecture)
        */
        private TraitementFichier traitementFichier;

        /**
	 * Cree une instance d'un codage de huffman
	 * 
	 * @param nomFichierAModifier
	 *            nom du fichier a modifier
         * @param nomFichierModifie
	 *            nom du fichier modifie
         * 
	 */
	public CodageHuffman(String nomFichierAModifier, String nomFichierModifie) {
		this.nomFichierAModifier = nomFichierAModifier;
		this.nomFichierModifie = nomFichierModifie;
	}

         /**
	 * Compresse un fichier en lisant le fichier original puis en generant un arbre de Huffman 
         * a partir des caracteres lus. Cet arbre va permettre de traduire les caracteres du fichier original
         * afin de les reecrire de maniere compresse.
	 * 
	 */
	public void compresserFichier() throws FileNotFoundException, IOException, Exception {
                traitementFichier = new TraitementFichier();
		ArbreHuffman<Integer> arbre;           
                int nbCaracteresDifferentsLus = 0;

                // Lecture du fichier original afin d'obtenir un tableau d'associations (code ascii - arbres de huffman)
                int nbCaracteresLus = traitementFichier.lectureFichierOriginal(nomFichierAModifier);
                System.out.println("Nombre de caracteres lus : " + nbCaracteresLus);                
                
                if(traitementFichier.associationsCodeAsciiArbresHuffman.length > 0){ // Le fichier a ompresser n'est pas vide
                    Comparator comparator = new PrioriteArbreHuffmanComparator();
                    fileArbres = new PriorityQueue(traitementFichier.associationsCodeAsciiArbresHuffman.length, comparator);
                    
                    // Ajout de chaque arbre de huffman dans une file triée selon la priorité de l'arbre en question
                    for(int i = 0; i < traitementFichier.associationsCodeAsciiArbresHuffman.length; i++){
                        if(traitementFichier.associationsCodeAsciiArbresHuffman[i] != null){
                            fileArbres.add((ArbreHuffman<Integer>)traitementFichier.associationsCodeAsciiArbresHuffman[i]);
                            nbCaracteresDifferentsLus++;
                        }
                    }

                    // Création de l'arbre de Huffman final à partir des arbres de Huffman tries par priorite
                    arbre = creationArbreHuffman();
                    
                    // Parcours de l'arbre genere afin d'obtenir une liste chaînee d'associations (symbole - code de Huffman nassocié)
                    traitementFichier.associationsCodeAsciiCodeHuffman = new LinkedHashMap();
                    arbre.paroursPrefixe(traitementFichier.associationsCodeAsciiCodeHuffman);
                    
                    // Ecriture des caracteres du fichier original dans un fichier compresse (en les ayant traduit par leur code de Huffman associé)
                    traitementFichier.ecritureFichierCompresse(nomFichierAModifier, nomFichierModifie, nbCaracteresDifferentsLus);
                }
	}

        /**
	 * Decompresse un fichier en lisant l'entete du fichier compresse puis en generant un arbre 
         * a partir de cette entete. Cet arbre va permettre de traduire les caracteres codes du fichier 
         * original en caractere du code ASCII afin de les reecrire de maniere decompressee.
	 * 
	 */
	public void decompresserFichier() throws FileNotFoundException, IOException {
            ArbreBinaire<Integer> arbre = recreationArbre();
            traitementFichier.ecritureFichierDecompresse(nomFichierAModifier, nomFichierModifie, arbre);
	}

        /**
	 * Genere un arbre de Huffman final a partir d'une file d'arbres de Huffman tries par priorite
	 * 
	 */
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
				
				nouvelArbre = new ArbreHuffman(new Noeud(null, arbreLePlusPrioritaire.getRacine(), secondArbreLePlusPrioritaire.getRacine()), 
                                                                arbreLePlusPrioritaire.getPriorite() + secondArbreLePlusPrioritaire.getPriorite());
                                fileArbres.add(nouvelArbre); // On cree le nouvel arbre a partir des deux arbres les plus prioritaires dans la file
			} else { // Il ne reste plus d'elements dans la file de priorite
				break;
			}
		}
                
		return arbreLePlusPrioritaire;
	}
        
        /**
	 * Genere un arbre a partir des couples (code ASCII - longueur du code de Huffman associé) lus dans l'entete
         * du fichier a decompresser.
         * 
	 * @return l'arbre genere
         *  
	 */
        private ArbreBinaire<Integer> recreationArbre() throws FileNotFoundException, IOException{
            traitementFichier = new TraitementFichier();
            return traitementFichier.lectureEnteteFichierCompresse(nomFichierAModifier);
        }

}
