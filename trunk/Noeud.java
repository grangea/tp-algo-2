package codagehuffman;

public class Noeud<E extends Comparable<E>>  {
	
	private E val;
	private Noeud<E> fg;
	private Noeud<E> fd;
	
	public Noeud(E val) {
		this.val = val;
	}
	
	public Noeud(E val, Noeud<E> fg, Noeud<E> fd) {
		this.val = val;
	}
	
	@Override
	public String toString() {
		return this.fg.toString() + "" + this.val + "" + this.fd.toString() ;
	}
	
	protected boolean estPresent(E v) {
		return (this.equals(val) || this.fd.estPresent(val) || this.fg.estPresent(val) );
	}
	
	protected Noeud<E> inserer(E v) {
		int res = this.val.compareTo(v);

		if (res < 0) { // e > val
			//fils droit
				fd = this.fd.inserer(v);
		} else if (res > 0) { // e < val
			//fils gauche
				fg = this.fg.inserer(v);
		}
		return this;
	}


	public abstract int hauteur();
	
	public abstract int nbNoeuds();
	
	public abstract String affichagePrefixeRecursif();
	
}
