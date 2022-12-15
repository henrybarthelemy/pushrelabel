import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PushRelabelLaunch {

  public static void main(String[] args) {
    PushRelabelLaunch pl = new PushRelabelLaunch();
    try {
      pl.run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() throws IOException {
    int V = 6;
    Graph g = new Graph(6);

    InputStream in = getClass().getClassLoader().getResourceAsStream("input.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = null;
    while ( (line = reader.readLine()) != null) {
      String[] str = line.split(" ");
      g.addEdge(Integer.parseInt(str[0]) - 1,
          Integer.parseInt(str[1]) - 1,
          Integer.parseInt(str[2]));
    }

    System.out.println("Max flow : " + g.getMaxFlow(0, V - 1));
  }
}


