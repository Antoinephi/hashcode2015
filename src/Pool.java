import java.util.ArrayList;
import java.util.List;

public class Pool {

	List<Server> servers;
	
	public Pool() {
		servers = new ArrayList<Server>();
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

}
