package codagehuffman;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import arbre.*;

public class TraitementFichier<E> {
	/**
	 * @brief Traite un fichier qui doit compresser ou decompresser
	 * @date Fevrier 2013
	 * @author Alice GRANGE & Romain LHORTOLAT
	 */
	
	protected Hashtable<Integer, ArbreHuffman<Integer>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Integer>>();
	/**
	 * Hashtable permettant de stocker le couple (codeASCII d'un caractere,
	 * nombre d'occurences de ce caractere)
	 */
        Object[] tableauArbresHuffman;

	LinkedHashMap<Integer,String> codageLettres;
	/**
	 * Hastable permettant de stocker le couple(Cle:code ascii du caractere,
	 * code binaire)
	 */

	private Hashtable<String, Integer> decodageLettres = new Hashtable<String, Integer>();
	/**
	 * Hashtable permettant de stocker le couple(Cle:code binaire, code ascii du
	 * caractere)
	 */
        
        byte[] texteALireFichierOriginal;
        
        

	/**
	 * Lit les caracteres du fichier a compresser Ajoute chaque caractere dans
	 * la Hashtable si non present Sinon modifie leur priorite
	 */
	protected int lireFichierACompresser(String nomFichierOriginal)
			throws FileNotFoundException, IOException {
		// Permet de lire le fichier a compresser
		FileInputStream fichierTexte = new FileInputStream(nomFichierOriginal);
                texteALireFichierOriginal = new byte[fichierTexte.available()];
		// Code ASCII correspondant au caractere lu
                tableauArbresHuffman = new Object[256];
		int caractereLuCodeAscii;
		int nbCaracteresLus = 0;

		// Lecture des caracteres dans le fichier
                fichierTexte.read(texteALireFichierOriginal);
		for(int j=0; j<texteALireFichierOriginal.length; j++) {
			caractereLuCodeAscii = texteALireFichierOriginal[j];
			nbCaracteresLus++;
			
                        if(tableauArbresHuffman[caractereLuCodeAscii] != null){
                            ArbreHuffman ab = (ArbreHuffman)tableauArbresHuffman[caractereLuCodeAscii];
                            ab.incrementerPriorite();
                        }else{
                            tableauArbresHuffman[caractereLuCodeAscii] = new ArbreHuffman<Integer>(caractereLuCodeAscii);
                        }		
                }
		
                return nbCaracteresLus;
	}

	/**
	 * Ecrit l'entete contenant chaque symbole avec son code Puis le texte a
	 * l'aide des codes des caractEres que l'on a determine avec notre arbre de
	 * huffman
	 */
	protected void ecrireFichierACompresser(String nomFichierOriginal,
			String nomFichierCompresse, int nbCaracteresLus, int nbCaracteresLusDifferents)
			throws FileNotFoundException, IOException { // A revoir    
                
                FileOutputStream fichierTexteCompresse = new FileOutputStream(
				nomFichierCompresse);
                                
                //String enteteAEcrire = "";                
                    
                byte[] enteteEtContenuAEcrire = new byte[(nbCaracteresLusDifferents*2)+texteALireFichierOriginal.length];
                int k = 0;
		// Enregistrement de l'entete    
                Set cles = codageLettres.keySet();
                Iterator it = cles.iterator();
                String[] codageLettresTableau = new String[256];
                while (it.hasNext()){
                   Integer cle = (Integer)it.next();
                   String valeur = (String)codageLettres.get(cle);
                   codageLettresTableau[cle] = valeur;
                   enteteEtContenuAEcrire[k] = (byte)(int)cle;
                   enteteEtContenuAEcrire[k+1] = (byte)valeur.length();
                   k = k+2;
                }
                                     
                // Enregistrement du contenu après traduction grace au codage de Huffman
                int nombreBytesContenuAEcrire = k;
		String codeCaractereTemp;
                int caractereLuCodeAscii;
                int offset = 0;
		int bits = 0;
                int finOctet = 0;                
		for(int i=0; i<texteALireFichierOriginal.length; i++) {
                        caractereLuCodeAscii = texteALireFichierOriginal[i];
			// Recuperation du code correspond au caractere lu
			codeCaractereTemp = codageLettresTableau[caractereLuCodeAscii];

			// Lecture du code bit par bit
			for (int j = 0; j < codeCaractereTemp.length(); j++) {
				if (codeCaractereTemp.charAt(j) == '0') {
					bits <<= 1;
				} else {
					bits = bits << 1 | 1;
				}
				offset++;
				if (offset == 8) {
                                        enteteEtContenuAEcrire[nombreBytesContenuAEcrire] = (byte)bits;
                                        nombreBytesContenuAEcrire++;
					bits = 0;
					offset = 0;
				}
			}
		}                
                if(offset != 8){ // Dans ce cas, le dernier octet n'est pas complet, il faut "bourrer"
                    for(finOctet=0; offset<8; finOctet++){
                        bits = bits << 1;
                        offset++;
                    }
                    // Enregistrement du dernier octet (il y aura (8-finOctet) bits de bourrage dans le dernier octet)
                    enteteEtContenuAEcrire[nombreBytesContenuAEcrire] = (byte)bits;
                    nombreBytesContenuAEcrire++;
                }        
               
                // Ecriture de l'entete et du contenu            
                fichierTexteCompresse.write((String.valueOf(nbCaracteresLus)+';').getBytes());
                fichierTexteCompresse.write((String.valueOf(nbCaracteresLusDifferents)+';').getBytes());
                fichierTexteCompresse.write(enteteEtContenuAEcrire,0,nombreBytesContenuAEcrire);
                
		//fichierTexte.close();
		fichierTexteCompresse.close();

		/*System.out
				.println("Nombre de caracteres differents : "
						+ codageLettres.length());*/
	}

