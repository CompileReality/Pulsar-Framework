package GameEngine.Animation;

import GameEngine.GameFactory;
import GameEngine.Graphics.GraphicalObject;
import GameEngine.AnnoLink.Executable;
import GameEngine.AnnoLink.Function;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlayableAnimation {
    protected Animation animation;
    GraphicalObject graphicalObject;
    ArrayList<String> ImgFiles = new ArrayList<>();
    int animatorNum = 0;

    public void Play() throws ClassNotFoundException {
        graphicalObject = (GraphicalObject) Class.forName(animation.Class).cast(GraphicalObject.class);
        for (String file:animation.SlideImages) {
            ImgFiles.add(new File(file).getName());
        }
        graphicalObject.Animator = new Function("Animator",this);
        graphicalObject.file = ImgFiles;
    }

    @Executable(key = "Animator")
    public Graphics Animate(Graphics g,ArrayList<String> ImgFiles) throws IOException {
        if (GameFactory.FrameCounter % animation.FrameDelay == 0){
            BufferedImage img = ImageIO.read(new File(ImgFiles.get(animatorNum)));
            g.drawImage(img,0,0,null);
            if (animatorNum == ImgFiles.size()-1) {
                animatorNum++;
            }else {
                animatorNum = 0;
            }
        }

        return g;
    }

    public void Stop(){
        graphicalObject.Animator = null;
        animatorNum = 0;
    }

    public void Pause(){
        graphicalObject.Animator = null;
    }

    public void Resume(){
        graphicalObject.Animator = new Function("Animator",this);
        graphicalObject.file = ImgFiles;
    }


}
