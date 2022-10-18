/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cluster;
import model.Titik;
import org.math.plot.Plot3DPanel;
import java.io. * ; 
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author Saiful
 */
public class MainManualForm extends javax.swing.JFrame {

//Number of Clusters. This metric should be related to the number of titiks
    private int typeCluster = 0; 
    private int jmlCluster = 0;    
    //Number of Titiks
    private int jmlTitik = 0;
    //Min and Max X and Y
    private int koordinatMin = 0;
    private int koordinatMax = 0;
    
    private String path = "";
    private List<Titik> titiks;
    private List<Cluster> clusters;
    /**
     * Creates new form MainForm
     */
    public MainManualForm() {
        initComponents();
        this.titiks = new ArrayList();
    	this.clusters = new ArrayList(); 
        //SET CENTER SCREEN
        
        
        jPanel1.setVisible(true);
        jPanel2.setVisible(false);
        
        jPanel3.setVisible(true);
        jPanel4.setVisible(false);
        
        jPanel5.setVisible(true);
        jPanel6.setVisible(false);
        
        jPanel7.setVisible(true);
        jPanel8.setVisible(false);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    } 
     //Initializes the process
    public void init() { 
    	//Create Clusters
    	//Set Random Centroids 
    	for (int i = 0; i < jmlCluster; i++) {
    		Cluster cluster = new Cluster(i);
//    		Titik centroid = Titik.createRandomTitik(koordinatMin,koordinatMax);
    		Titik centroid = getRandomTitik();
    		cluster.setCentroid(centroid); 
    		clusters.add(cluster);
    	}  
        //Show Centroid Data To Table
        getCentroid();
    	//Print Initial state
    	plotClusters();
    }
    
