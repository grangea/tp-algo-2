package codagehuffman;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Comparator;

public class CodageHuffman {

	public class KeyComparator<E> implements
	Comparator<ArbreHuffman<Character>> {

@Override
public int compare(ArbreHuffman<Character> a1,
		ArbreHuffman<Character> a2) {
	if (a1.getPriorite() > a2.getPriorite())
		return 1;
	else if (a1.getPriorite() < a2.getPriorite())
		return -1;
	else
		return 0;
}

}
	protected Comparator<ArbreHuffman<Character>> comparator = new KeyComparator<ArbreHuffman<Character>>();
	//PAS TOP DE METTRE UNE CAPACITE A REVOIR
	protected PriorityQueue<ArbreHuffman<Character>> fileArbres = new PriorityQueue<ArbreHuffman<Character>>(1000,comparator);
	protected TraitementFichier tf ;
	protected String nomFichier ;
	
	/*----------------------METHODES PRINCIPALES-----------------------*/
	public CodageHuffman(String nomFichier){
		this.nomFichier=nomFichier;
	}
	
	public void compresserFichier() {
		ArbreHuffman<Character> arbre = null;
		tf.lireFichierACompresser(nomFichier);

		try {
			
			fileArbres.addAll(tf.arbreHuffmanLettresFichier.values());
			
			if (fileArbres.isEmpty() && tf.arbreHuffmanLettresFichier.isEmpty()) {
				throw new Exception();
			} else {
				System.out.println("Taille file : " + fileArbres.size());
				System.out.println("Taille de l'arbre : "
						+ tf.arbreHuffmanLettresFichier.size());
			}
			
			arbre = creationArbreHuffman();
			if(arbre!=null){
				String entete = arbre.remplissageTableauPrefixeRecursif();
				tf.ecrireFichierACompresser(entete);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public void decompresserFichier() {
		// --DECODER L'ENTETE DU FICHIER

		tf.lireFichierADecompresser("fichierTexteCompresse.txt");

		// --GENERER ARBRE HUFFMAN

		// --DECODER LES BITS

		// --REGENERER LE TEXTE
	}

	/*----------------------METHODES SUBORDONNEES-----------------------*/
	// METHODE A VIRER ---------------------
	static private void affichageHashTable(
			Hashtable<Integer, ArbreHuffman<Character>> arbreH) {

		Set<Integer> keySet = arbreH.keySet();
		Iterator it = keySet.iterator();

		while (it.hasNext()) {
			Object key = it.next();
			Noeud<Character> n = arbreH.get(key).racine;
			System.out.println("cle : " + (Integer) key);
			// - valeur : "n.getValeur().toString()+ " - priorit�: "+
			// arbreH.get(key).getPriorite() );
		}
	}

	private ArbreHuffman<Character> creationArbreHuffman(){

		ArbreHuffman<Character> a1 = null;
		ArbreHuffman<Character> a2 = null;
		ArbreHuffman<Character> a3 = null;
		Noeud<Character> nvelleracine = null;
		
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
				a3 = new ArbreHuffman<Character>(nvelleracine,a1.getPriorite() + a2.getPriorite());
				fileArbres.add(a3);
			} else { // Il ne reste aucun element
				break;
			}
		}
		return a1;
	}

}
