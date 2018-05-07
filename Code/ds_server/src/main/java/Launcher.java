import game.GameEngine;

public class Launcher {

    // Main Programme that start the Launcher
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        GameEngine gameEngine = new GameEngine();
        }
}
