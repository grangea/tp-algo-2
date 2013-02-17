package codagehuffman;

public class ArbreBinaire<E> extends Arbre<E> {
	//Un arbre binaire est un type d'arbre
	
	/** Cree un arbre binaire vide */
	public ArbreBinaire() {
		this.racine = null;		
	}

	/** Cree un arbre contenant une unique valeur */
	public ArbreBinaire(E val) {
		NoeudBasique<E> n = new NoeudBasique<E>(val);
		this.racine = n ;		
	}
	
	/** 
	 * Cree un arbre a partir d'une valeur racine et de 2 arbres,
	 * qui deviennent les fils g et d du noeud cree.
	 */
	public ArbreBinaire(E val, ArbreBinaire<E> g, ArbreBinaire<E> d) {
		Noeud<E> ng = null;
		Noeud<E> nd = null;
		
		if(g != null){
			//arbre gauche non vide
			ng = g.racine;
		}
		if(d != null){
			//arbre droit non vide
			nd = d.racine;
		}		
		
		Noeud<E> n = new Noeud<E>(val,ng,nd);
		this.racine = n;
	}

	
}
