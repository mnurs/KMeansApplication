/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saiful
 */
public class Titik {
    private double x = 0;
    private double y = 0;
    private double z = 0;
    private int cluster_number = 0;
 
    public Titik(double x, double y, double z)
    {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    
    public Titik(double x, double y)
    {
        this.setX(x);
        this.setY(y); 
    } 
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double getX()  {
        return this.x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double getY() {
        return this.y;
    }

    /**
     * @return the z
     */
    public double getZ() {
        return this.z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(double z) {
        this.z = z;
    }
    
    public void setCluster(int n) {
        this.cluster_number = n;
    }
    
    public int getCluster() {
        return this.cluster_number;
    }
    
    //Calculates the distance between two titiks.
    public static double distance(Titik p, Titik centroid,int type) {
        double hasil = 0.0;
        if(type == 2){
            hasil = Math.pow((centroid.getX() - p.getX()), 2) + Math.pow((centroid.getY() - p.getY()), 2);
        }else{
            hasil = Math.pow((centroid.getX() - p.getX()), 2) + Math.pow((centroid.getY() - p.getY()), 2) + Math.pow((centroid.getZ() - p.getZ()), 2);
        }
        
        return Math.sqrt(hasil);
    }
    
    //Creates random titik
    public static Titik createRandomTitik(int min, int max,int type) {
    	Random r = new Random();
    	double x = min + (max - min) * r.nextDouble();
    	double y = min + (max - min) * r.nextDouble();
    	double z = min + (max - min) * r.nextDouble();
        if(type == 2){ 
            return new Titik(x,y);
        }else{ 
            return new Titik(x,y,z);
        }
    }
    
    public static List<Titik> createRandomTitiks(int min, int max, int number,int type) {
    	List<Titik> titiks = new ArrayList(number);
    	for(int i = 0; i < number; i++) {
    		titiks.add(createRandomTitik(min,max,type));
    	}
    	return titiks;
    } 
    
    public static List<Titik> readFile(String path,int type){
        String line = "";
        String splitBy = ";";
    	List<Titik> titiks = new ArrayList(); 
        try {
          //parsing a CSV file into BufferedReader class constructor  
          BufferedReader br = new BufferedReader(new FileReader(path));
          while ((line = br.readLine()) != null)
          //returns a Boolean value  
          {
            String[] data = line.split(splitBy);
            Titik titik;
            if(type == 2){ 
                titik = new Titik(Double.parseDouble(data[0]),Double.parseDouble(data[1]));
                titiks.add(titik); 
            }else{ 
                titik = new Titik(Double.parseDouble(data[0]),Double.parseDouble( data[1]),Double.parseDouble( data[2]));
                titiks.add(titik);  
            } 
            //use comma as separator  
//            System.out.println(data[2]);
          }
        }
        catch(IOException e) {
          e.printStackTrace();
        }
        return titiks;
    }
    public String toString() { 
        return "("+x+","+y+","+z+")"; 
    } 
    public String toString(int type) {
        if(type == 2){ 
            return "("+x+","+y+")";
        }else{ 
            return "("+x+","+y+","+z+")";
        }
    } 
}
