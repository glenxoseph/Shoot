package shoot;
import java.awt.image.BufferedImage;
/** bigAirplane: a flyingObject and an enemy, it can score */
public class BigAirplane extends FlyingObject implements Enemy {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5]; // 5 images
		for(int i = 0; i < images.length; i++) {
			images[i] = loadImage("bigplane" + i + ".png");
		}
	}
	
	private double speed; // speed of moving
	/** constructor */
	public BigAirplane(){
		super(106, 208);
		speed = Math.random() + 3.0;
	}
	
	/** overwrite step() */
	public void step() {
		y += speed; // y + (downwards)
	}
	
	int index = 1; // index
	/** overwrite getImage() to get images */
	public BufferedImage getImage() { // operates every 10 ms
		if(isLife()) { // alive
			return images[0]; // return images[0]
		}else if(isDead()) { // dead
			BufferedImage img = images[index++]; // start from the second image
			if(index==images.length) { // if at the last image
				state = REMOVE; // change the state to REMOVE
			}
			return img; // return explosion image
		}
		return null; // return null when state removed
	}
	
	/** overwrite getScore() */
	public int getScore(){
		return 3; // hit bigAirplane, player gets 3 points
	}
	
}