package repositories;

import java.util.List;

public interface Repository<T> {

    //create
    void insert(T o);

    //read
    T get(int id);
    List<T> getAll();

    //update
    void update(T o);

    //delete
    void delete(T o);


}
