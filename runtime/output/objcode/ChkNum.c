#include <stdlib.h>
#include <stdio.h>
struct ChkNum
{
    int a;
    int (*isEven)(int x, struct ChkNum *_this);
    void (*teste)(struct ChkNum *_this);
};
void teste(struct ChkNum *);
void met();
int isEven(int, struct ChkNum *);
struct ChkNum *ChkNum()
{
    struct ChkNum *_this = malloc(sizeof(struct ChkNum));
    _this->isEven = isEven;
    _this->teste = teste;
    return _this;
}
char b = 'b';
int isEven(int x, struct ChkNum *_this)
{
    if (COMPARISON_OPERATOR)
    {
        return 1;
    }
    else
    {
        return 0;
    }
}
void met() void teste(struct ChkNum *_this)
{
    NAME = 1;
    NAME = 'x';
    char c = NAME;
    NAME++ _this->isEven(obj->a, struct _this);
}