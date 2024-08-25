package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import gui.ListGUI;
import gui.PropertyFrameAddElement;

public class AddListElementEntityListener implements ActionListener
{
	ListGUI listGUI;
	PropertyFrameAddElement propertyFrame;
	public AddListElementEntityListener(ListGUI listGUI,PropertyFrameAddElement propetry) {
		this.listGUI = listGUI;
		propertyFrame = propetry;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {		
		listGUI.addListElement(propertyFrame.getInputName(),propertyFrame.getInputType());
		propertyFrame.dispose();
	}
}
