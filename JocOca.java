import java.util.InputMismatchException;
import java.util.Scanner;

public class JocOca {
    public static void main(String[] args) {
        JocOca p = new JocOca();
        p.principal();
    }

    Scanner h = new Scanner(System.in);
    String[] noms ;
    int numPlayers;
    public void principal() {
        System.out.println("EL JOC DE L'OCA \n");
        config();
        play();

    }

    public void config() {
        
        do {
            System.out.print("Quants jugadors juguen?: ");
            numPlayers = scTryCatchInt(h);
            if (numPlayers < 2 || numPlayers > 4) {
                System.out.println("Han de jugar de 2 a 4 persones");
            }
        } while (numPlayers < 2 || numPlayers > 4);

        h.nextLine();

        noms = nomsJugadors(h);
        definirOrdre(numPlayers, noms);
    }

    public int scTryCatchInt(Scanner scan) {
        int entrada = 0;
        boolean valid = false;

        while (!valid) {
            try {
                entrada = scan.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Introdueix un número vàlid:");
                scan.next();
            }
        }
        return entrada;
    }

    public String[] nomsJugadors(Scanner sc) {
        String[] names = new String[numPlayers];

        for (int c = 0; c < numPlayers; c++) {
            System.out.print("Com es diu el jugador " + (c + 1) + "?: ");
            names[c] = scTryCatchString(sc); 
        }

        return names;
    }

    public String scTryCatchString(Scanner scan) {
        String entrada = "";
        boolean valid = false;

        while (!valid) {
            try {
                entrada = scan.nextLine();
                if (!entrada.trim().isEmpty()) {
                    valid = true;
                } else {
                    System.out.println("Nom no vàlid, torna-ho a provar:");
                }
            } catch (Exception e) {
                System.out.println("Error desconegut. Torna a escriure:");
            }
        }
        return entrada;
    }

    public void definirOrdre(int num, String[] names) {
        int[] tirades = new int[num];

        System.out.println("\n--- TIRADES PER DEFINIR L'ORDRE ---");

        for (int c = 0; c < num; c++) {
            tirades[c] = tirarDau(c);
            System.out.println("El jugador " + names[c] + " ha tret un " + tirades[c]);
        }
        for (int i = 0; i < num - 1; i++) {
            for (int j = 0; j < num - 1 - i; j++) {
                if (tirades[j] < tirades[j + 1]) {
                    int tempTirada = tirades[j];
                    tirades[j] = tirades[j + 1];
                    tirades[j + 1] = tempTirada;
                    String tempNom = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempNom;
                }
            }
        }
        

        System.out.println("\nORDRE DE TIRADA:");
        for (int i = 0; i < num; i++) {
            System.out.println((i + 1) + ". " + names[i] + " (ha tret " + tirades[i] + ")");
        }

    }

    public void play() {
    int torn= 0;
    boolean tornActiu = true;
    boolean end = false;
    do{
    while(tornActiu){
    if (torn >=noms.length){
        torn = 0;
    }
    System.out.println("Torn de: "+ noms[torn]);
    int dice= 0;
    int move;
    move = tirarDau(dice);
    torn++;
    }
    }while(!end);
    }
    public int tirarDau(int dice){
        dice = (int) (Math.random() * 12) + 1;         
        return dice;
    }
}
