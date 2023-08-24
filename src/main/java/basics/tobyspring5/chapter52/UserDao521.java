package basics.tobyspring5.chapter52;

import java.util.List;

public interface UserDao521 {

    public abstract void add(User521 user);

    public abstract User521 get(String id);

    public abstract void update(User521 user);

    public abstract int getCount();

    public abstract List<User521> getAll();

    public abstract void deleteAll();

}
