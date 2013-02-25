package codagehuffman;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class TraitementFichier {

    // Hashtable permettant de stocker le couple (codeASCII d'un caractere,
    // nombre d'occurences de ce caractere)
    protected Hashtable<Integer, ArbreHuffman<Integer>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Integer>>();;
    // Hastable permettant de stocker le couple(Cle:code ascii du caractere,
    // code binaire)
    protected Hashtable<Integer, String> codageLettres = new Hashtable<Integer, String>();
    // Hastable permettant de stocker le couple(Cle:code binaire, code ascii du
    // caractere)
    protected Hashtable<String, Integer> decodageLettres = new Hashtable<String, Integer>();

    /**
     * Lit les caracteres du fichier a compresser Ajoute chaque caractere dans
     * la Hashtable si non present Sinon modifie leur priorite
     */
    public void lireFichierACompresser(String nomFichier) {
        try {
            // Permet de lire le fichier a compresser
            BufferedReader fichierTexte = new BufferedReader(new FileReader(
                    new File(nomFichier)));
            // Code ASCII correspondant au caractere lu
            int caractereLuCodeAscii;

            if (fichierTexte == null)
                throw new FileNotFoundException("Le fichier " + nomFichier
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

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Ecrit l'entete contenant chaque symbole avec son code Puis le texte a
     * l'aide des codes des caractï¿½res que l'on a determine avec notre arbre
     * de huffman
     */
    public void ecrireFichierACompresser(String nomFichier,
            String nomFichierModifie, String entete) { // A revoir
        String chaineAecrire = "";
        // Code ASCII correspondant a un caractere
        int caractereLuCodeAscii;
        File f = new File(nomFichierModifie);
        // "C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt");
        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(new FileOutputStream(f));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Affichage Temporaire
        /*
         * Set keySet = codageLettres.keySet(); Object[] keys =
         * keySet.toArray(); for(int i=0; i < codageLettres.size(); i++){ Object
         * key = keys[i]; System.out.println("cle : "+ key +" valeur : " +
         * codageLettres.get(key)); }
         */

        try {
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
        }
    }

    public void lireFichierADecompresser(String nomFichier,
            String nomFichierModifie) {

        try {

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
        }

    }
}