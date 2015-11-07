package DataStructures;

import java.util.ArrayList;

/**
 * Created by rasrivastava on 10/28/15.
 */
class Node {
    private ArrayList<Node> children;
    private int values[];
    Node(int m) {
        values = new int[m];
        for (int i = 0;i<m;++i) {
            values[m] = Integer.MAX_VALUE;
        }

    }

    void addNode(int data) {
        for (int i = 1;i<values.length-1;++i) {
            if (values[i] != Integer.MAX_VALUE && values[i] > data) {
                addChild(data,i);
            }
        }

        //No empty person
    }

    void addChild(int data,int i) {

    }
}
public class BTree {

}
