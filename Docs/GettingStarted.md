# Getting Started

The first step for learning this framework is to learn Swing framework and some library included like AnnoLink (Important).This Framework uses Swing to create GUI for games, Gson library for converting neccessary classes,files to json and vice versa, Bluecove for bluetooth communication for Multiplayer.

---

The GameEngine folder under src folder contains :
1. Animation folder
2. Communication folder
3. Sound folder
4. AnnoLink folder
5. Graphics folder
6. DebugLine.java file
7. GameFactory.java file
8. GameActions.java file
9. GameProperty.java file

---

## 1.GameFactory

GameFactory class contains main logic and code to start the game. GameFactory has instance of JFrame, GameProperty, and many more. The **GameProperty** describes how game works or defines the essiential Objects neccesary to create and run the game. GameProperty is a class which contains only variables.
```java
    public int delay;
    public boolean enableRootAccess;
    public String ResourceFolder;
    public GameActions gameActions;
```

This variables tells the GameFactory to how to run/execute the game. Variable `delay` tells how much delay should be there between two frames.
> **Note:** The `delay` is delay between the time when one Frame is called and the time when next Frame is called. All execution takes place in this time only. The Timer triggers repeatedly after delay in milliseconds.

The Variable `enableRootAccess` tells GameFactory whether to allow access to Root window i.e. JFrame. 
The Variable `ResourceFolder` is path to resource folder which helps to store game assets, Debug log files, Animation files.
The Variable `gameActions` is instance of class which implement GameActions interface. <br>
This **GameActions** interface only contains three method.

```java
public interface GameActions {
    void update();

    default void OnPause(){

    }

    void start();
}
```
These three functions are called by GameFactory.
The `update()` is called when timer triggers but except the first frame.When Timer triggers and it is First Frame then `start()` is called.
`start()` can be used to initialize game objects and add to GameFactory.`update()` can be used to change position of object, animate on specific time or anything else.
Whereas `OnPause()` is called when game is on pause.
> **Note:** When game is paused, the timer is also stopped. When game's state is changed from Pause to Running before starting the timer again, once `update()` is called. Then timer calls `update()` as it triggers. Also the method `OnPause()` is set to default, that means there's no problem if you don't implement it, Which makes the code shorter if you don't need this method.

Now we have understand `GameActions`,`GameProperty`.let's now focus on initializing the game.
Initializing the `GameFactory` intializes the JFrame and makes it visible.
