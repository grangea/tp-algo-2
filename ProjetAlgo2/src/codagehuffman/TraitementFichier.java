package codagehuffman;

import arbre.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

public class TraitementFichier<E> {
	/**
	 * @brief Traite un fichier qui doit compresser ou decompresser
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	
        protected Object[] associationsCodeAsciiArbresHuffman;
    
       

	protected LinkedHashMap<Integer,String> associationsCodeAsciiCodeHuffman;
	/**
	 * Hastable permettant de stocker le couple(Cle:code ascii du caractere,
	 * code binaire)
	 */
        private FileInputStream fichierCompresse;
        
        

	/**
	 * Lit les caracteres du fichier original que l'on souhaite compresser, puis
         * ajoute le code ascii du caractere dans le tableau tableauArbresHuffman (en 
         * ayant au préalable créé un arbre de Huffman pour ce caractere) si le caractere 
         * n'est pas déjà présent dans le tableau sinon incremente la priorite de l'arbre 
         * de Huffman correspondant à ce caractère
	 */
	protected int lectureFichierOriginal(String nomFichierOriginal)
			throws FileNotFoundException, IOException {
            
		FileInputStream fichierTexteOriginal = new FileInputStream(nomFichierOriginal); // Permet de lire le fichier a compresser
                byte[] texteLuFichierOriginal; // Permet de stocker le contenu du fichier orignal
                int caractereLuCodeAscii; // Code ASCII du caractere lu
		int nbCaracteresLus = 0;

                texteLuFichierOriginal = new byte[fichierTexteOriginal.available()]; // Alloue l'espace mémoire nécessaire pour stocker le contenu du fichier original
		associationsCodeAsciiArbresHuffman = new Object[256]; // Alloue l'espace mémoire nécessaire pour stocker les arbres de Huffman correspondants aux 256 caractères du code ASCII
                
                // --- Traitement des caracteres du fichier original et creation des arbres de Huffman associés ---
                fichierTexteOriginal.read(texteLuFichierOriginal); // Lecture du contenu du fichier original
		for(int j = 0; j < texteLuFichierOriginal.length; j++) { // Traitement de chaque caractere lu
			caractereLuCodeAscii = texteLuFichierOriginal[j];
			nbCaracteresLus++;
			
                        if(associationsCodeAsciiArbresHuffman[caractereLuCodeAscii] != null){ // Arbre de Huffman déjà existant dans le tableau
                            ((ArbreHuffman<Integer>)associationsCodeAsciiArbresHuffman[caractereLuCodeAscii]).incrementerPriorite();
                        }else{ // Arbre de Huffman non existant dans le tableau : création de l'arbre de Huffman
                            associationsCodeAsciiArbresHuffman[caractereLuCodeAscii] = new ArbreHuffman(caractereLuCodeAscii);
                        }		
                }
		
                return nbCaracteresLus;
	}

	/**
	 * Ecrit l'entete contenant chaque symbole avec son code Puis le texte a
	 * l'aide des codes des caractEres que l'on a determine avec notre arbre de
	 * huffman
	 */
	protected void ecritureFichierCompresse(String nomFichierOriginal,
			String nomFichierCompresse, int nbCaracteresDifferentsLus)
			throws FileNotFoundException, IOException { // A revoir    
                
                FileInputStream fichierTexteOriginal = new FileInputStream(nomFichierOriginal);
                FileOutputStream fichierTexteCompresse = new FileOutputStream(nomFichierCompresse);
                                                
                byte[] texteLuFichierOriginal = new byte[fichierTexteOriginal.available()]; // Alloue l'espace mémoire nécessaire pour stocker le contenu du fichier original    
                byte[] enteteEtContenuAEcrire = new byte[(nbCaracteresDifferentsLus*2)+texteLuFichierOriginal.length];
                String[] associationsCodeAsciiCodeHuffmanTableau = new String[256];
                String codeCaractereHuffman;
                int nombreBytesEcrits = 0;
                
		// --- Enregistrement de chaque association (formant l'entete) dans le tableau de bytes à écrire ---
                Set cles = associationsCodeAsciiCodeHuffman.keySet();
                Iterator it = cles.iterator(); // Parcours grâce à un itérateur sur les clés de la liste chainee
                Integer codeAscii;               
                
                while (it.hasNext()){
                   codeAscii = (Integer)it.next();
                   codeCaractereHuffman = (String)associationsCodeAsciiCodeHuffman.get(codeAscii);                   
                   associationsCodeAsciiCodeHuffmanTableau[codeAscii] = codeCaractereHuffman; // Enregistrement de l'association (codeAscii - code caractere Huffman) dans le tableau                   
                   
                   // Enregistrement de l'association dans le tableau de bytes à ecrire
                   enteteEtContenuAEcrire[nombreBytesEcrits] = (byte)(int)codeAscii;
                   enteteEtContenuAEcrire[nombreBytesEcrits+1] = (byte)codeCaractereHuffman.length();                   
                   nombreBytesEcrits = nombreBytesEcrits+2;
                }
                                     
                // --- Enregistrement de chaque caractere du fichier original après traduction grace au codage de Huffman ---
                int offset = 0;
		int bits = 0;
              
                fichierTexteOriginal.read(texteLuFichierOriginal); // Lecture du contenu du fichier original
		for(int i=0; i<texteLuFichierOriginal.length; i++) { // Lecture du fichier caractere par caractere
			// Recuperation du code de huffman correspond au caractere lu
                        codeCaractereHuffman = associationsCodeAsciiCodeHuffmanTableau[texteLuFichierOriginal[i]];

			// Traitement bit par bit du code Huffman obtenu pour le caractere lu
			for (int j = 0; j < codeCaractereHuffman.length(); j++) {
                                // Enregistrement du bit à la place correcte dans un byte
				if (codeCaractereHuffman.charAt(j) == '0') {
					bits <<= 1;
				} else {
					bits = bits << 1 | 1;
				}
				offset++;
				if (offset == 8) { // Le byte contient 8 bits et peut être enregistré dans le tableau de bytes a ecrire
                                        enteteEtContenuAEcrire[nombreBytesEcrits] = (byte)bits;
                                        nombreBytesEcrits++;
					bits = 0;
					offset = 0;
				}
			}
		}                
                if(offset != 8){ // Dans ce cas, le dernier byte n'est pas complet, il faut "bourrer"
                    for(int j = 0; offset < 8; j++){
                        bits = bits << 1;
                        offset++;
                    }
                    
                    // Enregistrement du dernier byte dans le tableau de bytes a ecrire (il y aura (8-j) bits de bourrage dans le dernier octet)
                    enteteEtContenuAEcrire[nombreBytesEcrits] = (byte)bits;
                    nombreBytesEcrits++;
                }        
               
                // Ecriture du nb de caracteres lus et differents et du tableau de bytes (entete et contenu)       
                fichierTexteCompresse.write((String.valueOf(nbCaracteresDifferentsLus)+';').getBytes());
                fichierTexteCompresse.write(enteteEtContenuAEcrire,0,nombreBytesEcrits);
                
		fichierTexteOriginal.close();
		fichierTexteCompresse.close();

		System.out.println("Nombre de caracteres differents : " + nbCaracteresDifferentsLus);
	}
        
