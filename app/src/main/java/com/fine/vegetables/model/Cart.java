package com.fine.vegetables.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fine.vegetables.Preference;
import com.fine.vegetables.listener.OnCartChangeListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Cart implements Parcelable {

    private static Cart cartCollection;

    private static List<OnCartChangeListener> cartChangeListeners = new ArrayList<>();

    private List<CartItem> cartItems;


    public void registerOnCartChangeListener(OnCartChangeListener onCartChangeListener) {
        cartChangeListeners.add(onCartChangeListener);
    }

    public void unRegisterOnCartChangeListener(OnCartChangeListener onCartChangeListener) {
        cartChangeListeners.remove(onCartChangeListener);
    }


    public static Cart get() {
        if (cartCollection == null) {
            String cartJson = Preference.get().getCartJson(LocalUser.getUser().getUserInfo().getAccount());
            if (!TextUtils.isEmpty(cartJson)) {
                Gson gson = new Gson();
                cartCollection = gson.fromJson(cartJson, Cart.class);
            } else {
                cartCollection = new Cart();
            }
            return cartCollection;
        }
        return cartCollection;
    }

    public void addCommodity(Commodity commodity, int amount) {
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        CartItem cart = new CartItem();
        cart.setId(commodity.getId());
        cart.setCount(amount);
        cart.setLogo(commodity.getLogo());
        cart.setPrice(commodity.getPrice());
        cart.setName(commodity.getName());
        cart.setUnitName(commodity.getUnitName());
        boolean isExist = false;
        for (CartItem item : cartItems) {
            if (item.getId().equals(cart.getId())) {
                isExist = true;
                item.setCount(item.getCount() + cart.getCount());
            }
        }
        if (!isExist) {
            cartItems.add(cart);
        }
        broadCastChange();
        saveToPreference();
    }

    public void broadcastCartChange() {
        broadCastChange();
        saveToPreference();
    }

    public void removeItem(CartItem cart) {
        if (cartItems == null) {
            return;
        }
        for (CartItem item : cartItems) {
            if (item.getId().equals(cart.getId())) {
                cartItems.remove(cart);
                break;
            }

        }
        broadCastChange();
        saveToPreference();
    }

    public void removeItems(List<CartItem> items) {
        if (cartItems == null) {
            return;
        }
        for (CartItem removeItem : items) {
            for (CartItem item : cartItems) {
                if (item.getId().equals(removeItem.getId())) {
                    cartItems.remove(item);
                    break;
                }

            }
        }
        broadCastChange();
        saveToPreference();

    }

    private void broadCastChange() {
        for (OnCartChangeListener onCartChangeListener : cartChangeListeners) {
            onCartChangeListener.onCartChange();
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems == null ? new ArrayList<>() : cartItems;
    }

    public List<CartItem> getSelectedItems() {
        List<CartItem> selectedList = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selectedList.add(item);
            }
        }
        return selectedList;
    }

    public int getCount() {
        int count = 0;
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                count = count + item.getCount();
            }
        }
        return count;
    }

    private void saveToPreference() {
        String cartJson = new Gson().toJson(this);
        Preference.get().setCartJson(LocalUser.getUser().getUserInfo().getAccount(), cartJson);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cartItems);
    }

    public Cart() {
    }

    protected Cart(Parcel in) {
        this.cartItems = in.createTypedArrayList(CartItem.CREATOR);
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        public Cart createFromParcel(Parcel source) {
            return new Cart(source);
        }

        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };
}
