package repository;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Entity;


/**
 * Класс данных, который оперирует их сохранением, загрузкой, и хранением в памяти. 
 * Он нужен для отделения Модели от Представления (см. <a href="https://ru.wikipedia.org/wiki/Model-View-Controller">MVC</a>), чтобы их можно было менять независимо друг от друга.
 * Например, при добавлении новой кнопки данные можно будет получить через существующий интерфейс класса не меняя код.
 * 
 * ВАЖНО: данный класс хранит так же актуальную копию XML-представления файла 
 * */
public class Project implements Iterable<Entity> {
	
	/**
	 * Путь к XML по-умолчанию.
	 * Просто заглушка, обычно заменяется актуальным путём в ходе изменения программы. <br> 
	 * Cм. {@link #setXMLPath(String)} и {@link #getXMLPath()}
	 * */
	public static final String DEFAULT_XML_PATH = "res/";
	public static final String DEFAULT_XML_FILENAME = "objecttypes.xml";
	
	static Project thisProject;
	private List <Entity> listEntity = new ArrayList<Entity>();
	private String path;	
	private String fileName;
	 // Получение фабрики, чтобы после получить билдер документов.
    private DocumentBuilderFactory factory;
    // Получили из фабрики билдер, который парсит XML, создает структуру Document в виде иерархического дерева.
    private DocumentBuilder builder;
    // Запарсили XML, создав структуру Document. Теперь у нас есть доступ ко всем элементам, каким нам нужно.
    private Document document;
    
	private static Logger logger = Logger.getLogger("repository.Project");
    
    //!!!РЕАЛИЗАЦИЯ СИНГЛИТОНА!НАЧАЛО!!!
	private Project(){};
	
	public static Project getInstance() {
		if(thisProject == null) {
			thisProject = new Project();
		}
		return thisProject;
	};
	//!!!РЕАЛИЗАЦИЯ СИНГЛИТОНА!ОКОНЧАНИЕ!!!
	
	/**
	 * Устанавливает значение пути к XML. Все операции загрузки и сохранения будут работать с этой директорией.<br>
	 * По-умолчанию - {@link #DEFAULT_XML_PATH}
	 * @param newPath - абсолютный или относительный адрес папки в виде строки. 
	 * <br><i>например: "C:/User/map/" или "/home/username/map/" или "res/map" (относительный путь рассчитывается от корня проекта)</i>
	 * */
	public void setXMLPath(String newPath) {
		path = newPath;
	}

	/**
	 * Возвращает актуальный путь к директории, в которой лежит XML-файл с типами объектов, а так же папки с ресурсами. <br>
	 * ps. считается, что ресурсы находятся в той же папке что XML-файл
	 * */
	public String getXMLPath() {
		return path;
	}
	
	/**
	 * Устанавливает имя файла, из которого загружаются сущности. Ещё нужен путь (см. {@link #getXMLPath()})
	 * */
	public void setXMLFileName(String name) {
		this.fileName = name;
	}	
	
	/**
	 * Возвращает имя файла, из которого загружаются ресурсы. Нужен для контроля доступа к нему, а так же избавления от жёстких зависимостей.<br>
	 * <i>Например, если fileName в какой-то момент со String сменится на URL достаточно будет адаптировать геттер а не переписывать 
	 * все места где происходят к нему обращения.</i>
	 * */
	public String getXMLFileName() {
		return fileName;
	}
	
	/**
	 * Перегрузка {@link #load()}.<br>
	 * Использует {@link #setXMLFileName(String)} и {@link #setXMLPath(String)} для того, чтобы сохранить новый путь к XML-файлу и его имя. 
	 * Повторно вызывать их вручную не обязательно.
	 * 
	 * @param directory - папка где хранится XML-файл и ресурсы (см. {@link #getXMLPath()})
	 * @param name - имя XML-файла с определениями типов сущностей
	 * */
	public void load(String directory, String name) {
		setXMLPath(directory);
		setXMLFileName(name);
		try {
			load();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.err.println("Failed to load project! Cause: "+e.getMessage());
		}
	}

	/**
	 * Загружает типы сущностей из файла с заданным именем и расположением.<br>
	 * */
	public void load() throws SAXException, IOException, ParserConfigurationException {		
		listEntity.clear();
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		document = builder.parse(new File(path+fileName));
	    // Получение списка всех элементов objecttype внутри корневого элемента (getDocumentElement возвращает ROOT элемент XML файла).
	    NodeList objecttypeElements = document.getDocumentElement().getElementsByTagName("objecttype");
		for(int i = 0; i < objecttypeElements.getLength(); i++) {
			//System.out.println("---------------------");
			Node objecttype = objecttypeElements.item(i);
			NamedNodeMap attributesObject = objecttype.getAttributes();
			String entityName = new String(attributesObject.getNamedItem("name").getNodeValue());
			//System.out.println("Name: "+entityName);
			parsingElementXMLtoElementList(entityName,objecttype);
			//System.out.println("---------------------");
		}
	}
	
	// эта колбаса парсит сущности из XML в программные объекты внутри модели, является частью внутренней кухни так что обычно её не нужно трогать
	private void parsingElementXMLtoElementList(String entityName,Node objecttype) {
		String newDrawbox = null;
		String newHitbox = null;
		String type = null; 
		Element element = (Element)objecttype;
		NodeList propertyElements = element.getElementsByTagName("property");
		if(propertyElements!=null) {
			for(int i = 0; i < propertyElements.getLength(); i++) {
				Element property = (Element)propertyElements.item(i);
				String propertyName = property.getAttribute("name");
				String defaultProperty = property.getAttribute("default");
				switch(propertyName) {
					case "drawbox":
						newDrawbox = defaultProperty;
						break;
					case "hitbox":
						newHitbox = defaultProperty;
						break;
					case "class":
						type = defaultProperty;
						break;
				}
			}
			Entity e = new Entity(entityName,newDrawbox,newHitbox);
			e.setType(type);
			listEntity.add(e);
		}
	}
	
