package service;


import dao.AccountDao;
import java.util.Optional;
import model.User;

public class AccountService {

    private AccountDao accountDao;

    public AccountService() {
        this.accountDao = new AccountDao();
    }

    /**
     * Kiểm tra đăng nhập bằng cách so sánh mật khẩu.
     */
    public Optional<User> login(String email, String password) {
        Optional<User> userOpt = accountDao.getAccountByEmail(email);

        // Nếu tài khoản tồn tại và mật khẩu nhập vào trùng khớp với mật khẩu trong DB
        if (userOpt.isPresent() && userOpt.get().getPass().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }
}