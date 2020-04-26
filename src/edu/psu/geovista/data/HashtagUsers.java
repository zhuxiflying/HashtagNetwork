package edu.psu.geovista.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/*
 * This class implements methods counting the number of users of each hashtag;
 */
public class HashtagUsers {

	private static HashSet<String> targetHashtags = null;
	private static HashMap<String, HashSet<String>> hashtag_users = null;
	private static HashMap<String, Integer> hashtag_userCount = null;

	// This file records all the hashtags and its frequency;
	private static String hashtagFile = "D:\\Projects\\HashtagNetwork\\Data\\hashtag_frequency.csv";

	// Data file folder
	private static String dataFileFolder = "D:\\Data\\HashtagNetwork\\hashtagTweets\\";
	
	
	private static String dataFileFolder2 = "D:\\Projects\\HashtagNetwork\\Data\\hashtag_users\\";
	

	public static void main(String[] args) throws IOException {

//		loadTargetHashTag();
//		System.out.println(targetHashtags.size());
//		countHashtagUsers();
		sumHashtagUsers();
	}
	

	private static void loadTargetHashTag() throws IOException {

		int minThres = 1;
		int maxThres = 2;
		targetHashtags = new HashSet<String>();
		Reader in = new FileReader(hashtagFile);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String hashtags = record.get("Hashtag");
			int frequency = Integer.valueOf(record.get("Frequency"));
			if (frequency >= minThres&&frequency<maxThres) {
				targetHashtags.add(hashtags);
			}
		}
	}

	/*
	 *  count the number of adopters for target hashtags;
	 */
	private static void countHashtagUsers() throws IOException {
		hashtag_users = new HashMap<String, HashSet<String>>();

		File dir = new File(dataFileFolder);
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing) {
			System.out.println(file.getPath());
			countUser(file.getPath());
		}
		saveHashtagUsers();
	}

	/*
	 *  count the number of adopters for one file;
	 */
	private static void countUser(String fileName) throws IOException {
		Reader in = new FileReader(fileName);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String hashtags = record.get("Hashtags_in_text");
			String userId = record.get("User_id");
			String[] tags = hashtags.split("\\|");
			// remove duplicate
			HashSet<String> tagSet = new HashSet<String>(Arrays.asList(tags));
			ArrayList<String> tagList = new ArrayList<String>(tagSet);
			for (String tag : tagList) {
				if (targetHashtags.contains(tag)) {
					if (hashtag_users.keySet().contains(tag)) {
						HashSet<String> users = hashtag_users.get(tag);
						users.add(userId);
						hashtag_users.put(tag, users);
					} else {
						HashSet<String> users = new HashSet<String>();
						users.add(userId);
						hashtag_users.put(tag, users);
					}

				}
			}
		}
	}

	/*
	 * write hashtag user statistics to file;
	 */
	private static void saveHashtagUsers() throws IOException {

		String outputFileName = "D:\\Projects\\HashtagNetwork\\Data\\hashtag_users_0.csv";
		// write elements to csv file
		FileWriter out = new FileWriter(outputFileName);
		CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Hashtag", "Users").print(out);

		for (String hashtag : hashtag_users.keySet()) {
			printer.printRecord(hashtag, hashtag_users.get(hashtag).size());
		}
		out.close();

	}
	
	private static void saveHashtagUserCount() throws IOException {

		String outputFileName = "D:\\Projects\\HashtagNetwork\\Data\\hashtag_users.csv";
		// write elements to csv file
		FileWriter out = new FileWriter(outputFileName);
		CSVPrinter printer = CSVFormat.DEFAULT.withHeader("Hashtag", "Users").print(out);

		for (String hashtag : hashtag_userCount.keySet()) {
			printer.printRecord(hashtag, hashtag_userCount.get(hashtag));
		}
		out.close();

	}
	
	
	private static void sumHashtagUsers() throws IOException {
		hashtag_userCount = new HashMap<String, Integer>();

		File dir = new File(dataFileFolder2);
		File[] directoryListing = dir.listFiles();
		for (File file : directoryListing) {
			System.out.println(file.getPath());
			loadHashtagFile(file.getPath());
		}
		saveHashtagUserCount();
		
	}
	
	/*
	 * load hashtag-user statistical file;
	 */
	private static void loadHashtagFile(String fileName) throws IOException
	{
		Reader in = new FileReader(fileName);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String hashtags = record.get("Hashtag");
			int userCount = Integer.valueOf(record.get("Users"));
			hashtag_userCount.put(hashtags, userCount);
		}
		System.out.println(hashtag_userCount.keySet().size());
	}
}
