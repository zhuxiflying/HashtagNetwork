package edu.psu.geovista.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/*
 * This class count the frequency of hashtags;
 */
public class HashtagFrequency {
	
	private static String hashtagFolder = "D:\\Data\\HashtagNetwork\\hashtagTweets\\";
	private static HashMap<String,Integer> hashtag_count = null;
	
	public static void main(String[] args) throws IOException {
		
		hashtag_count = new HashMap<String,Integer>();

		File dir = new File(hashtagFolder);
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing) {
			System.out.println(file.getPath());
			hashtagFrequency(file.getPath());
		}

		saveHashtagFrequency();

	}
	
	//count the frequency of hashtags for input data file;
	private static void hashtagFrequency(String fileName) throws IOException
	{
		Reader in = new FileReader(fileName);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String hashtags = record.get("Hashtags_in_text");
			String[] tags = hashtags.split("\\|");
			// remove duplicate
			HashSet<String> tagSet = new HashSet<String>(Arrays.asList(tags));
			ArrayList<String> tagList = new ArrayList<String>(tagSet);
			for(String tag:tagList)
			{
				if(hashtag_count.keySet().contains(tag))
				{
					hashtag_count.put(tag, hashtag_count.get(tag) + 1);
				}
				else
				{
					hashtag_count.put(tag, 1);
				}
			}
		}
	}
	
	
	/*
	 * write hashtag frequency statistics to file; the hashtags are sorted by descending order
	 */
	private static void saveHashtagFrequency() throws IOException
	{
		
		String outputFileName = "D:\\Projects\\HashtagNetwork\\Data\\hashtag_frequency.csv";
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hashtag_count.entrySet()); 
  
        // Sort the list by descending order
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
        
        //write elements to csv file
        FileWriter out = new FileWriter(outputFileName);
		CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Hashtag", "Frequency").print(out);

		for (Map.Entry<String, Integer> element : list) {
			printer.printRecord(element.getKey(),element.getValue());
		}
		out.close();
        
	}
}
