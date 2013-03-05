package arbre;

import arbre.Arbre;
import arbre.ArbreBinaire;
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
	 * Cree un arbre binaire compose d'une racine
	 * 
	 * @param sa
	 *            valeur portee sur la racine
	 */
	public ArbreBinaire(E val) {
		super(val);
	}

	/**
	 * Cree un arbre binaire non vide
	 * 
	 * @param sa
	 *            valeur portee sur la racine
	 * @param son
	 *            sous-arbre gauche
	 * @param son
	 *            sous-arbre droit
	 */
	public ArbreBinaire(E val, ArbreBinaire<E> filsGauche,
			ArbreBinaire<E> filsDroit) {
		Noeud<E> noeudFilsGauche = null;
		Noeud<E> noeudFilsDroit = null;

		if (filsGauche != null) {
			// arbre gauche non vide
			noeudFilsGauche = filsGauche.getRacine();
		}
		if (filsDroit != null) {
			// arbre droit non vide
			noeudFilsDroit = filsDroit.getRacine();
		}

		Noeud<E> racine = new Noeud<E>(val, noeudFilsGauche, noeudFilsDroit);
		this.setRacine(racine);
	}

	/**
	 * Realise le parcours prefixe de l'arbre binaire
	 * 
	 * @param une
	 *            linkedHashMap<Integer,String>
	 * */
	public void paroursPrefixe(LinkedHashMap<Integer, String> codageLettres) {
		if (!estVide()) {
			getRacine().parcoursPrefixe(codageLettres, "");
		}
	}

	/**
	 * Insere un noeud a la bonne hauteur dans cet arbre binaire
	 * 
	 * @param valeur
	 *            portee par le noeud
	 * @param hauteur
	 *            a laquelle doit etre le noeud
	 * @return noeud cree
	 * */
	public Noeud<E> insererNoeudHauteurPrecise(E val, int hauteur) {
		if (estVide())
			setRacine(new Noeud<E>());

		return getRacine().insererHuffman(val, hauteur);
	}

}
