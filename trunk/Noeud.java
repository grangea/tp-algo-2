package codagehuffman;

import codagehuffman.Noeud;

public class Noeud<E extends Comparable<E>> {

	private E val;
	private Noeud<E> filsGauche;
	private Noeud<E> filsDroit;

	public Noeud(E val) {
		this.val = val;
	}

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

	public E getValeur() {
		return this.val;
	}

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

	public int nbNoeuds() {
		int nombreNoeudsSousArbreGauche = 0;
		int nombreNoeudsSousArbreDroit = 0;

		if (this.filsGauche != null) {
			nombreNoeudsSousArbreGauche = filsGauche.nbNoeuds();
		}
		if (this.filsDroit != null) {
			nombreNoeudsSousArbreDroit = filsDroit.nbNoeuds();
		}
		return (1 + nombreNoeudsSousArbreGauche + nombreNoeudsSousArbreDroit);
	}

	public String affichagePrefixeRecursif() {
		String s = this.val + " ";

		if (this.filsGauche != null) {
			s += this.filsGauche.affichagePrefixeRecursif();
		}
		if (this.filsDroit != null) {
			s += this.filsDroit.affichagePrefixeRecursif();
		}

		return s;
	}

}
