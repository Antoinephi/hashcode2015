
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
	private int slot;
	
	/**
	 * ratio = capacity / size
	 */
	private float ratio;
	
	private Pool pool;
	
	private Row row;
	
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
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public void setRow(Row row) {
		this.row = row;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public Row getRow() {
		return this.row;
	}
	
}
