package GameEngine;

public interface GameActions {
    void update();

    default void OnPause(){

    }

    void start();
}
