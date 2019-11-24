#include <stdio.h>

void evenCheck(int x)
{
    if ((x % 2) == 0)
    {
        printf("%d is Even.\n", x);
    }
    else
    {
        printf("%d is not Even.\n", x);
    }
}