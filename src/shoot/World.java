package shoot;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/** entire game world */
public class World extends JPanel {
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	static {
		start = loadImage("start.png");
		pause = loadImage("pause.png");
		gameover = loadImage("gameover.png");
	}
	public static String startMusicName = "start";
	public static String playingMusicName = "shoot!";
	public static String gameOverMusicName = "gameover";
	public static String hitEnemyEffect = "hit1";
	public static String hitStarEffect = "hit2";
	
	static Music startMusic = new Music(startMusicName);
	static Music playingMusic = new Music(playingMusicName);
	static Music gameOverMusic = new Music(gameOverMusicName);
	
	public static final int WIDTH = 640;  // width of window
	public static final int HEIGHT = 1136; // height of window
	
	private int state = START;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	
	private Sky sky = new Sky();    // sky object
	private Hero hero = new Hero(); // hero object
	private FlyingObject[] enemies = {}; // enemies array (airplane, bigAirplane, and star)
	private Bullet[] bullets = {}; // bullets object
	
	/** generate enemies objects (airplane, bigAirplane, and star) */
	public FlyingObject nextOne() {
		Random rand = new Random(); // random number object
		int type = rand.nextInt(20); // 0~19
		if(type < 4) { // 0~3, return star object
			return new Star();
		}else if(type < 12) { // 4~11, return airplane object
			return new Airplane();
		}else { // 12~19, return bigAirplane object
			return new BigAirplane();
		}
	}
	
	int enterIndex = 0; // counting enemies appearance
	/** enemies enter (airplane, bigAirplane, and star) */
	public void enterAction() { // run every 10 ms
		enterIndex++; // add one every 10 ms
		if(enterIndex % 40 == 0) { // run every 400 ms
			FlyingObject flyobj = nextOne(); // get enemy object
			enemies = Arrays.copyOf(enemies, enemies.length + 1); // provide array with a larger space
			enemies[enemies.length - 1] = flyobj; // put the enemy object to the last position of enemies
		}
	}
	
