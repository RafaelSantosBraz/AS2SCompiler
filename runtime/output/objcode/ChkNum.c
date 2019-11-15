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
    struct ChkNum *_this = malloc(sizeof(ChkNum));
    _this->isEven = isEven;
    _this->teste = teste;
    return _this;
}
char b = 'b';
int isEven(int x, struct ChkNum *_this)
{
    if (((x % 2) == 0))
    {
        return 1;
    }
    else
    {
        return 0;
    }
}
void met() {}
void teste(struct ChkNum *_this)
{
    b = 1;
    b = 'x';
    char c = b;
    //b++;
    struct ChkNum *obj = ChkNum();
    obj->a = 42;
    printf("%d\n", _this->isEven(obj->a, _this));
}

void main(){
    struct ChkNum* oi = ChkNum();
    oi->teste(oi);
    printf("%c\n", b);
}