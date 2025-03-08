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
``` java
    public int delay;
    public boolean enableRootAccess;
    public String ResourceFolder;
    public GameActions gameActions;
```

This variables tells the GameFactory to how to run/execute the game. Variable `delay` tells how much delay should be there between two frames.
> **Note:** The `delay` is delay between the time when one Frame is called and the time when next Frame is called. All execution takes place in this time only. The Timer triggers repeatedly after this delay in milliseconds.

The Variable `enableRootAccess` tells GameFactory whether to allow access to Root window i.e. JFrame. 
The Variable `ResourceFolder` is path to resource folder which helps to store game assets, Debug log files, Animation files.
The Variable `gameActions` is instance of class which implement GameActions interface. <br>
This **GameActions** interface only contains three method.

``` java
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
To create the instance, we will create object of `GameFactory`.

``` java
    public GameFactory(int height, int width,String Title, GameProperty gameProperty)
```

GameFactory has only one constructor. This constructor has 4 parameters. `height`,`width` sets the height and width of JFrame for game.The `Title` sets the title of JFrame for game. The `gameProperty` set properties to game. The `Title` can be changed later using :
``` java
    public void setTitle(String title)
```
To retrive the titlle we can use :
``` java
    public String getTitle()
```
After creating the instance the JFrame will be visible and starts the main loop logic for execution of game.
Now the every frame the `update()` is called except the First Frame.
To change the status of game i.e. to pause or resume the game, we use `status` variable for describing the status of game.This `status` variable is integer.
There are three types of Status defining variables :
``` java
    public static final int RUNNING = 0;
    public static final int PAUSED = 1;
    public static final int EXCEPTIONAL_PAUSE = 2;
```
By default the status is set `RUNNING`. `RUNNING` sets game to run, `PAUSED` sets game to hold by stoping the timer and calls `OnPause()`, whereas `EXCEPTIONAL_PAUSE` sets game to hold but doesn't calls `OnPause()`.
To exit the game or close the game, we use :
``` java
    public void destroy()
```
By calling this method, the games will end by disposing all the nested JFrame then disposing itself. 
Now to access the root window that is main JFrame : 
``` java
    public JFrame getRootWindow()
```
> **Note:** This will only work if `enableRootAccess` is true

Now we can access the root window and change the setting accordingly.
To add Game Objects to window or game we use :
``` java
    public void AddGraphicalObject(GraphicalObject obj)
```
Here, `GraphicalObject` is class which extends JPanel, This class can perform Animation, render, event handling.
We will learn about this class later.
If want to run new `GameActions` but don't want to dispose the main JFrame. we can use :
``` java
    public void ChangeActions(GameActions actions)
```
This will change the GameActions without interrupting the timer and the main JFrame. After changing the GameActions, the next frame will be treated as First frame and calls `start()` then continuous by calling `update()` when timer triggers.
There's another class named `DebugLine`. This is advanced Debug system having features like Inter-class communication, debugging, GUI based interaction. To initialize this class, we have to first call this method :
``` java
 public void RegisterDebugLine()
```
This method creates an instance of `DebugLine` by passing neccessary parameters.
> **Note:** Don't try to create `DebugLine` instance manually.This can cause malfunctioning of DebugLine GUI.

After calling that method, to retrive the instance of `DebugLine` we will use :
```java
    public DebugLine getDebugLine()
```
There's another method in GameFactory :
```java
    public JFrame CreateNewWindow(int ID,int width, int Height, String Title, boolean RegisterForUpdate, boolean StopOnGamePause, Function update, GraphicalObject parentObject)
```
This method creates sub window which can be used as Dialog box, Settings panel. The Variable `ID` is used assign a seperate ID for each Window, `Title` sets the title of JFrame,`RegisterForUpdate` if set true then every frame the update method is called on Function passed as argument i.e. `update`, `StopOnGamePause` tells whether to stop calling `update` if game is paused, `parentObject` is GraphicalObject instance which will be displayed on JFrame. After all of this setting up the new JFrame is returned.
If `RegisterForUpdate` is false then the variables `StopOnGamePause` and `update()` will be ignored by GameFactory.
To dispose the specific sub window safely, we use :
``` java
    public void DisposeWindow(int ID)
```
This closes the sub window having ID equal to `ID`. This ensure that this window will not recive any Updates further.

---

# 2.DebugLine
Documentation for `DebugLine` is provided there [DebugLine](DebugLine.md).
