#include <stdio.h>
#include <stdlib.h>
/*
void main(void);
void pr(struct A*);

struct A
{
    struct A * this;
    int a;
    void (*main_ptr)(void);
};

struct A *A()
{
    struct A *new = malloc(sizeof(struct A));
    new->main_ptr = &main;
    new->this = new;
    new->a = 2;
    return new;
}

void pr(struct A*this){
    printf("%d\n", this->a);
}

int soup(int a, int b)
{
    return a + b;
}

void main()
{
    int (*soup_ptr)(int, int);
    soup_ptr = &soup;
    printf("%d\n", soup_ptr(2, 2));    
    printf("hi :)\n");
}
*/

void teste(){
    int c = (42 + 3) * 2 - fun(2);
    c++;
    if (c >= 2){
        printf("oi\n");
    }
}