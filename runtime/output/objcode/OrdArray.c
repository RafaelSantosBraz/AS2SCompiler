#include <stdlib.h>
#include <stdio.h>
struct OrdArray
{
    int nElems;
    int (*find)(int searchKey, int a[], struct OrdArray *_this);
    void (*insert)(int value, int a[], struct OrdArray *_this);
    void (*display)(int a[], struct OrdArray *_this);
};
void teste();
void display(int[], struct OrdArray *);
void insert(int, int[], struct OrdArray *);
int find(int, int[], struct OrdArray *);
struct OrdArray *OrdArray()
{
    struct OrdArray *_this = malloc(sizeof(struct OrdArray));
    _this->nElems = 0;
    _this->find = find;
    _this->insert = insert;
    _this->display = display;
    return _this;
}
int find(int searchKey, int a[], struct OrdArray *_this)
{
    int lowerBound = 0;
    int upperBound = (_this->nElems - 1);
    int curIn;
    while (1)
    {
        curIn = ((lowerBound + upperBound) / 2);
        if ((a[curIn] == searchKey))
        {
            return curIn;
        }
        else
        {
            if ((lowerBound > upperBound))
            {
                return _this->nElems;
            }
            else
            {
                if ((a[curIn] < searchKey))
                {
                    lowerBound = (curIn + 1);
                }
                else
                {
                    upperBound = (curIn - 1);
                }
            }
        }
    }
}
void insert(int value, int a[], struct OrdArray *_this)
{
    int j;
    for (j = 0; ((j < _this->nElems)); j++)
    {
        if ((a[j] > value))
        {
            break;
        }
    }
    int k;
    for (k = _this->nElems; ((k > j)); k--)
    {
        a[k] = a[(k - 1)];
    }
    a[j] = value;
    _this->nElems++;
}
void display(int a[], struct OrdArray *_this)
{
    int j;
    for (j = 0; ((j < _this->nElems)); j++)
    {
        printf("%d ", a[j]);
    }
    printf("\n");
}
void teste() {}