package shoot;
import java.awt.image.BufferedImage;
/** hero class */
public class Hero extends FlyingObject {
	private static BufferedImage[] images;
	static {
		images = new BufferedImage[2]; // e images
		images[0] = loadImage("hero0.png");
		images[1] = loadImage("hero1.png");
	}
	
	private int life;       // life
	private int doubleFire; // fire
	/** constructor */
	public Hero(){
		super(157, 156, 242, 400);
		life = 3;
		doubleFire = 0;
	}
	
	/** hero moves with mouse  x, y coordinates of mouse */
	public void moveTo(int x, int y) {
		this.x = x - this.width / 2;  // x of hero = x of mouse - 1 / 2 width of hero
		this.y = y - this.height / 2; // y of hero = y of mouse - 1 / 2 height of hero
	}
	
	/** overwrite step() */
	public void step() {
	}
	
	int index = 0; // index
	/** overwrite getImage() */
	public BufferedImage getImage() { // operates every 10 ms
		if(isLife()) {
			return images[index++ % images.length];
		}
		return null;
		/*                   index=0 
		 * 10M return images[0] index=1
		 * 20M return images[1] index=2
		 * 30M return images[0] index=3
		 * 40M return images[1] index=4
		 * 50M return images[0] index=5
		 */
	}
	
	/** generate bullet object (hero omits bullets) */
	public Bullet[] shoot() {
		double xStep = this.width / 4; // 1/4 width of hero
		int yStep = -40; // constant 20
		if(doubleFire > 0) { // double
			Bullet[] bs = new Bullet[2]; // 2 bullets
			bs[0] = new Bullet(this.x + 1 * xStep, this.y - yStep);
			// x of bullet: x of hero + 1 / 4 width of hero
			// y of bullet: y of hero - constant
			bs[1] = new Bullet(this.x + 3 * xStep - 18, this.y - yStep);
			// x of bullet: x of hero + 3 / 4 width of hero
			// y of bullet: y of hero - constant
			doubleFire -= 2; //fire -2 when double is shot
			return bs;
		}else { // single
			Bullet[] bs = new Bullet[1]; // 1 bullet
			bs[0] = new Bullet(this.x + 2 * xStep - 9, this.y - yStep); 
			// x of bullet: x of hero + 2 / 4 width of hero
			// y of bullet: y of hero - constant
			return bs;
		}
	}
	
	/** hero adds life */
	public void addLife(){
		life++; // life adds 1
	}
	
	/** hero minus life */
	public void minusLife(){
		life--; // life minus 1
	}
	
	/** gets hero life */
	public int getLife(){
		return life; // return life
	}
	
	/** hero adds fire */
	public void addDoubleFire(){
		doubleFire += 40; // fire adds 40
	}
	
}