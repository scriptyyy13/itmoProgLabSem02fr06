public class Main {
    public static void main() {
        ClientCommandManager clientCommandManager = new ClientCommandManager(new Reader());
        clientCommandManager.start();
    }

}