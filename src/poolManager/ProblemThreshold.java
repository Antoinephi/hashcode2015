package poolManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dataCenter.Server;

public class ProblemThreshold extends Problem {


	public void resolve() {
		
		//minimale threshold
		int threshold = 400;
		
		// sort by capacity desc then by size desc
		System.out.println("Sort server...");
		this.sortedServers = new ArrayList<Server>(this.servers);
		Collections.sort(this.sortedServers, new Comparator<Server> (){


			@Override
			public int compare(Server s1, Server s2) {
				if(s1.getCapacity() == s2.getCapacity()) {
					return s1.getSize()-s2.getSize();
				}
				else 
					return s2.getCapacity()-s1.getCapacity();
			}
		});

		// server selection
		System.out.println("Server selection...");
		List<Server>[] pools = new List[this.nbPool];
		for(int i=0; i<this.nbPool; i++) {
			int nbServer = 0;
			System.out.println("Fill pool "+(i+1)+"...");
			pools[i] = new ArrayList<Server>();
			
			int maxCapacity = this.sortedServers.get(0).getCapacity();
			int currentCapacity = 0;
			
			while(currentCapacity-maxCapacity < threshold) {

				if(threshold-(currentCapacity-maxCapacity) > this.sortedServers.get(0).getCapacity()) {	//take the highest
					Server s = this.sortedServers.get(0);
					pools[i].add(s);
					s.setPool(this.pools[i]);
					currentCapacity += s.getCapacity();
					this.sortedServers.remove(0);
					nbServer++;
					//System.out.println("Add server "+s.getCapacity());
				}
				else { //search best fit
					int minTarget = threshold-(currentCapacity-maxCapacity);
					Server s = null;
					Server lastTargetServer = null;
					for(int j=0; j<this.sortedServers.size(); j++) {
						if(this.sortedServers.get(j).getCapacity() == minTarget) {
							s = this.sortedServers.get(j);
							break;
						}
						else if(this.sortedServers.get(j).getCapacity() < minTarget) {
							if(lastTargetServer == null) {
								System.err.println("no server :(");
								return;
							}
							
							s = lastTargetServer;
							break;
						}
						
						if(lastTargetServer == null || lastTargetServer.getCapacity() != this.sortedServers.get(j).getCapacity()) {
							lastTargetServer = this.sortedServers.get(j);
						}
					}
					
					if(s == null) {
						if(lastTargetServer == null) {
							System.err.println("no server found :(");
							return;
						}
						s = lastTargetServer;
					}
					
					pools[i].add(s);
					s.setPool(this.pools[i]);
					currentCapacity += s.getCapacity();
					this.sortedServers.remove(s);
				}
			}
			System.out.println("Pool "+(i+1)+" capacity = "+(currentCapacity-maxCapacity)+" ("+currentCapacity+"-"+maxCapacity+") number = "+nbServer);
		}
		
		int totalSize = 0;
		int nbServer = 0;
		for(int i=0; i<this.nbPool; i++) {
			for(Server s : pools[i]) {
				totalSize += s.getSize();
				nbServer++;
			}
		}
		System.out.println("Free slots : "+this.nbFreeSlots+", totale size : "+totalSize+", used number : "+nbServer);
		
		//sort remain server by ratio
		Collections.sort(this.sortedServers, new Comparator<Server> (){
			public int compare(Server s1, Server s2) {
				if (s1.getRatio() == s2.getRatio())
					return 0;
				else
					return s1.getRatio() - s2.getRatio() < 0.0 ?1 : -1 ;
			}
		});
		
		System.out.println("Reduce servers size...");
		
		//servers size reduce
		
		List<Server> replaceServers = new ArrayList<Server>();
		for(int i=this.nbPool-1; i>=0; i--) {
			for(int j=0; j<pools[i].size(); j++) {
				int currentSize = 0;
				int currentCapacity = 0;
				
				while(currentCapacity < pools[i].get(j).getCapacity()) {
					if(currentCapacity+this.sortedServers.get(0).getCapacity() ==  pools[i].get(j).getCapacity()) {
						replaceServers.add(this.sortedServers.get(0));
						currentSize += this.sortedServers.get(0).getSize();
						currentCapacity += this.sortedServers.get(0).getCapacity();
						this.sortedServers.remove(0);
					}
					else {
						int minTarget = pools[i].get(j).getCapacity();
						Server s = null;
						Server lastTargetServer = null;
						for(int k=0; k<this.sortedServers.size(); k++) {
							if(this.sortedServers.get(k).getCapacity() == minTarget) {
								s = this.sortedServers.get(k);
								break;
							}
							else if(this.sortedServers.get(k).getCapacity() < minTarget) {
								if(lastTargetServer == null) {
									break;
								}
								
								s = lastTargetServer;
								break;
							}
							
							if(lastTargetServer == null || lastTargetServer.getCapacity() != this.sortedServers.get(k).getCapacity()) {
								lastTargetServer = this.sortedServers.get(k);
							}
						}
						
						if(s == null) {
							if(lastTargetServer == null) {
								break;
							}
							s = lastTargetServer;
						}
						
						replaceServers.add(s);
						currentSize += s.getSize();
						currentCapacity += s.getCapacity();
						this.sortedServers.remove(s);
					}
				}
				
				if(currentCapacity >= pools[i].get(j).getCapacity() && currentSize < pools[i].get(j).getSize()) {
					System.out.println("Found size redude : "+pools[i].get(j).getSize()+" => "+currentSize);
				}
				else {
					this.sortedServers.addAll(replaceServers);
					Collections.sort(this.sortedServers, new Comparator<Server> (){
						public int compare(Server s1, Server s2) {
							if (s1.getRatio() == s2.getRatio())
								return 0;
							else
								return s1.getRatio() - s2.getRatio() < 0.0 ?1 : -1 ;
						}
					});
				}
			}
		}
		
		totalSize = 0;
		for(int i=0; i<this.nbPool; i++) {
			for(Server s : pools[i]) {
				totalSize += s.getSize();
			}
		}
		System.out.println("Free slots : "+this.nbFreeSlots+", totale size : "+totalSize);
		
		//sort by size
		List<Server> selectedServer = new ArrayList<Server>();
		for(int i=0; i<this.nbPool; i++) {
			for(Server s : pools[i]) {
				selectedServer.add(s);
			}
		}
		
		Collections.sort(selectedServer, new Comparator<Server> (){


			@Override
			public int compare(Server s1, Server s2) {
				return s2.getSize()-s1.getSize();
			}
		});
		
		totalSize = 0;
		for(int j=0; j<selectedServer.size(); j++) {
			totalSize += selectedServer.get(j).getSize();
		}
		System.out.println("server remain : "+selectedServer.size()+" "+totalSize);
		
		totalSize = 0;
		for(int j=0; j<this.nbRow; j++) {
			totalSize += this.row[j].getNbFree();
		}
		System.out.println("free space : "+totalSize);
		
		for(int i=0; i<this.nbRow; i++) {
			
			List<Integer> usedPool = new ArrayList<Integer>();
			int cursor = 0;
			while(true) {
				
				int nextInterval = 0;
				
				
				while(cursor < this.slotPerRow && !this.row[i].isFree(cursor))
					cursor++;
				
				while(cursor < this.slotPerRow && this.row[i].isFree(cursor)) {
					cursor++;
					nextInterval++;
				}
				
				if(nextInterval == 0) {
					break;
				}
				
				System.out.println("Next interval :"+nextInterval+" server nb = "+selectedServer.size());
				
				for(int j=0; j<usedPool.size(); j++) {
					System.out.print(usedPool.get(j).intValue()+ " ");
				}
				System.out.println();
				
				Collections.sort(selectedServer, new Comparator<Server> (){
					@Override
					public int compare(Server s1, Server s2) {
						//return s2.getSize()-s1.getSize();
		
						return s2.getPool().getGuarenteedCapacity()-s1.getPool().getGuarenteedCapacity();
					}
				});
				
				List<Integer> list = getServerPlacement(selectedServer, generateIndexServer(selectedServer), new ArrayList<Integer>(), new ArrayList<Integer>(usedPool), nextInterval, 1);
				if(list == null) {
					while(cursor < this.slotPerRow && this.row[i].isFree(cursor)) {
						cursor++;
					}
					continue;
				}
				
				totalSize = 0;
				for(int j=0; j<selectedServer.size(); j++) {
					totalSize += selectedServer.get(j).getSize();
				}
				System.out.println("server remain : "+selectedServer.size()+" "+totalSize+" add : "+countSize(selectedServer, list));
				
				// sort index desc (for remove)
				Collections.sort(list, new Comparator<Integer> (){
	
					public int compare(Integer i1, Integer i2) {
						return i2.intValue()-i1.intValue();
					}
					
				});
				
				for(int j=0; j<list.size(); j++) {
					int slot;
					if((slot = this.row[i].addServer(selectedServer.get(list.get(j)))) == -1) {
						System.err.println("Can't add server");
						this.row[i].display();
						return;
					}
					
					selectedServer.get(list.get(j)).setSlot(slot);
					selectedServer.get(list.get(j)).setRow(this.row[i]);
					
					System.out.println("added "+selectedServer.get(list.get(j)).getCapacity()+" "+selectedServer.get(list.get(j)).getSize());
					
					if(usedPool.contains(new Integer(selectedServer.get(list.get(j)).getPool().getIndex()))) {
						System.err.println("Collision pool");
						System.exit(0);
					}
					
					usedPool.add(selectedServer.get(list.get(j)).getPool().getIndex());
					
					selectedServer.remove(list.get(j).intValue());
					
					
				}
			}
			System.out.print((i+1)+" : ");
			this.row[i].display();
			
			for(int j=0; j<selectedServer.size(); j++) {
				System.out.println(">"+selectedServer.get(j).getPool().getGuarenteedCapacity());
			}
			
			System.exit(0);
			
			totalSize = 0;
			for(int j=0; j<selectedServer.size(); j++) {
				totalSize += selectedServer.get(i).getSize();
			}
			System.out.println("server remain : "+selectedServer.size()+" "+totalSize);
			
			System.out.println("free remain : "+this.row[i].getNbFree());
		}
		
		System.out.println("========");
		for(int i=0; i<this.nbRow; i++) {
			System.out.println("free remain : "+this.row[i].getNbFree());
		}
		
		System.out.println("========");
		System.out.println("SCORE : "+this.getScore());
		
		
	}

