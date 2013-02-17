package codagehuffman;

public class ArbreHuffman<E extends Comparable<E>> extends ArbreBinaire<E> {
    private int priorite;
    
    public ArbreHuffman() {
        this.racine = null;	
        this.priorite = 0;
    }
    
    public ArbreHuffman(E val) {
        Noeud<E> racine = new Noeud<E>(val);
        this.racine = racine;
        this.priorite = 1;
    }
    
    public ArbreHuffman(E val, int priorite) {
        Noeud<E> racine = new Noeud<E>(val);
        this.racine = racine;
        this.priorite = priorite;
    }
    
    public void incrementerPriorite(){
        this.priorite++;
    }
}
