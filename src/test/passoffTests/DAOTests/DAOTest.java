package passoffTests.DAOTests;

import Server.DataAccessObjects.DBAuthDAO;
import Server.Models.AuthToken;
import dataAccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class DAOTest {
    AuthToken newAuth = new AuthToken("TestAuth", "TestUser");
    DBAuthDAO authDAO = new DBAuthDAO();
//    @BeforeEach
//    public void reset() {
//        try {
//            authDAO.clear();
//        } catch (DataAccessException e) {
//            return;
//        }
//    }

    @Test
    public void validRegisterService() throws DataAccessException{
        AuthToken expectedResponse = new AuthToken("TestAuth", "TestUser");
        try {
            AuthToken response = authDAO.find(newAuth);
            Assertions.assertEquals(expectedResponse, response);
        } catch (DataAccessException e) {
            throw e;
        }

    }
}
