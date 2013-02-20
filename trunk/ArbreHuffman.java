package codagehuffman;

import codagehuffman.ArbreBinaire;
import codagehuffman.Noeud;

public class ArbreHuffman<E extends Comparable<E>> extends ArbreBinaire<E> {

	// Un arbre de huffman est un type d'arbre binaire mais a en plus une
	// priorit�

	private int priorite;

	/** Cree un arbre de huffman vide de priorit� 0 */
	public ArbreHuffman() {
            this.racine = null;
            this.priorite = 0;
	}

	/** Cree un arbre de huffman contenant une unique valeur de priorit� 1 */
	public ArbreHuffman(E val) {
            Noeud<E> racine = new Noeud<E>(val);
            this.racine = racine;
            this.priorite = 1;
	}

	/** Cree un arbre de huffman contenant une unique valeur de priorit� donn�e */
	public ArbreHuffman(E val, int priorite) {
            Noeud<E> racine = new Noeud<E>(val);
            this.racine = racine;
            this.priorite = priorite;
	}
        
        public ArbreHuffman(Noeud<E> racine, int priorite) {
            this.racine = racine;
            this.priorite = priorite;
	}

	/** Renvoie la priorit� de l'arbre de huffman */
	public int getPriorite() {
            return this.priorite;
	}

	/** Incr�mente de un la priorit� de l'arbre de huffman */
	public void incrementerPriorite() {
            this.priorite++;
	}
        
        public int nbNoeuds() {
		if (estVide())
			return 0;

		return racine.nbNoeudsHuffman();
	}

}
