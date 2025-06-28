package service;

import dao.CartDAO;
import entites.Cart;


import java.util.List;

public class CartService {
    private CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    public List<Cart> getCartsByUserId(int userId) {
        return cartDAO.getAllCarts(userId);
    }

    public Cart getCartById(int cartId) {
        return cartDAO.getCartById(cartId);
    }

    public boolean createCart(Cart cart) {
        return cartDAO.insertCart(cart);
    }

    public boolean updateCart(Cart cart) {
        return cartDAO.updateCart(cart);
    }

    public boolean deleteCart(int cartId) {
        return cartDAO.deleteCart(cartId);
    }
}
