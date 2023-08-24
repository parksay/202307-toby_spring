package basics.tobyspring5.chapter51;

import java.util.List;

public interface UserDao511 {

    public abstract void add(User511 user);

    public abstract User511 get(String id);

    public abstract void update(User511 user);

    public abstract int getCount();

    public abstract List<User511> getAll();

    public abstract void deleteAll();

}
