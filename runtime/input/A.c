#include <stdio.h>

int a;
int b = 0;
int c[2];
int d[] = {2,4};
int e[2] = {1,2};

int factorial(int num, int a[])
{
    int fat = 1;
    while (num > 1)
    {
        fat *= num--;
    }
    return fat;
}

void main()
{
    char vet[] = {'a', 'b', 'c', 'd', 'e', 'f'};
    //printf("%d\n", sizeof(vet));
    //int p = binary(vet, sizeof(vet), 'c');
    //printf("%d\n", factorial(15));
    //printf("%c\n", vet[p]);
}