#include <stdio.h>

void bubble(char item[], int count)
{
    int a;
    int b;
    char t;
    for (a = 1; a < count; ++a)
    {
        for (b = count - 1; b >= a; --b)
        {
            if (item[b - 1] > item[b])
            {
                t = item[b - 1];
                item[b - 1] = item[b];
                item[b] = t;
            }
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