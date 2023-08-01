package basics.tobyspring1.chapter14;

import java.sql.SQLException;

public class DaoFactory141 {

    public UserDao141 userDao() throws ClassNotFoundException, SQLException {
        return new UserDao141(new NaverConnectionMaker141());
    }

}

//p.89
//이렇게 Factory 를 분리했어.
//UserDao 클래스를 오브젝트로 만들 때 어떤 재료들을 넣어줄지?
//어떤 오브젝트들을 생성자에 넘겨줄지?
//그런 걸 정하는 게 Factory 가 됐지.
//이건 오브젝트들 끼리의 관계를 설정하는 것과도 같아.
//이 오브젝트 만들 때는 A오브젝트, B오브젝트 조합해서 만들고,
//B오브젝트 만들 때는 생성자에 F오브젝트 넣어주고,
//D오브젝트는 A오브젝트와 B오브젝트를 가져다 쓰고,
//누가 누구한테 의존할지, 누구는 누구랑 짝지어주고,
//이거랑 저거랑 조합해서 그거 만들고....
//이걸 애플리케이션 전체로 본다면 설계도와 다름 없지.
//그래서 팩토리는 애플리케이션을 동작시키는 오브젝트들의 설계도 역할을 해.

//p.91
//여기서 한 번 더 문제점을 생각해 보자.
//지금은 userDao() 하나만 다루고 있지만 DaoFactory 가 애플리케이션 전체에 걸쳐서 여러 Dao 들을 생성해주는 역할을 해.
//그래서 userDao / AccountDao / messageDao ....  계속 추가됐어.
//그러면 아래 같은 모습이 되겠지.
//public class DaoFactory {
//    public UserDao userDao() throws ClassNotFoundException, SQLException {
//        return new UserDao(new NaverConnectionMaker());
//    }
//    public AccountDao userDao() throws ClassNotFoundException, SQLException {
//        return new AccountDao(new NaverConnectionMaker());
//    }
//    public MessageDao userDao() throws ClassNotFoundException, SQLException {
//        return new MessageDao(new NaverConnectionMaker());
//    }
//}
//보면 딱 보이겠지만 NaverConnectionMaker(); 부분이 중복되지.
//CoonectionMaker 를 Naver 가 아니라 Daum 으로 바꾸려고 한다면...?
//NaverConnectionMaker 일일이 찾아서 DaumConnectionMaker 로 바꿔 줘야 해.
//우리 맨 처음 UserDao 에서 '메소드 추출'이라는 리팩토링 기법 배웠지.
//한 클래스 안에서 반복되는 코드는 별도 메소드로 따로 빼서 한 번에 관리한다고.
//그럼 ConnectionMaker 만드는 부분을 메소드 추출해 보자.
//그렇게 만든 게  DaoFactory142.java