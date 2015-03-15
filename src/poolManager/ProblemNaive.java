package poolManager;
import io.OutputWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sun.security.sasl.ServerFactoryImpl;

import dataCenter.Pool;
import dataCenter.Row;
import dataCenter.Server;


public class ProblemNaive extends Problem {

	public int getMinCapacityOfPool(Pool p, List<Integer> usedRows) {
		int min= 1000000, rowMin = -1;
		for (int i = 0 ; i < this.row.length ; i++) {
			if (!usedRows.contains(i)) {
			int tmp = this.row[i].getGroupCapacity(p);
				if (tmp < min) {
					min = tmp;
					rowMin = i;
				}
			}
		}
		return rowMin;
	}
	
	public int getMinPool() {
		int min= 1000000, poolMin = -1;
		for (int i = 0 ; i < this.pools.length ; i++) {
			int tmp = this.pools[i].getGuarenteedCapacity();
				if (tmp < min) {
					min = tmp;
					poolMin = i;
				}
			}
		return poolMin;
	}
	
	class PriorityPlacement {
		public int row;
		public int pool;
		
		public PriorityPlacement(int row, int pool) {
			this.row = row;
			this.pool = pool;
		}
	}
	
	public void resolve() {
		sortListServer();
		bestCurrentScore = 0;
		List<PriorityPlacement> priorityList = new ArrayList<PriorityPlacement>();
		solve(priorityList);
		System.out.println("Score initiale after main algorithm : "+this.getScore());	
		
		upgrade(1);
		
		System.out.println("score="+this.getScore());
		/*for(int pass=0; pass<3; pass++) {
		
	
			//search critical pool/row
			int min= 1000000, minPool = -1;
			for (int i = 0 ; i < this.pools.length ; i++) {
				int tmp = this.pools[i].getGuarenteedCapacity();
				if (tmp < min) {
					min = tmp;
					minPool = i;
				}
			}
			
			int maxCapacityOnRow = 0; 
			int maxRow = -1;
			
			for(int i = 0; i < this.getNbRow(); i++) {
				if(maxCapacityOnRow < this.row[i].getGroupCapacity(this.pools[minPool])) {
					maxCapacityOnRow = this.row[i].getGroupCapacity(this.pools[minPool]);
					maxRow = i;
				}
			}
			
			System.out.println("Critical pool : "+minPool);
			System.out.println("Critical row : "+maxRow);
			
			clean();
			
			priorityList.add(new PriorityPlacement(maxRow, minPool));
			
			//priorityList = testAllOrder(priorityList);
			solve(priorityList);
			System.out.println("Score After pass "+pass+" : "+this.getScore());	
		}*/
		
			
		
		/*for(int i=0; i<1; i++) {
			upgrade();
			System.out.println("Score Before upgrade "+(i+1)+" algorithm : "+this.getScore());
		}*/
		
		OutputWriter writer = new OutputWriter("out.txt");
		
		int cpt = 0;
		for (Server s : this.servers) {
			if (s.getPool() != null) {
				writer.addServer(s.getRow().getIndex(), 
						s.getSlot(),
						s.getPool().getIndex());
			} else {
				cpt++;
				writer.unusedServer();
			}
		}
		
				
	}
	
	public int bestCurrentScore;
	
	private void writeResult() {
		OutputWriter writer = new OutputWriter("out.txt");
		
		int cpt = 0;
		for (Server s : this.servers) {
			if (s.getPool() != null) {
				writer.addServer(s.getRow().getIndex(), 
						s.getSlot(),
						s.getPool().getIndex());
			} else {
				cpt++;
				writer.unusedServer();
			}
		}
		writer.close();
	}
	
	private List<PriorityPlacement> testAllOrder(List<PriorityPlacement> priorityList, PriorityPlacement np, int deepRemain) {
		int max = 0;
		List<PriorityPlacement> bestList = null;
		
		if(deepRemain == 0) {
			return null;
		}
		
		for(int i=0; i<priorityList.size()+1; i++) {
			List<PriorityPlacement> currentList = new ArrayList<PriorityPlacement>();
			
			for(int j=0; j<i; j++)
				currentList.add(priorityList.get(j));
			
			if(np != null)
				currentList.add(np);
			
			for(int j=i; j<priorityList.size(); j++)
				currentList.add(priorityList.get(j));			
			solve(currentList);

			int score = this.getScore();
			if(score > max) {
				if(score >bestCurrentScore) {
					System.out.println("New best score ! "+score);
					bestCurrentScore = score;
					writeResult();	
				}
				bestList = currentList;
				max = score;
				
			}
			
			int min= 1000000, minPool = -1;
			for (int j = 0 ; j < this.pools.length ; j++) {
				int tmp = this.pools[j].getGuarenteedCapacity();
				if (tmp < min) {
					min = tmp;
					minPool = j;
				}
			}
			
			int maxCapacityOnRow = 0; 
			int maxRow = -1;
			
			for(int j = 0; j < this.getNbRow(); j++) {
				if(maxCapacityOnRow < this.row[j].getGroupCapacity(this.pools[minPool])) {
					maxCapacityOnRow = this.row[j].getGroupCapacity(this.pools[minPool]);
					maxRow = j;
				}
			}
			clean();			
			currentList = testAllOrder(currentList, new PriorityPlacement(maxRow, minPool), deepRemain-1);
			
			if(currentList == null)
				continue;
			solve(currentList);
			score = this.getScore();
			if(score > max) {
				if(score >bestCurrentScore) {
					System.out.println("New best score ! "+score);
					bestCurrentScore = score;
					writeResult();	
				}
				bestList = currentList;
				max = score;
			}
			clean();
		}
		return bestList;
	}

