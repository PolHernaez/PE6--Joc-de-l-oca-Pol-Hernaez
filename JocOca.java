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
        rondesSanció = new int[num];

        System.out.println("\n--- TIRADES PER DEFINIR L'ORDRE ---");

        for (int c = 0; c < num; c++) {
            tirades[c] = (int) (Math.random() * 6) + 1;
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
        int[] tauler = new int[numPlayers];
        sanció = new boolean[numPlayers];
        System.out.println("\nCOMENÇA LA PARTIDA");
        int torn = 5;
        int ronda = 0;
        do {

            seguentTorn = false;

            while (!seguentTorn) {
                seguentTorn = true;

                if (torn >= noms.length) {
                    torn = 0;
                    ronda++;
                    System.out.println("RONDA " + ronda + "\n");
                }
                System.out.println("Torn de: " + noms[torn] + ", està a la casella " + tauler[torn]);
                int dice = 0;
                int dau1;
                int dau2 = 0;

                if (sanció[torn]) {
                    rondesSanció[torn]--;
                    System.out.println(noms[torn] + " està sancionat i perd el torn.");
                    if (rondesSanció[torn] == 0) {
                        sanció[torn] = false;
                    }
                } else {
                    if (tauler[torn] < 60) {
                        dau1 = tirarDau(dice, tauler, torn);
                        dau2 = tirarDau(dice, tauler, torn);
                    } else {
                        dau1 = tirarDau(dice, tauler, torn);
                        dau2 = 0;
                    }

                    System.out.println(noms[torn] + " ha tret un " + dau1 + " i " + dau2 + ": " + (dau1 + dau2));
                    int move = dau1 + dau2;
                    tauler[torn] += move;
                    if (ronda == 1) {

                        daus3_6_4_5(tauler, torn, dau1, dau2);
                    }

                    pont(tauler, torn);
                    ocaEnOca(tauler, torn);

                    System.out.println(noms[torn] + " es mou fins la casella: " + tauler[torn]);
                    comprovarCasella63(tauler, torn);
                    

                    fonda(tauler, torn, rondesSanció);
                    pou(tauler, torn, rondesSanció);
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
        if (d2 == 3 && d1 == 6) {
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
            int sobrants = 0;
            while (tauler[torn] > 63) {
                sobrants++;
                tauler[torn]--;

            }
            tauler[torn] = tauler[torn] - sobrants;
            System.out.println(noms[torn] + " s'ha mogut fins a " + tauler[torn] + "\n");
        }
    }

    public void ocaEnOca(int tauler[], int torn) {
        System.out.println("Es mou fins la casella " + tauler[torn]);
        int[] oques = { 5, 9, 14, 18, 23, 27, 32, 36, 41, 45, 50, 54, 59 };

        for (int i = 0; i < oques.length; i++) {
            if (tauler[torn] == oques[i]) {
                if (i + 1 < oques.length) {
                    tauler[torn] = oques[i + 1];
                    System.out.println("De oca a oca! " + noms[torn] +
                            " avança fins la casella " + tauler[torn]);
                    seguentTorn = false;
                }
                return;
            }

        }
    }

    public void pont(int tauler[], int torn) {
        if (tauler[torn] == 6) {
            System.out.println("Casella 6: De puente a puente y tiro porque me lleva la corriente.");
            System.out.println("Has abançat fins la casella 12");

            tauler[torn] = 12;
            seguentTorn = false;
        } else if (tauler[torn] == 12) {
            System.out.println("Casella 12: De puente a puente y tiro porque me lleva la corriente.");
            System.out.println("Has retrocedit fins la casella 6");

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
            for (int i = 0; i < tauler.length; i++){
                if (sanció[i] && tauler[i]==31){
                    sanció[i] = false;
                rondes[i] = 0;
                System.out.println(noms[i] + " surt del pou.");
                }
            }
            sanció[torn] = true;
            rondes[torn] = 2;

        }
    }
}
