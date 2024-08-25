package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Entity;
import repository.Project;

public abstract class Editable extends JPanel implements MouseListener, MouseMotionListener, ListSelectionListener, ActionListener {
	
	protected ListGUI listGUI;
	protected Entity entity;
	protected String name;
	protected BufferedImage image;
	JPanel drawPanel;

	private static Logger logger = Logger.getLogger("gui.Editable");
	
	
	Editable(ListGUI listGUI){
		this.listGUI = listGUI;
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// РАСКОММЕНТИТЬ ЕСЛИ НУЖНО ВЫВОДИТЬ ПОДРОБНЫЕ ЛОГИ
		logger.setLevel(Level.ALL);
	}
	
    
    //Абстрактные методы
    public abstract void paintLoadData(Graphics2D g);
    public abstract void drawing(Graphics2D g);
    public abstract void saveDataInEntity();
    
	
	
	//методы родительского класса Editable
	
    //get,set для name
    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	//заполнить текущую сущность по имени.
	public void setEntityByName(String name) throws Exception {
		entity = Project.getInstance().getEntityByName(name);
	}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); 
        if(entity != null) {
        	paintLoadData((Graphics2D) g); 
        	drawing((Graphics2D)g);
        }
    }
    
    // из mouse motion listener'a
    @Override
    public void mouseDragged(MouseEvent e) {}
    
    // этих ребят нас обязывает создать MouseListener, так что они здесь
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
    @Override
    public void valueChanged(ListSelectionEvent e) {
		logger.log(Level.FINEST, "entering method {0} of class {1}", new Object[]{"MouseClicked()" , this.getClass().getName()});
    	//logger.entering(this.getClass().getName(), "MouseClicked()");
    	if(e.getSource() instanceof JList) {
    		//TODO: надо бы элегантнее пробросить сюда ListGUI - просто передача его в аргументах немножко громоздкая
    		// как-то обыграть это через события? 
    		// ps. попытки обратиться к eventSource проваливаются - ListGUI это панель, уже внутри которой лежит JList
    		name = listGUI.getSelectedName();
    		try {
				entity = Project.getInstance().getEntityByName(name);
			} catch (Exception e1) {
				logger.severe("Entity with name '"+name+"' was not found! Cannot display it on panel!");
			}
    		image = Project.getInstance().loadImageByName(name);
    		//TODO: if(image == null) вызов FileChooser'a и выбор изображения
    		
    		this.repaint();
    	}
    }	
}
