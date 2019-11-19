/* Binary Search
*  adapted from (C Completo e Total 3ed)
*/

#include <stdio.h>

char vc[] = {'a', 'b', 'c'};

int binary(char item[], int count, char key)
{
    int low;
    int high;
    int mid;
    int x[20];
    low = 0;
    double j;
    double y = -2.5 +( (1.0 )/ 3.0)*1;
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

void main()
{
    char key = 'd';
    int p = binary(vc, 3, key);
    if (p == -1)
    {
        printf("nao tem\n");
    }
    else
    {
        printf("encontrou na pos. %d\n", p);
    }
}