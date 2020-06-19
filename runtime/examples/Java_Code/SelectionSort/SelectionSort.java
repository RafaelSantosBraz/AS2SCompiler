/* Ordered Array
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class SelectionSort {

    public void select(char item[], int count) {
        int a;
        int b;
        int c;
        int exchange;
        char t;
        for (a = 0; a < count - 1; ++a) {
            exchange = 0;
            c = a;
            t = item[a];
            for (b = a + 1; b < count; ++b) {
                if (item[b] < t) {
                    c = b;
                    t = item[b];
                    exchange = 1;
                }
            }
            if (exchange != 0) {
                item[c] = item[a];
                item[a] = t;
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

    public static void select_this(char[] item, int count) {
        SelectionSort s = new SelectionSort();
        s.select(item, count);
        display(item, count);
    }

}
