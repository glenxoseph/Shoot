package shoot;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
/** sky */
public class Sky extends FlyingObject {
	private static BufferedImage image;
	static {
		image = loadImage("background.png");
	}
	
	private int speed; // moving speed
	private int y1;    // y of second image
	/** constructor */
	public Sky(){
		super(World.WIDTH, World.HEIGHT, 0, 0);
		speed = 1;
		y1 = - World.HEIGHT;
	}
	
	/** overwrite step() */
	public void step() {
		y += speed;  // y + (downwards)
		y1 += speed; // y1 + (downwards)
		if(y >= World.HEIGHT) { // y >= window height, cannot get down anymore
			y = - World.HEIGHT;  // set y to negative window height (top)
		}
		if(y1 >= World.HEIGHT) { // y1 >= window height, cannot get down anymore
			y1 = - World.HEIGHT;  // set y1 to negative window height (top)
		} 
	}
	
	/** overwrite getImage() */
	public BufferedImage getImage() {
		return image; // return image
	}
	
	/** paint object, g: painter */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), (int)x, (int)y, null);
		g.drawImage(getImage(), (int)x, (int)y1, null);
	}
	
}