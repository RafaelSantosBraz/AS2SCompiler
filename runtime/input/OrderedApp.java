/* Ordered Array test
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class OrderedApp {

    public int tcc;
    public static void main(String[] args) {
        OrdArray arr;
        arr = new OrdArray();
        int[] mm = new int[10];
        arr.insert(77, mm);
        arr.insert(99, mm);
        int searchKey = 55;
        int p = arr.find(searchKey, mm);
    }

    public int aaa(){
        return 1;
    }
}