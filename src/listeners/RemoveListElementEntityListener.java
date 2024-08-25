package listeners;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import gui.ListGUI;
import model.Entity;
import repository.Project;

public class RemoveListElementEntityListener implements ActionListener{
	
	ListGUI listGUI;
	
	public RemoveListElementEntityListener(ListGUI listGUI) {
		this.listGUI = listGUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String name = listGUI.getSelectedName();
		if(name == null) return;
//		System.out.println(name);
		try {
			Entity e = Project.getInstance().getEntityByName(name);
			Project.getInstance().removeEntity(e);
			listGUI.updateList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
