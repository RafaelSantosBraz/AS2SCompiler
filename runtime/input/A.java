/* Even or Odd
*  adapted from (Java para Iniciantes 6ed)
*/

public class ChkNum {

    int a;
    static char b = 'b';

    boolean isEven(int x) {
        if ((x % 2) == 0) {
            return true;
        } else {
            return false;
        }
    }

    static void met(){

    }

    void teste(){
        this.b = 1;
        ChkNum.b = 'x';
        char c = ChkNum.b;
        ChkNum.b++;       
        this.isEven(obj.a);
    }

    public ChkNum(){
     
    }
}