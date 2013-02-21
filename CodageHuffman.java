package codagehuffman;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Comparator;

public class CodageHuffman {
    
    public class KeyComparator<E> implements Comparator<ArbreHuffman<Character>> {

        @Override
        public int compare(ArbreHuffman<Character> a1, ArbreHuffman<Character> a2) {
            if(a1.getPriorite() > a2.getPriorite())
                return 1;
            else if(a1.getPriorite() < a2.getPriorite())
                return -1;
            else
                return 0;
        }

   }
    
    /*private Queue<ArbreHuffman<Character>> fileArbres = new PriorityQueue<ArbreHuffman<Character>>(256,new Comparator<ArbreHuffman<Character>>() {
        public int compare(ArbreHuffman<String> a1, ArbreHuffman<String> a2) {
    			if(a1.getPriorite() > a2.getPriorite()){
    				return 1 ;
    			}
    			else{
    				return -1 ;
    			}
        }
    });*/
 
   
    //private Map <String, byte> dictio = new Map <String,byte>();
    //attribute de type static Map pour lier un symbole a son code
    //ATTENTION CARACTERE UNIQUE MAIS PEUT ETRE VIDE

    //RAPPEL FILS GAUCHE = 0 / FILS DROIT = 1

/*----------------------METHODES PRINCIPALES-----------------------*/
    public void compresserFichier(String nomFichier){ 
        Comparator<ArbreHuffman<Character>> comparator = new KeyComparator<ArbreHuffman<Character>>();
        PriorityQueue<ArbreHuffman<Character>> fileArbres = new PriorityQueue<ArbreHuffman<Character>>(1000,comparator);
        
        TraitementFichier tf = new TraitementFichier();
        tf.lireDecompresse(nomFichier);
        
        ArbreHuffman<Character> a1 = null;
        ArbreHuffman<Character> a2 = null;
        ArbreHuffman<Character> a3 = null;
        Noeud racine;
        
        
        
        try{        	
            CodageHuffman.affichageHashTable(tf.arbreHuffmanLettresFichier);
            if (!tf.arbreHuffmanLettresFichier.isEmpty()){
                    System.out.println("Taille de l'arbre : " +tf.arbreHuffmanLettresFichier.size());
            }
            fileArbres.addAll(tf.arbreHuffmanLettresFichier.values());
            
            if(fileArbres.isEmpty()){
                throw new Exception();
            }else{
            	 System.out.println("Taille file : "+fileArbres.size());
            }            
            
            while(!fileArbres.isEmpty()){
                // On récupère l'element avec le poids le plus faible donc le plus prioritaire
                a1 = fileArbres.poll();
                
                if(!fileArbres.isEmpty()){ // Il restait au moins deux elements
                    // On récupère l'element avec le poids le plus faible donc le plus prioritaire                   
                    a2 = fileArbres.poll();                                       
                    
                    // On crée le nouvel arbre
                    racine = new Noeud(null, a1.racine, a2.racine);
                    a3 = new ArbreHuffman<Character>(racine, a1.getPriorite()+a2.getPriorite());
                    fileArbres.add(a3);
                }else{ // Il restait plus qu'un element
                    break;
                } 
            }
        }catch(Exception e){
            //System.out.println("Une erreur s'est produite.");
            System.out.println(e.getMessage());
        }
        	
        String entete = a1.remplissageTableauPrefixeRecursif();
        tf.ecrireCompresse(entete);
            //--CREER ARBRE DE HUFFMAN 
            //tq priorityqueue a plus d'un element, fusion entre les arbres

            //---CODER LES CARACTERES
            //determiner les codes des caracteres 
            //les ajouter dans la map 

            //--ENCODER LE FICHIER COMPRESSE
            //creer en-tete avec symbole/longueur pour pouvoir refaire l'arbre DANS l'ORDRE OU ILS SONT RANGES DANS L'ARBRE => prefix�
            //ecrire la suite de bits codant l'ensemble des caracteres du fichier d'origine	
    }

    public void decompresserFichier(){
            //--DECODER L'ENTETE DU FICHIER
        TraitementFichier tf = new TraitementFichier();
        tf.lireCompresse("fichierTexteCompresse.txt");

            //--GENERER ARBRE HUFFMAN

            //--DECODER LES BITS

            //--REGENERER LE TEXTE
    }


/*----------------------METHODES SUBORDONNEES-----------------------*/	
    static private void affichageHashTable(Hashtable<Integer, ArbreHuffman<Character>> arbreH){

        Set<Integer> keySet=arbreH.keySet();
        Iterator it=keySet.iterator();

        while (it.hasNext()){
        Object key=it.next();
        Noeud<Character> n = arbreH.get(key).racine;
        System.out.println("cle : "+(Integer)key );
        		//- valeur : "n.getValeur().toString()+ " - priorit�: "+ arbreH.get(key).getPriorite() );
        }
    }

    public static void main(String[] args) {
        CodageHuffman codagehuffman = new CodageHuffman();
        //codagehuffman.compresserFichier("C:/Users/Lutine/Documents/fichierTexte.txt");

        codagehuffman.compresserFichier("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt");
    }
}
