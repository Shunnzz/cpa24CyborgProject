package com.example.monkeyfx;

public class BinaryTree {
    private Node root;

    public void insert(long data) {
        root = insertRec(root, data);
    }

    public Node insertRec(Node root, long data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }

        if (data < root.data) {
            root.left = insertRec(root.left, data);
        } else if (data > root.data) {
            root.right = insertRec(root.right, data);
        }

        return root;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        inorderTraversal(root, result);
        return result.toString();
    }

    public void inorderTraversal(Node root, StringBuilder result) {
        if (root != null) {
            inorderTraversal(root.left, result);
            result.append(root.data).append(" milliseconds\n");
            inorderTraversal(root.right, result);
        }
    }

    public static class Node {
        long data;
        Node left, right;

        Node(long data) {
            this.data = data;
            left = right = null;
        }
    }
}