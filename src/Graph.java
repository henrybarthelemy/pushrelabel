import java.util.ArrayList;

public class Graph {
  int size; //number of vertices
  ArrayList<Vertex> ver;
  ArrayList<Edge> edge;

  Graph(int size) {
    this.size = size;
    ver = new ArrayList<Vertex>();
    edge = new ArrayList<Edge>();
    //start all vertices with 0 height and 0 excess flow
    for (int i = 0; i < size; i += 1) {
      ver.add(new Vertex(0, 0));
    }
  }

  void addEdge(int u, int v, int capacity){
    edge.add(new Edge(0, capacity, u, v));
  }

  void printGraph(){
    System.out.println("Vertex info of the graph:");
    for (int i = 0; i < ver.size(); i += 1) {
      System.out.println("Vertex " + i + " has height of " + ver.get(i).h
          + " and excess flow of " + ver.get(i).ef);
    }

    System.out.println("Edge info of the graph:");
    for (Edge e : edge) {
      System.out.println("Edge from (" + e.u + ", " + e.v + ") has flow "
          + e.flow + " and cap " + e.capacity);
    }
  }


  void preflow(int s) {
    //Preflow:
    // - make height of s equal to the # of vertices
    // - height of all others is zero (done by construction)
    // - flow as much as possible to adj edges and update excess

    //make height of s equal to the # of vertices
    Vertex source = ver.get(s);
    source.h = size;

    //flow as much as possible to adj edges and update excess
    for (int i = 0; i < edge.size(); i += 1) {
      //if edge is coming from source, push flow through
      if (edge.get(i).u == s) {
        Edge adj = edge.get(i);
        adj.flow = adj.capacity;
        Vertex to = ver.get(adj.v);
        to.ef = adj.flow;

        //residual edge
        edge.add(new Edge(-adj.flow, 0, adj.v, s));
      }
    }
  }

  //index of overflow vertex, -1 if doesn't exist
  int overFlow(){
    //starting at i = 1 skips source and at < size - 1 skips sink
    for (int i = 1; i < this.size - 1; i += 1){
      if (ver.get(i).ef > 0) {
        return i;
      }
    }
    return -1;
  }

  //Update reverse flow for flow added on ith Edge
  void updateReverseEdgeFlow(int i, int flow) {
    int u = this.edge.get(i).v;
    int v = this.edge.get(i).u;

    for (Edge e : this.edge) {
      if (e.v == v && e.u == u) {
        e.flow -= flow;
        return;
      }
    }

    Edge e = new Edge(0, flow, u, v);
    edge.add(e);
  }

  //pushes flow from overflowing vertex u
  boolean push(int u) {
    for (int i = 0; i < edge.size(); i += 1) {
      Edge e = edge.get(i);
      //u of current edge is same as given (i.e. adj list of u)
      if (e.u == u) {
        if (e.flow == e.capacity) {
          continue;
        }

        // push can only be done when height of adjacent is smaller than cur
        if (ver.get(u).h > ver.get(e.v).h) {
          int flow = Math.min(e.capacity - e.flow, ver.get(u).ef);
          //reduce excess flow from cur
          ver.get(u).ef -= flow;
          //increase for adj
          ver.get(e.v).ef += flow;
          //add residual flow
          e.flow += flow;

          this.updateReverseEdgeFlow(i, flow);
          return true;
        }
      }
    }
    return false;
  }

  //update height of u to be 1 + minimum height of connected nodes
  void relabel(int u) {

    int mh = Integer.MAX_VALUE;
    //Find the adjacent with minimum height
    for (int i = 0; i < edge.size(); i += 1) {
      if (edge.get(i).u == u) {
        // if flow is equal to capacity then no relabeling
        if (edge.get(i).flow == edge.get(i).capacity) {
          continue;
        }

        //updating minimum height
        if (ver.get(edge.get(i).v).h < mh) {
          mh = ver.get(edge.get(i).v).h;
          // updating height of u
          ver.get(u).h = mh + 1;
        }
      }
    }
  }

    //Gets the max flow of this graph from some source s and sink t
    int getMaxFlow(int s, int t) {
      this.printGraph();
      this.preflow(s);
      this.printGraph();
      while(this.overFlow() != -1) {
        int u = this.overFlow();
        //prirorizes pushing over relabeling
        if(!this.push(u)){
          this.relabel(u);
        }
        this.printGraph();
      }

      return ver.get(ver.size() - 1).ef;
    }

}


