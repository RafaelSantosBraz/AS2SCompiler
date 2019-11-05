/* Bubble Sort 
*  adapted from (C Completo e Total 3ed)
*/

void bubble(char item[], int count)
{
    int a;
    int b;
    char t;
    for (a = 1; a < count; ++a)
    {
        for (b = count - 1; b >= a; --b)
        {
            if (item[b - 1] > item[b])
            {
                t = item[b - 1];
                item[b - 1] = item[b];
                item[b] = t;
            }
        }
    }
}