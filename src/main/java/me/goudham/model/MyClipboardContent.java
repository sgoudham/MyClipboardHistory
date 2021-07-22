package me.goudham.model;

import java.util.Objects;

public class MyClipboardContent<T, U> {
    private T oldContent;
    private U newContent;

    public MyClipboardContent() {

    }

    public MyClipboardContent(T oldContent) {
        this.oldContent = oldContent;
    }

    public void setOldContent(Object oldContent) {
        this.oldContent = (T) oldContent;
    }

    public void setNewContent(Object newContent) {
        this.newContent = (U) newContent;
    }

    public T getOldContent() {
        return oldContent;
    }

    public U getNewContent() {
        return newContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyClipboardContent<?, ?> that = (MyClipboardContent<?, ?>) o;
        return Objects.equals(oldContent, that.oldContent) && Objects.equals(newContent, that.newContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldContent, newContent);
    }
}
