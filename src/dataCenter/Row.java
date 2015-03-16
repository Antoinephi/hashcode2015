package dataCenter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Béni
 *
 *	class for row
 */
public class Row {
	
	private int index;
	
	private List<Server> servers;
	
	/**
	 * list of group in the row
	 */	
	private boolean slot [];
	
	/**
	 * number of space
	 */
	private int size;
	
	
	public int getIndex() {
		return this.index;
	}
	
	
	public Row(int size, int index) {
		this.index = index;
		this.size = size;
		this.slot = new boolean[this.size];
		for (int i = 0 ; i < this.size ; i++)
			this.slot[i] = true;
		this.servers = new ArrayList<Server>();
	}

	public List<Server> getServers() {
		return this.servers;
	}
	
	public void addUnvailable(int index) {
		this.slot[index] = false;
	}

	public int addServer(Server s) {
		//best case 
		/*for (int i = 0 ; i <= this.size - s.getSize() ; i++ ) {
			boolean ok = true;
			for (int k = 0 ; k < s.getSize() ; k++) {
				if (!this.slot[i+k]) {
					ok = false;
					break;
				}
			}
			if ( ( i+s.getSize() >= this.getSize() || this.slot[i+s.getSize()] ) && ok) {
				for (int j = 0 ; j < s.getSize() ; j++ ) {
					this.slot[i+j] = false;
				}
				this.servers.add(s);
				return i;
			}
		}*/
		//worst case
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
				return i;
			}
		}
		return -1;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getGroupCapacity(Pool group) {
		int capacity = 0;
		for(int i = 0; i < servers.size(); i++) {
			if(servers.get(i).getPool().equals(group)) {
				capacity += servers.get(i).getCapacity();
			}
		}
		
		return capacity;
	}

	public boolean isFree(int j) {
		return this.slot[j];
	}

	public void removeServer(Server server, int slot2) {
		for (int j = 0 ; j < server.getSize() ; j++ ) {
			this.slot[slot2+j] = true;
		}
		
		if(!this.servers.remove(server)) {
			System.err.println("Unable to find server in row (remove)");
		}
	}


	public Server getServerAtSlot(int slot) {
		for (int i = 0 ; i < this.servers.size() ; i++ ) {
			if(this.servers.get(i).getSlot() == slot)
				return this.servers.get(i);
		}
		return null;
	}


	public int addServerAtSlot(Server server, int slot) {
		boolean ok = true;
		for (int j = 0 ; j < server.getSize() ; j++ ) {
			if (!this.slot[slot+j]) {
				ok = false;
				break;
			}
		}
		if (ok) {
			for (int j = 0 ; j < server.getSize() ; j++ ) {
				this.slot[slot+j] = false;
			}
			this.servers.add(server);
			return slot;
		}
		else
			return -1;
	}


	public int getNbFree() {
		int nb = 0;
		for (int i = 0 ; i < this.slot.length ; i++ ) {
			if (this.slot[i]) {
				nb++;
			}
		}
		return nb;
			
	}


	public void display() {
		for (int i = 0 ; i < this.slot.length ; i++ ) {
			if (this.slot[i]) {
				System.out.print("[ ] ");
			}
			else {
				boolean ok = true;
				for (int j = 0 ; j < this.servers.size() ; j++ ) {
					if(i >= servers.get(j).getSlot() && i < servers.get(j).getSlot()+servers.get(j).getSize()) {
						if(!ok) {
							System.err.println("Collision");
							System.exit(0);
						}
						System.out.print("["+servers.get(j).getPool().getIndex()+"] ");
						ok = false;
						//break;
					}
				}
				
				if(ok)
					System.out.print("[X] ");
			}
			
		}
		System.out.println();
		System.out.println("| nb server = "+this.servers.size());
	}
	
}
