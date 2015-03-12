import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Béni
 *
 *	class for row
 */
public class Row {
	
	private List<Server> servers;
	
	/**
	 * list of group in the row
	 */	
	private boolean slot [];
	
	/**
	 * number of space
	 */
	private int size;
	
	public Row(int size) {
		this.size = size;
		this.slot = new boolean[this.size];
		for (int i = 0 ; i < this.size ; i++)
			this.slot[i] = true;
		this.servers = new ArrayList<Server>();
	}
	
	public void addUnvailable(int index) {
		this.slot[index] = false;
	}

	public boolean addServer(Server s) {
		for (int i = 0 ; i <= this.size - s.getSize() ; i++ ) {
			boolean ok = true;
			for (int j = 0 ; j < s.getSize() ; j++ ) {
				if (!this.slot[i+j]) {
					ok = false;
					break;
				}
			}
			if (ok) {
				for (int j = 0 ; j < s.getSize() ; j++ ) {
					this.slot[i+j] = false;
				}
				this.servers.add(s);
				return true;
			}
		}
		return false;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getGroupCapacity(Pool group) {
		int capacity = 0;
		for(int i = 0; i < servers.size(); i++) {
			if(servers.get(i).getPool().equals(group)) {
				capacity += servers.get(i).getSize();
			}
		}
		return capacity;
	}
	
}
