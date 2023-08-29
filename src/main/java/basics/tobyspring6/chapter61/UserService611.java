package basics.tobyspring6.chapter61;


import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class UserService611 {

    private UserDao611 userDao;
    private PlatformTransactionManager transactionManager;
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;


    public void setUserDao(UserDao611 userDao) {
        this.userDao = userDao;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void upgradeLevels() {
        //
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            upgradeLevelsInternal();
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void upgradeLevelsInternal() {
        List<User611> userList = this.userDao.getAll();
        for (User611 user : userList) {
            if (this.canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        }
    }

    public boolean canUpgradeLevel(User611 user) {
        Level611 currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        }
    }


    public void upgradeLevel(User611 user) {
        user.upgradeLevel();
        this.userDao.update(user);
    }


    public void add(User611 user) {
        if(user.getLevel() == null) {
            user.setLevel(Level611.BASIC);
        }
        this.userDao.add(user);
    }

}


//p.400 - chapter6.1.1
//스프링의 '3대 기반 기술'이 있다.
//(IoC/DI) + (서비스 추상화) + (AOP)
//스프링의 기반 기술이라는 말을 좀 더 생각해 보자.
//'스프링'이라는 프레임워크를 만드는 데에 필요했던 기술들을 말한다.
//다시 강조하자면, 스프링을 사용하는 데에 알아야 하는 개념이 아니다.
//스프링이 '제공하는' 3대 기술이 아니다.
//스프링 프레임워크를 만들 때 바탕이 됐던 3대 기술이라는 뜻이다.
//이중에서도 AOP 는 가장 악명이 높다.
//이해하기 어려운 용어와 개념 때문이다.
//무작정 암기하려고만 해서는 안 된다.
//스프링이 왜 AOP 를 도입할 수밖에 없었는가?
//어떤 배경과 흐름 속에서 AOP 가 필요했나?
//이런 질문들에 충분히 이해가 있어야 한다.
//그래야 AOP 의 가치를 이해하고 효과적으로 사용할 수가 있다.
//나아가서 스프링을 더 깊이 이해하고 활용할 수 있게 된다.
//스프링에 쓰인 AOP 기술 중에서 가장 인기 있는 예시는 '선언적 트랜잭션' 기능이다.
//chapter5 에서는 트랜잭션 기술을 도입하고 관리했다.
//chapter6 에서는 트랜잭션 기술에 AOP 를 활용하여 더욱 세련되고 깔끔한 방식으로 개선해 보자.
//
////
//일단 userService.upgradeLevels() 를 보면 또 분리할 수 있는 두 영역이 보인다.
//하나는 비지니스 로직/ 다른 하나는 트랜잭션 관리 로직
//일단 두 종류 코드가 뒤섞여 있으니 분리해 보자.
//그렇게 만든게 UserService611 클래스에서 upgradeLevels() 메소드랑 upgradeLevelsInternal() 메소드.
//일단 분리는 잘 됐긴 했다.
//훨씬 깔끔해보이기는 하다.
////
//그런데 만약 UserService 안에 트랜잭션이 필요한 메소드가 추가된다면?
//upgradeLevels() 메소드를 보면 트랜잭션 로직은 try/catch 블록으로 틀이 짜여 있다.
//클래스 안에 있는 여러 메소드마다 트랜잭션 try/catch 블록이 반복될 것이다.
//내가 당장 떠올린 것은 템플릿/콜백 패턴이었다.
//반복되는 try/catch 블록 틀을 따로 메소드로 빼둔다.
//트랜잭션 try/catch 메소드를 실행할 때는 콜백 함수를 파라미터로 넣어준다.
//메소드는 콜백 함수를 받아서 try 블록 안을 채워준다.
//그러면 트랜잭션 틀 안에 내용물만 바꿔가면서 동적으로 실행할 수 있다.
//그런데 아마 트랜잭션 try/catch 틀이 UserService 클래스 안에서만 반복되지는 않을 것이다.
//BookService / CarService / BankService ....
//Service 클래스마다 트랜잭션 관리 로직이 반복될 것이다.
//그러면 트랜잭션 try/catch 틀을 메소드로 빼지 말고 클래스로 빼면 된다.
//아니 그렇게 하면 해결된다는 게 아니라, 내가 생각한 해결책이 그랬다고.
//근데 스프링에서는 다르게 해결했더라고.
//UserServiceImpl612 로 가보자.




