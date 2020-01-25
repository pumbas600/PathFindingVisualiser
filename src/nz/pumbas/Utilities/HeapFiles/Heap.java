package nz.pumbas.Utilities.HeapFiles;

import java.lang.reflect.Array;

public class Heap<T extends HeapItem<T>> {

    private T[] items;
    private int currentItemCount;

    public Heap(Class<T> clazz, int maxHeapSize) {

        @SuppressWarnings("unchecked cast")
        final T[] array = (T[]) Array.newInstance(clazz, maxHeapSize);
        items = array;
    }

    public void updateItem(T item) {
        sortUp(item);
        sortDown(item);
    }

    public int size() {
        return currentItemCount;
    }

    public boolean isEmpty() {
        return currentItemCount == 0;
    }

    public boolean contains (T item) {
        return items[item.getItemIndex()].equals(item);
    }

    public void addItem(T item) {
        item.setItemIndex(currentItemCount);
        items[item.getItemIndex()] = item;
        currentItemCount++;
        sortUp(item);
    }

    public T removeFirstItem() {
        T firstItem = items[0];
        currentItemCount--;
        items[0] = items[currentItemCount];
        if (currentItemCount != 0) {
            items[0].setItemIndex(0);
            items[currentItemCount] = null;
            sortDown(items[0]);
        }

        return firstItem;
    }

    public void removeItem(T item) {
        if (items[item.getItemIndex()] == item) {
            currentItemCount--;
            T lastItem = items[currentItemCount];
            items[currentItemCount] = null;

            lastItem.setItemIndex(item.getItemIndex());
            items[lastItem.getItemIndex()] = lastItem;
            sortDown(lastItem);
        }
    }

    private void sortDown(T item) {
        while (true) {
            int leftChildIndex = item.getItemIndex() * 2 + 1;
            int rightChildIndex = item.getItemIndex() * 2 + 2;
            int swapIndex;

            //Swap index holds the index of the child item with the highest priority
            if (leftChildIndex < currentItemCount) {
                swapIndex = leftChildIndex;

                if (rightChildIndex < currentItemCount) {
                    if (items[leftChildIndex].compareTo(items[rightChildIndex]) < 0) {
                        swapIndex = rightChildIndex;
                    }
                }

                //If the highest priority child is of greater priority than its parent, swap them.
                if (item.compareTo(items[swapIndex]) < 0) swapItems(item, items[swapIndex]);
                //Otherwise they're all in the right spot, so break out of the loop
                else return;
            }
            //Break loop
            else return;
        }
    }

    private void sortUp(T item) {
        while (true) {
            //if items index is 0, due to integer division the parent index will also be 0,
            // which will cause the if statement to fail and the while loop to be broken.
            int parentIndex = (item.getItemIndex() - 1) / 2;
            //Swap the items if the item is of higher priority than its parent item
            if (item.compareTo(items[parentIndex]) > 0) swapItems(item, items[parentIndex]);
            else break;
        }
    }


    private void swapItems(T item1, T item2) {
        int newIndex = item1.getItemIndex();
        item1.setItemIndex(item2.getItemIndex());
        item2.setItemIndex(newIndex);

        items[item1.getItemIndex()] = item1;
        items[newIndex] = item2;
    }

}

