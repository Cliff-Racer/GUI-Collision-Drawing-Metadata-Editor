package gui;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import listeners.AddListElementEntityListener;
import listeners.CopyImageToFile;

public class PropertyFrameAddElement extends JFrame {
	
	JPanel contents;
	JButton acceptSettingsJButton;
	JButton addPuthImage;
	JTextField nameTextField,typeTextField;
	ActionListener acceptSettingListener;
	ActionListener copyImageToFile;
	JLabel nameLabel,typeLabel,imageLabel;
	public PropertyFrameAddElement(ListGUI listGUI) {

		this.setTitle("Add Entity");
		this.setLayout(null);
		this.setSize(400,250);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	
		nameLabel = new JLabel("Введите имя сущности:");
		nameLabel.setSize(150,20);
		nameLabel.setLocation(20, 20);
		typeLabel = new JLabel("Введите тип сущности:");
		typeLabel.setSize(150,20);
		typeLabel.setLocation(20, 50);
		imageLabel = new JLabel("Для выбора нажмите:");
		imageLabel.setSize(150,20);
		imageLabel.setLocation(20, 108);
		
		nameTextField = new JTextField();
		nameTextField.setSize(200,20);
		nameTextField.setLocation(160, 20);
		typeTextField = new JTextField();
		typeTextField.setSize(200,20);
		typeTextField.setLocation(160, 50);
		
		
		contents = new JPanel(new FlowLayout(FlowLayout.LEFT));
		contents.setLayout(null);
		contents.setSize(600,400);
		contents.add(nameLabel);
		contents.add(typeLabel);
		contents.add(imageLabel);
		contents.add(nameTextField);
		contents.add(typeTextField);
		acceptSettingListener = new AddListElementEntityListener(listGUI,this);
		copyImageToFile = new CopyImageToFile(this);
		acceptSettingsJButton = createButton("Accept", 148, 170, acceptSettingListener, null);
		addPuthImage = createButton("Добавить image", 160, 100, copyImageToFile, null);
		addPuthImage.setSize(160, 40);
		contents.add(acceptSettingsJButton); 
		contents.add(addPuthImage);
		setContentPane(contents);
	}
	
	
	
	private JButton createButton(String text,int width,int height,ActionListener listener,String pathImage) {
		JButton button = new JButton(text);
		button.setSize(90, 40);
		button.setLocation(width, height);
		button.addActionListener(listener); 
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		this.add(button);	
		return button;
	}
	
	public String getInputName() {
		String name = nameTextField.getText();
		return name;
	}
	public String getInputType() {
		String type = typeTextField.getText();
		return type;
	}
}
