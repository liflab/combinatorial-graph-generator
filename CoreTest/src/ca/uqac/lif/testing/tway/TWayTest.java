package ca.uqac.lif.testing.tway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.Test;

import ca.uqac.lif.testing.tway.TWayProblem.VectorIterator;

public class TWayTest
{
	@Test(expected = NoSuchElementException.class)
	public void testIterator1()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator(0, "a");
		hg.addDomain("a", "0", "1");
		VectorIterator it = hg.new VectorIterator();
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "0");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1");
		assertFalse(it.hasNext());
		vector = it.next();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testIterator2()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator(0, "a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1");
		VectorIterator it = hg.new VectorIterator();
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "0", "0");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "0");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "0", "1");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "1");
		assertFalse(it.hasNext());
		vector = it.next();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testIterator3()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator(0, "a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1");
		Map<String,String> fixed_vars = new HashMap<String,String>();
		fixed_vars.put("b", "0");
		VectorIterator it = hg.new VectorIterator(fixed_vars);
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "0", "0");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "0");
		assertFalse(it.hasNext());
		vector = it.next();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testIterator4()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator(0, "a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1", "2");
		Map<String,String> fixed_vars = new HashMap<String,String>();
		fixed_vars.put("a", "1");
		VectorIterator it = hg.new VectorIterator(fixed_vars);
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "0");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "1");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "2");
		assertFalse(it.hasNext());
		vector = it.next();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testIterator5()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator(0, "a", "b", "c");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1", "2");
		hg.addDomain("c", "0", "1", "2");
		Map<String,String> fixed_vars = new HashMap<String,String>();
		fixed_vars.put("a", "1");
		fixed_vars.put("c", "2");
		VectorIterator it = hg.new VectorIterator(fixed_vars);
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "0", "2");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "1", "2");
		assertTrue(it.hasNext());
		vector = it.next();
		listEquals(vector, "1", "2", "2");
		assertFalse(it.hasNext());
		vector = it.next();
	}
	
	/**
	 * Checks if a list is equal to a sequence of values
	 * @param list The list
	 * @param values The values
	 */
	public static void listEquals(List<String> list, String ... values)
	{
		assertEquals(values.length, list.size());
		for (int i = 0; i < values.length; i++)
		{
			assertEquals(values[i], list.get(i));
		}
	}
}