	/**
	 * Загружает с диска изображение с заданным именем. Подразумевается, что изображение находится {@link #path там же} где
	 * XML-файл. Возвращает null если изображение не найдено.
	 * */
	public BufferedImage loadImageByName(String name) {
		//TODO: сделать кеширование - не дело подгружать одну и ту же картинку по десять раз!
		
		String path = Project.getInstance().getXMLPath();
		String extension = "png";
		// TODO: изображения следует подгружать в отдельном потоке!
		
		try {
			File imageFile = new File(path + name + '.' + extension);
			if (!imageFile.exists())
				throw new FileNotFoundException();
			BufferedImage image = ImageIO.read(imageFile);
			return image;
		} catch (FileNotFoundException fe) {
			logger.warning("Image file \""+path+name+'.'+extension+"\" is not found!");
		} catch (IOException e) {
			logger.warning("Cannot read file \""+path+name+'.'+extension+"\"!");
		}
		return null;
	}
	
	public void addEntity(Entity e) {
		listEntity.add(e);
		Element objecttype = document.createElement("objecttype");
		objecttype.setAttribute("name", e.getName());
		objecttype.setAttribute("color", "000000");//color of entity, needed by Tiled editor
		document.getElementsByTagName("objecttypes").item(0).appendChild(objecttype);
		/*
		 * TODO: set "class" property too (code below)
		Element property = document.createElement("property");
		objecttype.appendChild(property);
		document.appendChild(objecttype);
		property.setAttribute("name", "class");
		property.setAttribute("type", "string");
		property.setAttribute("default", e.getType());
		
		 * creating hitbox or drowbox attributes in advance is insufficient
		 * they will be added while saving
		 */
	}
	
	
	public void removeEntity(Entity e) {
		listEntity.remove(e);
		NodeList nl = document.getElementsByTagName("objecttype");
		for(int i = 0; i < nl.getLength(); i++) {
			Node objecttype = nl.item(i);
			if(objecttype.getAttributes().getNamedItem("name").getNodeValue().equals(e.getName()))
				document.getElementsByTagName("objecttypes").item(0).removeChild(objecttype);
		}
	}
	
	public Entity getEntity(int id) {
		return listEntity.get(id);
	}
	
	/**
	 * Возвращает объект сущности с заданным именем, или вбрасывает исключение, если такой сущности не существует.
	 * */
	public Entity getEntityByName(String name) throws Exception {
		//debug print
		//System.out.println("----- session started ------");
		for(Entity e: listEntity) {
			//System.out.println("we need "+ name +" we got "+e.getName());
			if(e.getName().equals(name)) 
				return e;
		}
		
		// можно было бы и просто null возвращать, но обращение за несуществующей сущностью само по себе нехороший прецедент
		// так что исключение призвано обратить внимание пользователя, если такое действительно случится
		throw new Exception("No entity with such name!");
	}
	
	public void save() {
		/*
		 * thanks to addEntity() and removeEntity() our Document and listEntity
		 * are interchangeable - they content exactly the same set of objects.
		 * So we'll iterate over Document instead listEntity. It's more convient
		 * in this case.
		 * */
		NodeList objecttypes = document.getElementsByTagName("objecttype");
		for(int i = 0; i < objecttypes.getLength(); i++) {
			Node objecttype = objecttypes.item(i);
			NodeList propertiesList = objecttype.getChildNodes();
			for(int j = 0; j < objecttypes.getLength(); j++) {
				Element property = (Element) propertiesList.item(j);
				NamedNodeMap attributes = property.getAttributes();
				String name = attributes.getNamedItem("name").getNodeValue();
				Entity entity = null;
				try {
					entity = getEntityByName(name);
					
				} catch (Exception e) {
					//if there is no entity with such name we'll skip further parsing
					System.err.println("Entity "+name+" is not found");
				}
			}
		}
		
		// parsing complete, writing to file now
		writeXML();
	}
	
	private void writeXML() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new FileOutputStream(path));
			transformer.transform(source, result);
		} catch (TransformerException | FileNotFoundException e) {
			System.err.println("Saving project is unsuccsessfull! Erorr is: "+e);
		}
	}
		
	public void PrintEntitys() {
		// TODO Auto-generated method stub
		for(Entity ent:listEntity) {
			ent.PrintEntity();
		}
		
	}

	@Override
	public Iterator<Entity> iterator() {
		return listEntity.iterator();
	}

	/** 
	 * Лучше бы пользоваться этой функцией поменьше - почти всю работу вполне можно сделать через интерфейс Project<br>
	 * Например, если нужно перебрать все сущности, стоит использовать цикл foreach с использованием инстанса Project, например:<br> 
	 * {@code for(Entity e: Project.getInstance()){ *тут клиентский код* }}
	 * */
	@Deprecated
	public List<Entity> getListEntity() {
		return listEntity;
	}
}
//в момент окончания рисования, в зависимости в какой мы рисуем вкладке хитбокса,
//в зависимости от того в какой панельке(jpanel)и подклассе интерфейса Editable
//мы дорисовали фигуру, такой форматер и создается.
//хотя не стоит забывать про сейв(не знаю зачем, но стоит подумать, когда сяду снова делать)