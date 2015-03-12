import java.util.ArrayList;
import java.util.List;

public class Pool {

	private Problem prob;
	private List<Server> servers;
	private int index;
	
	public Pool(Problem prob, int index) {
		this.index = index;
		this.prob = prob;
		this.servers = new ArrayList<Server>();
	}
	
	public void addServer(Server serv) {
		this.servers.add(serv);
		serv.setPool(this);
	}
	
	public int getIndex() {
		return this.index;
	}
	
	public int getNbServers() {
		return this.servers.size();
	}
	
	public Server getServer(int i) {
		return this.servers.get(i);
	}
	
	public int getTotalCapacity() {
		int totalSize = 0;
		for(int i = 0; i < this.getNbServers(); i++) {
			totalSize += this.getServer(i).getSize();
		}
		return totalSize;
	}
	
	public int getGuarenteedCapacity() {
		int maxCapacityOnRow = 0; 
		for(int i = 0; i < prob.getNbRow(); i++) {
			if(maxCapacityOnRow < prob.getRow(i).getGroupCapacity(this)) {
				maxCapacityOnRow = prob.getRow(i).getGroupCapacity(this);
			}
		}
		return this.getTotalCapacity() - maxCapacityOnRow;
	}

}
