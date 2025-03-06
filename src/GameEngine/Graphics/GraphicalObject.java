package GameEngine.Graphics;

import GameEngine.AnnoLink.Function;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicalObject extends JPanel {

    Function Graphicalproperty;
    public Function Animator;
    public boolean debugMode = false;
    public ArrayList<String> file = new ArrayList<>();

    int[] pos = new int[2];
    Events events;

    public GraphicalObject(int x,int y){
        pos[0] = x;
        pos[1] = y;
        this.setLocation(x,y);
    }

    public GraphicalObject(byte[] bytes){
        setBytes(bytes);
        ChangePos(pos);
    }

    public void ChangePos(int[] pos){
        this.setLocation(pos[0],pos[1]);
        this.pos = pos;
    }

    public Function getGraphicalproperty() {
        return Graphicalproperty;
    }

    public void ChangeRenderingProperty(Function graphicalproperty) {
        this.Graphicalproperty = graphicalproperty;
    }


    public void setSize(int[] size){
        this.setSize(size[0],size[1]);
    }

    public byte[] toBytes(){
        StringBuilder data = new StringBuilder();

        Serializable json = new Serializable();
        json.Animator = this.Animator;
        json.pos = this.pos;
        json.file = this.file;
        json.Graphicalproperty = this.Graphicalproperty;

        data.append(new Gson().toJson(json));

        return data.toString().getBytes();
    }

    public void setBytes(byte[] data){
        Gson json = new Gson();
        Serializable copy = json.fromJson(new String(data),Serializable.class);
        this.file = copy.file;
        this.pos = copy.pos;
        this.Animator = copy.Animator;
        this.Graphicalproperty = copy.Graphicalproperty;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (Graphicalproperty != null) {
            Graphicalproperty.execute(new Object[]{g});
        }
        if(Animator != null){
            Animator.execute(new Object[]{g,file});
        }
        if (debugMode){
            g.drawRect(1,1,getWidth()-2,getHeight()-2);
        }
    }

    public void AddListener(Events event){
        events = event;
        this.addKeyListener(event);
        this.addMouseListener(event);
        this.addMouseMotionListener(event);
        this.addMouseWheelListener(event);
    }

    public Events getEventsAttached(){
        return events;
    }
}
