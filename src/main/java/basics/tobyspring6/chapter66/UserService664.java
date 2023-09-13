package basics.tobyspring6.chapter66;

import basics.tobyspring6.chapter61.User611;

import java.util.List;

public interface UserService664 {

    public abstract void add(User611 user);
    public abstract User611 get(String id);
    public abstract List<User611> getAll();
    public abstract void deleteAll();
    public abstract void update(User611 user);
    public abstract void upgradeLevels();
}
