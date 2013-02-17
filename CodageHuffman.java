package codagehuffman;

public class CodageHuffman {

	private Map <String, String> dictio = new HashTable <String,String>();
	//attribute de type static Map pour lier un symbole à son code
	//ATTENTION CARACTERE UNIQUE
	
	private Queue <Arbre> priori = new PriorityQueue <Arbre>();
	//attribut de type static Queue pour stocker les arbres
	
	//RAPPEL FILS GAUCHE = 0 / FILS DROIT = 1
	
/*----------------------METHODES PRINCIPALES-----------------------*/
	public void compresserFichier(){
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
		//creer en-tete avec symbole/longueur pour pouvoir refaire l'arbre DANS l'ORDRE OU ILS SONT RANGES DANS L'ARBRE => prefixé
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
        
    }
}
