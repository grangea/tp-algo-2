package codagehuffman;

import codagehuffman.ArbreBinaire;
import codagehuffman.Noeud;

public class ArbreHuffman<E extends Comparable<E>> extends ArbreBinaire<E> {
	/**
	 * @brief  Gere un arbre de huffman c'est a dire un arbre binaire particulier
	 * compose d'une priorite
	 * @date   Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	private int priorite;

	/** Cree un arbre de huffman vide de priorit� 0 */
	public ArbreHuffman() {
		this.racine = null;
		this.priorite = 0;
	}

	/** Cree un arbre de huffman contenant une unique valeur de priorite 1 */
	public ArbreHuffman(E val) {
		Noeud<E> racine = new Noeud<E>(val);
		this.racine = racine;
		this.priorite = 1;
	}

	/**
	 * Cree un arbre de huffman contenant une unique valeur de priorite
	 * donnee
	 */
	public ArbreHuffman(E val, int priorite) {
		Noeud<E> racine = new Noeud<E>(val);
		this.racine = racine;
		this.priorite = priorite;
	}

	public ArbreHuffman(Noeud<E> racine, int priorite) {
		this.racine = racine;
		this.priorite = priorite;
	}

	/** Renvoie la priorite de l'arbre de huffman */
	public int getPriorite() {
		return this.priorite;
	}

	/** Incremente de un la priorite de l'arbre de huffman */
	public void incrementerPriorite() {
		this.priorite++;
	}

	public int nbNoeuds() {
		if (estVide())
			return 0;

		return racine.nbNoeudsHuffman();
	}

}
