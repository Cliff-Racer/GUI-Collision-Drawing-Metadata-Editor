package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

public class CustomIcon implements Icon {
	private BufferedImage image;
	private int width, heigth;
	
	public CustomIcon(Image image, int width, int height) {
		this.image = toBufferedImage(image);
		this.width = width;
		this.heigth = height;
	}
	
	private final Color color = new Color(1f, 0.82f, 0.45f, 0.4f);
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        int imageWidth = image.getWidth(),
        	imageHeigth = image.getHeight();
        // setting image position to center
        int resultX = x + (width-imageWidth)/2,
        	resultY = y + (heigth-imageHeigth)/2;
        //g2d.drawRect(x, y, width, heigth);
        g2d.setColor(color);
        g2d.fillOval(x, y, width, heigth);
		g2d.drawImage(image, resultX, resultY, null);
	}

	private BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();
 
	    return bimage;
	}
	
	@Override
	public int getIconWidth() {
		return width;
	}

	@Override
	public int getIconHeight() {
		return heigth;
	}

}
