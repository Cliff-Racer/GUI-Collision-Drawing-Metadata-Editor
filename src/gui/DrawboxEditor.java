package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import model.Drawbox;
import model.Point;

public class DrawboxEditor extends Editable {
	
	private List<Point> drawboxPoints;
	private List<Point> basePoints;
	Logger logger = Logger.getLogger("gui.HitboxRectangleEditor");
	
	Point currentPoint = new Point(0, 0);

    DrawboxEditor(ListGUI listGUI) {
        super(listGUI);

        logger.setLevel(Level.CONFIG);
    }


	@Override
    public void paintLoadData(Graphics2D g) {



    }

	@Override
	public void drawing(Graphics2D g) {
		Drawbox drawbox = entity.getDrawbox();
        drawboxPoints = drawbox.getDrawboxlistPoints();
        basePoints = drawbox.getbaseListPoints();
        logger.finest("drawbox point size: "+ drawboxPoints.size());
        for(Point p: drawboxPoints) 
            logger.finest("DrawBoxPoint: ["+ p.x + ":"+p.y+"]");
        for(Point p: basePoints) 
            logger.finest("BasePoint: ["+ p.x + ":"+p.y+"]");
        
		if(drawboxPoints.size() >= 1 && drawboxPoints.size() < 4) {
			Point lastPoint = drawboxPoints.get(drawboxPoints.size()-1);
			g.drawLine((int)lastPoint.x, (int)lastPoint.y, (int)currentPoint.x, (int)currentPoint.y);
	        for(int i = 0; i < drawboxPoints.size()-1;i++) {
	            int x1 = (int)drawboxPoints.get(i).x;
	            int y1 = (int)drawboxPoints.get(i).y;
	            int x2 = (int)drawboxPoints.get(i+1).x;
	            int y2 = (int)drawboxPoints.get(i+1).y;
	            g.drawLine(x1, y1, x2, y2);
	        }
		} else {
	        for(int i = 0; i < drawboxPoints.size();i++) {
	            int x1 = (int)drawboxPoints.get(i % drawboxPoints.size()).x;
	            int y1 = (int)drawboxPoints.get(i % drawboxPoints.size()).y;
	            int x2 = (int)drawboxPoints.get((i+1) % drawboxPoints.size()).x;
	            int y2 = (int)drawboxPoints.get((i+1) % drawboxPoints.size()).y;
	            g.drawLine(x1, y1, x2, y2);
	        }
//	         ОТРИСОВКА ОСНОВАНИЯ
	        g.setColor(Color.BLUE);
	        for(int i = 0; i < basePoints.size()-1;i++) {
	            int x1 = (int)basePoints.get(i).x;
	            int y1 = (int)basePoints.get(i).y;
	            int x2 = (int)basePoints.get(i+1).x;
	            int y2 = (int)basePoints.get(i+1).y;
	            g.drawLine(x1, y1+3, x2, y2+3);
	        }
		}
	}

	@Override
	public void saveDataInEntity() { }


	@Override
	public void mousePressed(MouseEvent e) {
		if(drawboxPoints.size() < 4) {
			Point p = new Point(e.getX(), e.getY());
			drawboxPoints.add(p);
			if(drawboxPoints.size() == 4) {
				Point baseStart = drawboxPoints.get(0), 
					  baseEnd = drawboxPoints.get(1);
				for(Point pseudo: drawboxPoints) { // если не будет цикла в цикле - не выйдет сравнить всех со всеми
					for(Point vertex: drawboxPoints) { // что приведёт к артефактам построения основания
						if(vertex != baseStart && vertex != baseEnd) {
							if(vertex.y >= baseStart.y) baseStart = vertex;
							else if(vertex.y >= baseEnd.y) baseEnd = vertex;
						}
					}
				}
				basePoints.add(baseStart);
				basePoints.add(baseEnd);
			}
		} 
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentPoint.x = e.getX();
		currentPoint.y = e.getY();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTabbedPane parent = (JTabbedPane) getParent();
		if(parent.getSelectedComponent() == this){
			if(entity != null) {
				entity.getDrawbox().getDrawboxlistPoints().clear();
				entity.getDrawbox().getbaseListPoints().clear();
				repaint();
			}
		}
	}
	
}
