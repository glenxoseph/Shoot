package shoot;
import java.util.Random;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics;
/** flying object */
public abstract class FlyingObject {
	public static final int LIFE = 0;   // alive
	public static final int DEAD = 1;   // dead (not removed yet)
	public static final int REMOVE = 2; // removed
	protected int state = LIFE; // current state (alive as default)
	
	protected double width;
	protected double height;
	protected double x;
	protected double y;
	/** for hero, sky and bullet */
	public FlyingObject(int width, int height, double d, double e){
		this.width = width;
		this.height = height;
		this.x = d;
		this.y = e;
	}
	/** for airplane, bigAirplane and star */
	public FlyingObject(double width, double height){
		this.width = width;
		this.height = height;
		Random rand = new Random(); // random object
		x = rand.nextInt(World.WIDTH - (int)this.width);
		// x: 0 to random number within (width of window - width of enemy)
		y = - this.height; // y: negative height of enemy
	}
	
	/** load image */
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(FlyingObject.class.getResource(fileName)); // load image from the same package
			return img;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	/** flying object moves */
	public abstract void step();
	
	/** get image */
	public abstract BufferedImage getImage();
	
	/** check if is alive */
	public boolean isLife() {
		return state == LIFE; // current LIFE for alive，return true
	}
	/** check if is dead */
	public boolean isDead() {
		return state == DEAD; // current DEAD for dead, return true
	}
	/** check if is removed */
	public boolean isRemove() {
		return state == REMOVE; // current REMOVE for removed, return true
	}
	
	/** paint the object, g: painter */
	public void paintObject(Graphics g) {
		g.drawImage(getImage(), (int)x, (int)y, null);
	}
	
	/** check if off bound */
	public boolean outOfBounds(){
		return this.y >= World.HEIGHT; // y of enemy >= height of window，off the bound
	}
	
	/** check if hit, this: enemy, bullet */
	public boolean hit(Bullet bullet){
		double x1 = this.x - bullet.width;  // x1: x of enemy - width of bullet
		double x2 = this.x + this.width;   // x2: x of enemy + width of enemy
		double y1 = this.y - bullet.height; // y1: y of enemy - height of bullet
		double y2 = this.y + this.height;  // y1: y of enemy + height of enemy
		double x = bullet.x;              // x: x of bullet
		double y = bullet.y;              // y: y of bullet
		
		return x >= x1 && x <= x2 
			   && 
			   y >= y1 && y <= y2; // x between x1 and x2, and y between y1 and y2
	}
	
	/** check if hit hero, this: enemy, hero */
	public boolean hit(Hero hero){
		double xh1 = hero.x;
		double xh2 = hero.x + hero.width;
		double yh1 = hero.y;
		double yh2 = hero.y + hero.height;
		double xf1 = this.x;
		double xf2 = this.x + this.width;
		double yf1 = this.y;
		double yf2 = this.y + this.height;
		boolean ifHit = xh1 < xf2 && xf1 < xh2 && yh1 < yf2 && yf1 < yh2;
		return ifHit;
	}
	
	/** kill flying object */
	public void goDead(){
		state = DEAD; // change state to DEAD
	}
	
}