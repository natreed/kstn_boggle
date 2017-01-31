package com.pdx.kstn.kstn_boggle;

/**
 * Created by thanhhoang on 1/30/17.
 */

public class Node {

    public Node[] links;
    public boolean isWord;

    public Node() {
        this.isWord = false;
        this.links = new Node[26];
    }

    /**
     *
     * @param letterIndex
     * @param node
     * @return
     */
    static public Node findNextNode(int letterIndex, Node node) {
        if (node.links[letterIndex] != null)
            return node.links[letterIndex];

        return null;
    }
}
