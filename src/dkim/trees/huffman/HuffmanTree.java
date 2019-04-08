package dkim.trees.huffman;

import java.util.*;

public class HuffmanTree implements Comparable<HuffmanTree> {

    /* Root Node of the Tree */
    private Node root;

    private String inputString;

    /* Constructor */
    public HuffmanTree(Node root) {
        this.root = root;
    }

    public HuffmanTree(String inputString) {
        this.inputString = inputString;
    }

    /**
     * Creates Huffman Tree from char frequency : chars following this algo:
     * 1. Create Node instance for each char in a message
     * 2. Create Tree instance for each Node instance. Node becomes root
     * 3. Offer each Tree in a Priority Queue. The least frequency has the highest priority
     * 4. Poll two Trees from the Priority Queue and merge them to a new parent Node.
     * Parent Node frequency is a sum of it's child's frequencies. (field "character" can be null)
     * 5. Offer new Tree to the Priority Queue
     * 6. Repeat 4-5 until 1 Tree left. This Tree is the Huffman Tree
     *
     * @return Huffman Tree
     */
    public HuffmanTree buildHuffmanTree() {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<>();
        Integer[] charFrequencies = charFrequencyTable().values().toArray(new Integer[0]);
        Character[] chars = charFrequencyTable().keySet().toArray(new Character[0]);

        for (int i = 0; i < charFrequencies.length; i++) {
            if (charFrequencies[i] > 0) {
                trees.offer(new HuffmanTree(new Node(charFrequencies[i], chars[i])));
            }
        }

        while (trees.size() > 1) {
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
            trees.offer(new HuffmanTree(new Node(a, b)));
        }

        return trees.poll();
    }

    /**
     * Creates Character Frequency Table
     * from the inputString
     *
     * @return frequency table
     */
    private Map<Character, Integer> charFrequencyTable() {
        Map<Character, Integer> frequencyTable = new HashMap<>();

        char[] chars = inputString.toCharArray();
        for (char c : chars) {
            if (frequencyTable.containsKey(c)) {
                Integer frequency = frequencyTable.get(c);
                frequency++;
                frequencyTable.put(c, frequency);
            } else {
                frequencyTable.put(c, 1);
            }
        }

        return frequencyTable;
    }

    /**
     * Encode some text according to the code table
     * Replacing each char in a String with corresponding "0101"-sequence
     * @param text
     * @return encoded text
     */
    public String encode(String text) {
        StringBuilder encode = new StringBuilder();
        Map<Character, String> codeTable = codeTable();

        for (int i = 0; i < text.length(); i++) {
            encode.append(codeTable.get(text.charAt(i)));
        }
        return encode.toString();
    }

    /**
     * Creates code table according to the Huffman Tree
     * for encoding/decoding
     *
     * @return codeTable
     */
    public Map<Character, String> codeTable() {
        Map<Character, String> codeTable = new HashMap<>();
        StringBuilder code = new StringBuilder();
        codeTable(root, code, codeTable);

        return codeTable;
    }

    /**
     * Recursive method for building Code Table
     * Left Node = 0
     * Right Node = 1
     *
     * @param node      - current node
     * @param code      - code for the current node
     * @param codeTable
     */
    private void codeTable(Node node, StringBuilder code, Map<Character, String> codeTable) {
        /* leaf node */
        if (node.character != null) {
            if (code.toString().isEmpty()) {
                code.append("0");
            }
            codeTable.put(node.character, code.toString());
            return;
        }
        /* left subtree*/
        codeTable(node.leftNode, code.append('0'), codeTable);
        code.deleteCharAt(code.length() - 1);

        /* right subtree*/
        codeTable(node.rightNode, code.append('1'), codeTable);
        code.deleteCharAt(code.length() - 1);
    }


    @Override
    public int compareTo(HuffmanTree tree) {
        return root.frequency - tree.root.frequency;
    }

    /**
     * Node class
     */
    class Node {

        /* Char Frequency */
        private Integer frequency;

        /* Character */
        private Character character;

        /* Left Child Node = 0*/
        private Node leftNode;

        /* Right Child Node = 1*/
        private Node rightNode;

        /* Constructors */
        public Node(Integer frequency, Character character) {
            this.frequency = frequency;
            this.character = character;
        }

        public Node(HuffmanTree left, HuffmanTree right) {
            frequency = left.root.frequency + right.root.frequency;
            leftNode = left.root;
            rightNode = right.root;
        }

        @Override
        public String toString() {
            return "[freq: " + frequency + " | char: " + character + "]";
        }
    }

}
