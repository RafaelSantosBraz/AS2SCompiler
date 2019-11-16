#include <stdlib.h>
#include <stdio.h>
struct OrderedApp
{
    int tcc;
    int (*aaa)(struct OrderedApp *_this);
};
int aaa(struct OrderedApp *);
void main();
struct OrderedApp *OrderedApp()
{
    struct OrderedApp *_this = malloc(sizeof(struct OrderedApp));
    _this->aaa = aaa;
    return _this;
}
void main()
{
    struct OrdArray *arr;
    arr = OrdArray();
    int mm[10];
    arr->insert(77, mm, arr);
    arr->insert(99, mm, arr);
    int searchKey = 55;
    int p = arr->find(searchKey, mm, arr);
}
int aaa(struct OrderedApp *_this) { return 1; }