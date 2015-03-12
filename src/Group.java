import java.util.ArrayList;
import java.util.List;

public class Group {

	List servers;
	
	public Group() {
		servers = new ArrayList();
	}
	
	public void addServer(Server serv) {
		this.servers.add(serv);
	}
	
	public int getNbServers() {
		return this.servers.size();
	}
	
	public Server getServer(int i) {
		return this.servers(i);
	}
	
	public int getTotalCapacity() {
		int totalSize = 0;
		for(int i = 0; i < this.getNbServers(); i++) {
			totalSize += this.getServer(i).getSize();
		}
		return totalSize;
	}

}
