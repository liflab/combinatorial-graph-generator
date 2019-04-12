/*
  Copyright 2015
  Laboratoire d'informatique formelle
  Université du Québec à Chicoutimi, Canada
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
      http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
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
  
  /**
   * The interval at which the output stream is flushed when writing
   * graph edges
   */
  protected int m_flushEvery = 10000;

  public TWayGraphProblem(int t, String... var_names)
  {
    super(t, var_names);
  }

  public TWayGraphProblem(int t, List<String> var_names)
  {
    super(t, var_names);
  }

  /**
   * Sets the print stream where the output is written
   * 
   * @param ps
   *          The print stream
   */
  public void setOutput(PrintStream /* @NonNull */ ps)
  {
    m_output = ps;
  }
  
  /*@ require interval > 0 @*/
  /**
   * Sets the interval at which the output stream is flushed when writing
   * graph edges
   * @param interval The interval. Must be positive.
   */
  public void setFlushInterval(int interval)
  {
    m_flushEvery = interval;
  }

  /**
   * Generates the set of hyperedges corresponding to the <i>t</i>-way test case
   * generation problem.
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
   * Returns the number of edges generated in this graph. This value only makes
   * sense if the method is called <strong>after</strong>
   * {@link #generateTWayEdges()}.
   * 
   * @return The number of edges
   */
  public long getEdgeCount()
  {
    return m_edgeCount;
  }

  /**
   * Returns the number of edges generated in this graph.
   * 
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
