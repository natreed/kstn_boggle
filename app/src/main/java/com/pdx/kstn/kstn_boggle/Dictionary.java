package com.pdx.kstn.kstn_boggle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by thanhhoang on 1/28/17.
 */


// read dictionary from inputstream, then add them into trie
public class Dictionary {

    private Node root;
    public ArrayList<String> listWord;

    public Dictionary() {
        this.root = new Node();
    }

    public void insert(String word) {
        if (word.length() < 3) return;

        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (current.links[index] == null) {
                Node temp = new Node();
                current.links[index] = temp;
                current = temp;
            } else {
                current = current.links[index];
            }
        }
        current.isWord = true;
    }

    public boolean isWord(String word) {
        Node current = searchNode(word);
        if (current != null && current.isWord == true)
            return true;
        return false;
    }

    private Node searchNode(String word) {
        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (current.links[index] == null) return null;
            else current = current.links[index];
        }
        if (current == root) return null;

        return current;
    }

    public Node getRootDictionary() {
        return root;
    }

    static public Node nextNode(String word, Node node) {
        Node currentNode = node;
        if (currentNode == null) return null;
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            int index = letter - 'a';

            if (currentNode.links[index] == null) return null;
            else currentNode = currentNode.links[index];
        }
        return currentNode;
    }

    public void createDictionary(InputStream in) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                insert(line);
            }
            in.close();
        } catch (Exception e) {

        }

    }

}
