/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Saiful
 */
public class Cluster {
    public List<Titik> titiks;
    public Titik centroid;
    public int id;

    //Creates a new Cluster
    public Cluster(int id) {
            this.id = id;
            this.titiks = new ArrayList();
            this.centroid = null;
    }

    public List<Titik> getTitiks() {
            return titiks;
    }

    public void addTitik(Titik titik) {
            titiks.add(titik);
    }

    public void setTitiks(List titiks) {
            this.titiks = titiks;
    }

    public Titik getCentroid() {
            return centroid;
    }

    public void setCentroid(Titik centroid) {
            this.centroid = centroid;
    }

    public int getId() {
            return id;
    }

    public void clear() {
            titiks.clear();
    }

    public void plotCluster(int type) {
            System.out.println("[Cluster: " + id+"]");
            System.out.println("[Centroid: " + centroid.toString(type) + "]");
            System.out.println("[Titiks: \n");
            for(Titik p : titiks) {
                    System.out.println(p.toString(type));
            }
            System.out.println("]");
    }
}
