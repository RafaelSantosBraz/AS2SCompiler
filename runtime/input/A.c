#include <stdio.h>
#include <math.h>

char agora(int a, int b){    
    //int i;
    for (i = 0; i < 10; i++)
    {
        a += -1 * b / ((a + b) - 4);
    }
    
}

int main()
{
    agora(2, 4);
    return 0;
}