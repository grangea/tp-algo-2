package codagehuffman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

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

	String[] codageLettres;
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
			// Si le caractere n'a jamais ete vu avant
			// On l'ajoute dans la Hashtable
			/*if (!arbreHuffmanLettresFichier.containsKey(caractereLuCodeAscii)) {
				arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
						new ArbreHuffman<Integer>(caractereLuCodeAscii));

			} else { // Si le caractere a deja ete vu et enregistre
						// On augmente son nombre d'occurences dans la
						// hashtable a partir de son codeASCII
				ArbreHuffman<Integer> arbreHuffman = arbreHuffmanLettresFichier
						.get(caractereLuCodeAscii);
				arbreHuffman.incrementerPriorite();
			}*/
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
		for (int i = 0; i < codageLettres.length; i++) {
                    if(codageLettres[i] != null){
                        enteteEtContenuAEcrire[k] = (byte)i;
                        enteteEtContenuAEcrire[k+1] = Byte.parseByte(codageLettres[i]);
                        k = k+2;
                    }
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
			codeCaractereTemp = codageLettres[caractereLuCodeAscii];

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
                int nbCaracteresLus;
                String nbCaracteresLusDifferentsString = "";
                int nbCaracteresDifferentsLus;
                int i=0;
                int j=0;
                
                for(i=0; buffer[i]!=';'; i++){
                    nbCaracteresLusString += buffer[i]-48;
                }
                nbCaracteresLus = Integer.parseInt(nbCaracteresLusString);
                
                for(i = i+1; buffer[i]!=';'; i++){
                    nbCaracteresLusDifferentsString += buffer[i]-48;
                }
                nbCaracteresDifferentsLus = Integer.parseInt(nbCaracteresLusDifferentsString);
                
                ArbreBinaire<Integer> af = new ArbreBinaire();
                for(i = i+1; nbCaracteresDifferentsLus > 0; i=i+2){
                    System.out.println("Code ascii : "+(int)buffer[i]);
                    System.out.println("Code car : "+Integer.parseInt(Byte.toString(buffer[i+1]),2));
                    af.insererNoeudHauteurPrecise((int)buffer[i],Integer.parseInt(Byte.toString(buffer[i+1]),2)); 
                    nbCaracteresDifferentsLus--;
		}              
		// ----- Fin de la lecture de l'entete et de la génération de la hashtable temporaire ------              
                
                // ----- Lecture du texte code et decompression ---------------------
                if(!af.estVide()){ // L'arbre reconstruit n'est pas vide
                    byte[] texteAEcrireFichierDecompresse = new byte[nbCaracteresLus];
                    int nombreCaracteresEcritsFichierDecompresse = 0;
                    int[] masques = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

                    Noeud noeudTmp = af.racine;
                    Integer valNoeud;

                    for(i = i+1; nombreCaracteresEcritsFichierDecompresse < nbCaracteresLus; i++){  
                        for(j=7; (j >= 0) && (nombreCaracteresEcritsFichierDecompresse < nbCaracteresLus); j--){                    
                            if((buffer[i] & masques[j]) == 0)
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