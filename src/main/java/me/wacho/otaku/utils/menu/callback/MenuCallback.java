package me.wacho.otaku.utils.menu.callback;

import java.io.Serializable;

public interface MenuCallback<T> extends Serializable {
    void callback(T data);
}
