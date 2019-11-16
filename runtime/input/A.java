/* Tower of Hanoi
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class TowersApp {

    public static int nDisks = 3;

    public static void doTowers(int topN, char from, char inter, char to) {
        if (topN == 1) {
            System.out.printf("Disk 1 from %c to %c\n", from, to);
        } else {
            doTowers(topN - 1, from, to, inter);
            System.out.printf("Disk %d from %c to %c\n", topN, from, to);
            doTowers(topN - 1, inter, from, to);
        }
    }

    // Example
    public static void main(String[] args) {
        doTowers(nDisks, 'A', 'B', 'C');
    }
}