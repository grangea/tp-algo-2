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

public class TraitementFichier {
    
        protected Hashtable<Integer, ArbreHuffman<Character>> arbreHuffmanLettresFichier;

	/** Lit les caracteres du fichier a compresser
	 *  Ajoute chaque caractere dans la Hashtable si non pr�sent
	 *  Sinon modifie leur priorite*/
            public void lireDecompresse(
			String nomFichier) {
		try {
			BufferedReader fichierTexte = new BufferedReader(new FileReader(
					new File(nomFichier)));
			int caractereLuCodeAscii;
			Hashtable<Integer, ArbreHuffman<Character>> arbreHuffmanLettresFichier = new Hashtable<Integer, ArbreHuffman<Character>>();

			if (fichierTexte == null)
				throw new FileNotFoundException("Le fichier " + nomFichier
						+ " n'a pas ete trouve:");

			while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
                            //this.texte += (char)caractereLuCodeAscii;
				if (!arbreHuffmanLettresFichier
						.containsKey(caractereLuCodeAscii)) {
					arbreHuffmanLettresFichier.put(caractereLuCodeAscii,
							new ArbreHuffman((char) caractereLuCodeAscii));
				} else {
					ArbreHuffman<Character> arbreHuffman = arbreHuffmanLettresFichier
							.get(caractereLuCodeAscii);
					arbreHuffman.incrementerPriorite();
				}
			}

                        this.arbreHuffmanLettresFichier = arbreHuffmanLettresFichier;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}
            
        public void lireCompresse(String nomFichier){
            
        }

	public void ecrireCompresse(String entete) { // A revoir
            File f = new File ("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexteCompresse.txt");
             DataOutputStream  dos = null;
             String chaineAecrire = "";
             int caractereLuCodeAscii;
            
            try{
                dos = new DataOutputStream (new FileOutputStream (f));
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
            
            String[] enteteElements = entete.split("\\|");
            Hashtable<String, String> ht = new Hashtable();
            
            for(int i=0; i<enteteElements.length; i=i+3){
                ht.put(enteteElements[i+1], enteteElements[i]);
            }
            
            try{
                BufferedReader fichierTexte = new BufferedReader(new FileReader(
                                            new File("C:/Users/Romain/Documents/NetBeansProjects/CodageHuffman/src/codagehuffman/fichierTexte.txt"))); // A changer pour le nom du fichier

                while ((caractereLuCodeAscii = fichierTexte.read()) != -1) {
                    chaineAecrire += ht.get((char)caractereLuCodeAscii);
                }
            }catch(IOException e){
                //blabla
            }
            
            chaineAecrire = entete+chaineAecrire;
            
            try{   
                int conv;
                for(int i=0;i<chaineAecrire.length();i+=8){
                    conv = 0;
                    //convertit 8 bits :
                    for(int j=0;j<8;j++){
                        conv = conv<<1;   //décale d'un bit vers la gauche ton nombre convertit (revient a multiplier par 2!)

                         //conversion du bit
                         conv = conv+(chaineAecrire.charAt(i+j)-'0');
                     }

                     //ecriture dans le fichier :
                     dos.write(conv);
                }
                
                dos.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
	}
}
