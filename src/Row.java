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
	
	public void addUnvaible(int index) {
		this.slot[index] = false;
	}

}
