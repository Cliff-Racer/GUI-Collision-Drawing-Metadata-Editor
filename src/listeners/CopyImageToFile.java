package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.PropertyFrameAddElement;
import repository.Project;

public class CopyImageToFile extends JFileChooser implements ActionListener {
	File source;
	File dest;
	PropertyFrameAddElement propertyFrame;
	public String directory = "res/";
	public String file = "objecttypes.xml";
	
	public CopyImageToFile(PropertyFrameAddElement propFrame){
		propertyFrame = propFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		dest = new File(Project.getInstance().getXMLPath()+propertyFrame.getInputName()+".png");
		this.setDialogTitle("Choose file");
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image PNG", "png");
		this.setFileFilter(filter);
        int result = this.showOpenDialog(CopyImageToFile.this);
        System.out.println("propertyFrame.getInputName() = "+propertyFrame.getInputName());
        if (result == JFileChooser.APPROVE_OPTION && propertyFrame.getInputName()!=null)
        {
        	System.out.println("Выбрали!!!!!!!!!!!!");
        	directory = this.getCurrentDirectory().toString()+'/';
        	file = this.getName(getSelectedFile()).toString();
        	source = new File(directory+file);
        	try {
				copyFileUsingJava7Files(source,dest);
				System.out.println("ФАЙЛ ВРОДЕ КАК ДОЛЖЕН БЫЛ СОЗДАТЬСЯ, ИДИ ПРОВЕРЬ");
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Выбранное изображение уже существует!");
				e1.printStackTrace();
			}
        }
        
	}
	
	
	// простой и удобный метод копирования файла в Java 7
	private static void copyFileUsingJava7Files(File source, File dest) throws IOException {
	    Files.copy(source.toPath(), dest.toPath());
	}
}
