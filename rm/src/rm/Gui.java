package rm;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  
import com.ireasoning.protocol.*;
import com.ireasoning.protocol.snmp.*;
import rm.BgpTable;
import rm.CustomTableCellRender;


public class Gui extends Thread{
	 private JFrame f; 
	 private Panel header = new Panel();
	 private Panel center = new Panel();
	 private String globalIp="";
	 private Label updateTime;
	
	 public Gui() throws IOException{  
		
	    f=new JFrame("BGP table");
	    f.setLayout(new GridLayout(2, 1));
	    header.setLayout(new GridLayout(3, 1));
	    center.setLayout(new BorderLayout());
	    updateTime=new Label("");  
	    f.add(header);
	    f.add(center);
	    addTextButton();
	    this.start();
	    f.setSize(1000,400);    
	    f.setVisible(true);  
	 } 
	 private void addTextButton() {
		 
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		 Date date = new Date(System.currentTimeMillis());
		 
		 Panel header1 = new Panel(new BorderLayout());
		 Panel header2 = new Panel();
		 Panel header3 = new Panel();
		 TextField tf= new TextField(30);
		 Button b=new Button("Show Table");
		  b.addActionListener(new ActionListener(){  
			    public void actionPerformed(ActionEvent e){  
			            String ip = tf.getText(); 
			            globalIp=ip;
			            
			            try {
							drawTable();
							updateTime.setText(formatter.format(date));
							
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
			        }  
			    });
		 header.add(header1);
		 header.add(header2);
		 header.add(header3);
		 header1.add(updateTime,BorderLayout.CENTER);
		 header2.add(tf);
		 header3.add(b);
		 
	 }
	 private void drawTable() throws IOException {
			BgpTable bgpTable = new BgpTable(globalIp);
			SnmpDataType[][] fullBgpTable = new SnmpDataType[bgpTable.getLength()][9];
			final String[] titles = {"Network ", 
									 "Next hop ",
									 "Metric ",
									 "LocalPreference" ,
									 "AsPath ",
									 "Origin ",
									 "Atomic Aggregate ",
									 "Aggregator As ",
									 "Aggregator Addr "};
			List<Integer> redovi = new ArrayList<Integer>();
			
			for (int i = 0; i < bgpTable.getLength() ; i++) {
				fullBgpTable[i][0] = bgpTable.getNetworksAtt()[i].getValue();
				fullBgpTable[i][1]  = bgpTable.getNextHopAtt()[i].getValue();
				SnmpDataType bestRoute = bgpTable.getBestRouteAtt()[i].getValue();
			
				SnmpInt bestR = new SnmpInt((SnmpInt)bestRoute);
				if (bestR.getValue()==2) redovi.add(i);
				
				fullBgpTable[i][2] = bgpTable.getMedAtt()[i].getValue();
				SnmpInt metric = new SnmpInt((SnmpInt)fullBgpTable[i][2]);
				if(metric.getValue()== -1 ) {
					SnmpOctetString newMetric=new SnmpOctetString("");
					fullBgpTable[i][2]=newMetric;
				}
				
				fullBgpTable[i][3]  = bgpTable.getLpAtt()[i].getValue();
				SnmpInt localPreference = new SnmpInt((SnmpInt)fullBgpTable[i][3]);
				if(localPreference.getValue()== -1 ) {
					SnmpOctetString newlocalPreference=new SnmpOctetString("");
					fullBgpTable[i][3]=newlocalPreference;
				}
				fullBgpTable[i][4]  = bgpTable.getAsPathAtt()[i].getValue();
				fullBgpTable[i][5] = bgpTable.getOriginAtt()[i].getValue();
				fullBgpTable[i][6] = bgpTable.getAtomicAggregateAtt()[i].getValue();
				fullBgpTable[i][7] = bgpTable.getAggregatorAsAtt()[i].getValue();
				fullBgpTable[i][8] = bgpTable.getOriginAtt()[i].getValue();	
			}
			for (int i = 0; i < bgpTable.getLength() ; i++) {
				SnmpOctetString b=new SnmpOctetString((SnmpOctetString)fullBgpTable[i][4]);
				if(b.getLength()>2) {
					String strangeAsPath=b.toHexString2();
					String AsPath="";
					int br = b.getLength();
					br=br-3;
					strangeAsPath=strangeAsPath.substring(11);
					while(br > 0) {
						if(br % 2 == 0) {
							strangeAsPath=strangeAsPath.substring(3);
							br--;
							continue;
						}
						char tek=strangeAsPath.charAt(1);
						AsPath=AsPath+" "+tek;
						br--;
						if(br!= 0)strangeAsPath=strangeAsPath.substring(3);
					}
					SnmpOctetString newAsPath=new SnmpOctetString(AsPath);
					fullBgpTable[i][4]=newAsPath;	
				}
				else {
					SnmpOctetString newAsPath=new SnmpOctetString("");
					fullBgpTable[i][4]=newAsPath;
				}
			}
			
		    JTable jt=new JTable(fullBgpTable,titles);   
		    
		    
		    jt.setRowHeight(25);
		    jt.getColumnModel().getColumn(2).setPreferredWidth(10);
		    jt.getColumnModel().getColumn(3).setPreferredWidth(10);
		    jt.getColumnModel().getColumn(5).setPreferredWidth(10);
		    jt.getColumnModel().getColumn(6).setPreferredWidth(16);
		    jt.getColumnModel().getColumn(7).setPreferredWidth(16);
		    jt.getColumnModel().getColumn(8).setPreferredWidth(16);
		    
		    jt.setBounds(30,40,200,300);  
		    jt.setDefaultRenderer(Object.class, new CustomTableCellRender(redovi));;
		    JScrollPane sp=new JScrollPane(jt);
		    center.removeAll();
		    center.add(sp, BorderLayout.CENTER); 
		    f.setVisible(true); 
			
		    
		}
	 public void run(){
		 try {
			 while(!interrupted()) {
				 if(globalIp != "") {
					 
					 Thread.sleep(5000);
					 drawTable();
				     SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
					 Date date = new Date(System.currentTimeMillis());
					 updateTime.setText(formatter.format(date)); 
				 }
				
			 }
			
			
			
		} catch (InterruptedException | IOException e) {
			
		}
	    }
	 
	
}
