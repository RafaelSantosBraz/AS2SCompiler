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

    void teste(){
        _this.a = 2;
        _this.isEven(a);
        _this.isEven(_this.a);
    }

    public ChkNum(){
     
    }
}