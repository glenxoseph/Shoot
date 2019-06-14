package shoot;
import java.util.Random;
import java.awt.image.BufferedImage;
/** star: flying object and award */
public class Star extends FlyingObject implements Award  {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[5]; // 5 images
		for(int i = 0; i < images.length; i++) {
			images[i] = loadImage("star" + i + ".png");
		}
	}
	
	private double xSpeed; // x coordinate moving speed
	private double ySpeed; // y coordinate moving speed
	private int awardType; // award type
	/** constructor */
	public Star(){
		super(108, 59);
		xSpeed = Math.random() + 1.0;
		ySpeed = Math.random() + 1.0;
		Random rand = new Random();
		awardType = rand.nextInt(2); // random number between 0 and 1
	}
	
	/** overwrite step() */
	public void step() {
		x += xSpeed; // x + (left or right)
		y += ySpeed; //y + (downwards)
		if(x <= 0 || x >= World.WIDTH - this.width) {
			// if x <= 0 or x >= window width - star width, it's on the edge, switch direction
			xSpeed *= -1.0; // - to +, + to - (change direction)
		}
	}
	
	int index = 1; // index
	/** overwrite getImage() */
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
	
	/** overwrite getAwardType() */
	public int getAwardType(){
		return awardType; // return awardType
	}
	
}