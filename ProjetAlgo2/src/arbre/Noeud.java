package arbre;

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
	 * Renvoie la valeur contenue par ce noeud
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
	 * Ajoute une entrée dans la liste chaînée codageLettres si ce noeud est une racine
         * sinon se déplace récursivement dans ses fils de manière préfixée
	 * 
	 * @param linkedHashMap<String,Integer>
         * 
	 */
	public void parcoursPrefixe(LinkedHashMap<Integer, String> codageLettres,
			String codeCaractere) {
		String codeCaractereTmp;

		if (this.filsGauche == null && this.filsDroit == null && this.val != null) { // il s'agit d'une feuille
			codageLettres.put((Integer) val, codeCaractere);
		}
		if (this.filsGauche != null) { // on traite le sous-arbre gauche
			codeCaractereTmp = codeCaractere;
			codeCaractereTmp += "0";
			filsGauche.parcoursPrefixe(codageLettres, codeCaractereTmp);
		}
		if (this.filsDroit != null) { // on traite le sous-arbre droit
			codeCaractereTmp = codeCaractere;
			codeCaractereTmp += "1";
			filsDroit.parcoursPrefixe(codageLettres, codeCaractereTmp);
		}
	}

	/**
	 * Insere un noeud dans l'arbre a la hauteur souhaitee
	 * 
	 * @param val
	 *            valeur du noeud a inserer
	 * @param hauteur
	 *            hauteur a laquelle doit etre insere le noeud
	 * @return le noeud insere a l'endroit correct
	 */
	public Noeud<E> insererNoeudHauteurPrecise(E val, int hauteur) {
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
				retour = filsGauche.insererNoeudHauteurPrecise(val, hauteur - 1);
			}

			if (retour == null) {
				if (filsDroit == null) {
					filsDroit = new Noeud<E>();
				}
				if (filsDroit.val == null) {
					retour = filsDroit.insererNoeudHauteurPrecise(val, hauteur - 1);
				}
			}
		}

		return retour;
	}

}