    //Show Centroid Data To Table
    private void getCentroid(){  
        DefaultTableModel model; 
        if(typeCluster == 2){ 
            jPanel3.setVisible(false);
            jPanel4.setVisible(true);
           model  = (DefaultTableModel) jtCentroid1.getModel(); 
        }else{ 
            jPanel3.setVisible(true);
            jPanel4.setVisible(false);
           model  = (DefaultTableModel) jtCentroid.getModel();
        } 
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            Vector v = new Vector(); 
            v.add(String.valueOf(cluster.getId()));
            v.add(String.valueOf(cluster.getCentroid().getX()));
            v.add(String.valueOf(cluster.getCentroid().getY()));
            if(typeCluster == 3){ 
                v.add(String.valueOf(cluster.getCentroid().getZ()));
            }
            model.addRow(v);
            num = num+1;
        }
    }
    
    //Show Data To Table
    private void setDataAwal(){
        DefaultTableModel model;
        if(typeCluster == 2){ 
            jPanel1.setVisible(false);
            jPanel2.setVisible(true);
           model = (DefaultTableModel) jtAwal1.getModel(); 
        }else{ 
            jPanel1.setVisible(true);
            jPanel2.setVisible(false);
           model = (DefaultTableModel) jtAwal.getModel();
        } 
        model.setRowCount(0);
        int num = 1;
        for(Titik titik : titiks){  
            Vector v = new Vector(); 
            v.add(String.valueOf(num));
            v.add(String.valueOf(titik.getX()));
            v.add(String.valueOf(titik.getY()));  
            if(typeCluster == 3){ 
                v.add(String.valueOf(titik.getZ())); 
            }
            model.addRow(v);
            num = num+1;
        }
        
    }
    
    public Titik getRandomTitik()
    {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(titiks.size()); 
        return titiks.get(index);
    }
    
    private void plotClusters() {
    	for (int i = 0; i < jmlCluster; i++) {
    		Cluster c = clusters.get(i);
    		c.plotCluster(typeCluster);
    	}
    }
    
    //The process to calculate the K Means, with iterating method.
    public void calculate() {
        boolean finish = false;
        int iteration = 0;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
        	//Clear cluster state 
        	clearClusters();  
        	
        	List<Titik> lastCentroids = getCentroids(); 
        	
        	//Assign titiks to the closer cluster
        	assignCluster(); 
            
                //Calculate new centroids.
        	calculateCentroids(); 
        	
        	iteration++;
        	
        	List<Titik> currentCentroids = getCentroids(); 
        	
        	//Calculates total distance between new and old Centroids
        	double distance = 0;
        	for(int i = 0; i < lastCentroids.size(); i++) { 
        		distance += Titik.distance(lastCentroids.get(i),currentCentroids.get(i),typeCluster);
        	}
        	System.out.println("====================================");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	System.out.println("===================================="); 
        	plotClusters();
        	System.out.println("====================================");
        	    	
        	if(distance == 0) {
                       setHasilCluster();
                       getCentroidHasil();
        		finish = true;
        	}   
        }
    }
    
    private void setHasilCluster(){   
        DefaultTableModel model; 
        if(typeCluster == 2){ 
            jPanel5.setVisible(false);
            jPanel6.setVisible(true);
           model  = (DefaultTableModel) jtHasil1.getModel();
        }else{ 
            jPanel5.setVisible(true);
            jPanel6.setVisible(false);
           model  = (DefaultTableModel) jtHasil.getModel();
        }  
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            for(Titik titik : cluster.getTitiks()){
                Vector v = new Vector(); 
                v.add(String.valueOf(num));
                v.add(String.valueOf(titik.getX()));
                v.add(String.valueOf(titik.getY()));  
                if(typeCluster == 3){ 
                    v.add(String.valueOf(titik.getZ())); 
                }
                v.add(String.valueOf(cluster.getId())); 
                model.addRow(v);
                num = num+1;
            }
        }
    }
    
    
    private void getCentroidHasil(){  
        DefaultTableModel model; 
        if(typeCluster == 2){ 
            jPanel7.setVisible(false);
            jPanel8.setVisible(true);
           model  = (DefaultTableModel) jtCentroidHasil1.getModel();
        }else{ 
            jPanel7.setVisible(true);
            jPanel8.setVisible(false);
           model = (DefaultTableModel) jtCentroidHasil.getModel();
        }   
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            Vector v = new Vector(); 
            v.add(String.valueOf(cluster.getId()));
            v.add(String.valueOf(cluster.getCentroid().getX()));
            v.add(String.valueOf(cluster.getCentroid().getY())); 
            if(typeCluster == 3){ 
                v.add(String.valueOf(cluster.getCentroid().getZ()));
            }
            model.addRow(v);
            num = num+1;
        }
    }
    
    private void clearClusters() {
    	for(Cluster cluster : clusters) {
    		cluster.clear();
    	}
    }
    
    private List<Titik> getCentroids() {
    	List<Titik> centroids = new ArrayList(jmlCluster);
    	for(Cluster cluster : clusters) {
    		Titik aux = cluster.getCentroid(); 
                Titik titik;
                if(typeCluster == 2){ 
                    titik = new Titik(aux.getX(),aux.getY());
                }else{
                    titik = new Titik(aux.getX(),aux.getY(),aux.getZ());
                } 
    		centroids.add(titik);
    	}
    	return centroids;
    }
    
    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max; 
        int cluster = 0;                 
        double distance = 0.0; 
        
        for(Titik titik : titiks) {
        	min = max;
            for(int i = 0; i < jmlCluster; i++) {
            	Cluster c = clusters.get(i);
                distance = Titik.distance(titik, c.getCentroid(),typeCluster);
                if(distance < min){
                    min = distance;
                    cluster = i;
                }
            }
            titik.setCluster(cluster);
            clusters.get(cluster).addTitik(titik);
        }
    }
    
    private void calculateCentroids() {
        for(Cluster cluster : clusters) {
            Cluster newCluster =  new Cluster(cluster.getId());
            double sumX = 0.0;
            double sumY = 0.0;
            double sumZ = 0.0;
            List<Titik> list = cluster.getTitiks();
            int n_titiks = list.size();
            
            for(Titik titik : list) {
            	sumX += titik.getX();
                sumY += titik.getY();
                if(typeCluster == 3){ 
                    sumZ += titik.getZ();
                }
            }
            Titik new_titik;
            if(n_titiks > 0) {
            	double newX = sumX / n_titiks;
            	double newY = sumY / n_titiks;
                if(typeCluster == 3){ 
                    double newZ = sumZ / n_titiks;
                    new_titik = new Titik(newX, newY, newZ);
                    cluster.setCentroid(new_titik);
                }else{ 
                    new_titik = new Titik(newX, newY);
                    cluster.setCentroid(new_titik);
                } 
            }  
        } 
    }
    
    private void diagram3D() {
        ArrayList<double[]> x = new ArrayList<>();
        ArrayList<double[]> y = new ArrayList<>();
        ArrayList<double[]> z = new ArrayList<>(); 
         
        // create your PlotPanel (you can use it as a JPanel)
//        Plot3DPanel plot = new Plot3DPanel(); 
        Plot3DPanel plot = new Plot3DPanel();
        for(Cluster cluster : clusters){  
            double[] xD = new double[cluster.getTitiks().size()];
            double[] yD = new double[cluster.getTitiks().size()];
            double[] zD = new double[cluster.getTitiks().size()];
            int num = 0;
            for(Titik titik : cluster.getTitiks()){ 
                xD[num] = titik.getX();
                yD[num] = titik.getY();
                zD[num] = titik.getZ(); 
                num++;
            }
            x.add(xD);
            y.add(yD);
            z.add(zD);
        } 
        
        for(Cluster cluster : clusters){  
            System.out.println("X: " + x.get(cluster.getId()).length);
            plot.addBarPlot("Clutter : "+cluster.getId(), x.get(cluster.getId()), y.get(cluster.getId()), z.get(cluster.getId()));
        } 
        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);   
        frame.setVisible(true); 
     }
    
    private void diagram2D() {
        ArrayList<double[]> x = new ArrayList<>();
        ArrayList<double[]> y = new ArrayList<>(); 
         
        // create your PlotPanel (you can use it as a JPanel)
//        Plot3DPanel plot = new Plot3DPanel(); 
        Plot2DPanel plot = new Plot2DPanel();
        for(Cluster cluster : clusters){  
            double[] xD = new double[cluster.getTitiks().size()];
            double[] yD = new double[cluster.getTitiks().size()]; 
            int num = 0;
            for(Titik titik : cluster.getTitiks()){ 
                xD[num] = titik.getX();
                yD[num] = titik.getY(); 
                num++;
            }
            x.add(xD);
            y.add(yD); 
        } 
        
        for(Cluster cluster : clusters){  
            System.out.println("X: " + x.get(cluster.getId()).length);
            plot.addBarPlot("Clutter : "+cluster.getId(), x.get(cluster.getId()), y.get(cluster.getId()));
        } 
        // put the PlotPanel in a JFrame, as a JPanel
        JFrame frame = new JFrame("a plot panel");
        frame.setContentPane(plot);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);   
        frame.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtCentroid1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtCentroid = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtJmlCluster = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jtHasil1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtHasil = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtAwal1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtAwal = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jtCentroidHasil1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtCentroidHasil = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtTipe = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(795, 590));
        getContentPane().setLayout(null);

        jPanel4.setLayout(new java.awt.CardLayout());

        jtCentroid1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y"
            }
        ));
        jScrollPane6.setViewportView(jtCentroid1);

        jPanel4.add(jScrollPane6, "card2");

        getContentPane().add(jPanel4);
        jPanel4.setBounds(510, 200, 240, 140);

        jPanel3.setLayout(new java.awt.CardLayout());

        jtCentroid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y", "Z"
            }
        ));
        jScrollPane1.setViewportView(jtCentroid);

        jPanel3.add(jScrollPane1, "card2");

        getContentPane().add(jPanel3);
        jPanel3.setBounds(510, 200, 240, 140);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Jumlah Cluster");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(60, 90, 84, 14);

        txtJmlCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJmlClusterActionPerformed(evt);
            }
        });
        getContentPane().add(txtJmlCluster);
        txtJmlCluster.setBounds(170, 90, 117, 20);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Aplikasi Clustering Dengan K-Means");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 30, 774, 22);

        btnGenerate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });
        getContentPane().add(btnGenerate);
        btnGenerate.setBounds(200, 140, 100, 23);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Hasil Cluster");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(20, 350, 70, 14);

        jPanel6.setLayout(new java.awt.CardLayout());

        jtHasil1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y", "Cluster"
            }
        ));
        jScrollPane7.setViewportView(jtHasil1);

        jPanel6.add(jScrollPane7, "card2");

        getContentPane().add(jPanel6);
        jPanel6.setBounds(20, 380, 480, 140);

        jPanel5.setLayout(new java.awt.CardLayout());

        jtHasil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y", "Z", "Cluster"
            }
        ));
        jScrollPane2.setViewportView(jtHasil);

        jPanel5.add(jScrollPane2, "card2");

        getContentPane().add(jPanel5);
        jPanel5.setBounds(20, 380, 480, 140);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Data Awal ");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(20, 180, 60, 14);

        jPanel2.setLayout(new java.awt.CardLayout());

        jtAwal1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y"
            }
        ));
        jScrollPane5.setViewportView(jtAwal1);

        jPanel2.add(jScrollPane5, "card2");

        getContentPane().add(jPanel2);
        jPanel2.setBounds(22, 202, 480, 140);

        jPanel1.setLayout(new java.awt.CardLayout());

        jtAwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y", "Z"
            }
        ));
        jScrollPane3.setViewportView(jtAwal);

        jPanel1.add(jScrollPane3, "card2");

        getContentPane().add(jPanel1);
        jPanel1.setBounds(20, 200, 480, 140);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Centroid Awal");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(510, 180, 78, 14);

        jPanel8.setLayout(new java.awt.CardLayout());

        jtCentroidHasil1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y"
            }
        ));
        jScrollPane8.setViewportView(jtCentroidHasil1);

        jPanel8.add(jScrollPane8, "card2");

        getContentPane().add(jPanel8);
        jPanel8.setBounds(510, 380, 240, 140);

        jPanel7.setLayout(new java.awt.CardLayout());

        jtCentroidHasil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y", "Z"
            }
        ));
        jScrollPane4.setViewportView(jtCentroidHasil);

        jPanel7.add(jScrollPane4, "card2");

        getContentPane().add(jPanel7);
        jPanel7.setBounds(510, 380, 240, 140);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Centroid Hasil");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(510, 360, 78, 14);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Diagram");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(320, 140, 90, 23);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Kembali");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(430, 140, 90, 23);
        getContentPane().add(txtTipe);
        txtTipe.setBounds(390, 90, 130, 20);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel11.setText("Tipe");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(320, 94, 50, 10);

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton3.setText("Persiapan Data");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(60, 140, 120, 23);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/360_F_428592351_83V8npDJjl7efE6ESmvH12zpYOMwJiqI.jpg"))); // NOI18N
        getContentPane().add(jLabel10);
        jLabel10.setBounds(0, 0, 780, 560);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtJmlClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJmlClusterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJmlClusterActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here: 
        if(path.equals("")){
            JOptionPane.showMessageDialog(null, "Silahkan import file csv", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtJmlCluster.getText().equals("") || txtJmlCluster.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "Jumlah cluster tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtTipe.getText().equals("") || txtTipe.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "Type cluster tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else{ 
            this.clusters = new ArrayList();
            jmlCluster = Integer.parseInt(txtJmlCluster.getText());  
            typeCluster = Integer.parseInt(txtTipe.getText());
            titiks =  Titik.readFile(path,typeCluster);
            init();
            calculate(); 
        }
        
        
        
        
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(clusters.size() <= 0){
            JOptionPane.showMessageDialog(null, "Harap generate cluster terlebih dahulu", "Alert", JOptionPane.ERROR_MESSAGE);
        }else{
            if(typeCluster == 2){
                diagram2D();
            }else{
                diagram3D();
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new MainForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(txtTipe.getText().equals("") || txtTipe.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "Type cluster tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else{ 
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                path = selectedFile.getAbsolutePath();
                titiks =  Titik.readFile(selectedFile.getAbsolutePath(),Integer.parseInt(txtTipe.getText()));
                typeCluster = Integer.parseInt(txtTipe.getText());
                setDataAwal(); 
                System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainManualForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainManualForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainManualForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainManualForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainManualForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jtAwal;
    private javax.swing.JTable jtAwal1;
    private javax.swing.JTable jtCentroid;
    private javax.swing.JTable jtCentroid1;
    private javax.swing.JTable jtCentroidHasil;
    private javax.swing.JTable jtCentroidHasil1;
    private javax.swing.JTable jtHasil;
    private javax.swing.JTable jtHasil1;
    private javax.swing.JTextField txtJmlCluster;
    private javax.swing.JTextField txtTipe;
    // End of variables declaration//GEN-END:variables
}
