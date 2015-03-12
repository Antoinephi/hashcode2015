
/**
 * 
 * @author Béni
 *
 *	Class for a Server
 *
 */
public class Server {
	
	
	private int size;
	private int capacity;
	
	/**
	 * ratio = capacity / size
	 */
	private float ratio;
	
	private Pool pool;
	
	public Server(int size, int capacity) {
		this.size = size;
		this.capacity = capacity;
		this.ratio =  this.capacity / this.size;
	}
	
	public void setPool(Pool pool) {
		this.pool = pool;
	}

	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return capacity;
	}

	public float getRatio() {
		return ratio;
	}
	
	public Pool getPool() {
		return this.pool;
	}
	
}
