/**
 * @author ${Surajit Kundu}
 *
 * ${Read the Patient PHI from a DICOM Image and export it in json file}
 */
package skdcmManipulation;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import org.dcm4che2.io.DicomInputStream;
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

public class DicomRead{
	Logger logger = LoggerFactory.getLogger(DicomRead.class);
	
	public String WriteJSON(File dcmFile){
		String metadataURI="";
		try{
		DicomInputStream dis = new DicomInputStream(dcmFile);
		DicomObject dcmObj = dis.readDicomObject();
		JSONObject obj = new JSONObject();
		JSONArray list = new JSONArray();		
		Iterator<DicomElement> iter = dcmObj.iterator();
			while (iter.hasNext()) {
				DicomElement element = iter.next();
				int tag = element.tag();
				VR v = element.vr();
				
				JSONObject innerObj = new JSONObject();
					innerObj.put("tag",""+tag+"");
					innerObj.put("VM",""+((DicomObject) dcmObj).vm(tag)+"");
					innerObj.put("VR", ""+((DicomObject) dcmObj).vrOf(tag).toString()+"");
					innerObj.put("attribute", ""+((DicomObject) dcmObj).nameOf(tag).toString()+"");
					innerObj.put("value", ""+dcmObj.getString(tag)+"");
					list.add(innerObj);			
				
				if(!v.toString().equalsIgnoreCase("UN"))
					System.out.println(" : "+((DicomObject) dcmObj).vm(tag)+" : "+((DicomObject) dcmObj).vrOf(tag).toString()+" : "+((DicomObject) dcmObj).nameOf(tag).toString().replaceAll(" ","")+" : "+dcmObj.getString(tag));
			}
		obj.put("metadata",list);
		
		String JSONPath = System.getProperty("user.dir")+"/"+dcmObj.getString(Tag.PatientID )+"/"+dcmObj.getString(Tag.StudyInstanceUID)+"/"+dcmObj.getString(Tag.SeriesInstanceUID)+"/"+dcmObj.getString(Tag.SOPInstanceUID)+"/";
		File MetadataDir = new File(JSONPath);
			if(! MetadataDir.exists())
			{
				MetadataDir.mkdirs();
			}			
		metadataURI = MetadataDir+"/"+dcmObj.getString(Tag.SOPInstanceUID)+".json";
		FileWriter metafile = new FileWriter(metadataURI);
		metafile.write(obj.toJSONString());
		metafile.flush();
		}catch(Exception ex){System.out.println(ex.toString());}	
	return metadataURI; 		
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
	
	
	
}
