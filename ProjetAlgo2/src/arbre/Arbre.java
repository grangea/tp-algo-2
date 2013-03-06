package arbre;

abstract class Arbre<E> {
	/**
	 * @brief Gere un arbre compose d'un noeud appele racine
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	private Noeud<E> racine;

	/** Cree un arbre vide */
	public Arbre() {
		this.racine = null;
	}

	/**
	 * Cree un arbre dont la racine a la valeur val
	 * 
	 * @param val
	 *            valeur contenue par la racine
	 */
	public Arbre(E val) {
		this.racine = new Noeud<E>(val);
	}

	/**
	 * Teste si cet arbre est vide
	 * 
	 * @return vrai si cet arbre est vide ou faux sinon
	 */
	public boolean estVide() {
		return getRacine() == null;
	}

	/**
	 * Modifie la racine de l'arbre
	 * 
	 * @param racine
	 *            la racine qui va remplacer la racine actuelle de l'arbre
	 */
	public void setRacine(Noeud<E> racine) {
		this.racine = racine;
	}

	/**
	 * Renvoie la valeur portee par la racine
	 * 
	 * @return valeur de la racine
	 */
	public Noeud<E> getRacine() {
		return racine;
	}

}
