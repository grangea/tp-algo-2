package codagehuffman;

import arbre.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class TraitementFichier<E> {
	/**
	 * @brief Traite un fichier qui doit compresser ou decompresser
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	
         /**
	 * Tableau d'objets permettant de stocker des arbres dont l'indice est le code ascii 
         * associe a la racine de l'arbre
	 */
        protected Object[] associationsCodeAsciiArbresHuffman;    
       
        /**
        * Liste chainee permettant de stocker des associations code ascii (cle) - code de huffman (valeur)
        */
	protected LinkedHashMap<Integer,String> associationsCodeAsciiCodeHuffman;
	
        /**
        * Stream de lecture du fichier compresse (il s'agit d'un attribut car on a besoin de ce flux dans deux
        * methodes differentes donc on souhaite conserver le pointeur a l'endroit lu en dernier)  
        */
        private FileInputStream fichierCompresse; 
        
        private Integer nbBitsBourrageDernierOctetDecompression;
        
        /**
	 * Lit les caracteres du fichier original que l'on souhaite compresser, puis
         * ajoute le code ascii du caractere dans le tableau tableauArbresHuffman (en 
         * ayant au prealable cree un arbre de Huffman pour ce caractere) si le caractere 
         * n'est pas deja present dans le tableau sinon incremente la priorite de l'arbre 
         * de Huffman correspondant a ce caractere
	 * 
	 * @param nomFichierOriginal
	 *            nom du fichier original a lire
         * 
         * @return nombre de caracteres lus dans le fichier
         * 
	 */
	protected int lectureFichierOriginal(String nomFichierOriginal)
			throws FileNotFoundException, IOException, Exception {
            
		FileInputStream fichierTexteOriginal = new FileInputStream(nomFichierOriginal); // Permet de lire le fichier a compresser
                byte[] texteLuFichierOriginal; // Permet de stocker le contenu du fichier orignal
                int caractereLuCodeAscii; // Code ASCII du caractere lu
		int nbCaracteresLus = 0;
                int nbBytesALire;

                texteLuFichierOriginal = new byte[1000000]; // Alloue 1000000 bytes d'espace memoire necessaire pour stocker 1000000 bytes de contenu du fichier original
		associationsCodeAsciiArbresHuffman = new Object[128]; // Alloue l'espace memoire necessaire pour stocker les arbres de Huffman correspondants aux 128 caracteres du code ASCII simple
                
                // --- Traitement des caracteres du fichier original et creation des arbres de Huffman associes ---
                while(fichierTexteOriginal.available() > 0){
                    // On cherche a savoir si on traite le dernier bloc de 10000 bytes ou non
                    if(fichierTexteOriginal.available() >= 1000000){
                        nbBytesALire = fichierTexteOriginal.read(texteLuFichierOriginal, 0, 1000000);
                    }else{
                        nbBytesALire = fichierTexteOriginal.read(texteLuFichierOriginal, 0, fichierTexteOriginal.available());
                    }

                    // Lecture du bloc de nbBytesALire
                    for(int j = 0; j < nbBytesALire; j++) { // Traitement de chaque caractere lu
                            if((caractereLuCodeAscii = texteLuFichierOriginal[j]) < 0){
                                throw new Exception("L'un des caracteres rencontres ne ferait pas partie du ASCII simple.");
                            }
                            nbCaracteresLus++;

                            if(associationsCodeAsciiArbresHuffman[caractereLuCodeAscii] != null){ // Arbre de Huffman deja existant dans le tableau
                                ((ArbreHuffman<Integer>)associationsCodeAsciiArbresHuffman[caractereLuCodeAscii]).incrementerPriorite();
                            }else{ // Arbre de Huffman non existant dans le tableau : creation de l'arbre de Huffman
                                associationsCodeAsciiArbresHuffman[caractereLuCodeAscii] = new ArbreHuffman(caractereLuCodeAscii);
                            }		
                    }
                }
		
                return nbCaracteresLus;
	}

        /**
	 * Enregistre dans un tableau de bytes l'entete et le contenu du fichier compresse puis ecrit
         * ce tableau dans le fichier compresse
	 * 
	 * @param nomFichierOriginal
	 *            nom du fichier original a lire
         * @param nomFichierCompresse
	 *            nom du fichier compresse a ecrire
         * @param nbCaracteresDifferentsLus
	 *            nombre de caracteres differents lus dans le contenu du fichier original
         * 
	 */
	protected void ecritureFichierCompresse(String nomFichierOriginal,
			String nomFichierCompresse, int nbCaracteresDifferentsLus)
			throws FileNotFoundException, IOException {    
                
                FileInputStream fichierTexteOriginal = new FileInputStream(nomFichierOriginal);
                FileOutputStream fichierTexteCompresse = new FileOutputStream(nomFichierCompresse);
                                                
                byte[] texteLuFichierOriginal = new byte[1000000]; // Alloue l'espace memoire necessaire pour stocker 1000000 bytes lus dans le contenu du fichier original    
                byte[] enteteEtContenuAEcrire = new byte[1000000]; // Alloue l'espace memoire necessaire pour stocker 1000000 bytes a ecrire dans le fichier compresse
                String[] associationsCodeAsciiCodeHuffmanTableau = new String[128];
                String codeCaractereHuffman;
                int nbBytesEcrits = String.valueOf(nbCaracteresDifferentsLus).length()+2;
                int nbBytesALire;
                
		// --- Enregistrement de chaque association (code ascii - longueur du code de huffman) dans le tableau de bytes a ecrire ---
                Set cles = associationsCodeAsciiCodeHuffman.keySet();
                Iterator it = cles.iterator(); // Parcours grâce a un iterateur sur les cles de la liste chainee
                Integer codeAscii;               
                
                while (it.hasNext()){
                   codeAscii = (Integer)it.next();
                   codeCaractereHuffman = associationsCodeAsciiCodeHuffman.get(codeAscii);                   
                   associationsCodeAsciiCodeHuffmanTableau[codeAscii] = codeCaractereHuffman; // Enregistrement de l'association (codeAscii - code caractere Huffman) dans le tableau                   
                   
                   // Enregistrement de l'association dans le tableau de bytes a ecrire
                   enteteEtContenuAEcrire[nbBytesEcrits] = (byte)(int)codeAscii;
                   enteteEtContenuAEcrire[nbBytesEcrits+1] = (byte)codeCaractereHuffman.length();                   
                   nbBytesEcrits = nbBytesEcrits+2;
                }
                                     
                // --- Enregistrement de chaque caractere du fichier original apres traduction grace au codage de Huffman ---
                int offset = 0;
		int bits = 0;
                int nbBitsBourrage = 0;              
                
		while(fichierTexteOriginal.available() > 0){
                    // On cherche a savoir si on traite le dernier bloc de 1000000 bytes ou non
                    if(fichierTexteOriginal.available() >= 1000000){
                        nbBytesALire = fichierTexteOriginal.read(texteLuFichierOriginal, 0, 1000000);
                    }else{
                        nbBytesALire = fichierTexteOriginal.read(texteLuFichierOriginal, 0, fichierTexteOriginal.available());
                    }
                    
                    // On traite chaque byte du bloc de 1000000 bytes
                    for(int k = 0; (k < nbBytesALire); k++){
			// Recuperation du code de huffman correspondant au caractere lu
                        codeCaractereHuffman = associationsCodeAsciiCodeHuffmanTableau[texteLuFichierOriginal[k]];

			// Traitement bit par bit du code Huffman obtenu pour le caractere lu
			for (int j = 0; j < codeCaractereHuffman.length(); j++) {
                                // Enregistrement du bit a la place correcte dans un byte
				if (codeCaractereHuffman.charAt(j) == '0') {
					bits <<= 1;
				} else {
					bits = bits << 1 | 1;
				}
				offset++;
				if (offset == 8) { // Le byte contient 8 bits et peut être enregistre dans le tableau de bytes a ecrire
                                        enteteEtContenuAEcrire[nbBytesEcrits] = (byte)bits;
                                        nbBytesEcrits++;
					bits = 0;
					offset = 0;
				}
			}
                    }
                    
                    // Ecriture du bloc de bytes stockes
                    fichierTexteCompresse.write(enteteEtContenuAEcrire,0,nbBytesEcrits);
                    nbBytesEcrits = 0;
		}                
                if(offset != 8){ // Dans ce cas, le dernier byte n'est pas complet, il faut "bourrer"
                    for(nbBitsBourrage = 0; offset < 8; nbBitsBourrage++){
                        bits = bits << 1;
                        offset++;
                    }
                    
                    // Ecriture du dernier byte (il y aura nbBitsBourrage bits de bourrage dans le dernier octet)
                    fichierTexteCompresse.write((byte)bits);
                }    
                
                fichierTexteOriginal.close();
		fichierTexteCompresse.close();
               
                // Ecriture du nb de caracteres lus et du nombre de bits de bourrage au debut du fichier compresse                  
                RandomAccessFile rd = new RandomAccessFile(nomFichierCompresse,"rwd");
                rd.seek(0);
                rd.writeBytes(String.valueOf(nbCaracteresDifferentsLus)+';'+String.valueOf(nbBitsBourrage));
                
                System.out.println("Nombre de caracteres differents : " + nbCaracteresDifferentsLus);
	}
        
        /**
	 * Recree un arbre a partir de la lecture de l'entete dans le fichier original
	 * 
	 * @param nomFichierCompresse
	 *            nom du fichier compresse a lire
         * 
	 */
       protected ArbreBinaire<Integer> lectureEnteteFichierCompresse(String nomFichierCompresse) throws FileNotFoundException, IOException{
           fichierCompresse = new FileInputStream(nomFichierCompresse);
           ArbreBinaire<Integer> arbre = new ArbreBinaire();
           int caractereLuCodeAscii;
           String nbCaracteresLusDifferentsString = "";
           int nbCaracteresDifferentsALire;           
           
           // Lecture du nombre de caracteres differents dans le contenu du fichier a decompresser
           while((caractereLuCodeAscii = fichierCompresse.read()) != ';'){
                nbCaracteresLusDifferentsString += (char)caractereLuCodeAscii;
           }
           nbCaracteresDifferentsALire = Integer.parseInt(nbCaracteresLusDifferentsString);
           
           // Lecture du nombre de bits de bourrage dans le dernier octet
           nbBitsBourrageDernierOctetDecompression = Integer.parseInt(String.valueOf((char)fichierCompresse.read())); 
            
           // Recreation de l'arbre a partir de l'entete en inserant chaque noeud a une hauteur precise (determinee par la longueur du code de huffman)
           while(nbCaracteresDifferentsALire > 0){
                arbre.insererNoeudHauteurPrecise(fichierCompresse.read(),fichierCompresse.read()); 
                nbCaracteresDifferentsALire--;
           }   
                
           return arbre;
       }

        /**
	 * Lit un buffer de 1000000 octets dans le fichier compresse puis traite chaque bit de chaque byte afin se deplacer
         * dans l'arbre et de trouver le caractere correspondant que l'on enregistre dans un buffer d'ecriture. 
         * Ensuite ecrit le buffer.
	 * 
	 * @param nomFichierCompresse
	 *            nom du fichier compresse a lire
         * @param nomFichierDecompresse
	 *            nom du fichier decompresse a lire
         * @param arbre
	 *            l'arbre genere a partir de l'entete du fichier compresser
         * 
	 */
	protected void ecritureFichierDecompresse(String nomFichierCompresse,
			String nomFichierDecompresse, ArbreBinaire<Integer> arbre) throws FileNotFoundException,
			IOException {

                FileOutputStream fichierDecompresse = new FileOutputStream(nomFichierDecompresse);
                
                byte[] contenuFichierCompresse = new byte[1000000]; // Buffer pour stocker 1000 bytes du fichier de lecture 
                byte[] contenuFichierDecompresse = new byte[8000000]; // Buffer pour stocker entre 0 et 80000 bytes avant de les ecrire
                int[] masques = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};                
                Noeud noeudTmp = arbre.getRacine();
                Integer valNoeud;		
                int nbBytesEcrits;
                int nbBytesALire;
                
                // On lit dans le flux de lecture tant qu'il y a des caracteres
                while(fichierCompresse.available() > 0){
                    nbBytesEcrits = 0;
                    
                    // On cherche a savoir si on traite le dernier bloc de 1000000 bytes ou non
                    if(fichierCompresse.available() >= 1000000){
                        nbBytesALire = fichierCompresse.read(contenuFichierCompresse, 0, 1000000);
                    }else{
                        nbBytesALire = fichierCompresse.read(contenuFichierCompresse, 0, fichierCompresse.available());
                    }
                    
                    // On traite chaque byte du bloc de 1000000 bytes
                    for(int k = 0; (k < nbBytesALire); k++){ 
                        // On traite chaque bit de chaque byte
                        for(int j = 7; (j >= 0) && (k < (nbBytesALire-1) || j >= nbBitsBourrageDernierOctetDecompression); j--){ // On prend soin de ne pas ecrire les bits de bourrage du dernier octet       
                            // On cherche dans l'arbre a gauche ou a droite selon le bit
                            if((contenuFichierCompresse[k] & masques[j]) == 0){
                                noeudTmp = noeudTmp.getFilsGauche();
                            }else{
                                noeudTmp = noeudTmp.getFilsDroit();
                            }

                            valNoeud = (Integer)noeudTmp.getVal();

                            if(valNoeud != null){ // Enregistrement du code ascii car le noeud est une feuille (contient une valeur differente de null)
                                contenuFichierDecompresse[nbBytesEcrits] = (byte)(int)valNoeud; // Ajout de la valeur a ecrire dans le buffer de bytes
                                nbBytesEcrits++;
                                noeudTmp = arbre.getRacine();                                            
                            }
                        }
                    }                    
                    
                    fichierDecompresse.write(contenuFichierDecompresse,0,nbBytesEcrits); // Ecriture du buffer de bytes
                }
                
                fichierCompresse.close();
                fichierDecompresse.close();
	}

}