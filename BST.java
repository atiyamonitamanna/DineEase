package DineEase;

import java.io.FileWriter;
import java.io.IOException;


class Node {
    MenuItem menuItem;
    Node left, right;

    public Node(MenuItem menuItem) {
        this.menuItem = menuItem;
        left = right = null;
    }
}

public class BST {

    Node root;

    private Node insertRec(Node root, MenuItem menuItem) {
        if (root == null) {
            root = new Node(menuItem);
            return root;
        }
        if (menuItem.getName().compareTo(root.menuItem.getName()) < 0) {
            root.left = insertRec(root.left, menuItem);
        } else if (menuItem.getName().compareTo(root.menuItem.getName()) > 0) {
            root.right = insertRec(root.right, menuItem);
        }
        return root;
    }

    public void insert(MenuItem menuItem) {
        root = insertRec(root, menuItem);
    }

    public void delete(String name) {
        root = deleteRec(root, name);
    }

    private Node deleteRec(Node root, String name) {
        if (root == null) {
            return root;
        }
        if (name.compareTo(root.menuItem.getName()) < 0) {
            root.left = deleteRec(root.left, name);
        } else if (name.compareTo(root.menuItem.getName()) > 0) {
            root.right = deleteRec(root.right, name);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            root.menuItem = minValue(root.right);
            root.right = deleteRec(root.right, root.menuItem.getName());
        }
        return root;
    }

    private MenuItem minValue(Node root) {
        MenuItem minv = root.menuItem;
        while (root.left != null) {
            minv = root.left.menuItem;
            root = root.left;
        }
        return minv;
    }

    public MenuItem search(String name) {
        return searchRec(root, name);
    }

    private MenuItem searchRec(Node root, String name) {
        if (root == null || root.menuItem.getName().equals(name)) {
            return (root != null) ? root.menuItem : null;
        }
        if (name.compareTo(root.menuItem.getName()) < 0) {
            return searchRec(root.left, name);
        }
        return searchRec(root.right, name);
    }

    public void displayInOrder() {
        displayInOrderRec(root);
    }

    private void displayInOrderRec(Node root) {
        if (root != null) {
            displayInOrderRec(root.left);
            System.out.println(root.menuItem);
            displayInOrderRec(root.right);
        }
    }

    public void displayInOrder(MenuItemVisitor menuItemVisitor) {
        displayInOrderRec(root, menuItemVisitor);
    }

    private void displayInOrderRec(Node root, MenuItemVisitor menuItemVisitor) {
        if (root != null) {
            displayInOrderRec(root.left, menuItemVisitor);
            menuItemVisitor.visit(root.menuItem);
            displayInOrderRec(root.right, menuItemVisitor);
        }
    }



    public interface MenuItemVisitor {
        void visit(MenuItem menuItem);

    }

    public void writeToFile(FileWriter writer) throws IOException {
        writeToFile(root, writer);
    }

    private void writeToFile(Node root, FileWriter writer) throws IOException {
        if (root != null) {
            writeToFile(root.left, writer);
            writer.write(root.menuItem.getName() + "," + root.menuItem.getPrice() + "," + root.menuItem.getAvailableQuantity() + "\n");
            writeToFile(root.right, writer);
        }
    }




}
