#include <stdlib.h>
#include <stdio.h>
#include"OrdArray.c"

struct OrderedApp
{
};
void main();
struct OrderedApp *OrderedApp()
{
    struct OrderedApp *_this = malloc(sizeof(struct OrderedApp));
    return _this;
}
void main()
{
    int maxSize = 100;
    struct OrdArray *arr;
    arr = OrdArray(maxSize);
    arr->insert(77, arr);
    arr->insert(99, arr);
    int searchKey = 55;
    int p = arr->find(searchKey, arr);
}