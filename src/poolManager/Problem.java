package poolManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dataCenter.Pool;
import dataCenter.Row;
import dataCenter.Server;

public abstract class Problem {
	
	protected int nbRow;
	protected int slotPerRow;
	protected int nbPool;
	protected Row [] row;
	protected Pool [] pools;
	// list of servs we have to place
	protected List<Server> servers;
	protected List<Server> sortedServers;
	
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
	protected void sortListServer() {
		this.sortedServers = new ArrayList<Server>(this.servers);
		Collections.sort(this.sortedServers, new Comparator (){
			public int compare(Object arg0, Object arg1) {
				if (((Server)arg0).getRatio()==((Server)arg1).getRatio() )
					return 0;
				else
					return ((Server)arg0).getRatio() - ((Server)arg1).getRatio()>0.0?1:-1;
			}
		});
	}
		
	public int getNbRow() {
		return this.nbRow;
	}
	
	public abstract void resolve();
	
}
