public class B {

    static void arraytest(int a[]) {
        // changed the array a
        a[0] = a[0] + a[1];
        a[1] = a[0] - a[1];
        a[0] = a[0] - a[1];
    }

    public static void main(String[] args) {
        int arr[] = { 1, 2 };
        System.out.printf("%d \t %d", arr[0], arr[1]);
        arraytest(arr);
        System.out.printf("\n After calling fun arr contains: %d\t %d", arr[0], arr[1]);
    }

}