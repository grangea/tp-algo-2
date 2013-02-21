package codagehuffman;

import java.util.Hashtable;

import codagehuffman.Noeud;

public class Noeud<E extends Comparable<E>> {

	private E val;
	private Noeud<E> filsGauche;
	private Noeud<E> filsDroit;

	/** Cree un noeud avec une valeur donn�e */
	public Noeud(E val) {
		this.val = val;
		this.filsDroit = null;
		this.filsGauche = null;
	}

	/** Cree un noeud avec une valeur donn�e et deux fils */
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
	
	/** Renvoie la valeur contenue sur le noeud */
	public E getValeur() {
		return this.val;
	}

	/** Renvoie vrai si la valeur est pr�sente sur le noeud */
	protected boolean estPresent(E v) {
		int cmp = val.compareTo(v);

		if (cmp == 0) {
			return true;
		} else if (cmp < 0) { // val < v, on cherche a droite
			return filsDroit != null && filsDroit.estPresent(v);
		} else { // val > v, on cherche a gauche
			return filsGauche != null && filsGauche.estPresent(v);
		}
	}

	/** Ins�re un nouveau noeud contenant une valeur donn�e */
	protected void inserer(E v) {
		int cmp = val.compareTo(v);

		if (cmp < 0) { // val < v, on insere a droite
			if (filsDroit == null)
				filsDroit = new Noeud<E>(v);
			else
				filsDroit.inserer(v);
		} else if (cmp > 0) { // val > v, on insere a gauche
			if (filsGauche == null)
				filsGauche = new Noeud<E>(v);
			else
				filsGauche.inserer(v);
		}
		// else cmp == 0 => deja present
	}

	/** Renvoie la hauteur du noeud */
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
	
	/** Renvoie le nombre de noeuds � partir de ce noeud */
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

	/** Renvoie une chaine de caract�re contenant les valeurs contenues sur le noeud et ses fils */
	public String remplissageTableauPrefixeRecursif(Hashtable<String, String> ht, String chaine, int hauteur) {
		String s = "";
		String s2 = "";
		int h = hauteur + 1;

		if (this.filsGauche == null && this.filsDroit == null
				&& this.val != null) { // feuille
			s2 = chaine;
			s2 += "|" + val + "|" + h + "|";
			ht.put(this.val.toString(),chaine);
		}
		if (this.filsGauche != null) {
			s = chaine;
			s += "0";
			s2 += filsGauche.remplissageTableauPrefixeRecursif(ht,s, h);
		}
		if (this.filsDroit != null) {
			s = chaine;
			s += "1";
			s2 += filsDroit.remplissageTableauPrefixeRecursif(ht,s, h);
		}

		return s2;
	}
	

}