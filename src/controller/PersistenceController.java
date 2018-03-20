package controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import service.JSONPersistenceService;
import service.LegacyPersistenceService;
import service.PdfPersistenceService;
import service.PersistenceService;
import service.SQLitePersistenceService;
import service.TXTPersistenceService;
import service.XMLPersistenceService;

public class PersistenceController {

	private static volatile boolean changed = false;
	private static boolean tutorialShown = true;

	private static boolean sidesFlipped = false;
	private static boolean colorblind = false;
	private static String fileURL = null;
	private static String directoryURL = null;

	public static enum RatingDisplay {
		TEXT, STARS
	}

	private static RatingDisplay ratingdisplay = RatingDisplay.TEXT;

	public PersistenceController() {
		initPersistenceServices();
	}

	/* #####################Loading and Saving########################### */

	private Map<String, PersistenceService> persistenceServices;

	private void initPersistenceServices() {
		persistenceServices = new HashMap<>();		
		this.addPersistenceService(new LegacyPersistenceService());
		this.addPersistenceService(new TXTPersistenceService());
		this.addPersistenceService(new JSONPersistenceService());
		this.addPersistenceService(new XMLPersistenceService());
		this.addPersistenceService(new PdfPersistenceService());
		this.addPersistenceService(new SQLitePersistenceService());
	}
	
	private void addPersistenceService(PersistenceService serv){
		persistenceServices.put(serv.getType(), serv);
	}

	public boolean loadBy(String encoding, File file) {
		boolean success = persistenceServices.get(encoding).load(file);

		return success;
	}

	public boolean saveBy(String encoding, String path) {
		boolean success = persistenceServices.get(encoding).save(path);

		return success;
	}

	public boolean saveAllFormats(String path) {
		for (PersistenceService ps : persistenceServices.values()) {
			boolean success = ps.save(path);
			if (!success)
				return false;
		}
		return true;
	}
/*
	public boolean saveSerialized() {
		// serializing

		// single file
		try {
			FileOutputStream fos = new FileOutputStream(fileURL);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(Entry.getEntryMap());
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// multiple files
		String parent = new File(fileURL).getParent() + File.separator;
		for (Map.Entry<String, ArrayList<Entry>> e : Entry.getEntryMap().entrySet()) {
			try {
				FileOutputStream fos = new FileOutputStream(parent + e.getKey() + ".partlist");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(e.getValue());
				oos.close();
				fos.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		writeBackUpFile();
		saveSettings();
		SavedAnimation.play();
	}

	@SuppressWarnings("unchecked")
	public boolean loadSerialized() {
		// deserializing
		try {
			FileInputStream fis = new FileInputStream(fileURL);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Entry.setEntryMap((HashMap<String, ArrayList<Entry>>) ois.readObject());
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return false;
		}
	}

	public boolean saveTXT() {
		// write backup text file

		// single file
		ArrayList<String> lines = new ArrayList<String>();
		for (ArrayList<Entry> l : Entry.getEntryMap().values()) {
			for (Entry e : l) {
				lines.add(e.toString() + "\n");
			}
		}
		ArrayList<String> types = new ArrayList<String>();
		types.add(Entry.types().toString() + "\n");
		Path file = Paths.get(fileURL + ".backup");
		try {
			Files.write(file, types, Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
			Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// multiple files
		String parent = new File(fileURL).getParent() + File.separator;
		for (Map.Entry<String, ArrayList<Entry>> e : Entry.getEntryMap().entrySet()) {
			ArrayList<String> outputdata = new ArrayList<String>();
			outputdata.add(e.getKey());
			for (Entry tmp : e.getValue()) {
				outputdata.add(tmp.toString() + "\n");
			}
			Path tmpfile = Paths.get(parent + e.getKey() + ".partbackup");
			try {
				Files.write(tmpfile, outputdata, Charset.forName("UTF-8"), StandardOpenOption.WRITE,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public boolean loadTXT(String filename) {
		// read backup text file
		Entry.getEntryMap().clear();
		fileURL = null;
		try (Scanner scan = new Scanner(new File(filename), "UTF-8")) {
			String typestring = scan.nextLine();
			typestring = typestring.substring(typestring.indexOf('['), typestring.lastIndexOf(']'));
			typestring.replaceAll(", ", " ");
			Scanner sc = new Scanner(typestring);
			while (sc.hasNext()) {
				Entry.addNewCategory(sc.next());
			}
			sc.close();

			while (scan.hasNextLine()) {
				String input = scan.nextLine();
				StringTokenizer tokenizer = new StringTokenizer(scan.nextLine(), ";;");
				System.out.println(input);
				Entry e = new Entry();
				e.setTitle(tokenizer.nextToken());
				e.setRating(Integer.parseInt(tokenizer.nextToken()));
				e.setType(tokenizer.nextToken());
				e.setExperienced(Boolean.parseBoolean(tokenizer.nextToken()));
				Entry.addEntryTo(e, e.getType());

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean AddListFile(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<Entry> newentry = (ArrayList<Entry>) ois.readObject();
			if (!newentry.isEmpty()) {
				int counter = 1;
				String catname = "";
				if (!Entry.addNewCategory(catname = newentry.get(0).getType())) {
					while (!Entry.addNewCategory(catname = (newentry.get(0).getType() + counter))) {
						counter++;
					}
				}
				for (Entry tmp : newentry) {
					tmp.setType(catname);
					Entry.addEntryTo(tmp, catname);
				}
			} else {
				ois.close();
				fis.close();
				return false;
			}
			ois.close();
			fis.close();
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
			return false;
		}
	}*/

