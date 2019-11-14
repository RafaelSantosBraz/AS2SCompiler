#include <stdio.h>
#include <stdlib.h>



struct ChkNum
{
    int a;
    int (*isEven)(int, struct ChkNum *);
};

int isEven(int, struct ChkNum *);

char b = 'b';

int isEven(int x, struct ChkNum *_this)
{
    if ((x % 2) == 0)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

void main()
{
}