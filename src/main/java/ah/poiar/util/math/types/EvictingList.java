package ah.poiar.util.math.types;

import lombok.Getter;

import java.util.Collection;
import java.util.LinkedList;

@Getter
public final class EvictingList<T> extends LinkedList<T> {

    private final int maxSize;

    public EvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }

    public EvictingList(final Collection<? extends T> c, final int maxSize) {
        super(c);
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(final T t) {
        if (size() >= getMaxSize()) removeFirst();
        return super.add(t);
    }

    public boolean isFull() {
        return size() >= getMaxSize();
    }
}