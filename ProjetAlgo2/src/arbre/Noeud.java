package arbre;

import arbre.Noeud;

import java.util.LinkedHashMap;

public class Noeud<E> {
	/**
	 * @brief Gere les noeuds contenus dans un arbre
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	private E val;
	private Noeud<E> filsGauche;
	private Noeud<E> filsDroit;

	public Noeud() {
		this.val = null;
		this.filsDroit = null;
		this.filsGauche = null;
	}

	/**
	 * Cree un noeud avec une valeur donnee
	 * 
	 * @param valeur
	 *            portee par ce noeud
	 */
	public Noeud(E val) {
		this.val = val;
		this.filsDroit = null;
		this.filsGauche = null;
	}

	/**
	 * Cree un noeud avec une valeur donnee et deux fils
	 * 
	 * @param valeur
	 *            portee par ce noeud
	 * @param noeud
	 *            filsgauche de ce noeud
	 * @param noeud
	 *            filsdroit de ce noeud
	 */
	public Noeud(E val, Noeud<E> filsGauche, Noeud<E> filsDroit) {
		this.val = val;
		this.filsGauche = filsGauche;
		this.filsDroit = filsDroit;
	}

	@Override
	public String toString() {
		return this.filsGauche.toString() + "" + this.val + ""
				+ this.filsDroit.toString();
	}

	/**
	 * Renvoie la valeur contenue sur ce noeud
	 * 
	 * @return valeur portee sur ce noeud
	 * */
	public E getVal() {
		return this.val;
	}

	/**
	 * Renvoie le fils gauche de ce noeud
	 * 
	 * @return fils gauche de ce noeud
	 * */
	public Noeud<E> getFilsGauche() {
		return this.filsGauche;
	}

	/**
	 * Renvoie le fils droit de ce noeud
	 * 
	 * @return fils droit de ce noeud
	 * */
	public Noeud<E> getFilsDroit() {
		return this.filsDroit;
	}

	/**
	 * Renvoie la hauteur du noeud
	 * 
	 * @return la hauteur a laquelle se trouve ce noeud
	 */
	public int hauteur() {
		int hauteurSousArbreGauche = -1;
		int hauteurSousArbreDroit = -1;

		if (this.filsGauche != null) {
			hauteurSousArbreGauche = filsGauche.hauteur();
		}
		if (this.filsDroit != null) {
			hauteurSousArbreDroit = filsDroit.hauteur();
		}

		return (1 + Math.max(hauteurSousArbreGauche, hauteurSousArbreDroit));
	}

	/**
	 * Renvoie le nombre de noeuds a partir de ce noeud
	 * 
	 * @return nombre de noeuds
	 */
	public int nbNoeudsHuffman() {
		int nombreNoeudsSousArbreGauche = 0;
		int nombreNoeudsSousArbreDroit = 0;

		if (this.filsGauche != null) {
			nombreNoeudsSousArbreGauche = filsGauche.nbNoeudsHuffman();
		}
		if (this.filsDroit != null) {
			nombreNoeudsSousArbreDroit = filsDroit.nbNoeudsHuffman();
		}
		if (val != null) { // Un arbre de Huffman peut contenir des racines dont
							// la valeur est nulle
			return (1 + nombreNoeudsSousArbreGauche + nombreNoeudsSousArbreDroit);
		} else {
			return (nombreNoeudsSousArbreGauche + nombreNoeudsSousArbreDroit);
		}
	}

	/**
	 * Renvoie une chaine de caractere contenant les valeurs contenues sur le
	 * noeud et ses fils
	 * 
	 * @param linkedHashMap
	 *            <String,Integer>
	 * @return chaine de caractere contenant la valeur de ce noeud + taille du
	 *         code cree
	 */
	public String parcoursPrefixe(LinkedHashMap<Integer, String> codageLettres,
			String code) {
		String s = "";
		String s2 = "";
		// int h = hauteur + 1;

		if (this.filsGauche == null && this.filsDroit == null
				&& this.val != null) { // feuille
			int l = code.length();
			// s2 = code;
			// s2 += "|" + val + "|" + h + "|";
			s2 += val + "|" + (byte) (l + 48) + "|";

			codageLettres.put((Integer) val, code);
		}
		if (this.filsGauche != null) {
			s = code;
			s += "0";
			s2 += filsGauche.parcoursPrefixe(codageLettres, s);
		}
		if (this.filsDroit != null) {
			s = code;
			s += "1";
			s2 += filsDroit.parcoursPrefixe(codageLettres, s);
		}

		return s2;
	}

	/**
	 * Renvoie le noeud insere
	 * 
	 * @param la
	 *            valeur du noeud
	 * @param la
	 *            hauteur a laquelle doit se trouver le noeud
	 * @return ce noeud cree et insere
	 */
	public Noeud<E> insererHuffman(E val, int hauteur) {
		Noeud<E> retour = null;
		if (hauteur == 1) {
			if (filsGauche == null) {
				filsGauche = new Noeud<E>(val);
				retour = filsGauche;
			} else if (filsDroit == null) {
				filsDroit = new Noeud<E>(val);
				retour = filsDroit;
			}
		} else {
			if (filsGauche == null) {
				filsGauche = new Noeud<E>();
			}
			if (filsGauche.val == null) {
				retour = filsGauche.insererHuffman(val, hauteur - 1);
			}

			if (retour == null) {
				if (filsDroit == null) {
					filsDroit = new Noeud<E>();
				}
				if (filsDroit.val == null) {
					retour = filsDroit.insererHuffman(val, hauteur - 1);
				}
			}
		}

		return retour;
	}

}
