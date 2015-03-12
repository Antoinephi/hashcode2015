import java.util.ArrayList;
import java.util.List;


public class Problem {
	
	
	private int nbRow;
	private int slotPerRow;
	private int nbPool;
	private Row [] row;
	private Pool [] pools;
	// list of servs we have to place
	private List<Server> servers;
	
	public Problem() {
		this.servers = new ArrayList<Server>();
	}
	
	public void setNbRow(int nb) {
		this.nbRow = nb;
		this.row = new Row[nb];
	}

	public void setSlotPerRow(int nb) {
		this.slotPerRow = nb;
		for (int i = 0 ; i < nbRow ; i++) 
			row[i] = new Row(slotPerRow);
	}

	public void setnbPool(int nb) {
		this.nbPool = nb;
		this.pools = new Pool[this.nbPool];
	}

	public void addUnvailable(int row, int slot) {
		this.row[row].addUnvaible(slot);;
	}

	public void addServer(int size, int capacity) {
		this.servers.add(new Server(size, capacity));
	}

	public void resolve() {
		
		
		
	}

	
	

}
