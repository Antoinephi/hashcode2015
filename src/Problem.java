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
	private List<Server> sortedServers;
	
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
			row[i] = new Row(slotPerRow,i);
	}
	
	public Row getRow(int i) {
		return this.row[i];
	}

	public void setnbPool(int nb) {
		this.nbPool = nb;
		this.pools = new Pool[this.nbPool];
		for (int i = 0 ; i < nb ; i++)
			this.pools[i] = new Pool(this, i);
	}

	public void addUnvailable(int row, int slot) {
		this.row[row].addUnvailable(slot);;
	}

	public void addServer(int size, int capacity) {
		this.servers.add(new Server(size, capacity));
	}
	
	@SuppressWarnings("unchecked")
	private void sortListServer() {
		this.sortedServers = new ArrayList<Server>(this.servers);
		Collections.sort(this.sortedServers, new Comparator (){
			public int compare(Object arg0, Object arg1) {
				return ((Server)arg0).getRatio() - ((Server)arg1).getRatio()>0.0?1:-1;
			}
		});
	}
		
	public int getNbRow() {
		return this.nbRow;
	}

	public void resolve() {
		int currentPool = 0, currentRow = 0, slot;
		sortListServer();
		for (Server s : this.sortedServers) {
			this.pools[currentPool].addServer(s);
			s.setPool(this.pools[currentPool]);
			if((slot = this.row[currentRow].addServer(s)) >= 0)
				System.out.println("y'a plus de place boss");
			else {
				s.setRow(this.row[currentRow]);
				s.setSlot(slot);
			}
			currentPool=(currentPool+1)%nbPool;
			currentRow=(currentRow+1)%nbRow;
		}
		
		OutputWriter writer = new OutputWriter("out.txt");
		
		for (Server s : this.servers) {
			if (s.getPool() != null) {
				writer.addServer(s.getRow().getIndex(), s.getSlot(), s.getPool().getIndex());
			}
		}
	}

	
	

}
