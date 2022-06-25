package Controller;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * 背景音乐类
 *
 * @author leilei
 * @version 1.0
 */
public class PlayMusic {


    /**
     * 背景音乐播放状态
     */
    private static boolean playFlag = true;
    private static Clip clip;


    public static boolean isPlayFlag() {
        return playFlag;
    }

    public static void setPlayFlag(boolean playFlag) {
        PlayMusic.playFlag = playFlag;
    }

    /**
     * 循环播放音乐
     */
    public static void playMusic() {
        try {
            File musicPath = new File("src\\music\\1.wav");
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭音乐播放
     * */
    public static void close() {
        if (clip.isOpen()) {
            clip.close();
        }
    }
}
