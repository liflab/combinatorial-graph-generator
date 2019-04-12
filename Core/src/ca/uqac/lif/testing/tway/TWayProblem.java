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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class TWayProblem
{
  /**
   * The domains for each variable
   */
  protected Map<String, List<String>> m_domains;

  /**
   * The size of each domain (so that we don't recompute them)
   */
  protected Map<String, Integer> m_sizes;

  /**
   * The ordered list of all variable names
   */
  protected List<String> m_varNames;

  /**
   * The maximum size of the domain of a variable
   */
  protected int m_maxDomSize = 0;

  /**
   * The value of t
   */
  protected int m_t;

  /**
   * Create a new t-way problem
   * 
   * @param t
   *          The value of t
   * @param var_names
   *          The names of the variables that will be represented in each vertex
   */
  public TWayProblem(int t, String... var_names)
  {
    super();
    m_t = t;
    m_domains = new HashMap<String, List<String>>();
    m_sizes = new HashMap<String, Integer>();
    m_varNames = new ArrayList<String>(var_names.length);
    for (String v : var_names)
    {
      m_varNames.add(v);
    }
  }

  /**
   * Creates a new t-way problem
   * 
   * @param t
   *          The value of t
   * @param var_names
   *          The names of the variables that will be represented in each vertex
   */
  public TWayProblem(int t, /* @NonNull */ List<String> var_names)
  {
    super();
    m_t = t;
    m_domains = new HashMap<String, List<String>>();
    m_sizes = new HashMap<String, Integer>();
    m_varNames = new ArrayList<String>(var_names.size());
    for (String v : var_names)
    {
      m_varNames.add(v);
    }
  }

  /**
   * Specifies the domain of one of the variables
   * 
   * @param name
   *          The name of the variable
   * @param values
   *          The possible values for that variable
   */
  public void addDomain(String/* @NonNull */ name, String/* @NonNull */ ... values)
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
   * 
   * @param name
   *          The name of the variable
   * @param values
   *          The possible values for that variable
   */
  public void addDomain(String/* @NonNull */ name, Collection<String>/* @NonNull */ values)
  {
    List<String> vals = new ArrayList<String>(values.size());
    vals.addAll(values);
    m_domains.put(name, vals);
    m_sizes.put(name, values.size());
    m_maxDomSize = Math.max(m_maxDomSize, values.size());
  }

  /**
   * Specifies the domain of one or more variables
   * 
   * @param domain
   */
  public void addDomain(Map<String, List<String>> domain)
  {
    for (Map.Entry<String, List<String>> e : domain.entrySet())
    {
      addDomain(e.getKey(), e.getValue());
    }
  }

  /**
   * Generates all the combinations of t variables
   * 
   * @param t
   *          The number of variables in each tuple
   * @return The list of all combinations, each itself being a list of variable
   *         names of length t
   */
  public /* @NonNull */ List<List<String>> generateTuples()
  {
    List<List<String>> tuples = new ArrayList<List<String>>();
    TWayProblem hg = new TWayProblem(m_t, m_varNames);
    for (String v : m_varNames)
    {
      hg.addDomain(v, "0", "1");
    }
    VectorIterator it = hg.new VectorIterator();
    while (it.hasNext())
    {
      List<String> names = it.next();
      List<String> fixed_var_names = getFixedVarNames(names);
      if (fixed_var_names.size() != m_t)
      {
        // This is not a set of the right size; skip
        continue;
      }
      tuples.add(fixed_var_names);
    }
    return tuples;
  }

  /**
   * Iterates over all possible values for each variable
   */
  public class VectorIterator implements Iterator<List<String>>
  {
    /**
     * The list containing the valuation for each variable. The element at position
     * i is the value of the i-th variable
     */
    protected /* @NonNull */ List<String> m_vector;

    /**
     * The list of variables whose value is fixed in advance
     */
    protected /* @NonNull */ List<String> m_fixedVariables;

    /**
     * Whether a next solution was computed
     */
    protected boolean m_nextComputed = true;

    /**
     * Whether {@link #hasNext()} has been called
     */
    protected boolean m_hasNext = true;

    /**
     * Creates a new vector iterator
     */
    public VectorIterator()
    {
      this(new HashMap<String, String>());
    }

    /**
     * Creates a new vector iterator, and fixes the values of some of the variables
     * 
     * @param fixed_values
     *          A map associating variable names to values. These variables will
     *          always have that value in all iterated vectors. The values must be
     *          in the domain of the respective variable, as defined in the
     *          container hypergraph generator.
     */
    public VectorIterator(/* @NonNull */ Map<String, String> fixed_values)
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
      for (Map.Entry<String, String> e : fixed_values.entrySet())
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
        /* @NonNull */ String var = m_varNames.get(i);
        if (m_fixedVariables.contains(var))
        {
          // Don't touch to this element
          continue;
        }
        /* @NonNull */ List<String> dom = m_domains.get(var);
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
}
