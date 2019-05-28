package com.fine.vegetables.listener;

import com.fine.vegetables.model.CartItem;

public interface OnSelectCartListener {
    void select(CartItem cartItem);

    void unSelect(CartItem cartItem);
}
