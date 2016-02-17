package com.aco;
import java.util.*;
public class Ant4TSP extends Ant{
	private static final double B    = 2;
    private static final double Q0   = 0.8;
    private static final double R    = 0.1;
    
    private static final Random s_randGen = new Random(System.currentTimeMillis());
        
    protected Hashtable m_nodesToVisitTbl;
        
    public Ant4TSP(int startNode, Observer observer)
    {
        super(startNode, observer);
    }
    
    public void init()
    {
        super.init();
        
        final AntGraph graph = s_antColony.getGraph();
        
    
        m_nodesToVisitTbl = new Hashtable(graph.nodes());
        for(int i = 0; i < graph.nodes(); i++)
            m_nodesToVisitTbl.put(new Integer(i), new Integer(i));
        
   
        m_nodesToVisitTbl.remove(new Integer(m_nStartNode));
        

    }

    public int stateTransitionRule(int nCurNode)
    {
        final AntGraph graph = s_antColony.getGraph();
        
        // generate a random number
        double q    = s_randGen.nextDouble();
        int nMaxNode = -1;
        
        if(q <= Q0)  // Exploitation
        {

            double dMaxVal = -1;
            double dVal;
            int nNode;
            
            // search the max of the value 
            Enumeration  e = m_nodesToVisitTbl.elements();
            while(e.hasMoreElements())
            {
                // select a node
                nNode = ((Integer)e.nextElement()).intValue();
                
                // check on tau
                if(graph.tau(nCurNode, nNode) == 0)
                    throw new RuntimeException("tau = 0");
                
                // get the value
                dVal = graph.tau(nCurNode, nNode) * Math.pow(graph.etha(nCurNode, nNode), B);
                
                // check if it is the max
                if(dVal > dMaxVal)
                {
                    dMaxVal  = dVal;
                    nMaxNode = nNode;
                }
            }
        }
        else  // Exploration
        {
            
            double dSum = 0;
            int nNode = -1;
            
            // get the sum at denominator
            Enumeration e = m_nodesToVisitTbl.elements();
            while(e.hasMoreElements())
            {
                nNode = ((Integer)e.nextElement()).intValue();
                if(graph.tau(nCurNode, nNode) == 0)
                    throw new RuntimeException("tau = 0");
                
                // Update the sum
                dSum += graph.tau(nCurNode, nNode) * Math.pow(graph.etha(nCurNode, nNode), B);
            }
            
            if(dSum == 0)
                throw new RuntimeException("SUM = 0");
            
            // get the average value
            double dAverage = dSum / (double)m_nodesToVisitTbl.size();
            
           
            e = m_nodesToVisitTbl.elements();
            while(e.hasMoreElements() && nMaxNode < 0)
            {
                nNode = ((Integer)e.nextElement()).intValue();
                
            
                double p =
                    (graph.tau(nCurNode, nNode) * Math.pow(graph.etha(nCurNode, nNode), B)) / dSum;
                
                // if the value of p is greater the the average value the node is good
                if((graph.tau(nCurNode, nNode) * Math.pow(graph.etha(nCurNode, nNode), B)) > dAverage)
                {
                 
                    nMaxNode = nNode;
                }
            }
            
            if(nMaxNode == -1)
                nMaxNode = nNode;
       }
                 
        if(nMaxNode < 0)
            throw new RuntimeException("maxNode = -1");
        
        // delete the selected node from the list of node to visit
        m_nodesToVisitTbl.remove(new Integer(nMaxNode));
        
        return nMaxNode;
    }
    
    public void localUpdatingRule(int nCurNode, int nNextNode)
    {
        final AntGraph graph = s_antColony.getGraph();
        
      
        double val =
            ((double)1 - R) * graph.tau(nCurNode, nNextNode) +
            (R * (graph.tau0()));
        
        // update tau
        graph.updateTau(nCurNode, nNextNode, val);
    }
    
    public boolean better(double dPathValue1, double dPathValue2)
    {
        return dPathValue1 < dPathValue2;
    }

    public boolean end()
    {
        return m_nodesToVisitTbl.isEmpty();
    }

}
