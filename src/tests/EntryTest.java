package tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.SimpleBooleanProperty;
import model.Entry;


public class EntryTest {
			
	@Before
	public void setUp(){
		
	}
//------------------------------Name----------------------------------
	
	
	
	
//------------------------------Type----------------------------------
	
	
	
	
	

//------------------------------Tags----------------------------------
	
	@Test
	public void AddTest() {
		Entry entry = new Entry("käse","Food");
		entry.addTag("Cheese");
		entry.addTag("Meal");
		Assert.assertEquals(2, entry.getTags().size());
	}
	
	@Test
	public void RemoveTest() {
		Entry entry = new Entry("käse","Food");
		entry.addTag("Cheese");
		entry.addTag("Meal");
		entry.removeTag("Cheese");
		Assert.assertEquals(1,entry.getTags().size());
		entry.removeTag("Meal");
		Assert.assertTrue(entry.getTags().isEmpty());
	}
	
	@Test
	public void ContainsTest() {
		Entry entry = new Entry("käse","Food");
		entry.addTag("Cheese");
		entry.addTag("Meal");
		Assert.assertTrue(entry.hasTag("Meal"));
		Assert.assertTrue(entry.hasTag("Cheese"));
	}
	
//------------------------------Property----------------------------------
	
	@Test
	public void propertyTest(){
		Entry entry = new Entry("käse","Food");
		Assert.assertEquals(3, entry.getPropertyCount());
		entry.addProperty("done", new SimpleBooleanProperty(true));
		Assert.assertEquals(4, entry.getPropertyCount());
		Assert.assertTrue(entry.hasProperty("done"));
		entry.removeProperty("name");
		Assert.assertTrue(entry.hasProperty("name"));
		Assert.assertEquals(4, entry.getPropertyCount());
	}
	
	@Test
	public void ComplexTest() {
		
	}

}
