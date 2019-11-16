#include <stdlib.h>
#include <stdio.h>
struct TowersApp
{
    void (*teste)(int a, struct TowersApp *_this);
};
void main();
void doTowers(int, char, char, char);
void teste(int, struct TowersApp *);
struct TowersApp *TowersApp()
{
    struct TowersApp *_this = malloc(sizeof(struct TowersApp));
    _this->teste = teste;
    return _this;
}
int nDisks = 3;
void teste(int a, struct TowersApp *_this) {}
void doTowers(int topN, char from, char inter, char to)
{
    if ((topN == 1))
    {
        printf("Disk 1 from %c to %c\n", from, to);
    }
    else
    {
        doTowers((topN - 1), from, to, inter);
        printf("Disk %d from %c to %c\n", topN, from, to);
        doTowers((topN - 1), inter, from, to);
    }
}
void main() { doTowers(nDisks, 'A', 'B', 'C'); }