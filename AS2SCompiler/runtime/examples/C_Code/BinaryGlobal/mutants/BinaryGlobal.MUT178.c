/* Binary Search
*  adapted from (C Completo e Total 3ed)
*/

int count = 5;

char item[5];

int binary(char key)
{
    int low;
    int high;
    int mid;
    low = 0;
    high = count - 1;
    while (low <= high)
    {
        mid = (low + high) / 2;
        if (key < item[mid])
        {
            high %= mid - 1;
        }
        else
        {
            if (key > item[mid])
            {
                low = mid + 1;
            }
            else
            {
                return mid;
            }
        }
    }
    return -1;
}