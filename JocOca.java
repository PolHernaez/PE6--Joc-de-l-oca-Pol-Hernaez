import java.util.InputMismatchException;
import java.util.Scanner;

public class JocOca {
    public static void main(String[] args) {
        JocOca p = new JocOca();
        p.principal();
    }

    Scanner h = new Scanner(System.in);
    boolean[] sanció;
    String[] noms;
    int numPlayers;
    boolean seguentTorn = true;
    boolean end = false;
    int[] rondesSanció;

    public void principal() {
        System.out.println("EL JOC DE L'OCA \n");
        config(); //Inicia la configuració
        play(); //Comença el joc

    }

    public void config() {

        do {
            System.out.print("Quants jugadors juguen?: "); //bucle per comprovar que sigui correcte
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

        for (int c = 0; c < numPlayers; c++) { //Bucle per demanar els noms dels jugadors
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
        rondesSanció = new int[num];

        System.out.println("\n--- TIRADES PER DEFINIR L'ORDRE ---");

        for (int c = 0; c < num; c++) {
            tirades[c] = (int) (Math.random() * 6) + 1;
            System.out.println("El jugador " + names[c] + " ha tret un " + tirades[c]);
        }
        for (int i = 0; i < num - 1; i++) { //doble bucle per definir l'ordre
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
        int[] tauler = new int[numPlayers];
        sanció = new boolean[numPlayers];
        System.out.println("\nCOMENÇA LA PARTIDA");
        int torn = 5;
        int ronda = 0;
        do { //Fins que finalitzi la partida

            seguentTorn = false;

            while (!seguentTorn) { //Bucle per gestionar el torn del jugador
                seguentTorn = true;

                if (torn >= noms.length) {
                    torn = 0;
                    ronda++;
                    System.out.println("RONDA " + ronda + "\n");
                }
                System.out.println("Torn del jugador "+(torn+1)+": " + noms[torn] + ", està a la casella " + tauler[torn]);
                int dice = 0;
                int dau1 = 0;
                int dau2 = 0;

                if (sanció[torn]) {
                    rondesSanció[torn]--;
                    System.out.println(noms[torn] + " està sancionat i perd el torn.");
                    if (rondesSanció[torn] == 0) {
                        sanció[torn] = false;
                    }
                } else {
                    System.out.println("Li toca tirar a: " + noms[torn] + " (escriu 'tiro' per tirar els daus)");
                    boolean tirarcorrecte = true;
                    do {
                        String tirar = scTryCatchString(h);
                        if (tirar.equalsIgnoreCase("tiro")) {
                            if (tauler[torn] < 60) {
                                dau1 = tirarDau(dice, tauler, torn);
                                dau2 = tirarDau(dice, tauler, torn);
                            } else {
                                dau1 = tirarDau(dice, tauler, torn);
                                dau2 = 0;
                            }
                            tirarcorrecte = true;
                        } else {
                            System.out.println("Aquesta acció no fa res, escriu 'tirar'");

                            tirarcorrecte = false;
                        }
                    } while (!tirarcorrecte);

                    System.out.println(noms[torn] + " ha tret un " + dau1 + " i " + dau2 + ": " + (dau1 + dau2));
                    int move = dau1 + dau2;
                    tauler[torn] += move;
                    if (ronda == 1) {//per comprovar que es la 1a ronda

                        daus3_6_4_5(tauler, torn, dau1, dau2); //Comprovar daus 3 i 3 o 4 i 5
                    }
                    System.out.println("Es mou fins la casella " + tauler[torn]);
                    pont(tauler, torn); //Comprova si cau al pont

                    laberint(tauler, torn); //Comprova si cau al laberint

                    ocaEnOca(tauler, torn); //Comprova si cau a una oca
                    fonda(tauler, torn, rondesSanció); //Comprova si cau a la fonda
                    pou(tauler, torn, rondesSanció); //Comprova si cau al pou
                    preso(tauler, torn, rondesSanció); //Comprova si cau a la presó
                    laMort(tauler, torn); //Comprova si cau a la mort
                    comprovarCasella63(tauler, torn); //Comprova si guanya o rebota
                }
                System.out.println("");
            }

            torn++;
        } while (!end);
        System.out.println("Ha acabat el joc");
    }

    public int tirarDau(int dice, int tauler[], int torn) {
        if (tauler[torn] < 60) {
            dice = (int) (Math.random() * 6) + 1;
        } else {
            dice = (int) (Math.random() * 6) + 1;
        }
        return dice;
    }

    public void daus3_6_4_5(int tauler[], int torn, int d1, int d2) {
        if (d1 == 3 && d2 == 6) {
            tauler[torn] = 26;
            System.out.println("De dado a dado y tiro porque me ha tocado.");
            seguentTorn = false;
        } else if (d1 == 6 && d2 == 3) {
            tauler[torn] = 26;
            System.out.println("De dado a dado y tiro porque me ha tocado.");
            seguentTorn = false;
        }
        if (d1 == 4 && d2 == 5) {
            tauler[torn] = 53;

        } else if (d2 == 4 && d1 == 5) {
            tauler[torn] = 53;

        }
    }

    public void comprovarCasella63(int tauler[], int torn) {
        if (tauler[torn] == 63) {

            System.out.println("El jugador " + noms[torn] + " ha guanyat");
            seguentTorn = true;
            end = true;
        } else if (tauler[torn] > 63) {
            tauler[torn] = 63 - (tauler[torn] - 63);
            System.out.println(noms[torn] + " Rebota fins a " + tauler[torn] + "\n");

        }

    }

    public void ocaEnOca(int tauler[], int torn) {

        int[] oques = { 5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59, 63 };

        for (int i = 0; i < oques.length; i++) {
            if (tauler[torn] == oques[i]) {
                if (i + 1 < oques.length) {
                    tauler[torn] = oques[i + 1];
                    System.out.println("De oca en oca y tiro porque me toca." + noms[torn] +
                            " avança fins la casella " + tauler[torn]);
                    seguentTorn = false;
                }
                return;
            }

        }
        if (tauler[torn] == 63) {
            seguentTorn = true;
        }
    }

    public void pont(int tauler[], int torn) {
        if (tauler[torn] == 6) {
            System.out.println("Casella 6: De puente a puente y tiro porque me lleva la corriente.");

            tauler[torn] = 12;
            seguentTorn = false;
        } else if (tauler[torn] == 12) {
            System.out.println("Casella 12: De puente a puente y tiro porque me lleva la corriente.");

            tauler[torn] = 6;
            seguentTorn = false;
        }
    }

    public void fonda(int tauler[], int torn, int[] rondes) {

        if (tauler[torn] == 19) {
            System.out.println("Has caigut a la fonda! Perds el pròxim torn.\n");
            sanció[torn] = true;
            rondes[torn] = 1;
        }
    }

    public void pou(int tauler[], int torn, int[] rondes) {
        if (tauler[torn] == 31) {
            System.out.println("Has caigut a al Pou! Perds el pròxim torn.\n");
            for (int i = 0; i < tauler.length; i++) {
                if (sanció[i] && tauler[i] == 31) {
                    sanció[i] = false;
                    rondes[i] = 0;
                    System.out.println(noms[i] + " surt del pou.");
                }
            }
            sanció[torn] = true;
            rondes[torn] = 2;

        }
    }

    public void laberint(int tauler[], int torn) {
        if (tauler[torn] == 42) {
            System.out.println("Casella 42: Et perds en el laberint i tornes enrere fins la casella 39");
            tauler[torn] = 39;

        }
    }

    public void preso(int tauler[], int torn, int[] rondes) {
        if (tauler[torn] == 52) {
            System.out.println("Has entrat a la Presó! Perds 3 torns.\n");
            sanció[torn] = true;
            rondes[torn] = 3;
        }
    }

    public void laMort(int tauler[], int torn) {
        if (tauler[torn] == 58) {
            System.out.println("Has MORT! Tornes a començar");
            tauler[torn] = 1;
            System.out.println("Estàs a la casella: " + tauler[torn]);
        }
    }
}