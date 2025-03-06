package GameEngine.Sound;


import javax.sound.sampled.Clip;

public class PlayableAudio{

    protected Clip clip;
    long Microsecond = 0;

    public void Play(){
        clip.start();
    }

    public Clip getClip() {
        return clip;
    }

    public void setClip(Clip clip) {
        this.clip = clip;
    }

    public void Pause(){
        Microsecond = clip.getMicrosecondPosition();
        clip.stop();
    }

    public void Restart(){
        clip.start();
        clip.setMicrosecondPosition(Microsecond);
    }

    public void setMicrosecond(long pos){
        clip.setMicrosecondPosition(pos);
    }
    public long getMicrosecond(){
        return clip.getMicrosecondPosition();
    }

    public long getMicrosecondLength(){
        return clip.getMicrosecondLength();
    }

    public void Stop(){
        clip.stop();
    }

    public void Close(){
        clip.close();
    }

}
