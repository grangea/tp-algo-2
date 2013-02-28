package codagehuffman;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class TraitementFichier {
    
    public class LongueurStringeyComparator implements
	Comparator<String> {

@Override
public int compare(String s1,
		String s2) {
	if (s1.length() > s2.length())
		return 1;
	else
		return -1;
}

}

    protected Comparator comparator = new LongueurStringeyComparator();
    // Hashtable permettant de stocker le couple (codeASCII d'un caractere,
    // nombre d'occurences de ce caractere)
    protected Hashtable<Integer, ArbreHuffman<Integer>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Integer>>();;
    // Hastable permettant de stocker le couple(Cle:code ascii du caractere,
    // code binaire)
    protected Hashtable<Integer, String> codageLettres = new Hashtable<Integer, String>();
    // Hastable permettant de stocker le couple(Cle:code binaire, code ascii du
    // caractere)
    protected TreeMap<String, Integer> decodageLettres = new TreeMap<String, Integer>(comparator);

    /**
     * Lit les caracteres du fichier a compresser Ajoute chaque caractere dans
     * la Hashtable si non present Sinon modifie leur priorite
     */
    public void lireFichierACompresser(String nomFichierOriginal) throws FileNotFoundException, IOException {
        // Permet de lire le fichier a compresser
        BufferedReader fichierTexte = new BufferedReader(new FileReader(
                new File(nomFichierOriginal)));
        // Code ASCII correspondant au caractere lu
        int caractereLuCodeAscii;

        if (fichierTexte == null)
            throw new FileNotFoundException("Le fichier " + nomFichierOriginal
                    + " n'a pas ete trouve:");

        // Lecture des caracteres un par un dans le fichier
        while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
            // this.texte += (char)caractereLuCodeAscii;

            // Si le caractere n'a jamais ete vu avant
            // On l'ajoute dans la Hashtable
            if (!arbreHuffmanLettresFichier
                    .containsKey(caractereLuCodeAscii)) {
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

    }

    /**
     * Ecrit l'entete contenant chaque symbole avec son code Puis le texte a
     * l'aide des codes des caractï¿½res que l'on a determine avec notre arbre
     * de huffman
     */
    public void ecrireFichierACompresser(String nomFichierOriginal,
        String nomFichierCompresse, String entete) throws FileNotFoundException, IOException { // A revoir
        int caractereLuCodeAscii;
        char caractereLuBit;
        String codeCaractere;

        BufferedReader fichierTexte = new BufferedReader(new FileReader(new File(nomFichierOriginal)));
        FileOutputStream fichierTexteCompresse = new FileOutputStream(nomFichierCompresse);
        
        int offset = 0;
        int bits = 0;
        
        // Ecritute de l'entete
        for(int i=0; i<entete.length()-1; i++){
            fichierTexteCompresse.write(entete.charAt(i));
        }
        fichierTexteCompresse.write(';');
        
        // Ecriture du corps
        while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
            // Récupération du code correspond au caractère lu
            codeCaractere = codageLettres.get(caractereLuCodeAscii);

            // Lecture du code bit par bit
            for(int i=0; i<codeCaractere.length(); i++){
                caractereLuBit = codeCaractere.charAt(i);
                
                if (caractereLuBit == '0'){
                    bits <<= 1;
                }else{
                    bits = bits << 1|1;
                }
                offset++;
                if (offset == 8){
                  //fichierTexteCompresse.write(bits);
                  bits = 0;
                  offset = 0;
                }
            }
        }
        
        fichierTexte.close();
        fichierTexteCompresse.close();


        // Affichage Temporaire
        /*
         * Set keySet = codageLettres.keySet(); Object[] keys =
         * keySet.toArray(); for(int i=0; i < codageLettres.size(); i++){ Object
         * key = keys[i]; System.out.println("cle : "+ key +" valeur : " +
         * codageLettres.get(key)); }
         */

        /*try {
            BufferedReader fichierTexte = new BufferedReader(new FileReader(
                    new File(nomFichier)));

            // "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt")));

            while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
                chaineAecrire += codageLettres.get(caractereLuCodeAscii);
            }
            fichierTexte.close();
        } catch (IOException e) {

        }

        chaineAecrire = entete + "§|" + chaineAecrire;

        try {
            int conv;
            for (int i = 0; i < chaineAecrire.length(); i += 8) {
                conv = 0;
                // convertit 8 bits :
                for (int j = 0; j < 8; j++) {
                    conv = conv << 1; // decale d'un bit vers la gauche ton
                                        // nombre convertit (revient a
                                        // multiplier par 2!)

                    // conversion du bit
                    conv = conv + (chaineAecrire.charAt(i + j) - '0');
                }

                // ecriture dans le fichier :
                dos.write(conv);
            }

            dos.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }*/
    }

    public void lireFichierADecompresser(String nomFichierCompresse,
            String nomFichierDecompresse) throws FileNotFoundException, IOException {
        String entete = "";
        
        FileInputStream fichierCompresse = new FileInputStream(nomFichierCompresse);
        FileOutputStream fichierDecompresse = new FileOutputStream(nomFichierDecompresse);
        
        // Lecture de l'entete et génération de la hashtable
        boolean estEntete = true;
        boolean entreeEntiere = false;
        String codeAsciiCaractere = "";
        String codeCaractere = "";
        int caractereLuCodeAscii;
        char caractereLuChar;
        while(estEntete){
            caractereLuCodeAscii = fichierCompresse.read();
            caractereLuChar = (char)caractereLuCodeAscii; // A voir si c crade ou pas
            if(caractereLuChar == ';'){
                estEntete = false;
                decodageLettres.put(codeCaractere,Integer.parseInt(codeAsciiCaractere));
                codeCaractere = "";
            }else if((char)caractereLuCodeAscii == '|'){
                if(entreeEntiere){
                    decodageLettres.put(codeCaractere,Integer.parseInt(codeAsciiCaractere));
                    codeAsciiCaractere = "";
                    codeCaractere = "";
                    entreeEntiere = false;
                }else{
                    entreeEntiere = true;
                }
            }else{
                if(entreeEntiere){ // On a fini de trouver la valeur de l'entrée
                    codeAsciiCaractere += caractereLuChar;         
                }else{ // On a fini de trouver la clé de l'entrée
                    codeCaractere += caractereLuChar;
                }
            }            
        }
              
        int bits = 0;  
        int mask = 0;
        int maskTmp = 0;
        int bitsTmp = 0;       
        while(true){
            maskTmp = mask;
            
            if (maskTmp == 0) {
                bitsTmp = fichierCompresse.read();
                if(bitsTmp == -1)
                    break;

                bits = bitsTmp;
                maskTmp = 0x80;
            } else {
                bitsTmp = bits;
            }

            // On cherche à savoir si l'on a le bit 0 ou 1
            if((bitsTmp & maskTmp) == 0) {
                mask = maskTmp>>1;
                codeCaractere += '0';
            } else {
                mask = maskTmp>>1;
                codeCaractere += '1';
            }

            // On cherche à savoir si le code correspond à un code de la hashtable
            if(decodageLettres.containsKey(codeCaractere)){
                fichierDecompresse.write(decodageLettres.get(codeCaractere));
                codeCaractere = "";
            }
        }

        fichierCompresse.close();
        fichierDecompresse.close();
        
        
        
        
        
        

        /*try {

            // --DECODER L'ENTETE DU FICHIER => mettre dans le hashtable
            // les codes binaires + symbole en code ascii
            // --DECODER LES BITS + REGENERER LE TEXTE => ecrire dans un
            // autre fichier les caracteres qui
            // correspondent aux codes lus
            
        
            
            
            
            Scanner scannerFichier = new Scanner(new File(nomFichier));
            
            scannerFichier.useDelimiter(Pattern.compile("[|]"));
            File fmodif = new File(nomFichierModifie);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(
                    fmodif));

            boolean boolEntete = true;
            int valeurAscii = 0;
            String code = "";
            int nbSeparation = 1;
            String caractereLu = "";
            String codeForme = "";
            
            while (scannerFichier.hasNext()) {
                if (boolEntete) {
                    //---PARTIE ENTETE DU FICHIER-----//
                    if (nbSeparation == 1) {
                        if (scannerFichier.hasNextInt()) {
                            // code binaire compose d'entiers
                            code = scannerFichier.next();
                        } else {
                            boolEntete = false;
                            scannerFichier = scannerFichier.useDelimiter("");
                            scannerFichier.next();
                            scannerFichier.next();
                        }
                    } else if (nbSeparation == 2) {
                        // caractère en code ASCII
                        valeurAscii = scannerFichier.nextInt();
                    }
                    nbSeparation++;
                    if (nbSeparation == 3) {
                        // on a lu le code binaire et le caractere
                        decodageLettres.put(code, valeurAscii);
                        nbSeparation = 1;
                    }

                    // sinon lecture dans le fichier et reecriture dans le
                    // nouveau
                    // fichier
                } else {
                    //---PARTIE TEXTE DU FICHIER-----//
                    caractereLu = scannerFichier.next();
                    codeForme = codeForme + caractereLu;

                    // chercher dans hashtable
                    if (decodageLettres.containsKey(codeForme)) {
                        // code trouve dans la hashtable
                        System.out.println("Code forme " + codeForme);

                        // réecrire le caractere trouve dans le nouveau fichier
                        dos.writeChar(decodageLettres.get(codeForme));
                        System.out.println("Caractere: "
                                + decodageLettres.get(codeForme));
                        codeForme = "";
                    }

                }

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

    }
}