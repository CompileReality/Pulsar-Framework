package GameEngine;

import GameEngine.Graphics.GraphicalObject;
import GameEngine.AnnoLink.Function;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class GameFactory{

    public static final int RUNNING = 0;
    public static final int PAUSED = 1;
    public static final int EXCEPTIONAL_PAUSE = 2;
    public volatile int status = RUNNING;
    public GameProperty property;
    boolean disposing = false;
    boolean rootaccess;
    JFrame window = new JFrame();
    GameActions actions;
    public static int FrameCounter = 0;
    volatile Timer FPS;
    File ResourceFolder;
    boolean isFirstFrame = true;
    String Title;
    public DebugLine debug;
    public boolean debugMode = false;
    public ArrayList<GraphicalObject> obj = new ArrayList<>();
    ArrayList<ArrayList<Object>> Windows = new ArrayList<>();

    public GameFactory(int height, int width,String Title, GameProperty gameProperty){
        window.setSize(width,height);
        window.setTitle(Title);
        window.setResizable(false);
        property = gameProperty;
        rootaccess = gameProperty.enableRootAccess;
        actions = gameProperty.gameActions;
        ResourceFolder = gameProperty.ResourceFolder == null ? null:new File(gameProperty.ResourceFolder);
        this.Title = Title;
        FPS = new Timer(gameProperty.delay,new loop());
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        FPS.start();
    }

    public void Start(){
        actions.start();
    }

    public JFrame getRootWindow(){
        return rootaccess?window:null;
    }

    public void AddGraphicalObject(GraphicalObject obj){
        window.add(obj);
        this.obj.add(obj);
    }

    public void RegisterDebugLine(){
        if (this.debug == null) {
            this.debug = new DebugLine(this,CreateNewWindow(0, 800, 500, "Debug Line", false, false, null, null));
        }
    }
    public DebugLine getDebugLine(){
        return this.debug;
    }
    public JFrame CreateNewWindow(int ID,int width, int Height, String Title, boolean RegisterForUpdate, boolean StopOnGamePause, Function update, GraphicalObject parentObject){
        JFrame frame = new JFrame(Title);
        frame.setSize(width,Height);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if (!(parentObject == null)) {
            frame.add(parentObject);
        }
        if (RegisterForUpdate){
            ArrayList<Object> temp = new ArrayList<>();
            temp.add(0,frame);
            temp.add(1,StopOnGamePause);
            temp.add(2,update);
            temp.add(3,ID+1);
            Windows.add(temp);
        }
        return frame;
    }

    public void ChangeActions(GameActions actions){
        this.actions = actions;
        this.isFirstFrame = true;
    }

    public void DisposeWindow(int ID){
        for (ArrayList<Object> frame:Windows) {
            if ((int)frame.get(3) == ID+1){
                ((JFrame)frame.get(0)).dispose();
            }
        }
    }


    public void destroy() {
        FPS.stop();
        disposing = true;
        for (ArrayList<Object> frame:Windows) {
            ((JFrame)frame.get(0)).dispose();
        }
        window.dispose();
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
        window.setTitle(title);
    }

    class loop implements ActionListener{

        public long RenderSpeed = 0;
        public long Delay = 0;
        boolean check = false;
        boolean check1 = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                RenderSpeed = 0;
                long i = System.currentTimeMillis();
                if (debugMode && !check){
                    for (GraphicalObject object:obj){
                        object.debugMode = true;
                    }
                    check = true;
                    check1 = true;
                }else if (!debugMode && check1){
                    for (GraphicalObject object:obj){
                        object.debugMode = false;
                    }
                    check = false;
                    check1 = false;
                }
                if (isFirstFrame) {
                    Start();
                    isFirstFrame = false;
                } else {
                    Delay = property.delay - RenderSpeed;
                    if (status == GameFactory.RUNNING) {
                        actions.update();
                        for (ArrayList<Object> frame : Windows) {
                            ((Function) frame.get(2)).execute(null);
                            ((JFrame) frame.get(0)).repaint();
                        }
                        FrameCounter++;
                    } else if (status == GameFactory.PAUSED) {
                        boolean permission = true;
                        for (ArrayList<Object> frame : Windows) {
                            if (!((Boolean) frame.get(1))) {
                                permission = false;
                                break;
                            }
                        }
                        if (permission) {
                            FPS.stop();
                            Thread verifying = new Thread(() -> {
                                while (true) {
                                    if (status == GameFactory.RUNNING) {
                                        actions.update();
                                        FrameCounter++;
                                        FPS.restart();
                                        break;
                                    } else if (disposing) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException ex) {
                                        Thread.currentThread().interrupt();
                                    }
                                }
                            });
                            verifying.start();
                            actions.OnPause();
                        } else {
                            while (status != GameFactory.RUNNING) {
                                for (ArrayList<Object> frame : Windows) {
                                    if (!((Boolean) frame.get(1))) {
                                        ((Function) frame.get(2)).execute(null);
                                        ((JFrame) frame.get(0)).repaint();
                                    }
                                }
                            }
                        }
                    } else if (status == GameFactory.EXCEPTIONAL_PAUSE) {
                        FPS.stop();
                    }
                }
                window.repaint();
                RenderSpeed = System.currentTimeMillis() - i;
            }catch (Exception e1){
                if (debug!=null) {
                    debug.LogException(e1.getMessage());
                }
                e1.printStackTrace();
            }

        }
    }
}