	class ServerSave {
		
		public Server server;
		public int pool;
		public int row;
		public int slot;
		
		public ServerSave(Server server, int pool, int row, int slot) {
			this.server = server;
			this.pool = pool;
			this.row = row;
			this.slot = slot;
		}

		public ServerSave(Server server) {
			this.server = server;
			this.pool = server.getPool().getIndex();
			this.row = server.getRow().getIndex();
			this.slot = server.getSlot();
		}
		
	}
	
	void solve(List<PriorityPlacement> priority) {
		int currentPool = 0, currentRow = 0, slot = -1;
		boolean space;
		int priorityIndex = 0;
		

		for (Server s : this.sortedServers) {
			List<Integer> usedRows = new ArrayList<Integer>();
			space = false;
			currentPool= priorityIndex < priority.size() ? priority.get(priorityIndex).pool : getMinPool();
		
			while (usedRows.size() < this.row.length) {	
				currentRow = priorityIndex < priority.size() ? priority.get(priorityIndex).row : this.getMinCapacityOfPool(this.pools[currentPool], usedRows);
				usedRows.add(currentRow);
				if ((slot = this.row[currentRow].addServer(s)) >= 0){
					space = true;
					break;
				}
			}
			
			if (space) {
				this.pools[currentPool].addServer(s);
				s.setPool(this.pools[currentPool]);
				s.setRow(this.row[currentRow]);
				s.setSlot(slot);
			}
			priorityIndex++;
	
		}
	}
	
	public void clean() {
		/*for(int i=0; i<this.pools.length; i++)  {
			this.pools[i] = new Pool(this, i);
		}
		
		for(int i=0; i<this.row.length; i++)  {
			this.row[i] = new Row(this.slotPerRow, i);
		}*/
		
		for (Server s : this.sortedServers) {
			if(s.getPool() != null)
				s.remove();
		}
	}
	
	/**
	 * seek for change place of serve to up the score
	 */
	void upgrade(int level) {
		
		//System.out.println("====Upgrade pass start====");
		
		int min= 1000000, minPool = -1;
		for (int i = 0 ; i < this.pools.length ; i++) {
			int tmp = this.pools[i].getGuarenteedCapacity();
			if (tmp < min) {
				min = tmp;
				minPool = i;
			}
		}
		
		int maxCapacityOnRow = 0; 
		int maxRow = -1;
		
		for(int i = 0; i < this.getNbRow(); i++) {
			if(maxCapacityOnRow < this.row[i].getGroupCapacity(this.pools[minPool])) {
				maxCapacityOnRow = this.row[i].getGroupCapacity(this.pools[minPool]);
				maxRow = i;
			}
		}
		
		//System.out.println("Critical pool : "+minPool);
		//System.out.println("Critical row : "+maxRow);
		
		List<Server> serverCriticals = new ArrayList<Server>();
		
		for(Server s : this.row[maxRow].getServers()) {
			if(s.getPool().getIndex() == minPool) {
				serverCriticals.add(s);
			}
		}
		
		int score = this.getScore();
		
		
		
		//search server
		int cur = 0;
		for(int i=0; i<serverCriticals.size(); i++) {
			int maxScore = 0;
			List<ServerSave> maxScoreServer = null;
			for(Server s : this.servers) {
				/*if(level == 1)
					System.out.println(cur+" / "+servers.size());*/
				cur++;
				if(s.getPool() == null || serverCriticals.contains(s))
					continue;
				//select server list
				List<ServerSave> serverSave = new ArrayList<ServerSave>();
				int currentSize = 0;
				int currentSlot = s.getSlot();
				while(currentSize < serverCriticals.get(i).getSize()) {
					Server nextServer = this.row[s.getRow().getIndex()].getServerAtSlot(currentSlot);
					if(nextServer != null) {
						serverSave.add(new ServerSave(nextServer));
						currentSize += nextServer.getSize();
						currentSlot += nextServer.getSize();
					}
					else {
						break;
					}
				}
				
				if(currentSize != serverCriticals.get(i).getSize()) {
					continue;
				}
				
				ProblemState save = saveState();
				
				swapServer(serverCriticals.get(i), serverSave);

				if(level > 0) {
					upgrade(level-1);
				}
				
				int nScore = this.getScore();
				
				if(nScore > score) { //Win!
					//System.out.println("Upgrade found! "+score+"=>"+this.getScore());
					//return;
					if(nScore > maxScore) {
						maxScore = nScore;
						maxScoreServer = serverSave;
					}
				}
				/*else if(this.getScore() == score) {
					continue;
				}*/
				restoreState(save);
			}
			
			if(maxScore > score)
				swapServer(serverCriticals.get(i), maxScoreServer);
			
			/* free serv?
			List<Server> freeServer = new ArrayList<Server>();
			System.out.println("critical : size="+serverCriticals.get(i).getSize()+"; capacity="+serverCriticals.get(i).getCapacity());
			for(Server s : this.servers) {
				if(s.getPool() == null) {
					freeServer.add(s);
					System.out.println("free server : size="+s.getSize()+"; capacity="+s.getCapacity());
				}
				
			}*/
		}
		
	}
	
