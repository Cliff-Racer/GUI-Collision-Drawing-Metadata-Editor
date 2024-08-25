package model;

import java.util.ArrayList;
import java.util.List;

public class Drawbox {
	private List<Point>  drawboxlistPoints ;//для прямоугольника дравбокса.
	private List<Point>  baseListPoints ;//2 точки для линии основания
	
	public Drawbox(List<Point> drawboxlistPoints){
		this.drawboxlistPoints = new ArrayList<Point>(drawboxlistPoints) ;
		createBasePoint();
	}
	
	public Drawbox(String informationDrawbox){

		if(informationDrawbox!= null) {
			drawboxlistPoints = new ArrayList<Point>();
			baseListPoints = new ArrayList<Point>();
		    String[] informations = informationDrawbox.split(" ");
		    parseStringToDrawbox(informations);
		    createBasePoint();
	    }
	}
	
	private void parseStringToDrawbox(String[] informations) {
		//составляем точки по которым строится прямоугольник, и запихиваем их в лист с точками
		for(int i = 0;i<informations.length;i+=2) {
			float x = Float.parseFloat(informations[i]);
			float y = Float.parseFloat(informations[i+1]);
			Point point = new Point(x, y);
			drawboxlistPoints.add(point);
		}
	}
	private void createBasePoint() {
		//составляем точки по которым строится прямоугольник, и запихиваем их в лист с точками
		if(baseListPoints==null)baseListPoints = new ArrayList<Point>();
		if(drawboxlistPoints.size()>2) {
			for(int i = 0;i<2;i++) {
				float x = drawboxlistPoints.get(drawboxlistPoints.size()-i-1).x;
				float y = drawboxlistPoints.get(drawboxlistPoints.size()-i-1).y+3;
				Point point = new Point(x, y);
				baseListPoints.add(point);
			}
		}
	}
	
	public List<Point> getDrawboxlistPoints() {
		return drawboxlistPoints;
	}
	
	public List<Point> getbaseListPoints() {
		return baseListPoints;
	}
	public void Print() {
		System.out.println("|||Drawbox:");
		if(baseListPoints!=null&&drawboxlistPoints!=null) {
			System.out.println("drawboxlistPoints:");
			for(Point point: drawboxlistPoints) {
				System.out.print("("+point.x+";"+point.y+") ");
			}
			System.out.println();
			System.out.println("baseListPoints:");
			for(Point point: baseListPoints) {
				System.out.print("("+point.x+";"+point.y+") ");
			}
		}else {
			System.out.println("null");
		}
	}
}
