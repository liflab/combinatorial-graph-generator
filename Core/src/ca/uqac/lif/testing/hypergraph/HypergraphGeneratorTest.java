package ca.uqac.lif.testing.hypergraph;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import ca.uqac.lif.testing.hypergraph.HypergraphGenerator.VectorIterator;

public class HypergraphGeneratorTest 
{
	@Test
	public void testNodeId1()
	{
		HypergraphGenerator hg = new HypergraphGenerator("a");
		hg.addDomain("a", "0", "1");
		assertEquals(0, hg.getVertexId("0"));
		assertEquals(1, hg.getVertexId("1"));
	}
	
	@Test
	public void testNodeId2()
	{
		HypergraphGenerator hg = new HypergraphGenerator("a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1");
		assertEquals(0, hg.getVertexId("0", "0"));
		assertEquals(1, hg.getVertexId("1", "0"));
		assertEquals(2, hg.getVertexId("0", "1"));
		assertEquals(3, hg.getVertexId("1", "1"));
	}
	
	@Test
	public void testNodeId3()
	{
		HypergraphGenerator hg = new HypergraphGenerator("a", "b");
		hg.addDomain("a", "0", "1", "2");
		hg.addDomain("b", "0", "1");
		assertEquals(0, hg.getVertexId("0", "0"));
		assertEquals(1, hg.getVertexId("1", "0"));
		assertEquals(2, hg.getVertexId("2", "0"));
		assertEquals(3, hg.getVertexId("0", "1"));
		assertEquals(4, hg.getVertexId("1", "1"));
		assertEquals(5, hg.getVertexId("2", "1"));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testIterator1()
	{
		List<String> vector;
		HypergraphGenerator hg = new HypergraphGenerator("a");
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
		HypergraphGenerator hg = new HypergraphGenerator("a", "b");
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
		HypergraphGenerator hg = new HypergraphGenerator("a", "b");
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
		HypergraphGenerator hg = new HypergraphGenerator("a", "b");
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
		HypergraphGenerator hg = new HypergraphGenerator("a", "b", "c");
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
	
	@Test
	public void testGenerator1a()
	{
		CollectorGenerator hg = new CollectorGenerator("a");
		hg.addDomain("a", "0", "1");
		hg.generateTWayEdges(1);
		Set<List<List<String>>> edges = hg.getEdges();
		assertEquals(2, edges.size());
		assertTrue(containsHyperedge(edges, new String[][]{{"0"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"1"}}));
	}
	
	@Test
	public void testGenerator1b()
	{
		CollectorGenerator hg = new CollectorGenerator("a");
		hg.addDomain("a", "0", "1", "2");
		hg.generateTWayEdges(1);
		Set<List<List<String>>> edges = hg.getEdges();
		assertEquals(3, edges.size());
		assertTrue(containsHyperedge(edges, new String[][]{{"0"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"1"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"2"}}));
	}
	
	@Test
	public void testGenerator2()
	{
		CollectorGenerator hg = new CollectorGenerator("a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1");
		hg.generateTWayEdges(1);
		Set<List<List<String>>> edges = hg.getEdges();
		assertEquals(4, edges.size());
		assertTrue(containsHyperedge(edges, new String[][]{{"0", "0"}, {"0", "1"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"1", "0"}, {"1", "1"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"0", "0"}, {"1", "0"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"0", "1"}, {"1", "1"}}));
	}
	
	@Test
	public void testGenerator3()
	{
		CollectorGenerator hg = new CollectorGenerator("a", "b");
		hg.addDomain("a", "0", "1");
		hg.addDomain("b", "0", "1");
		hg.generateTWayEdges(2);
		Set<List<List<String>>> edges = hg.getEdges();
		assertEquals(4, edges.size());
		assertTrue(containsHyperedge(edges, new String[][]{{"0", "0"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"1", "0"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"0", "1"}}));
		assertTrue(containsHyperedge(edges, new String[][]{{"1", "1"}}));
	}
	
	/**
	 * Checks if a set of hyperedges contains a specific hyperedge
	 * @param edges The set of hyperedges
	 * @param edge The hyperedge to look for. The edge is represented as an
	 * array of String arrays; each 1-dimensional array represents one
	 * valuation (i.e. one vertex) of the hyperedge
	 * @return {@code true} if the hyperedge was found in the set;
	 *   {@code false} otherwise 
	 */
	public static boolean containsHyperedge(Set<List<List<String>>> edges, String[][] edge)
	{
		for (List<List<String>> e : edges)
		{
			boolean found = true;
			for (String[] valuation : edge)
			{
				if (!containsValuation(e, valuation))
				{
					found = false;
					break;
				}
			}
			if (found)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if a list of valuations contains a specific valuation
	 * @param edge A list of valuations; each valuation is itself a list of
	 *   values (strings)
	 * @param valuation The valuation to look for
	 * @return {@code true} if the valuation was found in the list;
	 *   {@code false} otherwise
	 */
	public static boolean containsValuation(List<List<String>> edge, String[] valuation)
	{
		for (List<String> edge_val : edge)
		{
			if (edge_val.size() != valuation.length)
				continue;
			boolean found = true;
			for (int i = 0; i < valuation.length; i++)
			{
				if (valuation[i].compareTo(edge_val.get(i)) != 0)
				{
					found = false;
					break;
				}
			}
			if (found)
				return true;
		}
		return false;
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
	
	/**
	 * Generator that simply collects all the generated hyperedges. This is
	 * used for testing only, as it keeps all hyperedges in memory. 
	 */
	public static class CollectorGenerator extends HypergraphGenerator
	{
		/**
		 * The set of all generated hyperedges
		 */
		Set<List<List<String>>> m_hyperedges = new HashSet<List<List<String>>>();
		
		/**
		 * The current hyperedges. Vertices are added one by one through
		 * calls to {@link #vertexCallback(List)}.
		 */
		List<List<String>> m_hyperedge;
		
		public CollectorGenerator(String ... vars)
		{
			super(vars);
		}
		
		/**
		 * Gets the set of all hyperedges generated
		 * @return The set of hyperedges
		 */
		public Set<List<List<String>>> getEdges()
		{
			return m_hyperedges;
		}
		
		@Override
		public void edgeStart()
		{
			m_hyperedge = new ArrayList<List<String>>();
		}
		
		@Override
		public void vertexCallback(List<String> vertex)
		{
			// Since the generator passes the same list every time,
			// we must copy the contents of vertex into a new list
			List<String> v = new ArrayList<String>(vertex.size());
			v.addAll(vertex);
			m_hyperedge.add(v);
		}
		
		@Override
		public void edgeEnd()
		{
			m_hyperedges.add(m_hyperedge);
		}
		
	}
}
