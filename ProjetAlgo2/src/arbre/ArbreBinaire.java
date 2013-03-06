package arbre;

import java.util.LinkedHashMap;

public class ArbreBinaire<E> extends Arbre<E> {
	/**
	 * @brief Gere un arbre binaire c'est a dire un arbre qui a deux sous
	 *        arbres(gauche/droit)
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	/** Cree un arbre binaire vide */
	public ArbreBinaire() {
		super();
	}

	/**
	 * Cree un arbre binaire dont la racine a la valeur val et les fils sont null
	 * 
	 * @param val
	 *            valeur portee par la racine
	 */
	public ArbreBinaire(E val) {
		super(val);
	}

	/**
	 * Cree un arbre binaire dont la racine a la valeur val, le fils gauche 
         * filsGauche et le fils droit filsDroit
	 * 
	 * @param val
	 *            valeur portee par la racine
	 * @param filsGauche
	 *            sous-arbre gauche
	 * @param filsDroit
	 *            sous-arbre droit
	 */
	public ArbreBinaire(E val, ArbreBinaire<E> filsGauche,
			ArbreBinaire<E> filsDroit) {
		Noeud<E> noeudFilsGauche = null;
		Noeud<E> noeudFilsDroit = null;

		if (filsGauche != null) { // arbre gauche non vide			
			noeudFilsGauche = filsGauche.getRacine();
		}
		if (filsDroit != null) { // arbre droit non vide			
			noeudFilsDroit = filsDroit.getRacine();
		}

		Noeud<E> racine = new Noeud<E>(val, noeudFilsGauche, noeudFilsDroit);
		this.setRacine(racine);
	}

	/**
	 * Remplie la liste chaînée codageLettres par des entrées 
         * (code ascii - code du caractère obtenu a l'aide de l'arbre de Huffman)
         * de manière préfixée : les noeuds de l'arbre les plus à gauche seront en 
         * tête de liste alors que les noeuds les plus à droite seront en fin de liste
	 * 
	 * @param codageLettres
	 *            linkedHashMap<Integer,String>
	 * */
	public void paroursPrefixe(LinkedHashMap<Integer, String> codageLettres) {
		if (!estVide()) {
			getRacine().parcoursPrefixe(codageLettres, "");
		}
	}

	/**
	 * Insere un noeud a la bonne hauteur dans cet arbre binaire 
         * en parcourant à partir de la racine
	 * 
	 * @param valeur
	 *            portee par le noeud
	 * @param hauteur
	 *            a laquelle doit etre le noeud
	 * @return noeud insere dans l'arbre binaire
	 * */
	public Noeud<E> insererNoeudHauteurPrecise(E val, int hauteur) {
		if (estVide())
			setRacine(new Noeud<E>());

		return getRacine().insererNoeudHauteurPrecise(val, hauteur);
	}

}
