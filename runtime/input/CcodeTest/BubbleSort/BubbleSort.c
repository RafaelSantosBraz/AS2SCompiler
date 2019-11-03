/* Bubble Sort 
*  adapted from ()
*/

#include <stdio.h>

void bubble(char item[], int count)
{
    int a, b;
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

void main(int argc, char *argv[])
{
    char vet[argc - 1];
    int c;
    for (c = 0; c < argc - 1; c++)
    {
        vet[c] = argv[c + 1][0];
    }
    bubble(vet, argc-1);
    for (c = 0; c < argc-1; c++){
        printf("%c\n", vet[c]);
    }
}