	/* ##################### Settings ########################### */

	private static void saveSettings() {
		try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(new File("settings.txt")))) {
			writer.writeChars(directoryURL);
			writer.writeBoolean(sidesFlipped);
			writer.writeBoolean(colorblind);
			writer.writeChars(ratingdisplay.name());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadSettings() {
		try (Scanner s = new Scanner(
				PersistenceController.class.getClassLoader().getResourceAsStream("settings.txt"))) {
			directoryURL = s.next();
			sidesFlipped = s.nextBoolean();
			colorblind = s.nextBoolean();
			ratingdisplay = RatingDisplay.valueOf(s.nextLine());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/* #####################Getters and Setters########################### */

	public static String getFileURL() {
		return fileURL;
	}

	public static void setFileURL(String fileURL) {
		PersistenceController.fileURL = fileURL;
		PersistenceController.directoryURL = new File(fileURL).getParent();
	}

	public static String getDirectoryURL() {
		return directoryURL;
	}

	public static void setDirectoryURL(String directoryURL) {
		PersistenceController.directoryURL = directoryURL;
	}

	public static boolean isSidesFlipped() {
		return sidesFlipped;
	}

	public static void setSidesFlipped(boolean sidesFlipped) {
		PersistenceController.sidesFlipped = sidesFlipped;
	}

	public static boolean isColorblind() {
		return colorblind;
	}

	public static void setColorblind(boolean colorblind) {
		PersistenceController.colorblind = colorblind;
	}

	public static RatingDisplay getRatingDisplay() {
		return ratingdisplay;
	}

	public static void setRatingDisplay(RatingDisplay ratingdisplay) {
		PersistenceController.ratingdisplay = ratingdisplay;
	}

	public static boolean isChanged() {
		return changed;
	}

	public static void setChanged(boolean changed) {
		PersistenceController.changed = changed;
	}

	public static boolean isTutorialShown() {
		return tutorialShown;
	}

	public static void setTutorialShown(boolean tutorialShown) {
		PersistenceController.tutorialShown = tutorialShown;
	}
	
	public int getPersistenceServiceCount(){
		return this.persistenceServices.size();
	}

}
