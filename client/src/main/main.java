import ui.Repl;

public class main {
    public static void main(String[] args) {
        String serverUrl = null;
        if (args.length == 1) {
            serverUrl = args[0];
        } else {
            serverUrl = "http://localhost:4567";
        }
        new Repl(serverUrl).run();
    }
}
