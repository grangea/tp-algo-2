package codagehuffman;

import java.util.Hashtable;

import codagehuffman.Noeud;

abstract class Arbre<E extends Comparable<E>> {

	protected Noeud<E> racine;

	/** Cree un arbre vide */
	public Arbre() {
		this.racine = null;
	}

	/** Cree un arbre avec une racine donn�e */
	public Arbre(E val) {
		this.racine = new Noeud<E>(val);
	}

	/** Renvoie vrai si l'arbre est vide / Faux sinon */
	public boolean estVide() {
		return racine == null;
	}

	/** Renvoie la hauteur de l'arbre / -1 si l'abre est vide */
	public int hauteur() {
		if (estVide())
			return -1;

		return racine.hauteur();
	}

	/** Renvoie vrai si la valeur e est sur un noeud de l'arbre */
	public boolean estPresent(E e) {
		if (estVide())
			return false;

		return racine.estPresent(e);
	}

	/** Renvoie une string contenant l'affichage prefixe de l'arbre */ 
        public String remplissageTableauPrefixeRecursif(Hashtable<E,String> ht) {
		if (estVide())
			return "";

		return racine.remplissageTableauPrefixeRecursif(ht,"");
	}

}