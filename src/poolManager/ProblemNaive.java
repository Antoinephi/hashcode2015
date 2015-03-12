package poolManager;
import io.OutputWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
	
	public void resolve() {
		int currentPool = 0, currentRow = 0, slot = -1;
		sortListServer();
		boolean space;
		for (Server s : this.sortedServers) {
			List<Integer> usedRows = new ArrayList<Integer>();
			space = false;
			currentPool=getMinPool();
			while (usedRows.size() < this.row.length) {
				currentRow = this.getMinCapacityOfPool(this.pools[currentPool], usedRows);
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
	
		}
		
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
		
		upgrade();
		
		displayScore();
		//System.out.println(this.getScore());
		System.out.println("=>Score = "+this.getScore());
		
		int cptFree = 0;
		for (int i = 0 ; i < this.row.length ; i++) {
			for (int j = 0 ; j < this.slotPerRow ; j++){
				if(this.row[i].isFree(j))
					System.err.println("AGHHHH");
			}
		}
					//cptFree += this.row[i].isFree(j)?1:0;
				
		System.out.println("nb us "+cpt + " nb Free " + cptFree);
		
		writer.close();
	}
	
	/**
	 * seek for change place of serve to up the score
	 */
	void upgrade() {
		int min= 1000000, minPool = -1;
		for (int i = 0 ; i < this.pools.length ; i++) {
			int tmp = this.pools[i].getGuarenteedCapacity();
			if (tmp < min) {
				min = tmp;
				minPool = i;
			}
		}
		System.out.println("=>Score = "+min);
		System.out.println("=>Critical pool = "+minPool);
		
		int maxCapacityOnRow = 0; 
		int maxRow = -1;
		
		for(int i = 0; i < this.getNbRow(); i++) {
			if(maxCapacityOnRow < this.row[i].getGroupCapacity(this.pools[minPool])) {
				maxCapacityOnRow = this.row[i].getGroupCapacity(this.pools[minPool]);
				maxRow = i;
			}
		}
		
		int score = this.getScore();
		
		//search server
		for(Server s : this.servers) {
			if(s.getPool() != null) {
				int pool = s.getPool().getIndex();
				int row = s.getRow().getIndex();
				int slot = s.getSlot();
				s.remove();
				if (this.getScore() > score) {
					for (Server sInPool : this.row[maxRow].getServers()) {
						if (sInPool.getPool().getIndex() == minPool) {
							swap(s,sInPool, pool, row, slot);
							break;
						}
					}
				} else {
					this.pools[pool].addServer(s);
					s.setPool(this.pools[pool]);
					s.setRow(this.row[row]);
					s.setSlot(slot);
				}
			}
		}
	}
	
	public void swap(Server s1, Server s2, int pool, int row, int slot) {
		int pool2 = s2.getPool().getIndex();
		int row2 = s2.getRow().getIndex();
		int slot2 = s2.getSlot();
		s2.remove();
		
		this.pools[pool2].addServer(s1);
		s1.setPool(this.pools[pool2]);
		s1.setRow(this.row[row2]);
		s1.setSlot(slot2);
		
		this.pools[pool].addServer(s2);
		s2.setPool(this.pools[pool]);
		s2.setRow(this.row[row]);
		s2.setSlot(slot);
	}

}
