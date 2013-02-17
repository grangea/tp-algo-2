package codagehuffman;

public class Noeud<E extends Comparable<E>> {

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
		return this.fg.toString() + "" + this.val + "" + this.fd.toString();
	}

	protected boolean estPresent(E v) {
		return (this.equals(val) || this.fd.estPresent(val) || this.fg
				.estPresent(val));
	}

	protected Noeud<E> inserer(E v) {
		int res = this.val.compareTo(v);

		if (res < 0) { // e > val
			// fils droit
			fd = this.fd.inserer(v);
		} else if (res > 0) { // e < val
			// fils gauche
			fg = this.fg.inserer(v);
		}
		return this;
	}

	public boolean estPresent(E val) {
		return (super.estPresent(val) || (fg != null && fg.estPresent(val)) || (fd != null)
				&& fd.estPresent(val));
	}

	public int hauteur() {
		int hg = -1;
		int hd = -1;

		if (this.fg != null) {
			hg = fg.hauteur();
		}
		if (this.fd != null) {
			hd = fd.hauteur();
		}
		return (1 + Math.max(hg, hd));
	}

	public int nbNoeuds() {
		int ng = 0;
		int nd = 0;

		if (this.fg != null) {
			ng = fg.nbNoeuds();
		}
		if (this.fd != null) {
			nd = fd.nbNoeuds();
		}
		return (1 + ng + nd);
	}

	public String affichagePrefixeRecursif() {
		String s = this.val + " ";

		if (this.fg != null) {
			s = s + this.fg.affichagePrefixeRecursif();
		}
		if (this.fd != null) {
			s = s + this.fd.affichagePrefixeRecursif();
		}

		return s;

	}

}
