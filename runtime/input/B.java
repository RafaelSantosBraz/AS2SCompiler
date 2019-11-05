public class B {

    public static int a;
    public static int b = 0;
    public static int c[] = new int[2];
    public static int d[] = { 2, 4 };
    public static int e[] = { 2, 4 };

    public static int factorial(int num) {
        int fat = 1;
        while (num > 1) {
            fat *= num--;
        }
        return fat;
    }

    public static void main(String[] args) {
        char vet[] = { 'a', 'b', 'c', 'd', 'e', 'f' };
        int i = factorial(2);
    }
}