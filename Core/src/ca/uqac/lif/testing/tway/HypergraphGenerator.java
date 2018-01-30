package ca.uqac.lif.testing.tway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a t-way problem instance into a hypergraph. The vertex
 * covering of this hypergraph is the solution to the t-way problem.
 */
public class HypergraphGenerator extends TWayGraphProblem
{
	public HypergraphGenerator(int t, String ... var_names)
	{
		super(t, var_names);
	}

	public HypergraphGenerator(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	/**
	 * Computes the ID of a vertex, based on the valuation of the
	 * variables it represents
	 * @param values A list of values; the i-th position of the list
	 *   contains the value of the i-th variable 
	 * @return The numerical ID of the corresponding vertex
	 */
	public long getVertexId(String ... values)
	{
		List<String> vals = new ArrayList<String>(values.length);
		for (String v : values)
		{
			vals.add(v);
		}
		return getVertexId(vals);
	}
	
	/**
	 * Gets the number of vertices in the hypergraph 
	 * @return The number of vertices
	 */
	public long getVertexCount()
	{
		return (long) Math.pow(m_maxDomSize, m_varNames.size());
	}
	
	/**
	 * Generates the set of hyperedges corresponding to the <i>t</i>-way
	 * test case generation problem.
	 * @param t The value of t
	 */
	@Override
	public void generateTWayEdges()
	{
		HypergraphGenerator hg = new HypergraphGenerator(m_t, m_varNames);
		for (String v : m_varNames)
		{
			hg.addDomain(v, "0", "1");
		}
		VectorIterator it = hg.new VectorIterator();
		while (it.hasNext() && m_running)
		{
			List<String> names = it.next();
			List<String> fixed_var_names = getFixedVarNames(names);
			if (fixed_var_names.size() != m_t)
			{
				// This is not a set of the right size; skip
				continue;
			}
			HypergraphGenerator hg2 = new HypergraphGenerator(0, fixed_var_names);
			for (String var_name : fixed_var_names)
			{
				hg2.addDomain(var_name, m_domains.get(var_name));
			}
			VectorIterator it2 = hg2.new VectorIterator();
			while (it2.hasNext() && m_running)
			{
				List<String> fixed_values = it2.next();
				Map<String,String> fixed_domains = new HashMap<String,String>();
				for (int j = 0; j < fixed_values.size(); j++)
				{
					fixed_domains.put(fixed_var_names.get(j), fixed_values.get(j));
				}
				VectorIterator it3 = new VectorIterator(fixed_domains);
				edgeStart();
				while (it3.hasNext() && m_running)
				{
					List<String> vertex = it3.next();
					vertexCallback(vertex);
				}
				edgeEnd();
			}
		}
	}

	/**
	 * Computes the ID of a vertex, based on the valuation of the
	 * variables it represents
	 * @param values A list of values; the i-th position of the list
	 *   contains the value of the i-th variable 
	 * @return The numerical ID of the corresponding vertex
	 */
	public long getVertexId(List<String> values)
	{
		long id = 0;
		int i = 0;
		for (String var : m_varNames)
		{
			int index = m_domains.get(var).indexOf(values.get(i));
			id += Math.pow(m_maxDomSize, i) * index;
			i++;
		}
		return id;
	}
	
	/**
	 * Method called when the generator adds a vertex to a hyperedge
	 * @param vertex The vertex
	 */
	public void vertexCallback(List<String> vertex)
	{
		// Do nothing
	}
}
