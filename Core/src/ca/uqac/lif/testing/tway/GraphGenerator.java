package ca.uqac.lif.testing.tway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphGenerator extends TWayGraphProblem 
{
	protected List<List<String>> m_tGroups;

	/**
	 * Create a new graph generator
	 * @param var_names The names of the variables that will be represented
	 * in each vertex
	 */
	public GraphGenerator(int t, String ... var_names)
	{
		super(t, var_names);
		m_tGroups = generateTuples();
	}

	/**
	 * Create a new hypergraph generator
	 * @param var_names The names of the variables that will be represented
	 * in each vertex
	 */
	public GraphGenerator(int t, List<String> var_names)
	{
		super(t, var_names);
		m_tGroups = generateTuples();
	}

	/**
	 * Computes the ID of a vertex, based on the valuation of the
	 * variables it represents
	 * @param tuple The names of the variables in this tuple
	 * @param tuple_nb The sequential number given to this combination of
	 * variables
	 * @param values A list of values; the i-th position of the list
	 *   contains the value of the i-th variable 
	 * @return The numerical ID of the corresponding vertex
	 */
	public long getVertexId(List<String> tuple, long tuple_nb, List<String> values)
	{
		long id = 0;
		int i = 0;
		for (String var : tuple)
		{
			int index = m_domains.get(var).indexOf(values.get(i));
			id += Math.pow(m_maxDomSize, i) * index;
			i++;
		}
		return tuple_nb * (long) Math.pow(m_maxDomSize, m_t) + id;
	}

	/**
	 * Computes the ID of a vertex, based on the valuation of the
	 * variables it represents
	 * @param tuple The names of the variables in this tuple
	 * @param values A list of values; the i-th position of the list
	 *   contains the value of the i-th variable 
	 * @return The numerical ID of the corresponding vertex
	 */
	public long getVertexId(List<String> tuple, List<String> values)
	{
		return getVertexId(tuple, getTupleNumber(tuple), values);
	}

	/**
	 * Computes the ID of a vertex, based on the valuation of the
	 * variables it represents.
	 * @param elements A list of strings, must be of length
	 * t &times; 2. Each pair of arguments represents the name of
	 * a variable, followed by the value of that variable. So, to
	 * represent the pair a=1, b=2, the arguments would be
	 * ("a", "1", "b", "2).
	 * @return The numerical ID of the corresponding vertex
	 */
	public long getVertexId(String ... elements)
	{
		if (elements.length != m_t * 2)
			return -1;
		List<String> tuple = new ArrayList<String>(m_t);
		List<String> values = new ArrayList<String>(m_t);
		for (int i = 0; i < elements.length; i += 2)
		{
			tuple.add(elements[i]);
			values.add(elements[i+1]);
		}
		return getVertexId(tuple, values);
	}

	/**
	 * Computes the sequential number of a tuple
	 * @param tuple A list of t variable names
	 * @return The tuple number, or -1 if no number could be computed
	 */
	public long getTupleNumber(List<String> tuple)
	{
		if (tuple.size() != m_t)
			return -1; // Wrong size!
		for (int i = 0; i < m_tGroups.size(); i++)
		{
			List<String> group = m_tGroups.get(i);
			boolean found = true;
			for (int j = 0; j < m_t; j++)
			{
				if (tuple.get(j).compareTo(group.get(j)) != 0)
				{
					found = false;
					break;
				}
			}
			if (found)
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Computes the sequential number of a tuple
	 * @param tuple A list of t variable names
	 * @return The tuple number, or -1 if no number could be computed
	 */
	public long getTupleNumber(String ... tuple)
	{
		List<String> t = new ArrayList<String>();
		for (String var : tuple)
		{
			t.add(var);
		}
		return getTupleNumber(t);
	}

	@Override
	public void generateTWayEdges()
	{
		for (int tuple_pos = 0; tuple_pos < m_tGroups.size(); tuple_pos++)
		{
			List<String> tuple = m_tGroups.get(tuple_pos);
			GraphGenerator gg = new GraphGenerator(m_t, tuple);
			for (String var : tuple)
			{
				gg.addDomain(var, m_domains.get(var));
			}
			VectorIterator it = gg.new VectorIterator();
			while (it.hasNext())
			{
				List<String> valuation = it.next();
				long cur_vertex_id = getVertexId(tuple, tuple_pos, valuation);
				// Go through all combinations of t variables previously processed,
				// and identify those that intersect with the current one
				for (int i = 0; i <= tuple_pos; i++)
				{
					List<String> previous_tuple = m_tGroups.get(i);
					List<String> common_vars = getIntersection(tuple, previous_tuple);
					if (common_vars.isEmpty())
					{
						// No conflict, skip
						continue;
					}
					// Generate values for common variables
					GraphGenerator gg2 = new GraphGenerator(common_vars.size(), common_vars);
					for (String cv : common_vars)
					{
						gg2.addDomain(cv, m_domains.get(cv));
					}
					VectorIterator vi = gg2.new VectorIterator();
					while (vi.hasNext())
					{
						List<String> values = vi.next();
						if (sameValues(tuple, valuation, common_vars, values))
						{
							// This is the same variable assignment as the current
							// valuation: skip
							continue;
						}
						// This assignment conflicts with the current one in at least
						// one variable
						GraphGenerator gg3 = new GraphGenerator(m_t, previous_tuple);
						for (String cv : previous_tuple)
						{
							gg3.addDomain(cv, m_domains.get(cv));
						}
						Map<String,String> fixed_vars = new HashMap<String,String>();
						for (int j = 0; j < common_vars.size(); j++)
						{
							fixed_vars.put(common_vars.get(j), values.get(j));
						}
						VectorIterator vi2 = gg3.new VectorIterator(fixed_vars);
						while (vi2.hasNext())
						{
							List<String> conflict_valuation = vi2.next();
							long new_vertex_id = getVertexId(previous_tuple, i, conflict_valuation);
							if (new_vertex_id < cur_vertex_id)
							{
								// Since the graph is undirected, each edge will be generated
								// twice (once in each direction). We only keep the one where
								// the first node id is greater than the second node id
								edgeStart();
								vertexCallback(cur_vertex_id, tuple, tuple_pos, valuation, new_vertex_id, previous_tuple, i, conflict_valuation);
								edgeEnd();
							}
						}
					}
				}
			}
		}
	}

	public void vertexCallback(long src_id, List<String> tuple1, int tuple_nb1, List<String> values1, long dst_id, List<String> tuple2, int tuple_nb2, List<String> values2)
	{
		// Do nothing
	}

	/**
	 * Checks if a valuation for variables has the same assignments than that
	 * of a given tuple
	 * @param tuple The variables in the tuple
	 * @param valuation The value for each of these variables
	 * @param common_vars The variables to look for
	 * @param values The values of these variables
	 * @return {@code true} if all variables in common_vars have the same
	 * value as in tuple, {@code false} otherwise
	 */
	protected boolean sameValues(List<String> tuple, List<String> valuation, List<String> common_vars, List<String> values)
	{
		for (int i = 0; i < common_vars.size(); i++)
		{
			int var_pos = tuple.indexOf(common_vars.get(i));
			assert var_pos >= 0;
			if (valuation.get(var_pos).compareTo(values.get(i)) != 0)
				return false;
		}
		return true;
	}

	/**
	 * Computes the common elements between two lists
	 * @param l1 The first list
	 * @param l2 The second list
	 * @return The common elements
	 */
	protected List<String> getIntersection(List<String> l1, List<String> l2)
	{
		List<String> intersection = new ArrayList<String>();
		for (int i = 0; i < m_t; i++)
		{
			String v = l1.get(i);
			for (int j = 0; j < m_t; j++)
			{
				if (l2.get(j).compareTo(v) == 0)
				{
					intersection.add(v);
				}
			}
		}
		return intersection;
	}

}
