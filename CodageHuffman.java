package codagehuffman;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Comparator;

public class CodageHuffman {

    private Queue<ArbreHuffman<String>> fileArbres = new PriorityQueue<ArbreHuffman<String>>(256,new Comparator<ArbreHuffman<String>>() {
        public int compare(ArbreHuffman<String> a1, ArbreHuffman<String> a2) {
    			if(a1.getPriorite() > a2.getPriorite()){
    				return 1 ;
    			}
    			else{
    				return -1 ;
    			}
        }
    });
   
    //private Map <String, byte> dictio = new Map <String,byte>();
    //attribute de type static Map pour lier un symbole a son code
    //ATTENTION CARACTERE UNIQUE MAIS PEUT ETRE VIDE

    //RAPPEL FILS GAUCHE = 0 / FILS DROIT = 1

/*----------------------METHODES PRINCIPALES-----------------------*/
    public void compresserFichier(String nomFichier){   
        Hashtable<Integer, ArbreHuffman<String>> arbreHuffmanLettresFichier = TraitementFichier.lire(nomFichier);
        
        try{
        	
        	CodageHuffman.affichageHashTable(arbreHuffmanLettresFichier);
        	if (!arbreHuffmanLettresFichier.isEmpty()){
        		System.out.println("Taille de l'arbre : " +arbreHuffmanLettresFichier.size());
        	}
            fileArbres.addAll(arbreHuffmanLettresFichier.values());
            
            if(fileArbres.isEmpty()){
                throw new Exception();
            }else{
            	 System.out.println("Taille file : "+fileArbres.size());
            }
        }catch(Exception e){
                System.out.println("Une erreur s'est produite.");
        }
        	
            //--CREER ARBRE DE HUFFMAN 
            //tq priorityqueue a plus d'un element, fusion entre les arbres

            //---CODER LES CARACTERES
            //determiner les codes des caracteres 
            //les ajouter dans la map 

            //--ENCODER LE FICHIER COMPRESSE
            //creer en-tete avec symbole/longueur pour pouvoir refaire l'arbre DANS l'ORDRE OU ILS SONT RANGES DANS L'ARBRE => prefixï¿½
            //ecrire la suite de bits codant l'ensemble des caracteres du fichier d'origine	
    }

    public void decompresserFichier(){
            //--DECODER L'ENTETE DU FICHIER

            //--GENERER ARBRE HUFFMAN

            //--DECODER LES BITS

            //--REGENERER LE TEXTE
    }


/*----------------------METHODES SUBORDONNEES-----------------------*/	
    static private void affichageHashTable(Hashtable<Integer, ArbreHuffman<String>> arbreH){

        Set<Integer> keySet=arbreH.keySet();
        Iterator it=keySet.iterator();

        while (it.hasNext()){
        Object key=it.next();
        Noeud<String> n = arbreH.get(key).racine;
        System.out.println("clé : "+(Integer)key );
        		//- valeur : "n.getValeur().toString()+ " - priorité: "+ arbreH.get(key).getPriorite() );
        }
    }

    public static void main(String[] args) {
        CodageHuffman codagehuffman = new CodageHuffman();
        codagehuffman.compresserFichier("C:/Users/Lutine/Documents/fichierTexte.txt");

        //codagehuffman.compresserFichier("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt");
    }
}
