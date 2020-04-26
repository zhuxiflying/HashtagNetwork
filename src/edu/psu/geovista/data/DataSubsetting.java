package edu.psu.geovista.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;


public class DataSubsetting {

	private static String dataFolder = "D:\\Data\\HashtagNetwork\\geotwitter\\";
	private static String hashtagFolder = "D:\\Data\\HashtagNetwork\\hashtagTweets\\";

	public static void main(String[] args) throws IOException {

		File dir = new File(dataFolder);
		File[] directoryListing = dir.listFiles();

		for (File file : directoryListing) {

			subsetFile(file.getName());
		}

	}
	
	/*
	 * subset tweets with hashtags and columns (Tweet_id,Time_of_tweet,User_id,Hashtags_in_text)
	 */
	private static void subsetFile(String fileName) throws IOException {
		String inputFileName = dataFolder + fileName;
		String outputFileName = hashtagFolder + fileName;

		//total count of tweets
		int count = 0;
		//tweets with hashtag
		ArrayList<String[]> tweets = new ArrayList<String[]>();
		

		Reader in = new FileReader(inputFileName);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String hashtags = record.get("Hashtags_in_text");
			count++;
			if (!hashtags.equals("")) {
				String[] tweet = new String[3];
				tweet[0] = record.get("Time_of_tweet");
				tweet[1] = record.get("User_id");
				tweet[2] = record.get("Hashtags_in_text");
				tweets.add(tweet);
			}
		}

		//output statistics of data file;
		System.out.println( FilenameUtils.removeExtension(fileName) + "," + count + "," + tweets.size());

		FileWriter out = new FileWriter(outputFileName);
		CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Time_of_tweet", "User_id", "Hashtags_in_text").print(out);

		for (String[] tweet : tweets) {
			printer.printRecord(tweet);
		}
		out.close();
	}
}
