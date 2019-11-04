/* Insertion Sort 
*  adapted from (C Completo e Total 3ed)
*/

void insert(char item[], int count)
{
    int a;
    int b;
    char t;
    for (a = 1; a < count; ++a)
    {
        t = item[a];
        for (b = a - 1; b >= 0 && t < item[b]; b--)
        {
            item[b + 1] = item[b];
        }
        item[b + 1] = t;
    }
}