package basics.tobyspring5.chapter51;

public class User511 {

    String id;
    String name;
    String password;
    Level511 level;
    int login;
    int recommend;

    public User511() {
    }

    public User511(String id, String name, String password, Level511 level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Level511 getLevel() {
        return level;
    }

    public void setLevel(Level511 level) {
        this.level = level;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

}


// Level 을 그냥 User 클래스에 코드값으로 관리하면 안 됨?
//
//class User {
//    private static final int BASIC = 1;
//    private static final int SILVER = 2;
//    private static final int GOLD = 3;
//
//    int level;
//    ...
//    ...
//}
//
//
//나도 처음에 이렇게 생각했는데 이게 위험할 수 있음.
//user1.setLevel(1000);
//이런 거 넣으면 어떡할래...?
//setter 에다가 validation 코드까지 넣을래...?
//넣을 때는 그렇게 한다 쳐도, 뺄 때는 그럼 어떡할 건데...?
//이런 저런 데이터 정합성 문제가 생길 수 있음.
//그래서 그냥 Enum 이늄이라고 이럴 때 쓰라고 있는 타입.
//상수 관리하는 타입이 있음.
//그걸로 선언해버리기.
//그게 지금 이제 Level511.class 인데.
//사용법은 나중에 더 자세히 공부해 보기.

