package codagehuffman;

public class ArbreBinaire<E extends Comparable<E>> extends Arbre<E> {
    
    //Un arbre binaire est un type d'arbre

    /** Cree un arbre binaire vide */
    public ArbreBinaire() {
        this.racine = null;		
    }

    /** Cree un arbre contenant une unique valeur */
    public ArbreBinaire(E val) {
        Noeud<E> racine = new Noeud<E>(val);
        this.racine = racine;		
    }

    /** 
     * Cree un arbre a partir d'une valeur racine et de 2 arbres,
     * qui deviennent les fils g et d du noeud cree.
     */
    public ArbreBinaire(E val, ArbreBinaire<E> filsGauche, ArbreBinaire<E> filsDroit) {
        Noeud<E> noeudFilsGauche = null;
        Noeud<E> noeudFilsDroit = null;

        if(filsGauche != null){
                //arbre gauche non vide
                noeudFilsGauche = filsGauche.racine;
        }
        if(filsDroit != null){
                //arbre droit non vide
                noeudFilsDroit = filsDroit.racine;
        }		

        Noeud<E> racine = new Noeud<E>(val, noeudFilsGauche, noeudFilsDroit);
        this.racine = racine;
    }
	
}
