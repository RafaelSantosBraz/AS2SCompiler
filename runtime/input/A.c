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

void main()
{
    char vet[] = {'r', 'a', 'f', 'a', 'e', 'l'};
    printf("%d\n", sizeof(vet));
    bubble(vet, sizeof(vet));
    printf("%s\n", vet);
}