package codagehuffman;

import java.util.Hashtable;

import codagehuffman.Noeud;
import java.util.LinkedHashMap;

public class Noeud<E extends Comparable<E>> {
	/**
	 * @brief  Gere les noeuds contenus dans un arbre 
	 * @date   Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	private E val;
	private Noeud<E> filsGauche;
	private Noeud<E> filsDroit;

        public Noeud(){
            this.val = null;
            this.filsDroit = null;
            this.filsGauche = null;
        }
        
	/** Cree un noeud avec une valeur donn�e */
	public Noeud(E val) {
		this.val = val;
		this.filsDroit = null;
		this.filsGauche = null;
	}

	/** Cree un noeud avec une valeur donn�e et deux fils */
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
	
	/** Renvoie la valeur contenue sur le noeud */       
        public E getVal(){
            return this.val;
        }
        
        public Noeud getFilsGauche(){
            return this.filsGauche;
        }
        
        public Noeud getFilsDroit(){
            return this.filsDroit;
        }

	/** Renvoie vrai si la valeur est pr�sente sur le noeud */
	protected boolean estPresent(E v) {
		int cmp = val.compareTo(v);

		if (cmp == 0) {
			return true;
		} else if (cmp < 0) { // val < v, on cherche a droite
			return filsDroit != null && filsDroit.estPresent(v);
		} else { // val > v, on cherche a gauche
			return filsGauche != null && filsGauche.estPresent(v);
		}
	}

	/** Ins�re un nouveau noeud contenant une valeur donn�e */
	protected void inserer(E v) {
		int cmp = val.compareTo(v);

		if (cmp < 0) { // val < v, on insere a droite
			if (filsDroit == null)
				filsDroit = new Noeud<E>(v);
			else
				filsDroit.inserer(v);
		} else if (cmp > 0) { // val > v, on insere a gauche
			if (filsGauche == null)
				filsGauche = new Noeud<E>(v);
			else
				filsGauche.inserer(v);
		}
		// else cmp == 0 => deja present
	}

	/** Renvoie la hauteur du noeud */
	public int hauteur() {
		int hauteurSousArbreGauche = -1;
		int hauteurSousArbreDroit = -1;

		if (this.filsGauche != null) {
			hauteurSousArbreGauche = filsGauche.hauteur();
		}
		if (this.filsDroit != null) {
			hauteurSousArbreDroit = filsDroit.hauteur();
		}

		return (1 + Math.max(hauteurSousArbreGauche, hauteurSousArbreDroit));
	}
	
	/** Renvoie le nombre de noeuds � partir de ce noeud */
	public int nbNoeudsHuffman() {
		int nombreNoeudsSousArbreGauche = 0;
		int nombreNoeudsSousArbreDroit = 0;

		if (this.filsGauche != null) {
			nombreNoeudsSousArbreGauche = filsGauche.nbNoeudsHuffman();
		}
		if (this.filsDroit != null) {
			nombreNoeudsSousArbreDroit = filsDroit.nbNoeudsHuffman();
		}
		if (val != null) { // Un arbre de Huffman peut contenir des racines dont
							// la valeur est nulle
			return (1 + nombreNoeudsSousArbreGauche + nombreNoeudsSousArbreDroit);
		} else {
			return (nombreNoeudsSousArbreGauche + nombreNoeudsSousArbreDroit);
		}
	}

	/** Renvoie une chaine de caract�re contenant les valeurs contenues sur le noeud et ses fils */
	public String parcoursPrefixe(LinkedHashMap<Integer,String> codageLettres, String code) {
		String s = "";
		String s2 = "";
		//int h = hauteur + 1;

		if (this.filsGauche == null && this.filsDroit == null && this.val != null) { // feuille
			int l = code.length();
                        //s2 = code;
			//s2 += "|" + val + "|" + h + "|";
			s2 += val + "|" + (byte)(l+48) + "|";
                        
			codageLettres.put((Integer)val,code);
		}
		if (this.filsGauche != null) {
			s = code;
			s += "0";
			s2 += filsGauche.parcoursPrefixe(codageLettres,s);
		}
		if (this.filsDroit != null) {
			s = code;
			s += "1";
			s2 += filsDroit.parcoursPrefixe(codageLettres,s);
		}

		return s2;
	}
        
        public Noeud insererHuffman(E val, int hauteur){
            Noeud retour = null;
            if(hauteur == 1){
                if(filsGauche == null){
                    filsGauche = new Noeud(val);
                    retour = filsGauche;
                } else if(filsDroit == null) {
                    filsDroit = new Noeud(val);
                    retour = filsDroit;
                }
            } else {
                if(filsGauche == null){
                    filsGauche = new Noeud();
                }      
                if(filsGauche.val == null){
                    retour = filsGauche.insererHuffman(val, hauteur-1);
                }
                
                if(retour == null){
                    if(filsDroit == null){
                        filsDroit = new Noeud();
                    }     
                    if(filsDroit.val == null){
                        retour = filsDroit.insererHuffman(val, hauteur-1);
                    }
                }
            }
            
            return retour;
        } 
        
}
