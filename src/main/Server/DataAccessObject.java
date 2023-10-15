package Server;

import dataAccess.DataAccessException;

import java.util.Collection;

public interface DataAccessObject {
    int find(int Id) throws DataAccessException;
    Collection<Integer> findAll() throws DataAccessException;
    void remove(int Id) throws DataAccessException;
    void clear() throws DataAccessException;
}
