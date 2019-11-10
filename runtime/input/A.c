#include <stdio.h>

int binary(char item[], int count, char key)
{
    int low;
    int high;
    int mid;
    low = 0;
    high = count - 1;
    while (low <= high)
    {
        mid = (low + high) / 2;
        if (key < item[mid])
        {
            high = mid - 1;
        }
        else
        {
            if (key > item[mid])
            {
                low = mid + 1;
            }
            else
            {
                return mid;
            }
        }
    }
    return -1;
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