package ca.uqac.lif.testing.tway;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GraphTest
{
	@Test
	public void testTupleNumber1()
	{
		GraphGenerator gg = new GraphGenerator(1, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		assertEquals(0, gg.getTupleNumber("a"));
		assertEquals(1, gg.getTupleNumber("b"));
		assertEquals(-1, gg.getTupleNumber("a", "b"));
	}
	
	@Test
	public void testTupleNumber2()
	{
		GraphGenerator gg = new GraphGenerator(2, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		assertEquals(-1, gg.getTupleNumber("a"));
		assertEquals(-1, gg.getTupleNumber("b"));
		assertEquals(0, gg.getTupleNumber("a", "b"));
		assertEquals(-1, gg.getTupleNumber("a", "c"));
	}
	
	@Test
	public void testTupleNumber3()
	{
		GraphGenerator gg = new GraphGenerator(2, "a", "b", "c");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		gg.addDomain("c", "0", "1");
		assertEquals(0, gg.getTupleNumber("a", "b"));
		assertEquals(1, gg.getTupleNumber("a", "c"));
		assertEquals(2, gg.getTupleNumber("b", "c"));
		assertEquals(-1, gg.getTupleNumber("b"));
	}
	
	@Test
	public void testVertexId1()
	{
		GraphGenerator gg = new GraphGenerator(1, "a");
		gg.addDomain("a", "0", "1");
		assertEquals(0, gg.getVertexId("a", "0"));
		assertEquals(1, gg.getVertexId("a", "1"));
	}
	
	@Test
	public void testVertexId2()
	{
		GraphGenerator gg = new GraphGenerator(1, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		assertEquals(0, gg.getVertexId("a", "0"));
		assertEquals(1, gg.getVertexId("a", "1"));
		assertEquals(2, gg.getVertexId("b", "0"));
		assertEquals(3, gg.getVertexId("b", "1"));
	}
	
	@Test
	public void testVertexId3()
	{
		GraphGenerator gg = new GraphGenerator(2, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		assertEquals(0, gg.getVertexId("a", "0", "b", "0"));
		assertEquals(1, gg.getVertexId("a", "1", "b", "0"));
		assertEquals(2, gg.getVertexId("a", "0", "b", "1"));
		assertEquals(3, gg.getVertexId("a", "1", "b", "1"));
	}
	
	@Test
	public void testVertexId4()
	{
		GraphGenerator gg = new GraphGenerator(2, "a", "b", "c");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		gg.addDomain("c", "0", "1");
		assertEquals(0, gg.getVertexId("a", "0", "b", "0"));
		assertEquals(1, gg.getVertexId("a", "1", "b", "0"));
		assertEquals(2, gg.getVertexId("a", "0", "b", "1"));
		assertEquals(3, gg.getVertexId("a", "1", "b", "1"));
		assertEquals(4, gg.getVertexId("a", "0", "c", "0"));
		assertEquals(5, gg.getVertexId("a", "1", "c", "0"));
		assertEquals(6, gg.getVertexId("a", "0", "c", "1"));
		assertEquals(7, gg.getVertexId("a", "1", "c", "1"));
		assertEquals(8, gg.getVertexId("b", "0", "c", "0"));
		assertEquals(9, gg.getVertexId("b", "1", "c", "0"));
		assertEquals(10, gg.getVertexId("b", "0", "c", "1"));
		assertEquals(11, gg.getVertexId("b", "1", "c", "1"));
	}
	
	@Test
	public void testColoring1()
	{
		GraphCollector gg = new GraphCollector(1, "a");
		gg.addDomain("a", "0", "1");
		gg.generateTWayEdges();
		List<String[]> edges = gg.m_edges;
		assertEquals(1, edges.size());
		assertTrue(containsUndirectedEdge(edges, "a", "0", "a", "1"));
	}
	
	@Test
	public void testColoring2()
	{
		GraphCollector gg = new GraphCollector(1, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		gg.generateTWayEdges();
		List<String[]> edges = gg.m_edges;
		assertEquals(2, edges.size());
		assertTrue(containsUndirectedEdge(edges, "a", "0", "a", "1"));
		assertTrue(containsUndirectedEdge(edges, "b", "0", "b", "1"));
	}
	
	@Test
	public void testColoring3()
	{
		GraphCollector gg = new GraphCollector(2, "a", "b");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		gg.generateTWayEdges();
		List<String[]> edges = gg.m_edges;
		assertEquals(6, edges.size());
		assertTrue(containsUndirectedEdge(edges, "a", "0", "b", "0", "a", "1", "b", "0"));
		assertTrue(containsUndirectedEdge(edges, "a", "0", "b", "0", "a", "0", "b", "1"));
		assertTrue(containsUndirectedEdge(edges, "a", "0", "b", "0", "a", "1", "b", "1"));
		assertTrue(containsUndirectedEdge(edges, "a", "0", "b", "1", "a", "1", "b", "1"));
		assertTrue(containsUndirectedEdge(edges, "a", "0", "b", "1", "a", "1", "b", "0"));
		assertTrue(containsUndirectedEdge(edges, "a", "1", "b", "1", "a", "1", "b", "0"));
	}
	
	@Test
	public void testColoring4()
	{
		GraphCollector gg = new GraphCollector(2, "a", "b", "c");
		gg.addDomain("a", "0", "1");
		gg.addDomain("b", "0", "1");
		gg.addDomain("c", "0");
		gg.generateTWayEdges();
		List<String[]> edges = gg.m_edges;
		assertEquals(16, edges.size());
	}
	
	/**
	 * Checks if a list of edges contains a specific edge; the direction of
	 * the edge (i.e. which half appears first) is not important
	 * @param edges
	 * @param vals
	 * @return
	 */
	public static boolean containsUndirectedEdge(List<String[]> edges, String ... vals)
	{
		if (containsEdge(edges, vals))
			return true;
		String[] swapped = swapHalf(vals);
		return containsEdge(edges, swapped);
	}
	
	/**
	 * Checks if a list of edges contains a specific edge
	 * @param edges
	 * @param vals
	 * @return
	 */
	public static boolean containsEdge(List<String[]> edges, String ... vals)
	{
		for (String[] edge : edges)
		{
			if (edge.length != vals.length)
				continue;
			boolean found = true;
			for (int i = 0; i < vals.length; i++)
			{
				if (edge[i].compareTo(vals[i]) != 0)
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
	 * Swaps the two halves of a string array
	 * @param values The original array
	 * @return The swapped array
	 */
	public static String[] swapHalf(String ... values)
	{
		String[] out = new String[values.length];
		int half = values.length / 2;
		for (int i = 0; i < half; i++)
		{
			out[i] = values[i + half];
			out[i + half] = values[i];
		}
		return out;
	}
	
	public static class GraphCollector extends GraphGenerator
	{
		List<String[]> m_edges = new ArrayList<String[]>(); 
		
		public GraphCollector(int t, List<String> var_names)
		{
			super(t, var_names);
		}
		
		public GraphCollector(int t, String ... var_names)
		{
			super(t, var_names);
		}
		
		@Override
		public void vertexCallback(long src_id, List<String> tuple1, int tuple_nb1, List<String> values1, long dst_id, List<String> tuple2, int tuple_nb2, List<String> values2)
		{
			String[] edge = new String[m_t * 4];
			for (int i = 0; i < m_t; i++)
			{
				edge[2*i] = tuple1.get(i);
				edge[2*i+1] = values1.get(i);
			}
			for (int i = 0; i < m_t; i++)
			{
				edge[2 * m_t + 2*i] = tuple2.get(i);
				edge[2 * m_t + 2*i+1] = values2.get(i);
			}
			m_edges.add(edge);
		}
	}
}
