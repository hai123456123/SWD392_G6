/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.CartDao;
import java.util.List;
import model.Cart;

/**
 *
 * @author ADMIN
 */
public class CartService {
    
    private CartDao cartDao;
    
    public CartService() {
        this.cartDao = new CartDao(); // Khởi tạo DAO
    }
    
    public List<Cart> GetCartByUid(){
        int uid = 2;
        return cartDao.getCartByUid(uid);
    }
    
    public long calculateTotalCartPrice() {
        int uid = 2;
        return cartDao.calculateTotalCartPrice(uid);
    }
    
}
