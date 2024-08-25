package launch;

import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gui.MainGUI;

public class Launcher {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		// Настройка системы логгирования
		// Указываем, в каком формате выводится строка в консоль
		System.setProperty("java.util.logging.SimpleFormatter.format",	
				   "[%1$tF %1$tT] [%4$-7s] %5$s %n");
		
		Logger rootLogger = LogManager.getLogManager().getLogger("");
    	//указываем, какой уровень логгирования учитывается при выводе - всё что меньше заданного уровня откидывается
		//p.s. уровень вывода и уровень регистрации (handler.level | logging.level) это не одно и то же
    	Handler[] handlers = rootLogger.getHandlers();
    	Level lv = Level.ALL;
    	for(int i = 0; i < handlers.length; i++) {
    		handlers[i].setLevel(lv);
    	}
		rootLogger.info("Root Handler Logging level is "+lv.getName());
    	
		MainGUI gui = new MainGUI();
		gui.setVisible(true);
	}

}
