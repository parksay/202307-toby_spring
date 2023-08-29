package basics.tobyspring6.chapter61;

import java.util.List;

public interface UserDao611 {

    public abstract void add(User611 user);

    public abstract User611 get(String id);

    public abstract void update(User611 user);

    public abstract int getCount();

    public abstract List<User611> getAll();

    public abstract void deleteAll();

}
