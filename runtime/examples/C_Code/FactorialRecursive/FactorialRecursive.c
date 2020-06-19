/* Factorial
*  adapted from (Elementos de Programação em C)
*/

int factorial_recursive(int num)
{
    if (num == 1)
    {
        return 1;
    }
    return num * factorial_recursive(num - 1);
}