	private List<Integer> generateIndexServer(List<Server> selectedServer) {
		List<Integer> list = new ArrayList<Integer>();
		
		int current = selectedServer.get(0).getSize();
		for(int i=1; i<selectedServer.size(); i++) {
			if(selectedServer.get(i).getSize() != current) {
				current = selectedServer.get(i).getSize();
				list.add(new Integer(i));
			}
		}
		
		return list;
	}

	private List<Integer> getServerPlacement(List<Server> server, List<Integer> serverIndex, List<Integer> usedIndex, List<Integer> usedPool, int nextInterval, int callDepth) {
		//System.out.println("depth : "+callDepth+" => "+nextInterval+" used pool = "+usedPool.size());
		int maxSize = 0;
		int nextIndex = 0;
		List<Integer> maxList = null;
		
		if(usedPool.size() == this.nbPool) {
			System.err.println("Row full of pool");
			System.exit(0);
		}
		
		//first heuristic : find the most pool 
		int maxPool = 0;
		int maxPoolIndex = -1;
		
		int[] poolArray = new int[this.nbPool];
		
		for(int i=0; i<this.nbPool; i++) {
			poolArray[i] = 0;
		}
		
		for(int i=0; i<server.size(); i++) {
			if(!usedIndex.contains(new Integer(i)) && !usedPool.contains(new Integer(server.get(i).getPool().getIndex()))) {
				poolArray[server.get(i).getPool().getIndex()]++;
			}
		}
		
		for(int i=0; i<this.nbPool; i++) {
			if(maxPool < poolArray[i]) {
				maxPool = poolArray[i];
				maxPoolIndex = i;
			}
		}
		
		for(int i=0; i<server.size(); i++) {
			if(!usedIndex.contains(new Integer(i)) && !usedPool.contains(new Integer(server.get(i).getPool().getIndex())) && server.get(i).getPool().getIndex() == maxPoolIndex) {
				if(server.get(i).getSize() == nextInterval) {
					List<Integer> result = new ArrayList<Integer>();
					result.add(new Integer(i));
					return result;
				}

			}
		}
		
		int serverMaxSize = 0;
		for(int i=0; i<server.size(); i++) {
			if(serverMaxSize < server.get(i).getSize())
				serverMaxSize = server.get(i).getSize();
		}
		
		for(int i=0; i<server.size(); i++) {
			
			/*if(firstCall)
				System.out.println(i+" => "+nextInterval+", "+serverIndex.size());*/
			if(!usedIndex.contains(new Integer(i)) && !usedPool.contains(new Integer(server.get(i).getPool().getIndex()))) {
				if(server.get(i).getSize() == nextInterval) {
					List<Integer> result = new ArrayList<Integer>();
					result.add(new Integer(i));
					return result;
				}
				
				
				if(nextInterval < server.get(i).getSize()) {
					continue;
				}
				
				Integer index = new Integer(i);
				usedIndex.add(index);
				
				Integer pool = new Integer(server.get(i).getPool().getIndex());
				usedPool.add(pool);
				
				List<Integer> result = getServerPlacement(server, serverIndex, usedIndex, new ArrayList<Integer>(usedPool), nextInterval-server.get(i).getSize(), callDepth+1);
				
				usedIndex.remove(index);
				if(result == null && nextInterval > server.get(i).getSize()) {
					break;
				}
				
				if(result != null) {
					int count = countSize(server, result);
					if(count+server.get(i).getSize() == nextInterval) {
						result.add(new Integer(i));
						return result;
					}
					
					if(maxSize < count) {
						maxSize = count;
						result.add(new Integer(i));
						maxList = result;
						
						//return maxList;
					}
					
					if(maxSize == count && server.get(i).getPool().getIndex() == maxPoolIndex) {
						maxSize = count;
						result.add(new Integer(i));
						maxList = result;
					}
				}
				else {
					if(nextIndex < serverIndex.size())
						i = serverIndex.get(nextIndex).intValue();
					nextIndex++;
				}
			}
		}
		return maxList;
	}

	private int countSize(List<Server> server, List<Integer> index) {
		int nb = 0;
		for(int i=0; i< index.size(); i++) {
			nb += server.get(index.get(i)).getSize();
		}
		return nb;
	}
}
