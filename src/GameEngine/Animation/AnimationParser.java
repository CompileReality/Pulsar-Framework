package GameEngine.Animation;

import GameEngine.GameFactory;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AnimationParser extends PlayableAnimation{
    public AnimationParser(File Jsonanimation) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(Jsonanimation);

        Gson gson = new Gson();
        byte[] text = fileInputStream.readAllBytes();
        ArrayList<Character> newText = new ArrayList<>();

        for (byte num:text){
            newText.add((char)num);
        }

        String Text = String.valueOf(newText);

        super.animation = gson.fromJson(Text,Animation.class);

    }

    public AnimationParser(String path, GameFactory game) throws IOException {
        File Jsonanimation = new File(game.property.ResourceFolder, path);
        FileInputStream fileInputStream = new FileInputStream(Jsonanimation);

        Gson gson = new Gson();
        byte[] text = fileInputStream.readAllBytes();
        ArrayList<Character> newText = new ArrayList<>();

        for (byte num:text){
            newText.add((char)num);
        }

        String Text = String.valueOf(newText);

        super.animation = gson.fromJson(Text,Animation.class);

    }

    public AnimationParser(Animation animation){
        super.animation = animation;
    }
}
