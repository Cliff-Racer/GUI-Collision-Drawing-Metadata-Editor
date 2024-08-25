package gui;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import listeners.CreateFrameAddElementListener;
import listeners.RemoveListElementEntityListener;
import model.Drawbox;
import model.Entity;
import model.Hitbox;
import model.Point;
import repository.Project;

public class ListGUI extends JPanel {
	private Map<String, Icon> iconMap = new HashMap<>();
	JButton addListElementEntity;
	JButton removeListElementEntity;
	JButton addPicEntity;
	String pathImage;
	JList list;
	JScrollPane scroll;
	ActionListener removeEntity;
	ActionListener addEntity;
	DefaultListModel<String> testModel;
	
	private static Logger logger = Logger.getLogger("gui.ListGUI");
			 
    public ListGUI() {
    	try {
			Project.getInstance().load();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO: сделать нормальную обработку исключений - ситуации когда файл не существует, случилась ошибка парсинга и тд должны обрабатываться отдельно
			// в идеале должны появляться разные окошки, которые обрисуют в чём проблема и предложения что делать
			JOptionPane.showMessageDialog(this, "Parser exception, cause: "+e);
		}
    	String[] nameList = createNameList();
        
        testModel = new DefaultListModel<>();

        list = new JList(testModel);
        list.setCellRenderer(new ListEntityRenderer());
        
        addEntity = new CreateFrameAddElementListener(this);       
        removeEntity = new RemoveListElementEntityListener(this);
        
        scroll = new JScrollPane(list);
        scroll.setSize(new Dimension(223, 638));
        scroll.setLocation(5, 5);
        this.add(scroll);
        
        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        addListElementEntity = createButton(5,645, addEntity,"res/addbutton.png");
        removeListElementEntity = createButton(117,645,removeEntity,"res/deletebutton.png");
    	
        
        /// ЧТОБЫ ОТКЛЮЧИТЬ ИЗБЫТОЧНЫЙ ВЫВОД В КОНСОЛЬ - НУЖНО СМЕНИТЬ УРОВЕНЬ ЛОГГИРОВАНИЯ В СТРОКЕ НИЖЕ
    	logger.setLevel(Level.FINEST);
    	// в идеале достаточно уровня WARNING, но если нужна более точна информация о модуле - можно снизить уровень
    	
        updateList();
    }
    
    public String getSelectedName() {
    	return (String) list.getSelectedValue();
    }
    
    public void addListElement(String name,String solid) {
    	// плейсхолдеры для новых хитбоксов и дроубоксов, иначе всё валится с NPE
    	List<Point> hitboxPoints = new LinkedList<Point>();
    	List<Point> drawboxPoints = new LinkedList<Point>();
    	Hitbox hitbox = new Hitbox("Rectangle", hitboxPoints);
    	Drawbox drawbox = new Drawbox(drawboxPoints);
    	
    	// а тут уже создание новой сущности
    	Entity e = new Entity(name, hitbox, drawbox);
    	e.setType(solid);
    	logger.finer("Entity \""+name+"\" was created.");
    	Project.getInstance().addEntity(e);
    	updateList();
    }
    
	public void updateList() {
		String[] nameList = createNameList();
		testModel.removeAllElements();
		for (String name : nameList)
			testModel.addElement(name);

		pathImage = Project.getInstance().getXMLPath();
		createImageMap(nameList);
		list.updateUI();
	}

    /**
     * Функция, формирующая список имён сущностей для JList, чтобы сформировать его итемы
     * */
	private String[] createNameList() {
		//TODO: meh, хочется избавиться от использования списка - нехорошее это дело, лучше работать через интерфейс Project
		int index = 0, 
			size = Project.getInstance().getListEntity().size();
		String[] names = new String[size];
    	for(Entity e: Project.getInstance()) 
    		names[index++] = e.getName();
    	//TODO: не будет ли лучше хранить в JList не только имена, но и ссылки на сущности?
    	//было бы удобно по нажатию на итем JList'а сразу дёргать из него нужную сущность а
    	//из неё её хитбоксы и дроубоксы вместо вызовов по типу "Project.getInstance().getEntityByName(jListItem.getText())"
    	//НО! текущий вариант не требует написания кастомного JList, без которого в него целую сущность не запихать...
    	
    	return names;
    }
	
    private final float iconMaxWidth = 40, iconMaxHeight = 40;

	public Icon imageScaling(BufferedImage image) {
		CustomIcon icon = null;
		try {
			int imageWidth = image.getWidth(), imageHeight = image.getHeight();
			float scaleFactorX = iconMaxWidth / imageWidth, scaleFactorY = iconMaxHeight / imageHeight,
					scaleFactor = scaleFactorX < scaleFactorY ? scaleFactorX : scaleFactorY;
			// debug print
			// System.out.printf("ImageWidth: %d, ImageHeight: %d, scaleFactorX: %f,
			// scaleFactorY: %f, scaleFactor: %f \n", imageWidth, imageHeight, scaleFactorX,
			// scaleFactorY, scaleFactor);
			Image scaledImage = image.getScaledInstance((int) (imageWidth * scaleFactor),
					(int) (imageHeight * scaleFactor), Image.SCALE_SMOOTH);
			icon = new CustomIcon(scaledImage, (int) iconMaxWidth, (int) iconMaxHeight);
		} catch (NullPointerException npe) {
			logger.log(Level.WARNING, "Cannot scale icon for list item - the reference is null");
		}
		return icon;
	}
    
    public class ListEntityRenderer extends DefaultListCellRenderer 
    {
        Font font = new Font("helvitica", Font.BOLD, 14);

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) 
        {
        	int iconTextOffset = 10;
        	
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Entity e = null;
			try {
				e = Project.getInstance().getEntityByName((String)value);
	            String text = label.getText();
	            text = "<html>"+text+"<br>"+"<small>"+e.getType()+"</small>";
	            label.setText(text);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Icon icon = iconMap.get((String) value);
			if(icon != null) {
				label.setIcon(icon);
				label.setIconTextGap((int)iconMaxWidth - icon.getIconWidth() + iconTextOffset);
			}
            label.setFont(font);
            return label;
        }
    }

	private void createImageMap(String[] nameList) {
		for (int i = 0; i < nameList.length; i++) {
			String name = nameList[i];
			BufferedImage image = Project.getInstance().loadImageByName(name);
			iconMap.put(name, imageScaling(image));
		}
	}

    private JButton createButton(int width,int height,ActionListener listener,String pathImage) {
		JButton button = new JButton(new ImageIcon(pathImage));
		button.setSize(110, 46);
		button.setLocation(width, height);
		button.addActionListener(listener); 
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		this.add(button);	
		return button;
	}
    
    /**
     * Немного костыль, который позволяет зарегестрировать слушатель мыши на списке компонентов
     * не создавая излишне тесных связей между компонентами
     * */
    public void registerJListListener(ListSelectionListener ml) {
    	list.addListSelectionListener(ml);
    }
    
   public void printNameList(String[] nameList) {
	   System.out.println("вызван принт лист: "+nameList.length);
	   for(int i = 0;i<nameList.length;i++) {
		   System.out.println(nameList[i]);
	   }
    }
}
