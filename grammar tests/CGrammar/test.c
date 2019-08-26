#include <stdio.h>

typedef struct
{
    
} Principal;

typedef struct
{
    int x;
    int y;
} Goal;

int sum(Goal *goal)
{
    return goal->x + goal->y;
}

void something(Principal *principal)
{
    Goal g;
    g.x = 2;
    g.y = 3;
    printf("%x\n", sum(&g));
}

int main()
{
    Principal principal;
    something(&principal);
    return 0;
}