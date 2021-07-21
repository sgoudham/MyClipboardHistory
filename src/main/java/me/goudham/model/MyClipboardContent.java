package me.goudham.model;

import java.util.Objects;

public class MyClipboardContent<T> {
    private T content;

    public MyClipboardContent(T content) {
        this.content = content;
    }

    public void setContent(Object content) {
        this.content = (T) content;
    }

    public T getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClipboardContent<?> that = (MyClipboardContent<?>) o;
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
