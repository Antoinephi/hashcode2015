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
