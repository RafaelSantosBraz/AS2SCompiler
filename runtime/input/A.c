#include <stdio.h>

void select(char item[], int count)
{
    int a;
    int b;
    int c;
    int exchange;
    char t;
    for (a = 0; a < count - 1; ++a)
    {
        exchange = 0;
        c = a;
        t = item[a];
        for (b = a + 1; b < count; ++b)
        {
            if (item[b] < t)
            {
                c = b;
                t = item[b];
                exchange = 1;
            }
        }
        if (exchange != 0)
        {
            item[c] = item[a];
            item[a] = t;
        }
    }
}

/*
void main()
{
    //char vet[] = {'a', 'b', 'c', 'd', 'e', 'f'};
    //int c[2];
    int i = factorial(2);
    int j;
    int w = 2 + 2;
    char z = vet[2];
    //printf("%d\n", sizeof(vet));
    //int p = binary(vet, sizeof(vet), 'c');
    //printf("%d\n", factorial(15));
    //printf("%c\n", vet[p]);
}
*/