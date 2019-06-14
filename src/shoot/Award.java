package shoot;
/** award interface */
public interface Award {
	public int DOUBLE_FIRE = 0; // fire
	public int LIFE = 1;        // life
	/** get award type (0 or 1) */
	public int getAwardType();
}