	/*public void swap(Server s1, Server s2) {
		ServerSave ss1 = new ServerSave(s1);
		ServerSave ss2 = new ServerSave(s2);
		
		//Server1
		if(this.row[ss1.row].addServer(ss2.server) == -1) {
			System.err.println("Can't re-add server!");
			return;
		}
		this.pools[ss1.pool].addServer(ss2.server);
		ss2.server.setPool(this.pools[ss1.pool]);
		ss2.server.setRow(this.row[ss1.row]);
		ss2.server.setSlot(ss1.slot);	
		
	}*/
	
	class ServerState {
		int pool;
		int row;
		int slot;
		
		public ServerState(int pool, int row, int slot) {
			this.pool = pool;
			this.row = row;
			this.slot = slot;
		}
	}
	
	class ProblemState {
		List<ServerState> servers; 
		
		public ProblemState() {
			this.servers = new ArrayList<ServerState>();
		}
	}
	
	public ProblemState saveState() {
		ProblemState save = new ProblemState();
		for(Server s : this.servers) {
			save.servers.add(new ServerState(s.getPool() != null ? s.getPool().getIndex() : -1, s.getRow() != null ? s.getRow().getIndex() : -1, s.getSlot()));
		}
		return save;
	}
	
	public void restoreState(ProblemState save) {
		clean();
		for(int i=0; i<save.servers.size(); i++) {
			if(save.servers.get(i).pool == -1)
				continue;
			
			if(this.row[save.servers.get(i).row].addServerAtSlot(this.servers.get(i), save.servers.get(i).slot) == -1) {
				System.err.println("Can't re-add server! (restore servers)");
				return;
			}
			this.pools[save.servers.get(i).pool].addServer(this.servers.get(i));
			this.servers.get(i).setPool(this.pools[save.servers.get(i).pool]);
			this.servers.get(i).setRow(this.row[save.servers.get(i).row]);
			this.servers.get(i).setSlot(save.servers.get(i).slot);
		}
	}
	
	public void swapServer(Server critical, List<ServerSave> serverSave) {
		for(ServerSave ss  : serverSave) {
			ss.server.remove();
		}
		
		ServerSave ssCritical = new ServerSave(critical);
		ssCritical.server.remove();
		
		//add critical
		if(this.row[serverSave.get(0).row].addServer(ssCritical.server) == -1) {
			System.err.println("Can't re-add server! (swap critical)");
			return;
		}
		this.pools[ssCritical.pool].addServer(ssCritical.server);
		ssCritical.server.setPool(this.pools[ssCritical.pool]);
		ssCritical.server.setRow(this.row[serverSave.get(0).row]);
		ssCritical.server.setSlot(serverSave.get(0).slot);
		
		//add list
		int currentSlot = ssCritical.slot;
		for(ServerSave ss  : serverSave) {
			if(this.row[ssCritical.row].addServer(ss.server) == -1) {
				System.err.println("Can't re-add server! (swap servers)");
				return;
			}
			this.pools[ss.pool].addServer(ss.server);
			ss.server.setPool(this.pools[ss.pool]);
			ss.server.setRow(this.row[ssCritical.row]);
			ss.server.setSlot(currentSlot);
			currentSlot += ss.server.getSize();
		}			
	}

}
