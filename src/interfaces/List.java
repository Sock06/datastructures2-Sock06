package interfaces;

import java.util.Iterator;

public interface List<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    E get(int i);

    void add(int i, E e);

    void addFirst(E e);

    void addLast(E e);

    E remove(int i);

    E removeFirst();

    E removeLast();

    Iterator<E> iterator();
}
