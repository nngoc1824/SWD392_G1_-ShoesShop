package service;

import dao.CartItemDAO;
import entites.CartItem;

import java.util.List;

public class CartItemService {

    private CartItemDAO cartItemDAO;

    public CartItemService() {
        cartItemDAO = new CartItemDAO();
    }

    // Lấy tất cả cart item theo cartId
    public List<CartItem> getCartItemsByCartId(int cartId) throws Exception {
        return cartItemDAO.getCartItemsByCartId(cartId);
    }

    // Thêm mới CartItem
    public void addCartItem(CartItem item) throws Exception {
        cartItemDAO.insertCartItem(item);
    }

    // Cập nhật số lượng CartItem
    public void updateQuantity(int cartItemId, int newQuantity) throws Exception {
        CartItem item = cartItemDAO.getCartItem(cartItemId);
        if (item != null) {
            item.setQuantity(newQuantity);
            cartItemDAO.updateCartItem(item);
        } else {
            throw new Exception("CartItem not found");
        }
    }

    // Xoá một cart item theo cartItemId
    public void deleteCartItem(int cartItemId) throws Exception {
        cartItemDAO.deleteCartItem(cartItemId);
    }

    // Xoá tất cả cart item theo cartId
    public void deleteAllByCartId(int cartId) throws Exception {
        cartItemDAO.deleteCartItemsByCartId(cartId);
    }
}
