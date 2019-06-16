package shoot;

import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class SoundEffects {
 
 /** play music */
 public static void playMusic(String filename) {
  try {
   InputStream music = World.class.getResource(filename + ".wav").openStream();
   AudioStream audios = new AudioStream(music);
   AudioPlayer.player.start(audios);
  }
  catch(Exception e) {
   e.printStackTrace();
   throw new RuntimeException();
  }
 }
 
 /** stop music */
 public static void stopMusic(String filename) {
  try {
   InputStream music = World.class.getResource(filename + ".wav").openStream();
   AudioStream audios = new AudioStream(music);
   AudioPlayer.player.stop(audios);
  }
  catch(Exception e) {
   e.printStackTrace();
   throw new RuntimeException();
  }
 }
}