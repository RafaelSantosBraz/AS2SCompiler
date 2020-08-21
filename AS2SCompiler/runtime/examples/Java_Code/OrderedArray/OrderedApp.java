/* Ordered Array test
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class OrderedApp {
    public static void main(String[] args) {
        int maxSize = 100;
        int[] vet = new int[maxSize];
        OrdArray arr;
        arr = new OrdArray();
        arr.insert(77, vet);
        arr.insert(99, vet);
        int searchKey = 55;
        int p = arr.find(searchKey, vet);
        System.out.printf("%d\n", p);
        arr.display(vet);
    }
}