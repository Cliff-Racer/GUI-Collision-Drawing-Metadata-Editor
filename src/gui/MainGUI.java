package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import listeners.OpenXMLFileButtonListener;

public class MainGUI extends JFrame{	
	ListGUI list;
	ActionListener OpenXMLFileButtonListener;
	JButton openXMLJButton;
	JButton saveXMLJButton;
	JButton clearLinesJButton;
	public static JTabbedPane editorPane;
	public static JTabbedPane hitdrawPane;
	List <Editable> listEditorPanel = new ArrayList<Editable>();
	DrawboxEditor drawBoxPanel;
	HitboxCircleEditor hitboxCirclePanel;
	HitboxRectangleEditor hitboxRectanglePanel;
	HitboxPoligonEditor hitboxPoligonPanel;

	
	
	public MainGUI() {
		
		setTitle("DrawBox points");
		setLayout(null);
		setSize(1200,780);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        
		list = new ListGUI(); 
		this.add(list);
		list.setLayout(null);
		list.setSize(230, 691);
		list.setLocation(0, 47);
		list.setVisible(true);
        
		drawBoxPanel = new DrawboxEditor(list);
		hitboxCirclePanel = new HitboxCircleEditor(list);
		hitboxRectanglePanel = new HitboxRectangleEditor(list);
		hitboxPoligonPanel = new HitboxPoligonEditor(list);
		listEditorPanel.add(drawBoxPanel);
		listEditorPanel.add(hitboxCirclePanel);
		listEditorPanel.add(hitboxRectanglePanel);
		listEditorPanel.add(hitboxPoligonPanel);
		
		list.registerJListListener(drawBoxPanel);
		list.registerJListListener(hitboxCirclePanel);
		list.registerJListListener(hitboxPoligonPanel);
		list.registerJListListener(hitboxRectanglePanel);
		
        OpenXMLFileButtonListener = new OpenXMLFileButtonListener(list);
		
		openXMLJButton = createButton("XML",5,5, OpenXMLFileButtonListener,"res/xml.png");
		saveXMLJButton = createButton("Save",80,5,null,"res/download.png");
		clearLinesJButton = createButton("Clear lines",155,5,null,"res/destroy.png");
		
		clearLinesJButton.addActionListener(drawBoxPanel);
		clearLinesJButton.addActionListener(hitboxCirclePanel);
		clearLinesJButton.addActionListener(hitboxPoligonPanel);
		clearLinesJButton.addActionListener(hitboxRectanglePanel);
		
		repaint();
		
		this.add(editorPane = createPane(editorPane, 230, 50));		
		
        editorPane.addTab("Hitbox", hitdrawPane = createPane(hitdrawPane,10, 10));
        editorPane.addTab("Drawbox", drawBoxPanel);
        hitdrawPane.addTab(null, new ImageIcon("res/square.png"),hitboxRectanglePanel, null);
        hitdrawPane.addTab(null, new ImageIcon("res/circle.png"),hitboxCirclePanel, null);
        hitdrawPane.addTab(null, new ImageIcon("res/formless.png"),hitboxPoligonPanel, null);
        hitdrawPane.setTabPlacement(JTabbedPane.LEFT);

	}
	@Override
	public void paint(Graphics g){
        super.paint(g);
        g.drawLine(0, 72, 1200, 72);
    }

	
	public JTabbedPane createPane(JTabbedPane pane, int x, int y) {
        pane = new JTabbedPane();
        pane.setSize(new Dimension(950, 688));
        pane.setLocation(new Point(x, y));
        pane.setVisible(true);
        return pane;
    }
	
	private JButton createButton(String text,int width,int height,ActionListener listener,String pathImage) {
		JButton button = new JButton(new ImageIcon(pathImage));
		button.setSize(68, 34);
		button.setLocation(width, height);
		button.addActionListener(listener); 
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		this.add(button);	
		return button;
	}
}
