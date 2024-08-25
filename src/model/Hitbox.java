package model;

import java.util.ArrayList;
import java.util.List;

public class Hitbox {
	private String shape = null;// shape - Форма хитбокса. Понадобится при написании сохранения. Один из параметров в xml-file.
	private List<Point> listPoints = null;//для прямоугольника - 4 точки. Для круга - надо посмотреть.
	
	public Hitbox(String shape,List<Point> listPoints){
		this.shape = new String(shape);
		this.listPoints = new ArrayList<Point>(listPoints) ;
	}
	
	public Hitbox(String informationHitbox){
		if(informationHitbox!= null) {
			listPoints = new ArrayList<Point>();
		    String[] informations = informationHitbox.split(" ");
		    //в 0-м индексе всегда идет название фигуры.Так сделан наш xml.
		    shape = new String(informations[0]);
		    //заполняем лист точками. Пока что делаю тупо и топорно. Хардкод. Потом можно переделать.
		    if(shape.equals("Rectangle")) {
		    	parseStringToRectangleHitbox(informations);
		    }else if(shape.equals("Circle")) {
		    	parseStringToCircleHitbox(informations);
		    }	
		}
	}
	
	private void parseStringToRectangleHitbox(String[] informations) {
		//составляем точки по которым строится прямоугольник, и запихиваем их в лист с точками
		for(int i = 1;i<informations.length;i+=2) {
			float x = Float.parseFloat(informations[i]);
			float y = Float.parseFloat(informations[i+1]);
			Point point = new Point(x, y);
			listPoints.add(point);
		}
	}
	
	private void parseStringToCircleHitbox(String[] informations) {
		//ЭТО ХАРДКОД.ДА.Но в данном случае, имхо, разумный(в каком то смысле).
		//первая точка где строится круг
			float x1 = Float.parseFloat(informations[1]);
			float y1 = Float.parseFloat(informations[2]);
			Point point1 = new Point(x1, y1);
		//вторая точка x2 - диаметр, а y2 = 0, потому что нам на него пофиг.
		//Просто же не создавать для круга новый класс хитбокса? Нет. Так что так.
			float x2 = Float.parseFloat(informations[3]);
			Point point2 = new Point(x2, 0);
			listPoints.add(point2);
	}
	
	
	
	public void addPoint(Point point) {
		listPoints.add(point);
	}
	
	public void clearPoints() {
		listPoints.clear();
	}
	public String getShape() {
		return shape;
	}
	public List<Point> getListPoints() {
		return listPoints;
	}
	public void Print() {
		System.out.println();
		System.out.println("|||Hitbox:");
		if(shape!=null&&listPoints!=null) {
			System.out.println("shape: " + shape);
			for(Point point: listPoints) {
				System.out.print("("+point.x+";"+point.y+") ");
			}
			System.out.println();
		}else {
			System.out.println("null");
		}
	}
	//дописать функцию возвращения listPonts и shape(форма), если будет нужно.
	// так же при написании функции возвращения нужных координат, надо их сделать целочисленными.
}
