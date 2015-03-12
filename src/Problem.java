import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
	
	public Row getRow(int i) {
		return this.row[i];
	}

	public void setnbPool(int nb) {
		this.nbPool = nb;
		this.pools = new Pool[this.nbPool];
	}

	public void addUnvailable(int row, int slot) {
		this.row[row].addUnvailable(slot);;
	}

	public void addServer(int size, int capacity) {
		this.servers.add(new Server(size, capacity));
	}
	
	@SuppressWarnings("unchecked")
	private void sortListServer() {
		Collections.sort(this.servers, new Comparator (){
			public int compare(Object arg0, Object arg1) {
				return ((Server)arg0).getRatio() - ((Server)arg1).getRatio()>0.0?1:-1;
			}
		});
	}
		
	public int getNbRow() {
		return this.nbRow;
	}

	public void resolve() {
		int currentPool = 0, currentRow = 0;
		sortListServer();
		for (Server s : this.servers) {
			this.pools[currentPool].addServer(s);
			if(this.row[currentRow].addServer(s))
				System.out.println("y'a plus de place boss");
		}
	}

	
	

}
