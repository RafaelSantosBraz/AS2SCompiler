#include <stdio.h>
#include <stdlib.h>

struct ChkNum
{
    int a;
    int (*isEven)(int, struct ChkNum *);
};

int isEven(int, struct ChkNum *);

struct ChkNum *ChkNum()
{
    struct ChkNum *_this = (struct ChkNum *)malloc(sizeof(struct ChkNum));
    _this->isEven = isEven;
    return _this;
}

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
    struct ChkNum *cc = ChkNum();
    printf("%d\n", cc->isEven(2, cc));
}