	/**
	 * Lit l'entete du texte qui a ete compresse Recree les couples de
	 * cle-valeur (code binaire, caracterere en ascii) Lit le texte compresse
	 * puis ecrit le texte decode dans le nouveau fichier
	 */
	protected void lireEcrireFichierADecompresser(String nomFichierCompresse,
			String nomFichierDecompresse) throws FileNotFoundException,
			IOException {

		FileInputStream fichierCompresse = new FileInputStream(nomFichierCompresse);
		FileOutputStream fichierDecompresse = new FileOutputStream(nomFichierDecompresse);

		// ----- Lecture de l'entete et generation de la hashtable temporaire ------
		boolean estEntete = true;
		boolean entreeEntiere = false;
		String codeAsciiCaractere = "";
		String codeCaractere = "";              

                byte[] buffer = new byte[fichierCompresse.available()]; 
                fichierCompresse.read(buffer);                
                String nbCaracteresLusString = "";
                int nbCaracteresALire;
                String nbCaracteresLusDifferentsString = "";
                int nbCaracteresDifferentsALire;
                int i=0;
                int j=0;
                
                for(i=0; buffer[i]!=';'; i++){
                    nbCaracteresLusString += buffer[i]-48;
                }
                nbCaracteresALire = Integer.parseInt(nbCaracteresLusString);
                
                for(i = i+1; buffer[i]!=';'; i++){
                    nbCaracteresLusDifferentsString += buffer[i]-48;
                }
                nbCaracteresDifferentsALire = Integer.parseInt(nbCaracteresLusDifferentsString);
                
                ArbreBinaire<Integer> af = new ArbreBinaire();
                for(i = i+1; nbCaracteresDifferentsALire > 0; i=i+2){
                    af.insererNoeudHauteurPrecise((int)buffer[i],(int)buffer[i+1]); 
                    nbCaracteresDifferentsALire--;
		}              
		// ----- Fin de la lecture de l'entete et de la génération de la hashtable temporaire ------              
                
                // ----- Lecture du texte code et decompression ---------------------
                if(!af.estVide()){ // L'arbre reconstruit n'est pas vide
                    byte[] texteAEcrireFichierDecompresse = new byte[nbCaracteresALire];
                    int nombreCaracteresEcritsFichierDecompresse = 0;
                    int[] masques = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

                    Noeud noeudTmp = af.getRacine();
                    Integer valNoeud;

                    for(i = i; nombreCaracteresEcritsFichierDecompresse < nbCaracteresALire; i++){  
                        for(j=7; (j >= 0) && (nombreCaracteresEcritsFichierDecompresse < nbCaracteresALire); j--){                    
                            if((buffer[i] & masques[j]) == 0)
                                noeudTmp = noeudTmp.getFilsGauche();
                            else
                                noeudTmp = noeudTmp.getFilsDroit();

                            valNoeud = (Integer)noeudTmp.getVal();

                            if(valNoeud != null){ // Ecriture du caractère
                                texteAEcrireFichierDecompresse[nombreCaracteresEcritsFichierDecompresse] = (byte)(int)valNoeud;
                                nombreCaracteresEcritsFichierDecompresse++;
                                noeudTmp = af.getRacine();                                
                            }
                        }
                    }

                    // Cas du dernier byte à écrire
                    /*if(j != -1){ // Le dernier byte n'est pas terminé                  
                        for(j = (7-j); j >= 0; j--){                    
                            if((buffer[i-1] & masques[j]) == 0)
                                    noeudTmp = noeudTmp.getFilsGauche();
                            else
                                    noeudTmp = noeudTmp.getFilsDroit();

                            valNoeud = (Integer)noeudTmp.getVal();

                            if(valNoeud != null){ // Ecriture du caractère
                                    texteAEcrireFichierDecompresse[nombreCaracteresEcritsFichierDecompresse] = (byte)(int)valNoeud;
                                    nombreCaracteresEcritsFichierDecompresse++;
                                    noeudTmp = af.racine;
                           }
                        }  
                    }*/
                    // ----- Fin de la lecture du texte code ----------------------------

                    fichierDecompresse.write(texteAEcrireFichierDecompresse);
                }

                fichierCompresse.close();
                fichierDecompresse.close();
	}

}