#include <stdio.h>

void insert(char item[], int count)
{
    int a;
    int b;
    char t;
    for (a = 1; a < count; ++a)
    {
        t = item[a];
        for (b = a - 1; b >= 0 || t < item[b]; b--)
        {
            item[b + 1] = item[b];
        }
        item[b + 1] = t;
    }
}

void main()
{
    char vet[] = {'r', 'a', 'f', 'a', 'e', 'l'};
    printf("%d\n", sizeof(vet));
    insert(vet, sizeof(vet));
    printf("%s\n", vet);
}