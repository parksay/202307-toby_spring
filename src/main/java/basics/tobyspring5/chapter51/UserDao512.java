package basics.tobyspring5.chapter51;

import java.util.List;

public interface UserDao512 {

    public abstract void add(User512 user);

    public abstract User512 get(String id);

    public abstract void update(User512 user);

    public abstract int getCount();

    public abstract List<User512> getAll();

    public abstract void deleteAll();

}
