/**--------------------------------------------------------------------------------
 * @author AKSHAYAN MOHANDAS - w1867142
 * Generating random values to form an acyclic graph and use it in the text file.
 * --------------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.Random;

public class TextValue {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayList<Integer> nodes = new ArrayList<>();
        nodes.add(0);
        for (int i = 1; i <= 640; i++) {
            int node = random.nextInt(i);
            System.out.println(nodes.get(node) + " " + i);
            nodes.add(i);
        }
    }
}

