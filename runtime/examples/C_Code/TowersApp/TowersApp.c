/* Tower of Hanoi
*  translated from (Estruturas de Dados & Algoritmos em Java 2ed)
*/

int nDisks = 3;

void doTowers(int topN, char from, char inter, char to)
{
    if (topN == 1)
    {
        printf("Disk 1 from %c to %c\n", from, to);
    }
    else
    {
        doTowers((topN - 1), from, to, inter);
        printf("Disk %d from %c to %c\n", topN, from, to);
        doTowers((topN - 1), inter, from, to);
    }
}
