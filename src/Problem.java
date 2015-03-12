
public class Problem {
	
	
	private int nbRow;
	private int slotPerRow;
	private int nbPool;
	private Group unavaible ;
	
	public Problem() {
		this.unavaible = new Group();
	}
	
	public void setNbRow(int nb) {
		this.nbRow = nb;
	}

	public void setSlotPerRow(int nb) {
		this.slotPerRow = nb;
	}

	public void setnbPool(int nb) {
		this.nbPool = nb;
	}

	public void addUnvailable(int row, int slot) {
		
	}

	public void addServer(int size, int capacity) {
		// TODO Auto-generated method stub
		
	}

	public void resolve() {
		Row [] row = new Row[nbRow];
		for (int i = 0 ; i < nbRow ; i++) 
			row[i] = new Row(slotPerRow);
		
	
		for
		
		
		
	}

	
	

}
