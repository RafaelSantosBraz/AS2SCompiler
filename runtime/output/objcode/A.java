public class A {
    public static char vc[] = { 'a', 'b', 'c' };

    public static int binary(char[] item, int count, char key) {
        int low;
        int high;
        int mid;
        int x[] = new int[20];
        low = 0;
        double j;
        double y = (-2.5 + ((1.0 / 3.0) * 1));
        high = (count - 1);
        while ((low <= high)) {
            mid = ((low + high) / 2);
            if ((key < item[mid])) {
                high = (mid - 1);
            } else {
                if ((key > item[mid])) {
                    low = (mid + 1);
                } else {
                    return mid;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        char key = 'd';
        int p = binary(vc, 3, key);
        if ((p == -1)) {
            System.out.printf("nao tem\n");
        } else {
            System.out.printf("encontrou na pos. %d\n", p);
        }
    }
}