package com.azure.digitrecognition;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PixelGrabber {
	private BufferedImage image;
	private File test;
	
	private int width;
	private int height;
	private String alpha;
	private String format;

	public PixelGrabber(String directory, int cl) throws IOException {
		File input = new File(directory);
		image = ImageIO.read(input);
		width = image.getWidth();
		height = image.getHeight();

		alpha = "";

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Color c = new Color(image.getRGB(j, i));
				int color = (c.getBlue()) > 128 ? 0 : 1;
				alpha += color + ",";
			}
		}
		alpha += cl;
	}

	public void createArff() throws IOException {
		test = new File("res/test.arff");
		FileInputStream input = new FileInputStream("res/format.arff");
		FileOutputStream out = new FileOutputStream(test);
		
		format = "";
		int data;
		while((data = input.read()) != -1) {
			format += (char)data;
		}
		
		format += alpha;
		
		for(int i = 0; i < format.length(); i++)
			out.write((int) format.charAt(i));
		
		input.close();
		out.close();
//		test.deleteOnExit();
	}
	
	public static void main(String[] args) throws IOException {
		new PixelGrabber("E:/num.png", 2).createArff();
	}
}