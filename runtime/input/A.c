#include <stdio.h>
#include <math.h>

char agora(int a, int b){    
    while(a >= 0){
        a--;
    }   
    while (a + b != 10) {
        a = a + 1;
    }
}

int main()
{
    agora(2, 4);
    return 0;
}