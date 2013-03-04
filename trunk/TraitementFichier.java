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

public class TraitementFichier {
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

	protected Hashtable<Integer, String> codageLettres = new Hashtable<Integer, String>();
	/**
	 * Hastable permettant de stocker le couple(Cle:code ascii du caractere,
	 * code binaire)
	 */

	private Hashtable<String, Integer> decodageLettres = new Hashtable<String, Integer>();
	/**
	 * Hashtable permettant de stocker le couple(Cle:code binaire, code ascii du
	 * caractere)
	 */
        
        byte[] texteALire;

	/**
	 * Lit les caracteres du fichier a compresser Ajoute chaque caractere dans
	 * la Hashtable si non present Sinon modifie leur priorite
	 */
	protected int lireFichierACompresser(String nomFichierOriginal)
			throws FileNotFoundException, IOException {
		// Permet de lire le fichier a compresser
		FileInputStream fichierTexte = new FileInputStream(nomFichierOriginal);
                texteALire = new byte[fichierTexte.available()];
		// Code ASCII correspondant au caractere lu
		int caractereLuCodeAscii;
		int nbCaracteresLus = 0;

		// Lecture des caracteres dans le fichier
                fichierTexte.read(texteALire);
		for(int j=0; j<texteALire.length; j++) {
			caractereLuCodeAscii = texteALire[j];
			nbCaracteresLus++;
			// Si le caractere n'a jamais ete vu avant
			// On l'ajoute dans la Hashtable
			if (!arbreHuffmanLettresFichier.containsKey(caractereLuCodeAscii)) {
				arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
						new ArbreHuffman<Integer>(caractereLuCodeAscii));

			} else { // Si le caractere a deja ete vu et enregistre
						// On augmente son nombre d'occurences dans la
						// hashtable a partir de son codeASCII
				ArbreHuffman<Integer> arbreHuffman = arbreHuffmanLettresFichier
						.get(caractereLuCodeAscii);
				arbreHuffman.incrementerPriorite();
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
			String nomFichierCompresse, String entete, int nbCaracteresLus)
			throws FileNotFoundException, IOException { // A revoir    
                //FileInputStream fichierTexte = new FileInputStream(nomFichierOriginal);
                FileOutputStream fichierTexteCompresse = new FileOutputStream(
				nomFichierCompresse);
                //byte[] texteALire = new byte[fichierTexte.available()];
                byte[] enteteAEcrire = new byte[entete.length()+1];
                byte[] contenuAEcrire = new byte[texteALire.length];                                
                int nombreBytesContenuAEcrire = 0;
                               
		String codeCaractereTemp;
                int caractereLuCodeAscii;
                int offset = 0;
		int bits = 0;
                int finOctet = 0;

		// Enregistrement de l'entete
		for (int i = 0; i < entete.length() - 1; i++) {
                        enteteAEcrire[i] = (byte)entete.charAt(i);
		}   
                
		// Enregistrement du contenu après traduction grace au codage de Huffman
                //fichierTexte.read(texteALire);
		for(int j=0; j<texteALire.length; j++) {
                        caractereLuCodeAscii = texteALire[j];
			// Recuperation du code correspond au caractere lu
			codeCaractereTemp = codageLettres.get(caractereLuCodeAscii);

			// Lecture du code bit par bit
			for (int i = 0; i < codeCaractereTemp.length(); i++) {
				if (codeCaractereTemp.charAt(i) == '0') {
					bits <<= 1;
				} else {
					bits = bits << 1 | 1;
				}
				offset++;
				if (offset == 8) {
                                        contenuAEcrire[nombreBytesContenuAEcrire] = (byte)bits;
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
                    contenuAEcrire[nombreBytesContenuAEcrire] = (byte)bits;
                    nombreBytesContenuAEcrire++;
                }        
                
                // Enregisrement dans l'entete du separateur et du nombre de bits significatifs du dernier octet
                enteteAEcrire[entete.length()-1] = (byte)';';
                enteteAEcrire[entete.length()] = (byte)(finOctet+48); 
               
                // Ecriture de l'entete et du contenu
                fichierTexteCompresse.write((String.valueOf(nbCaracteresLus)+';').getBytes());
                fichierTexteCompresse.write(enteteAEcrire);                
                fichierTexteCompresse.write(contenuAEcrire,0,nombreBytesContenuAEcrire);
                
		//fichierTexte.close();
		fichierTexteCompresse.close();

		System.out
				.println("Nombre de caracteres differents : "
						+ codageLettres.size());
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
		int caractereLuCodeAscii;               
                //Hashtable<String, Integer> decodageLettresTmp = new Hashtable<String, Integer>();

		/*while (estEntete) {
			caractereLuCodeAscii = fichierCompresse.read();
			caractereLuChar = (char) caractereLuCodeAscii; // A voir si c crade
															// ou pas
			if (caractereLuChar == ';') {
				estEntete = false;
				decodageLettres.put(codeCaractere,
						Integer.parseInt(codeAsciiCaractere));
				codeCaractere = "";
			} else if ((char) caractereLuCodeAscii == '|') {
				if (entreeEntiere) {
					decodageLettres.put(codeCaractere,
							Integer.parseInt(codeAsciiCaractere));
					codeAsciiCaractere = "";
					codeCaractere = "";
					entreeEntiere = false;
				} else {
					entreeEntiere = true;
				}
			} else {
				if (entreeEntiere) { // On a fini de trouver la valeur de
										// l'entree
					codeAsciiCaractere += caractereLuChar;
				} else { // On a fini de trouver la cle de l'entree
					codeCaractere += caractereLuChar;
				}
			}
		}*/
                byte[] buffer = new byte[fichierCompresse.available()]; 
                fichierCompresse.read(buffer);
                int i=0;
                String nbCaracteresLusString = "";
                int nbCaracteresLus;
                
                for(i=0; buffer[i]!=';'; i++){
                    nbCaracteresLusString += buffer[i]-48;
                }
                nbCaracteresLus = Integer.parseInt(nbCaracteresLusString);
                
                ArbreHuffman af = new ArbreHuffman();
                while (estEntete) {
                        i++;
			caractereLuCodeAscii = buffer[i];                        
															
			if (caractereLuCodeAscii == '|' || caractereLuCodeAscii == ';') {
				if (entreeEntiere) {
					// créer noeud
                                        af.insererHuffman(Integer.parseInt(codeCaractere),Integer.parseInt(codeAsciiCaractere));                          
                                    
                                        codeAsciiCaractere = "";
                                        codeCaractere = "";
					entreeEntiere = false;
				} else {
					entreeEntiere = true;
				}
                                
                                if(caractereLuCodeAscii == ';'){
                                    estEntete = false;
                                    codeCaractere = "";
                                    i++;
                                }
			} else {
				if (entreeEntiere) { // On a fini de lire la longueur associée
					codeAsciiCaractere += caractereLuCodeAscii-48;
                                        System.out.println("Longueur : "+codeAsciiCaractere);
				} else { // On a fini de lire le code ASCII du caractère
					codeCaractere += caractereLuCodeAscii-48;
                                        System.out.println("Code ASCII : "+codeCaractere);
				}
			}
		}
                int nombreBitsDeBourrageDernierOctet = buffer[i]-48;                
		// ----- Fin de la lecture de l'entete et de la génération de la hashtable temporaire ------
                
                // ----- Tri de la hashtable temporaire pour obtenir une linkedhashtable triée par éléments les plus souvent accédés ------
                /*List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(decodageLettresTmp.entrySet());
                Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
                  public int compare(final Entry<String, Integer> e1, final Entry<String, Integer> e2) {
                    if(e1.getKey().length() > e2.getKey().length()){
                        return 1;
                    }else if(e1.getKey().length() < e2.getKey().length()){
                        return -1;
                    }else{
                        return 0;
                    }
                  }
                }); 
                for (final Entry<String, Integer> entry : entries) {
                  decodageLettres.put(entry.getKey(), entry.getValue());
                }*/
                // ----- Fin du tri de la hashtable temporaire pour obtenir une linkedhashtable triée par éléments les plus souvent accédés ------


                
                

                /*for(int i = 0; i < buffer.length; i++)
                    bufferi[i] = Integer.valueOf(buffer[i]);*/
                /*int[] cc = new int[buffer.length];
                for(int i=0; i<buffer.length; i++){
                    int essai=buffer[i];
                    if(essai<0)
                        {
                        cc[i] = 256+buffer[i];
                        }
                    else
                        {
                        cc[i] =buffer[i];
                        }
                }*/
              
                byte[] texteAEcrire = new byte[nbCaracteresLus];
                  
		// ----- Lecture du texte code et decompression ---------------------
                Integer codeAsciiAEcrire;
                int k=0;
                int[] masques = {0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};

                for(i=i+1; i<(buffer.length-1); i++){  
                    for(int j=7; j>=0; j--){                    
                        if((buffer[i] & masques[j]) == 0)
                            codeCaractere += '0';
                        else
                            codeCaractere += '1';
                        
                        /*if (this.decodageLettres.containsKey(codeCaractere)) {
                                fichierDecompresse.write(this.decodageLettres.get(codeCaractere));
				codeCaractere = "";
			}    */
                        codeAsciiAEcrire = (Integer)af.existeFeuille(codeCaractere);
                        if(codeAsciiAEcrire != null){
                            texteAEcrire[k] = (byte)(int)codeAsciiAEcrire;
                            k++;
                            //fichierDecompresse.write(codeAsciiAEcrire);
                            codeCaractere = "";
                            /*String cc = String.valueOf(codeAsciiAEcrire);
                            texteAEcrire.concat(cc);*/
                        }
                    }
		}
                // Ecriture du dernier byte
                for(int j=7; j>=nombreBitsDeBourrageDernierOctet; j--){                    
                    if((buffer[i] & masques[j]) == 0) {
                        codeCaractere += '0';
                    } else {
                        codeCaractere += '1';
                    }

                    codeAsciiAEcrire = (Integer)af.existeFeuille(codeCaractere);
                    if(codeAsciiAEcrire != null){
                            //fichierDecompresse.write(codeAsciiAEcrire);
                        texteAEcrire[k] = (byte)(int)codeAsciiAEcrire;
                        k++;
                            codeCaractere = "";
                            /*String cc = String.valueOf(codeAsciiAEcrire);
                            texteAEcrire.concat(cc);*/
                    }
                }                
		// ----- Fin de la lecture du texte code ----------------------------
                
                fichierDecompresse.write(texteAEcrire);
                
		fichierCompresse.close();
		fichierDecompresse.close();
	}

}