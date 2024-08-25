package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gui.ListGUI;
import gui.PropertyFrameAddElement;

public class CreateFrameAddElementListener implements ActionListener {
	
	PropertyFrameAddElement frameProperty;
	ListGUI listGUI;
	
	public CreateFrameAddElementListener(ListGUI listGUI) {
		this.listGUI = listGUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frameProperty = new PropertyFrameAddElement(listGUI);
		frameProperty.setVisible(true);
	}

}