       protected ArbreBinaire<Integer> lectureEnteteFichierCompresse(String nomFichierCompresse) throws FileNotFoundException, IOException{
           fichierCompresse = new FileInputStream(nomFichierCompresse);
           int caractereLuCodeAscii;
           String nbCaracteresLusDifferentsString = "";
           int nbCaracteresDifferentsALire;
           ArbreBinaire<Integer> arbre = new ArbreBinaire();
           
           while((caractereLuCodeAscii = fichierCompresse.read()) != ';'){
                nbCaracteresLusDifferentsString += (char)caractereLuCodeAscii;
           }
           nbCaracteresDifferentsALire = Integer.parseInt(nbCaracteresLusDifferentsString);
            
           while(nbCaracteresDifferentsALire > 0){
                arbre.insererNoeudHauteurPrecise(fichierCompresse.read(),fichierCompresse.read()); 
                nbCaracteresDifferentsALire--;
           }   
                
           return arbre;
       }

	/**
	 * Lit l'entete du texte qui a ete compresse Recree les couples de
	 * cle-valeur (code binaire, caracterere en ascii) Lit le texte compresse
	 * puis ecrit le texte decode dans le nouveau fichier
	 */
	protected void ecritureFichierDecompresse(String nomFichierCompresse,
			String nomFichierDecompresse, ArbreBinaire<Integer> arbre) throws FileNotFoundException,
			IOException {

                FileOutputStream fichierDecompresse = new FileOutputStream(nomFichierDecompresse);
                
                byte[] contenuFichierCompresse = new byte[1000];  
                byte[] contenuFichierDecompresse = new byte[8000];
                int[] masques = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};                
                Noeud noeudTmp = arbre.getRacine();
                Integer valNoeud;		
                int nbCaracteresEcrits;
                int nbCaracteresALire;
                
                while(fichierCompresse.available() > 0){ // On lit dans le buffer de lecture tant qu'il y a des caracteres
                    nbCaracteresEcrits = 0;
                    
                    // On cherche à savoir si on traite le dernier bloc de 1000 bytes
                    if(fichierCompresse.available() >= 1000){
                        nbCaracteresALire = fichierCompresse.read(contenuFichierCompresse, 0, 1000);
                    }else{
                        nbCaracteresALire = fichierCompresse.read(contenuFichierCompresse, 0, fichierCompresse.available());
                    }
                    
                    // On traite chaque byte du bloc de 1000 bytes
                    for(int k = 0; (k < nbCaracteresALire) && (nbCaracteresALire > nbCaracteresEcrits); k++){ 
                        // On traite chaque bit de chaque byte
                        for(int j=7; (j >= 0); j--){          
                            // On cherche dans l'arbre à gauche ou à droite selon le bit
                            if((contenuFichierCompresse[k] & masques[j]) == 0){
                                noeudTmp = noeudTmp.getFilsGauche();
                            }else{
                                noeudTmp = noeudTmp.getFilsDroit();
                            }

                            valNoeud = (Integer)noeudTmp.getVal();

                            if(valNoeud != null){ // Ecriture du caractère car le noeud est une feuille (contient une valeur différente de null)
                                contenuFichierDecompresse[nbCaracteresEcrits] = (byte)(int)valNoeud; // Ajout de la valeur a ecrire dans le bloc
                                nbCaracteresEcrits++;
                                noeudTmp = arbre.getRacine();                                            
                            }
                        }
                    }
                    
                    fichierDecompresse.write(contenuFichierDecompresse,0,nbCaracteresEcrits); // Ecriture du bloc
                }

                //fichierDecompresse.write(texteAEcrireFichierDecompresse);

                fichierCompresse.close();
                fichierDecompresse.close();
	}

}