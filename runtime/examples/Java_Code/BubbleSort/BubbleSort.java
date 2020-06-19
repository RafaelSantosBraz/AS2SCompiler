/* BubbleSort
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class BubbleSort {

    public void bubble(char[] item, int count) {
        int a;
        int b;
        char t;
        for (a = 1; a < count; ++a) {
            for (b = count - 1; b >= a; --b) {
                if (item[b - 1] > item[b]) {
                    t = item[b - 1];
                    item[b - 1] = item[b];
                    item[b] = t;
                }
            }
        }
    }

    public static void display(char[] theArray, int size) {
        int j;
        for (j = 0; j < size; j++) {
            System.out.printf("%c ", theArray[j]);
        }
        System.out.printf("\n");
    }

    public static void do_bubble(char[] item, int count) {
        BubbleSort b = new BubbleSort();
        b.bubble(item, count);
        display(item, count);
    }

}