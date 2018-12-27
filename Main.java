/**
 * @author ${Surajit Kundu}
 *
 * ${Provide the GUI to perform the DICOM Manipulation process}
 */

package skdcmManipulation;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import java.util.Arrays;

public class Main extends JFrame {
	JFileChooser chooser = new JFileChooser("C://Work/Program/file/");
	JButton button = new JButton("Choose Dicom file...");
	JButton readButton = new JButton("Read");
	JButton exportButton = new JButton("ExportJSON");
	JButton defaultWButtton = new JButton("Auto Modify");
	JLabel l = new JLabel("No file is choosen");
	File dcmFile;
	
	public Main() {
		super("Dicom Reader");
		Container dcmContainer = getContentPane();
		dcmContainer.setLayout(new FlowLayout());
		dcmContainer.add(button);    
		dcmContainer.add(readButton);    
		dcmContainer.add(exportButton);    
		dcmContainer.add(defaultWButtton);
		dcmContainer.add(l);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int state = chooser.showOpenDialog(null);
				dcmFile = chooser.getSelectedFile();

				if(dcmFile != null && state == JFileChooser.APPROVE_OPTION) {
				  l.setText(dcmFile.getName());
				}
				else if(state == JFileChooser.CANCEL_OPTION) {
				  JOptionPane.showMessageDialog(null, "Canceled");
				}
			}
		});
		
		readButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFrame f=new JFrame();   
				String data[][] = new DicomRead().readDicom(dcmFile);
				String column[]={"TAG","VM","VR","Attribute","Value"};				
				JTable jt=new JTable(data,column); 
				
				jt.setCellSelectionEnabled(true);
				jt.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));				
				jt.setFont(new Font("Serif", Font.PLAIN, 16));				
				jt.setBounds(60,80,500,600);          
				JScrollPane sp=new JScrollPane(jt);    
				f.add(sp);          
				f.setSize(500,700);    
				f.setVisible(true);  
				//dcmContainer.revalidate();				
				
			}
		});
		
		exportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				  JOptionPane.showInputDialog( null,"Exported to : ",new DicomRead().WriteJSON(dcmFile));
			}
		});	
		
		defaultWButtton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f=new JFrame();   
				String data[][] = new DicomModify().autoModifyDCMandRead(dcmFile);
				String column[]={"TAG","VM","VR","Attribute","Value"};				
				JTable jt=new JTable(data,column); 
				
				jt.setCellSelectionEnabled(true);
				jt.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));				
				jt.setFont(new Font("Serif", Font.PLAIN, 16));				
				jt.setBounds(60,80,500,600);          
				JScrollPane sp=new JScrollPane(jt);    
				f.add(sp);          
				f.setSize(500,700);    
				f.setVisible(true); 
				
				JOptionPane.showMessageDialog( null,"Exported to : "+new DicomModify().getPath());
			}
		
		});			
	}
  public static void main(String args[]) {
    JFrame f = new Main();
    f.setBounds(300,300,450,300);
    f.setVisible(true);

    f.setDefaultCloseOperation(
      WindowConstants.DISPOSE_ON_CLOSE);
  
    f.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        System.exit(0);  
      }
    });
  }
}