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
public class ConfirmOrderService {
    private CartDao cartDao;
    
    public ConfirmOrderService() {
        this.cartDao = new CartDao(); // Khởi tạo DAO
    }
    
    public List<Cart> GetCartByUid(){
        int uid = 2;
        return cartDao.getCartByUid(uid);
    }
}
