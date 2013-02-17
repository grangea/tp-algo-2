package codagehuffman;

import java.util.Collection;
import java.util.PriorityQueue;

public class CodageHuffman {

    private PriorityQueue<ArbreHuffman> fileArbres;
    
    //private HashTable <String, byte> dictio = new HashMap <String,byte>();
    //attribute de type static Map pour lier un symbole � son code
    //ATTENTION CARACTERE UNIQUE

    //private Queue <Arbre> priori = new PriorityQueue <Arbre>();
    //attribut de type static Queue pour stocker les arbres

    //RAPPEL FILS GAUCHE = 0 / FILS DROIT = 1

/*----------------------METHODES PRINCIPALES-----------------------*/
    public void compresserFichier(String nomFichier){
        TraitementFichier lectureFichier = new TraitementFichier() {};    
        Collection arbreHuffmanLettresFichier = (Collection)lectureFichier.lire(nomFichier);
        
        try{
            fileArbres = new PriorityQueue<ArbreHuffman>(arbreHuffmanLettresFichier);
            
            if(fileArbres == null){
                throw new Exception();
            }else{
                
            }
        }catch(Exception e){
                System.out.println("Une erreur s'est produite.");
        }
        //---LIRE LE FICHIER SOURCE
            //lire les caracteres du fichier a compresser
            //les ajouter dans la queue en tant que Noeud
            //modifier la priorite lorsque plusieurs occurences =>regarder pour chaque caractere si deja present!

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

            //--GENERER ARBRE HUFFMAN

            //--DECODER LES BITS

            //--REGENERER LE TEXTE
    }


/*----------------------METHODES SUBORDONNEES-----------------------*/	



    public static void main(String[] args) {
        CodageHuffman codagehuffman = new CodageHuffman();
        codagehuffman.compresserFichier("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt");
    }
}
