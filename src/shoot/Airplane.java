package shoot;
import java.awt.image.BufferedImage;
/** airplane: a flyingObject and an enemy, it can score */
public class Airplane extends FlyingObject implements Enemy {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5]; // 5 images
		for(int i = 0; i < images.length; i++) {
			images[i] = loadImage("airplane" + i + ".png");
		}
	}
	
	private double speed; // speed of moving
	/** constructor */
	public Airplane(){
		super(106, 111);
		speed = Math.random() + 2.0;
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
		/*
		 *                   index=1
		 * 10M img=images[1] index=2                 return images[1]
		 * 20M img=images[2] index=3                 return images[2]
		 * 30M img=images[3] index=4                 return images[3]
		 * 40M img=images[4] index=5(REMOVE) 		return images[4]
		 * 50M return null
		 */
	}
	
	/** overwrite getScore() */
	public int getScore(){
		return 1; // hit airplane, player gets 1 point
	}
	
}