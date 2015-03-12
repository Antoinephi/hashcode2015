import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Béni
 *
 *	class for row
 */
public class Row {
	
	/**
	 * list of group in the row
	 */
	private List<Pool> groups;
	
	private boolean slot [];
	
	/**
	 * number of space
	 */
	private int size;
	
	public Row(int size) {
		this.size = size;
		this.slot = new boolean[this.size];
		for (int i = 0 ; i < this.size ; i++)
			this.slot[i] = true;
		this.groups = new ArrayList<Pool>();
	}
	
	public void addUnvailable(int index) {
		this.slot[index] = false;
	}

	public int getSize() {
		return this.size;
	}
	
	public int getGroupCapacity(Pool group) {
		int capacity = 0;
		for(int i = 0; i < this.getSize(); i++) {
			if(groups.get(i) == group) {
				capacity++;
			}
		}
		return capacity;
	}
	
}
