package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import model.Entry;

public class TXTPersistenceService implements PersistenceService {

	@Override
	public boolean load(File file) {
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
		return true;
	}

	@Override
	public boolean save(String path) {
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
		return true;
	}

	@Override
	public String getType() {
		return "txt";
	}

}
