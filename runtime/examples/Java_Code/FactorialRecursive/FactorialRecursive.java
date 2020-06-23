/* Factorial
*  adapted from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

public class FactorialRecursive {

    public int factorial_recursive(int num) {
        if (num == 1) {
            return 1;
        }
        return num * factorial_recursive(num - 1);
    }

    public static void do_factorial(int num) {
        FactorialRecursive f = new FactorialRecursive();
        System.out.printf("%d\n", f.factorial_recursive(num));
    }

}