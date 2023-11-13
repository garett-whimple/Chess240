package Server;

import dataAccess.DataAccessException;
import spark.Spark;

import static spark.Spark.before;
import static spark.Spark.halt;

public class Server {
    private static HtmlHandler instance;

    public static HtmlHandler getInstance() throws DataAccessException {
        if (instance == null) {
            instance = new HtmlHandler();
        }
        return instance;
    }
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Spark.port(port);

            Spark.staticFiles.location("/web");

            createRoutes();

            Spark.awaitInitialization();
            System.out.println("Listening on port " + port);
        } catch(ArrayIndexOutOfBoundsException | NumberFormatException ex) {
            System.err.println("Specify the port number as a command line parameter");
        }
    }

    private static void createRoutes() {
        Spark.delete("/db", (req, res) -> getInstance().clearHandler(req, res));
        Spark.post("/user", (req, res) -> getInstance().registerUserHandler(req, res));
        Spark.post("/session", (req, res) -> getInstance().loginHandler(req, res));
        Spark.delete("/session", (req, res) -> getInstance().logoutHandler(req, res));
        Spark.get("/game", (req, res) -> getInstance().listGameHandler(req, res));
        Spark.post("/game", (req, res) -> getInstance().createGameHandler(req, res));
        Spark.put("/game", (req, res) -> getInstance().joinGameHandler(req, res));
    }
}
