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
		
		for (Server s : this.servers) {
			if (s.getPool() != null) {
				writer.addServer(s.getRow().getIndex(), 
						s.getSlot(),
						s.getPool().getIndex());
			} else {
				writer.unusedServer();
			}
		}
		
		writer.close();
	}

}
