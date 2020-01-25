package nz.pumbas.Utilities.HeapFiles;

public interface HeapItem<T> extends Comparable<T> {

    void setItemIndex(int index);
    int getItemIndex();

}
