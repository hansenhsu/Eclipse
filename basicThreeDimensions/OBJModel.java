package basicThreeDimensions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OBJModel {
    private List<double[]> vertices = new ArrayList<>();
    private List<int[]> faces = new ArrayList<>();

    public void load(String filename) throws IOException {
    	Random rand = new Random();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s+");
            if (tokens[0].equals("v")) {
                double[] vertex = new double[3];
                vertex[0] = Double.parseDouble(tokens[1])*1000;
                vertex[1] = Double.parseDouble(tokens[2])*1000;
                vertex[2] = Double.parseDouble(tokens[3])*1000;
                vertices.add(vertex);
            } else if (tokens[0].equals("f")) {
                int[] face = new int[tokens.length];
                for (int i=0;i<face.length-1;i++) {
                    face[i] = Integer.parseInt(tokens[i+1].split("/")[0]);
                }
                face[face.length-1] = rand.nextInt(100);
                faces.add(face);
            }
        }
        reader.close();
    }

    public double[][] getVertices() {
        return vertices.toArray(new double[0][]);
    }

    public int[][] getFaces() {
        return faces.toArray(new int[0][]);
    }
    
    public void printModel() {
        System.out.println("Vertices:");
        for (double[] vertex : vertices) {
            System.out.println("{"+vertex[0]+", "+vertex[1]+", "+vertex[2]+"}");
        }

        System.out.println("\nFaces:");
        for (int[] face : faces) {
            System.out.print("{");
            for (int i=0;i<face.length;i++) {
                System.out.print(face[i]);
                if (i<face.length-1) {
                    System.out.print(", ");
                }
            }
            System.out.println("}");
        }
    }
}