/* Ordered Array
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class OrdArray {

    public int nElems;

    public OrdArray() {
        nElems = 0;
    }

    public int find(int searchKey, int[] a) {
        int lowerBound = 0;
        int upperBound = nElems - 1;
        int curIn;
        while (true) {
            curIn = (lowerBound + upperBound) / 2;
            if (a[curIn] == searchKey) {
                return curIn;
            } else {
                if (lowerBound > upperBound) {
                    return nElems;
                } else {
                    if (a[curIn] < searchKey) {
                        lowerBound = curIn + 1;
                    } else {
                        upperBound = curIn - 1;
                    }
                }
            }
        }
    }

    public void insert(int value, int[] a) {
        int j;
        for (j = 0; j < nElems; j++) {
            if (a[j] > value) {
                break;
            }
        }
        int k;
        for (k = nElems; k > j; k--) {
            a[k] = a[k - 1];
        }
        a[j] = value;
        nElems++;
    }

    public void display(int[] a) {
        int j;
        for (j = 0; j < nElems; j++) {
            System.out.printf("%d ", a[j]);
        }
        System.out.printf("\n");
    }

}