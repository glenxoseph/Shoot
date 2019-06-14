package shoot;
import java.awt.image.BufferedImage;
/** bullet */
public class Bullet extends FlyingObject {
	private static BufferedImage image;
	static {
		image = loadImage("bullet.png");
	}
	
	private int speed; // moving speed
	
	/** constructor */
	public Bullet(double d, double e){
		super(18, 21, d, e);
		speed = 4;
	}
	
	/** overwrite step() */
	public void step() {
		y -= speed; // y - (upwards)
	}
	
	/** overwrite getImage() */
	public BufferedImage getImage() {
		if(isLife()) { // alive
			return image; // return image
		}else if(isDead()) { // dead
			state = REMOVE; // change the state to REMOVE
		}
		return null; // return null for dead and removed
	}
	
	/** overwrite outOfBounds() */
	public boolean outOfBounds(){
		return this.y <= -this.height;
		// y of bullet <= negative height of bullet means out of bound
	}
	
}