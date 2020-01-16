package rm;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomTableCellRender extends DefaultTableCellRenderer {
	List<Integer> redovi = new ArrayList<Integer>();
	public CustomTableCellRender(List<Integer> redovi_) {
		redovi=redovi_;
	}
	@Override
	  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		
	    //Cells are by default rendered as a JLabel.
	    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
	    l.setForeground(Color.BLACK);
	    for(int i=0 ; i< redovi.size(); i++) {
	    	if(redovi.get(i) == row) l.setForeground(Color.RED);
		    
	    }
	    
	    
	   
	    
	    
	   

	  //Return the JLabel which renders the cell.
	  return l;

	}
}
