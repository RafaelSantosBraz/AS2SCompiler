/* Ordered Array test
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class OrderedApp {
    public static void main(String[] args) {
        int maxSize = 100;
        OrdArray arr;
        arr = new OrdArray(maxSize);
        arr.insert(77);
        arr.insert(99);
        int searchKey = 55;
        int p = arr.find(searchKey);
    }
}