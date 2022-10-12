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
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Cluster;
import model.Titik;
import org.math.plot.Plot3DPanel;

/**
 *
 * @author Saiful
 */
public class Main3DForm extends javax.swing.JFrame {

//Number of Clusters. This metric should be related to the number of titiks
    private int typeCluster = 3; 
    private int jmlCluster = 0;    
    //Number of Titiks
    private int jmlTitik = 0;
    //Min and Max X and Y
    private int koordinatMin = 0;
    private int koordinatMax = 0;
    
    private List<Titik> titiks;
    private List<Cluster> clusters;
    /**
     * Creates new form MainForm
     */
    public Main3DForm() {
        initComponents();
        this.titiks = new ArrayList();
    	this.clusters = new ArrayList(); 
        
        //SET CENTER SCREEN
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }
    
     //Initializes the process
    public void init() {
    	//Create Titiks
    	titiks = Titik.createRandomTitiks(koordinatMin,koordinatMax,jmlTitik,typeCluster); 
        
        //Show Data To Table
        setDataAwal(); 
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
        DefaultTableModel model = (DefaultTableModel) jtCentroid.getModel();
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            Vector v = new Vector(); 
            v.add(String.valueOf(cluster.getId()));
            v.add(String.valueOf(cluster.getCentroid().getX()));
            v.add(String.valueOf(cluster.getCentroid().getY()));
            v.add(String.valueOf(cluster.getCentroid().getZ()));
            model.addRow(v);
            num = num+1;
        }
    }
    
    //Show Data To Table
    private void setDataAwal(){
        DefaultTableModel model = (DefaultTableModel) jtAwal.getModel();
        model.setRowCount(0);
        int num = 1;
        for(Titik titik : titiks){  
            Vector v = new Vector(); 
            v.add(String.valueOf(num));
            v.add(String.valueOf(titik.getX()));
            v.add(String.valueOf(titik.getY())); 
            v.add(String.valueOf(titik.getZ())); 
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
    		c.plotCluster();
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
        DefaultTableModel model = (DefaultTableModel) jtHasil.getModel();
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            for(Titik titik : cluster.getTitiks()){
                Vector v = new Vector(); 
                v.add(String.valueOf(num));
                v.add(String.valueOf(titik.getX()));
                v.add(String.valueOf(titik.getY())); 
                v.add(String.valueOf(titik.getZ())); 
                v.add(String.valueOf(cluster.getId())); 
                model.addRow(v);
                num = num+1;
            }
        }
    }
    
    
    private void getCentroidHasil(){
        DefaultTableModel model = (DefaultTableModel) jtCentroidHasil.getModel();
        model.setRowCount(0);
        int num = 1;
        for(Cluster cluster : clusters){  
            Vector v = new Vector(); 
            v.add(String.valueOf(cluster.getId()));
            v.add(String.valueOf(cluster.getCentroid().getX()));
            v.add(String.valueOf(cluster.getCentroid().getY()));
            v.add(String.valueOf(cluster.getCentroid().getZ()));
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
    		Titik titik = new Titik(aux.getX(),aux.getY(),aux.getZ());
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
                sumZ += titik.getZ();
            }
             
            if(n_titiks > 0) {
            	double newX = sumX / n_titiks;
            	double newY = sumY / n_titiks;
            	double newZ = sumZ / n_titiks;
                cluster.getCentroid().setX(newX);
                cluster.getCentroid().setY(newY);
                cluster.getCentroid().setZ(newZ);
            }  
        } 
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtCentroid = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtJmlCluster = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtJmlData = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMinKoor = new javax.swing.JTextField();
        txtMaxKoor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtHasil = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtAwal = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtCentroidHasil = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jtCentroid.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y", "Z"
            }
        ));
        jScrollPane1.setViewportView(jtCentroid);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Jumlah Cluster");

        txtJmlCluster.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJmlClusterActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Aplikasi Clustering Dengan K-Means");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Jumlah Data");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Min Koordinat");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Max Koordinat");

        btnGenerate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel6.setText("Hasil Cluster");

        jtHasil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y", "Z", "Cluster"
            }
        ));
        jScrollPane2.setViewportView(jtHasil);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Data Awal ");

        jtAwal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nomor", "X", "Y", "Z"
            }
        ));
        jScrollPane3.setViewportView(jtAwal);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Centroid Awal");

        jtCentroidHasil.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cluster", "X", "Y", "Z"
            }
        ));
        jScrollPane4.setViewportView(jtCentroidHasil);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setText("Centroid Hasil");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton1.setText("Diagram");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButton2.setText("Kembali");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jScrollPane3))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel3))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtJmlCluster)
                                            .addComponent(txtJmlData, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(98, 98, 98)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4)
                                            .addComponent(jLabel5))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtMinKoor)
                                            .addComponent(txtMaxKoor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnGenerate)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton2)))
                                .addGap(144, 144, 144)))
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtJmlCluster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(txtJmlData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMinKoor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtMaxKoor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerate)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtJmlClusterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJmlClusterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJmlClusterActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here: 
        if(txtJmlCluster.getText().equals("") || txtJmlCluster.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "Jumlah cluster tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtJmlCluster.getText().equals("1")){
            JOptionPane.showMessageDialog(null, "Jumlah cluster harus lebih besar dari 1", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtJmlData.getText().equals("")  || txtJmlData.getText() == "0"){
            JOptionPane.showMessageDialog(null, "Jumlah titik Tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(Integer.parseInt(txtJmlCluster.getText()) >= Integer.parseInt(txtJmlData.getText())){
            JOptionPane.showMessageDialog(null, "Jumlah cluster tidak boleh lebih besar sama dengan jumlah titik", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtMinKoor.getText().equals("")){
            JOptionPane.showMessageDialog(null, "Koordinat minimal tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(txtMaxKoor.getText().equals("") || txtJmlCluster.getText().equals("0")){
            JOptionPane.showMessageDialog(null, "Koordinat maksimal Tidak boleh kosong", "Alert", JOptionPane.ERROR_MESSAGE);
        }else if(Integer.parseInt(txtMaxKoor.getText()) <= Integer.parseInt(txtMinKoor.getText())){
            JOptionPane.showMessageDialog(null, "Koordinat maksimal harus lebih besar dari pada koordinat minimal", "Alert", JOptionPane.ERROR_MESSAGE);
        }else{
            this.titiks = new ArrayList();
            this.clusters = new ArrayList();
            jmlCluster = Integer.parseInt(txtJmlCluster.getText());
            jmlTitik = Integer.parseInt(txtJmlData.getText()); 
            koordinatMin = Integer.parseInt(txtMinKoor.getText());
            koordinatMax = Integer.parseInt(txtMaxKoor.getText());

            init();
            calculate();
        }
        
        
        
        
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        if(clusters.size() <= 0){
            JOptionPane.showMessageDialog(null, "Harap generate cluster terlebih dahulu", "Alert", JOptionPane.ERROR_MESSAGE);
        }else{
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
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        new MainForm().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(Main3DForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main3DForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main3DForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main3DForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main3DForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jtAwal;
    private javax.swing.JTable jtCentroid;
    private javax.swing.JTable jtCentroidHasil;
    private javax.swing.JTable jtHasil;
    private javax.swing.JTextField txtJmlCluster;
    private javax.swing.JTextField txtJmlData;
    private javax.swing.JTextField txtMaxKoor;
    private javax.swing.JTextField txtMinKoor;
    // End of variables declaration//GEN-END:variables
}
