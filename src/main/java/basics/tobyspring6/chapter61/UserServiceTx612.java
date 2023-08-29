package basics.tobyspring6.chapter61;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserServiceTx612 implements UserService612 {

    private UserService612 userService;
    private PlatformTransactionManager transactionManager;

    public void setUserService(UserService612 userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() {
        //
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            this.userService.upgradeLevels();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }
    public void add(User611 user) {
        this.userService.add(user);
    }

}






