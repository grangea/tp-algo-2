package codagehuffman;

import codagehuffman.Arbre;
import codagehuffman.ArbreBinaire;
import codagehuffman.Noeud;
import java.util.Hashtable;

public class ArbreBinaire<E extends Comparable<E>> extends Arbre<E> {
	/**
	 * @brief Gere un arbre binaire c'est a dire un arbre qui a deux sous arbres(gauche/droit)
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */

	/** Cree un arbre binaire vide */
	public ArbreBinaire() {
		super();
	}

	/** Cree un arbre contenant une unique valeur */
	public ArbreBinaire(E val) {
		super(val);
	}

	/**
	 * Cree un arbre a partir d'une valeur racine et de 2 arbres, qui deviennent
	 * les fils g et d du noeud cree.
	 */
	public ArbreBinaire(E val, ArbreBinaire<E> filsGauche,
			ArbreBinaire<E> filsDroit) {
		Noeud<E> noeudFilsGauche = null;
		Noeud<E> noeudFilsDroit = null;

		if (filsGauche != null) {
			// arbre gauche non vide
			noeudFilsGauche = filsGauche.racine;
		}
		if (filsDroit != null) {
			// arbre droit non vide
			noeudFilsDroit = filsDroit.racine;
		}

		Noeud<E> racine = new Noeud<E>(val, noeudFilsGauche, noeudFilsDroit);
		this.racine = racine;
	}
        
        
	/** Renvoie une string contenant l'affichage prefixe de l'arbre */ 
        public void paroursTableauPrefixe(String[] codageLettres){
		if (!estVide()){                             
                    racine.chaineParoursTableauPrefixe(codageLettres, "");
                }
	}
        
        public Noeud insererNoeudHauteurPrecise(E val, int hauteur){
            if (estVide())
                racine = new Noeud();
            
            return racine.insererHuffman(val, hauteur);
        }
        
}
