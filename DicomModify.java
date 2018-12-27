/**
 * @author ${Surajit Kundu}
 *
 * ${Read the Patient PHI from a DICOM Image and hide the sensetive data to anonymize the dicom image}
 */

package skdcmManipulation;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.Tag;
import org.dcm4che2.data.VR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DicomModify{
	Logger logger = LoggerFactory.getLogger(DicomModify.class);
	String gmodfilePath = "";
	
    public static void writeFile(DicomObject obj, File f) {
        //File f = new File(copyServer + fileName);
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DicomOutputStream dos = new DicomOutputStream(bos);
        try {
            dos.writeDicomFile(obj);
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        } finally {
            try {
                dos.close();
            } catch (IOException ignore) {
            }
        }
    }	
	
	public void setPath(String path){
		gmodfilePath = path;
	}
	public String getPath(){
		return gmodfilePath;
	}

	public String[][] readDicom(File dcmFile){
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		try{
		DicomInputStream dis = new DicomInputStream(dcmFile);
		DicomObject dcmObj = dis.readDicomObject();		
		Iterator<DicomElement> iter = dcmObj.iterator();
			while (iter.hasNext()) {
				DicomElement element = iter.next();
				int tag = element.tag();
				VR v = element.vr();
				if(!v.toString().equalsIgnoreCase("UN")){
				String arr[] = new String[5];
					arr[0] = String.valueOf(tag); 
					arr[1] = String.valueOf(((DicomObject) dcmObj).vm(tag));
					arr[2] = ((DicomObject) dcmObj).vrOf(tag).toString();
					arr[3] = ((DicomObject) dcmObj).nameOf(tag).toString(); 
					arr[4] = dcmObj.getString(tag);
					data.add(arr);	
				}					
			}
		
		}catch(Exception ex){System.out.println(ex.toString());}
        String[][] result = data.toArray(new String[data.size()][0]);		
		return result;		
	}	
	
	public String autoModifyDCM(File dcmFile){
		String dcmURI = "";
		try{
			DicomInputStream dis = new DicomInputStream(dcmFile);
			DicomObject dcmObj = dis.readDicomObject();

				dcmObj.putString(Tag.PatientName, VR.PN, "*");
				dcmObj.putString(Tag.PatientBirthDate, VR.DA, "*");
				dcmObj.putString(Tag.PatientAge, VR.AS, "*");
				dcmObj.putString(Tag.PatientSex	, VR.CS, "*");
				dcmObj.putString(Tag.Modality, VR.CS, "*");
				dcmObj.putString(Tag.AccessionNumber, VR.SH, "*");
				
				dcmObj.putString(Tag.StudyDate, VR.DA, "00000000");
				dcmObj.putString(Tag.SeriesDate, VR.DA, "00000000");
				dcmObj.putString(Tag.ContentDate, VR.DA, "00000000");
				dcmObj.putString(Tag.InstanceCreationDate, VR.DA, "00000000");
				dcmObj.putString(Tag.StudyTime, VR.TM, "000000");
				dcmObj.putString(Tag.SeriesTime, VR.TM, "000000");
				dcmObj.putString(Tag.ContentTime, VR.TM, "000000");		

		String DCMPath = System.getProperty("user.dir")+"/"+dcmObj.getString(Tag.PatientID )+"/"+dcmObj.getString(Tag.StudyInstanceUID)+"/"+dcmObj.getString(Tag.SeriesInstanceUID)+"/"+dcmObj.getString(Tag.SOPInstanceUID)+"/";
		File modifiedDCMDir = new File(DCMPath);
			if(! modifiedDCMDir.exists())
			{
				modifiedDCMDir.mkdirs();
			}			
			String cDCMFile = dcmObj.getString(Tag.SOPInstanceUID)+".dcm";
			dcmURI = modifiedDCMDir+"/"+cDCMFile;	
			File f = new File(dcmURI);			
			writeFile(dcmObj, f);
			dis.close();
			
		}catch(Exception ex){System.out.println("Err : "+ex.toString());}
		return dcmURI;
	}
	
	public String[][] autoModifyDCMandRead(File dcmFile){
		File f =null ;
		try{
			DicomInputStream dis = new DicomInputStream(dcmFile);
			DicomObject dcmObj = dis.readDicomObject();

				dcmObj.putString(Tag.PatientName, VR.PN, "*");
				dcmObj.putString(Tag.PatientBirthDate, VR.DA, "*");
				dcmObj.putString(Tag.PatientAge, VR.AS, "*");
				dcmObj.putString(Tag.PatientSex	, VR.CS, "*");
				dcmObj.putString(Tag.PatientWeight	, VR.CS, "*");
				dcmObj.putString(Tag.Modality, VR.CS, "*");
				dcmObj.putString(Tag.AccessionNumber, VR.SH, "*");
				
				dcmObj.putString(Tag.InstitutionName, VR.PN, "*");
				dcmObj.putString(Tag.Manufacturer, VR.PN, "*");
				dcmObj.putString(Tag.ReferringPhysicianName, VR.PN, "*");
				dcmObj.putString(Tag.StationName, VR.PN, "*");
				dcmObj.putString(Tag.NameOfPhysiciansReadingStudy, VR.PN, "*");
				dcmObj.putString(Tag.HumanPerformerName , VR.PN, "*");
				dcmObj.putString(Tag.StudyDescription, VR.PN, "*");
				
				dcmObj.putString(Tag.StudyDate, VR.DA, "00000000");
				dcmObj.putString(Tag.SeriesDate, VR.DA, "00000000");
				dcmObj.putString(Tag.ContentDate, VR.DA, "00000000");
				dcmObj.putString(Tag.AcquisitionDate, VR.DA, "00000000");
				dcmObj.putString(Tag.InstanceCreationDate, VR.DA, "00000000");
				dcmObj.putString(Tag.StudyTime, VR.TM, "000000");
				dcmObj.putString(Tag.SeriesTime, VR.TM, "000000");
				dcmObj.putString(Tag.ContentTime, VR.TM, "000000");		
				dcmObj.putString(Tag.AcquisitionTime, VR.TM, "000000");		

		String DCMPath = System.getProperty("user.dir")+"/"+dcmObj.getString(Tag.PatientID )+"/"+dcmObj.getString(Tag.StudyInstanceUID)+"/"+dcmObj.getString(Tag.SeriesInstanceUID)+"/"+dcmObj.getString(Tag.SOPInstanceUID)+"/";
		File modifiedDCMDir = new File(DCMPath);
			if(! modifiedDCMDir.exists())
			{
				modifiedDCMDir.mkdirs();
			}			
			String cDCMFile = dcmObj.getString(Tag.SOPInstanceUID)+".dcm";
			setPath(cDCMFile);
			String dcmURI = modifiedDCMDir+"/"+cDCMFile;				
			 f = new File(dcmURI);			
			writeFile(dcmObj, f);
			dis.close();
			
		}catch(Exception ex){System.out.println("Err : "+ex.toString());}
		return readDicom(f);
	}	


	
}
