package ca.uqac.lif.testing.tway;

import java.io.PrintStream;
import java.util.List;

public abstract class TWayGraphProblem extends TWayProblem
{
	/**
	 * The print stream where the output is written
	 */
	protected PrintStream m_output = System.out;
	
	/**
	 * The number of edges created during the generation
	 */
	protected long m_edgeCount = 0;
	
	/**
	 * Flag used to manually interrupt the generator
	 */
	protected volatile boolean m_running = true;
	
	public TWayGraphProblem(int t, String ... var_names)
	{
		super(t, var_names);
	}
	
	public TWayGraphProblem(int t, List<String> var_names)
	{
		super(t, var_names);
	}
	
	/**
	 * Sets the print stream where the output is written
	 * @param ps The print stream
	 */
	public void setOutput(PrintStream /*@NonNull*/ ps)
	{
		m_output = ps;
	}
	
	/**
	 * Generates the set of hyperedges corresponding to the <i>t</i>-way
	 * test case generation problem.
	 */
	public abstract void generateTWayEdges();
	
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
		m_edgeCount++;
	}
	
	/**
	 * Returns the number of edges generated in this graph. This
	 * value only makes sense if the method is called <strong>after</strong>
	 * {@link #generateTWayEdges()}.
	 * @return The number of edges
	 */
	public long getEdgeCount()
	{
		return m_edgeCount;
	}

	/**
	 * Returns the number of edges generated in this graph.
	 * @return The number of vertices
	 */
	public abstract long getVertexCount(); 
	
	/**
	 * Interrupts the generator
	 */
	public void interrupt()
	{
		m_running = false;
	}
}
