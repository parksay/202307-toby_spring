package basics.tobyspring5.chapter51;

public class User512 {

    String id;
    String name;
    String password;
    Level512 level;
    int login;
    int recommend;

    public User512() {
    }

    public User512(String id, String name, String password, Level512 level, int login, int recommend) {
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

    public Level512 getLevel() {
        return level;
    }

    public void setLevel(Level512 level) {
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

    public void upgradeLevel() {
        Level512 nextLevel = this.level.nextLevel();
        if(nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드 할 수 없습니다" );
        } else {
            this.level = nextLevel;
        }

    }
}



