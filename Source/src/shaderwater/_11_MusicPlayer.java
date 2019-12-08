package shaderwater;

import java.applet.AudioClip;

/*
	Gameloop-Musik Beispiel
	von Marco Block
	Januar 2010
*/
public class _11_MusicPlayer implements Runnable {
    private AudioClip sound_smooth;
    private AudioClip sound_splash;
    
	
    public _11_MusicPlayer(AudioClip smooth, AudioClip splash)
    {
		this.sound_smooth 	= smooth;
		this.sound_splash 	= splash;
    }	

    public void run()
    {
    }

    public void start()
    {
    }

    public void soundStop(AudioClip audio)
    {
        if (audio!=null)
            audio.stop();
    }
    
    public void stopAll() {
		soundStop(sound_smooth);
		soundStop(sound_splash);
    }  

	public void playSoundSmooth() {
		soundStop(sound_smooth);
		soundStop(sound_splash);
		sound_smooth.play();
    }
	
	public void playSoundSmooth_loop() {
		soundStop(sound_smooth);
		soundStop(sound_splash);
		sound_smooth.loop();
    }
	
	
	public void playSoundSplash() {
		soundStop(sound_smooth);
		soundStop(sound_splash);
		sound_splash.play();
    }  	
}
