package ca.uqac.lif.testing.hypergraph;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class HypergraphGenerator
{
	/**
	 * The domains for each variable
	 */
	protected Map<String,List<String>> m_domains;

	/**
	 * The size of each domain (so that we don't recompute them)
	 */
	protected Map<String,Integer> m_sizes;

	/**
	 * The ordered list of all variable names
	 */
	protected List<String> m_varNames;

	/**
	 * The maximum size of the domain of a variable
	 */
	protected int m_maxDomSize = 0;
	
	public HypergraphGenerator(String ... var_names)
	{
		super();
		m_domains = new HashMap<String,List<String>>();
		m_sizes = new HashMap<String,Integer>();
		m_varNames = new ArrayList<String>(var_names.length);
		for (String v : var_names)
		{
			m_varNames.add(v);
		}
	}
	
	public HypergraphGenerator(List<String> var_names)
	{
		super();
		m_domains = new HashMap<String,List<String>>();
		m_sizes = new HashMap<String,Integer>();
		m_varNames = new ArrayList<String>(var_names.size());
		for (String v : var_names)
		{
			m_varNames.add(v);
		}
	}
	
	/**
	 * Method called when the generator starts a new hyperedge
	 */
	public void edgeStart()
	{
		// Do nothing
	}
	
	/**
	 * Method called when the generator ends a hyperedge
	 */
	public void edgeEnd()
	{
		// Do nothing
	}
	
	/**
	 * Method called when the generator adds a vertex to a hyperedge
	 * @param vertex The vertex
	 */
	public void vertexCallback(List<String> vertex)
	{
		// Do nothing
	}
	
	/**
	 * Specifies the domain of one of the variables
	 * @param name The name of the variable
	 * @param values The possible values for that variable
	 */
	public void addDomain(String name, String ... values)
	{
		List<String> vals = new ArrayList<String>(values.length);
		for (String v : values)
		{
			vals.add(v);
		}
		m_domains.put(name, vals);
		m_sizes.put(name, values.length);
		m_maxDomSize = Math.max(m_maxDomSize, values.length);
	}

	/**
	 * Specifies the domain of one of the variables
	 * @param name The name of the variable
	 * @param values The possible values for that variable
	 */
	public void addDomain(String name, Collection<String> values)
	{
		List<String> vals = new ArrayList<String>(values.size());
		vals.addAll(values);
		m_domains.put(name, vals);
		m_sizes.put(name, values.size());
		m_maxDomSize = Math.max(m_maxDomSize, values.size());
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
	 * test case generation problem. The hyperedges are printed to the
	 * print stream given to the generator using
	 * {@link #setPrintStream(PrintStream)}.
	 * @param t The value of t
	 */
	public void generateTWayEdges(int t)
	{
		HypergraphGenerator hg = new HypergraphGenerator(m_varNames);
		for (String v : m_varNames)
		{
			hg.addDomain(v, "0", "1");
		}
		VectorIterator it = hg.new VectorIterator();
		while (it.hasNext())
		{
			List<String> names = it.next();
			List<String> fixed_var_names = getFixedVarNames(names);
			if (fixed_var_names.size() != t)
			{
				// This is not a set of the right size; skip
				continue;
			}
			HypergraphGenerator hg2 = new HypergraphGenerator(fixed_var_names);
			for (String var_name : fixed_var_names)
			{
				hg2.addDomain(var_name, m_domains.get(var_name));
			}
			VectorIterator it2 = hg2.new VectorIterator();
			while (it2.hasNext())
			{
				List<String> fixed_values = it2.next();
				Map<String,String> fixed_domains = new HashMap<String,String>();
				for (int j = 0; j < fixed_values.size(); j++)
				{
					fixed_domains.put(fixed_var_names.get(j), fixed_values.get(j));
				}
				VectorIterator it3 = new VectorIterator(fixed_domains);
				edgeStart();
				while (it3.hasNext())
				{
					List<String> vertex = it3.next();
					vertexCallback(vertex);
				}
				edgeEnd();
			}
		}
	}
	
	protected List<String> getFixedVarNames(List<String> vector)
	{
		List<String> fixed_var_names = new ArrayList<String>();
		for (int i = 0; i < vector.size(); i++)
		{
			if (vector.get(i).compareTo("1") == 0)
			{
				fixed_var_names.add(m_varNames.get(i));
			}
		}
		return fixed_var_names;
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
	 * Iterates over all possible values for each variable
	 */
	public class VectorIterator implements Iterator<List<String>> 
	{
		protected List<String> m_vector;

		protected List<String> m_fixedVariables;

		protected boolean m_nextComputed = true;

		protected boolean m_hasNext = true;

		/**
		 * Creates a new vector iterator
		 */
		public VectorIterator()
		{
			this(new HashMap<String,String>());
		}

		/**
		 * Creates a new vector iterator, and fixes the values of some of
		 * the variables
		 * @param fixed_values A map associating variable names to values.
		 * These variables will always have that value in all iterated
		 * vectors.
		 * The values must be in the domain of the respective variable, as
		 * defined in the container hypergraph generator.
		 */
		public VectorIterator(/*@NotNull*/ Map<String,String> fixed_values)
		{
			super();
			m_vector = new ArrayList<String>(m_varNames.size());
			m_fixedVariables = new ArrayList<String>(fixed_values.size());
			// Initialize vector by setting all variables to "first" value
			for (String var : m_varNames)
			{
				m_vector.add(m_domains.get(var).get(0));
			}
			// ...except those that have a predefined fixed value
			for (Map.Entry<String,String> e : fixed_values.entrySet())
			{
				m_vector.set(m_varNames.indexOf(e.getKey()), e.getValue());
				m_fixedVariables.add(e.getKey());
			}
		}

		@Override
		public boolean hasNext()
		{
			if (m_nextComputed)
			{
				return m_hasNext;
			}
			m_nextComputed = true;
			for (int i = 0; i < m_vector.size(); i++)
			{
				String var = m_varNames.get(i);
				if (m_fixedVariables.contains(var))
				{
					// Don't touch to this element
					continue;
				}
				List<String> dom = m_domains.get(var);
				int idx = dom.indexOf(m_vector.get(i)) + 1;
				if (idx < m_sizes.get(var))
				{
					m_vector.set(i, dom.get(idx));
					return true;
				}
				else
				{
					m_vector.set(i, dom.get(0));
				}
			}
			m_hasNext = false;
			return false;
		}

		@Override
		public List<String> next() 
		{
			// hasNext was called first
			if (m_nextComputed)
			{
				if (m_hasNext)
				{
					m_nextComputed = false;
					return m_vector;
				}
				throw new NoSuchElementException();
			}
			else
			{
				// Call hasNext first
				hasNext();
				if (m_hasNext)
				{
					m_nextComputed = false;
					return m_vector;
				}
				throw new NoSuchElementException();
			}
		}
	}
}
