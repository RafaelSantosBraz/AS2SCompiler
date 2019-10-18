#include <stdio.h>
#include <math.h>

struct A{
    int a;
};

double x;

char agora(int a, int b){
    int y = a + b;
    return 'a';
}

int main()
{
    agora(2, 4);
    return 0;
}