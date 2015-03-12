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
	private List<Group> groups;
	
	/**
	 * number of space
	 */
	private int size;
	
	public Row(int size) {
		this.size = size;
		this.groups = new ArrayList<Group>();
	}
	
	public void addGroupTo(int index, Group group) {
		this.groups.add(index,group);
	}

}
