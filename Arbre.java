package codagehuffman;

abstract class Arbre<E extends Comparable<E>> {
	
    protected Noeud<E> racine;

    public boolean estVide() {
        return racine == null;
    }

    public int hauteur() {
        if (estVide())
            return -1;
        
        return racine.hauteur();
    }

    public int nbNoeuds() {
        if (estVide())
            return 0;
        
        return racine.nbNoeuds();
    }

    public boolean estPresent(E e) {
        if (estVide())
            return false;
        
        return racine.estPresent(e);
    }

    public String affichagePrefixeRecursif() {
        if (estVide())
            return "";
        
        return racine.affichagePrefixeRecursif();
    }

}