	int shootIndex = 0; // counting bullets appearance
	/** bullets enter (emitted by hero) */
	public void shootAction() { // run every 10 ms
		shootIndex++; // add one every 10 ms
		if(shootIndex % 50 == 0) { // run every 500 ms
			Bullet[] bs = hero.shoot(); // get bullet object
			bullets = Arrays.copyOf(bullets, bullets.length + bs.length); 
			// provide array with a larger space according to the number of elements of bullets
			System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length); // add additional bs elements into bullets
		}
	}
	
	/** flyingObjects move */
	public void stepAction() { // run every 10 ms
		sky.step(); // sky moves
		for(int i = 0; i < enemies.length; i++) { // traversing enemies
			enemies[i].step(); // enemies move
		}
		for(int i = 0; i < bullets.length; i++) { // traversing bullets
			bullets[i].step(); // bullets move
		}
	}
	
	/** delete flyingObjects out of bound */
	public void outOfBoundsAction(){ // run every 10 ms
		int index = 0; // 1) index of enemies array out of bound 2) number of enemies out of bound
		FlyingObject[] enemyLives = new FlyingObject[enemies.length]; 
		// enemies array in bound (initial length corresponding with enemies)
		for(int i = 0; i < enemies.length; i++){ // traversing enemies
			FlyingObject f = enemies[i]; // get every enemies
			if(!f.outOfBounds()){ // in bound
				enemyLives[index] = f; // add the in bound enemies into the in bound enemy array from the first element)
				index++; // 1) in bound enemies array index + 1 2) in bound enemy number + 1
			}
		}
		enemies = Arrays.copyOf(enemyLives,index); 
		// copy in bound enemies array into enemies, with length index number of in bound enemies
		
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i = 0; i < bullets.length; i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds()){
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives,index);
		
	}
	
	int score = 0; // score
	
	/** hero hits enemies */
	public void heroHitEnemiesAction() {
		for (int i = 0; i < enemies.length; i++) {
			FlyingObject f = enemies[i]; // get every enemy
			if (hero.isLife() && f.isLife() && f.hit(hero)) {
				hero.minusLife();
				SoundEffects.playMusic(hitEnemyEffect);
				if (hero.getLife() < 1) {
					state = GAME_OVER;
					playingMusic.stop();
					gameOverMusic.play();
				}
				f.goDead();
			}
		}
	}
	
	/** bullets hit enemies */
	public void bulletBangAction(){ // run every 10 ms
		for(int i = 0; i < bullets.length; i++){ // traverse all bullets
			Bullet b = bullets[i]; // get every bullet
			for(int j = 0; j < enemies.length; j++){ // traverse all enemies
				FlyingObject f = enemies[j]; // get every enemy
				if(b.isLife() && f.isLife() && f.hit(b)){ // hit
					b.goDead(); // bullet dies
					f.goDead(); // enemy dies

					if(f instanceof Enemy){ // if enemy scores
						Enemy e = (Enemy)f; // type casting to score interface
						score += e.getScore(); // player scores
						SoundEffects.playMusic(hitEnemyEffect);
 					}
					if(f instanceof Award){ // if award
						Award a = (Award)f; // type casting to award interface
						int type = a.getAwardType(); // get award type
						SoundEffects.playMusic(hitStarEffect);
						switch(type){ // get different award based on different award types
						case Award.DOUBLE_FIRE:   // if award type is fire value
							hero.addDoubleFire(); // hero adds fire
							break;
						case Award.LIFE:    // if award type is life
							hero.addLife(); // hero adds life
							break;
						}
					}
				}
			}
		}
	}
	
	/** program action */
	public void action() {
		// lister object
		MouseAdapter l = new MouseAdapter(){
			/** overwrite mouseMoved() */
			@Override
			public void mouseMoved(MouseEvent e) {
				// hero moves with mouse only when RUNNING
				if(state == RUNNING){
					// get new position of mouse
					int x = e.getX();
					int y = e.getY();
					// send the position of mouse to hero
					hero.moveTo(x, y);
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(state == START){ // START, click to RUNNING
					state = RUNNING;
					startMusic.stop();
					playingMusic.loop();
				} else if(state == PAUSE){ // PAUSE, click to RUNNING
					state = RUNNING;
					playingMusic.resume();
				} else if(state == RUNNING){ // RUNNING, click to PAUSE
					state = PAUSE;
					playingMusic.stop();
				} else if(state == GAME_OVER){ // GAME_OVER, click to START 
					state = START;
					// from GAME_OVER to START，initialize data again
					enemies = new FlyingObject[0];
					bullets = new Bullet[0];
					hero = new Hero();
				}
			}


			@Override
			public void mouseExited(MouseEvent e) {
				if(state == RUNNING){
					// only if RUNNING, PAUSE when mouse off bound
					state = PAUSE;
					playingMusic.stop();
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE){
					state = RUNNING;
					playingMusic.resume();
				}
			}
		};
		this.addMouseListener(l); // deal with event of mouse operation
		this.addMouseMotionListener(l); // deal with event of mouse clicks
		
		Timer timer = new Timer(); // timer object
		int interval = 10; // period, unit ms
		timer.schedule(new TimerTask() {
			public void run() { // what timer does, run every 10 ms
				if (state == RUNNING) {
					enterAction(); // enemies (airplanes, bigAirplanes, and stars) enter
					shootAction(); // bullets enter (omitted by hero)
					stepAction();  // flyingObjects move
					outOfBoundsAction(); // delete flyingObjects off the bound (enemies and bullets)
					heroHitEnemiesAction();
					bulletBangAction();  // collision of bullets and enemies
				}
				repaint();     // reuse paint() method
			}
		},interval,interval); // schedule
	}
	
	/** overwrite paint() method */
	public void paint(Graphics g) {
		sky.paintObject(g);  // paint sky
		hero.paintObject(g); // paint hero
		for(int i = 0; i < enemies.length; i++) { // traverse all enemies
			enemies[i].paintObject(g); // paint enemies
		}
		for(int i = 0; i < bullets.length; i++) { // traverse all bullets
			bullets[i].paintObject(g); // paint bullets
		}
		g.setColor(Color.WHITE);
		Font font = new Font(Font.SANS_SERIF,Font.BOLD,14);
		g.setFont(font);
		g.drawString("SCORE: " + score, 10, 25); // paint score
		g.drawString("LIFE: " + hero.getLife(), 10, 45); // paint lives
		if(state == START){
			g.drawImage(start, 0, 0, null);
		}else if(state == PAUSE){
			g.drawImage(pause, 0, 0, null);
		}else if(state == GAME_OVER){
			g.drawImage(gameover, 0, 0, null);
		}
	}
	
	/** load image */
	public static BufferedImage loadImage(String fileName) {
		try {
			BufferedImage img = ImageIO.read(World.class.getResource(fileName)); // load image from the same package
			return img;
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static void main(String[] args) {
		startMusic.play();
		JFrame frame = new JFrame();
		World world = new World();
		frame.add(world);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null); 
		frame.setVisible(true); // 1) set window visible 2) paint() asap
		
		world.action(); // start the program
	}
}