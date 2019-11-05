/* Factorial
*  adapted from (Elementos de Programação em C)
*/

int factorial(int num)
{
    int fat = 1;
    while (num > 1)
    {
        fat *= num--;
    }
    return fat;
}