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

struct MergeApp
{
    int a;
};

struct MergeApp* MergeApp(){
    return (struct MergeApp *)malloc(sizeof(struct MergeApp));   
}

void main()
{
    struct MergeApp *mg = MergeApp();
    mg = MergeApp();
    mg->a = 42;
    printf("%d\n", mg->a);
    free(mg) ;
    //int arrayA[] = { 23, 47, 81, 95 };
    //int arrayB[] = { 7, 14, 39, 55, 62, 74 };
    // int arrayC[10];
    // int j;
    //   for (j = 0; j < 10; j++) {
    //     printf("%d ", j);
    // }
}