public class A {
    public static void select(char[] item, int count) {
        int a;
        int b;
        int c;
        int exchange;
        char t;
        for (a = 0; ((a < (count - 1))); ++a) {
            exchange = 0;
            c = a;
            t = item[a];
            for (b = (a + 1); ((b < count)); ++b) {
                if ((item[b] < t)) {
                    c = b;
                    t = item[b];
                    exchange = 1;
                }
            }
            if ((exchange != 0)) {
                item[c] = item[a];
                item[a] = t;
            }
        }
    }
}