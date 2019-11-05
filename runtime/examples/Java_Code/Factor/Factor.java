/* Verify Factor
*  adapted from (Java para Iniciantes 6ed)
*/

public class Factor {
    boolean isFactor(int a, int b) {
        if ((b % a) == 0) {
            return true;
        } else {
            return false;
        }
    }
}