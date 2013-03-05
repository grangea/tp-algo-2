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
		this.setRacine(null);
	}

	/**
	 * Cree un arbre non vide
	 * 
	 * @param la
	 *            valeur contenue sur la racine
	 */
	public Arbre(E val) {
		this.setRacine(new Noeud<E>(val));
	}

	/**
	 * Teste si cet arbre est vide
	 * 
	 * @return vrai si cet arbre est vide / Faux sinon
	 */
	public boolean estVide() {
		return getRacine() == null;
	}

	/**
	 * Modifie la valeur contenue sur sa racine
	 * 
	 * @param sa
	 *            racine
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
