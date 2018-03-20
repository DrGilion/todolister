package tests;

import java.io.File;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.PersistenceController;
import service.*;

public class PersistenceTest {
	PersistenceService psLegacy = new LegacyPersistenceService();
	PersistenceService psPDF = new PdfPersistenceService();
	PersistenceService psJSON = new JSONPersistenceService();
	PersistenceService psXML = new XMLPersistenceService();
	PersistenceService psTXT = new TXTPersistenceService();
	PersistenceService psSQLite = new SQLitePersistenceService();
	
	PersistenceController controller;

	@Before
	public void setUp() {
		controller = new PersistenceController();
	}
	
	@Test
	public void testServicesMap(){
		Assert.assertEquals(6, controller.getPersistenceServiceCount());
	}

	@Test
	public void testLegacy() {
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(Paths.get(".").toAbsolutePath().normalize().toString()));
		fc.showOpenDialog(null);
		psLegacy.load(fc.getSelectedFile());
	}

}
