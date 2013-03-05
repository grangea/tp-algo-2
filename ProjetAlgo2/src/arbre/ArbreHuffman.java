package arbre;

import arbre.ArbreBinaire;

public class ArbreHuffman<E> extends ArbreBinaire<E> {
	/**
	 * @brief Gere un arbre de huffman c'est a dire un arbre binaire particulier
	 *        compose d'une priorite
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	private int priorite;

	/** Cree un arbre de huffman vide de priorite 0 */
	public ArbreHuffman() {
		this.setRacine(null);
		this.priorite = 0;
	}

	/**
	 * Cree un arbre de huffman non vide de priorite 1
	 * 
	 * @param valeur
	 *            portee par sa racine
	 * */
	public ArbreHuffman(E val) {
		Noeud<E> racine = new Noeud<E>(val);
		this.setRacine(racine);
		this.priorite = 1;
	}

	/**
	 * Cree un arbre de huffman non vide
	 * 
	 * @param valeur
	 *            portee par la racine
	 * @param priorite
	 *            de cet arbre
	 */
	public ArbreHuffman(E val, int priorite) {
		Noeud<E> racine = new Noeud<E>(val);
		this.setRacine(racine);
		this.priorite = priorite;
	}

	/**
	 * Cree un arbre de huffman non vide
	 * 
	 * @param noeud
	 *            formant la racine
	 * @param priorite
	 *            de cet arbre
	 */
	public ArbreHuffman(Noeud<E> racine, int priorite) {
		this.setRacine(racine);
		this.priorite = priorite;
	}

	/**
	 * Retourne la priorite de cet arbre de huffman
	 * 
	 * @return sa priorite
	 */
	public int getPriorite() {
		return this.priorite;
	}

	/** Incremente d'un la priorite de cet arbre de huffman */
	public void incrementerPriorite() {
		this.priorite++;
	}

	/** Retourne le nombre de noeuds que porte cet arbre */
	public int nbNoeuds() {
		if (estVide())
			return 0;
		return getRacine().nbNoeudsHuffman();
	}

}
