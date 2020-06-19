/* Testing Factor class
*  adapted from (Java para Iniciantes 6ed)
*/

public class IsFactor {
    public static void main(String[] args) {
        Factor x = new Factor();
        if (x.isFactor(2, 20)) {
            System.out.printf("is factor");
        }
    }
}