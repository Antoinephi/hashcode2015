import java.util.ArrayList;
import java.util.List;

public class Pool {

	Problem prob;
	List<Server> servers;
	
	public Pool(Problem prob) {
		this.prob = prob;
		this.servers = new ArrayList<Server>();
	}
	
	public void addServer(Server serv) {
		this.servers.add(serv);
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
