import view.MainFrame;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        new MainFrame(1000,760);
        MusicPlayer.playMusic("./src/汐.wav");
    }
    public static class MusicPlayer  {
        static Clip music = null; //声明Clip接口
        static File sourceFile = null; //声明文件变量

        /**
         * 音乐播放方法
         */
        //背景音乐
        public static void playMusic(String path){
            try {
                music = AudioSystem.getClip(); // 获取可用于播放音频文件或音频流的数据流
                sourceFile = new File(path);//获取文件
                AudioInputStream ais = AudioSystem.getAudioInputStream(sourceFile);//获得指示格式的音频输入流
                music.open(ais); //打开数据流
                music.loop(100);    //开始播放音乐
            } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
