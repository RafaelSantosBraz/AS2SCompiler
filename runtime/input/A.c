#include <stdio.h>
#include <math.h>

int agora(int a, int b)
{
    if (a == b)
    {
        return a;
    }
    return -1;   
}

int main()
{
    agora(2, 4);
    return 0;
}