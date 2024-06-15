package DineEase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuManagementGUI {
    private final MenuManagement menuManagement;
    private final JFrame frame;
    private final JTextArea menuDisplayArea;
    private final JTextArea orderDisplayArea;

    public MenuManagementGUI() {
        menuManagement = new MenuManagement();

        // Initial menu items
        menuManagement.addMenuItem("Burger", 5.99, 15);
        menuManagement.addMenuItem("Pizza", 8.99, 25);
        menuManagement.addMenuItem("Salad", 4.49, 20);

        frame = new JFrame("DineEase Menu Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        menuDisplayArea = new JTextArea();
        menuDisplayArea.setEditable(false);
        orderDisplayArea = new JTextArea();
        orderDisplayArea.setEditable(false);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));

        JButton displayMenuButton = new JButton("Display Menu");
        displayMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMenu();
            }
        });

        JButton addMenuItemButton = new JButton("Add Menu Item");
        addMenuItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMenuItem();
            }
        });

        JButton deleteMenuItemButton = new JButton("Delete Menu Item");
        deleteMenuItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMenuItem();
            }
        });

        JButton takeOrderButton = new JButton("Take Customer Order");
        takeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takeCustomerOrder();
            }
        });

        JButton displayOrderSummaryButton = new JButton("Display Order Summary");
        displayOrderSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayOrderSummary();
            }
        });

        JButton processNextOrderButton = new JButton("Process Next Order");
        processNextOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processNextOrder();
            }
        });

        controlPanel.add(displayMenuButton);
        controlPanel.add(addMenuItemButton);
        controlPanel.add(deleteMenuItemButton);
        controlPanel.add(takeOrderButton);
        controlPanel.add(displayOrderSummaryButton);
        controlPanel.add(processNextOrderButton);

        frame.add(new JScrollPane(menuDisplayArea), BorderLayout.CENTER);
        frame.add(new JScrollPane(orderDisplayArea), BorderLayout.SOUTH);
        frame.add(controlPanel, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private void displayMenu() {
        menuDisplayArea.setText("");
        menuManagement.menuBST.displayInOrder(new MenuItemPrinter());
    }

    private void addMenuItem() {
        String name = JOptionPane.showInputDialog("Enter item name:");
        double price = Double.parseDouble(JOptionPane.showInputDialog("Enter item price:"));
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter available quantity:"));
        menuManagement.addMenuItem(name, price, quantity);
        displayMenu();
    }

    private void deleteMenuItem() {
        String name = JOptionPane.showInputDialog("Enter item name to delete:");
        menuManagement.deleteMenuItem(name);
        displayMenu();
    }

    private void takeCustomerOrder() {
        int numItems = Integer.parseInt(JOptionPane.showInputDialog("How many items do you want to order?"));
        List<OrderItem> currentOrder = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            String itemName = JOptionPane.showInputDialog("Enter item name:");
            MenuItem menuItem = menuManagement.menuBST.search(itemName);

            if (menuItem == null) {
                JOptionPane.showMessageDialog(frame, "Item not found in the menu!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!menuItem.isAvailable(1)) {
                JOptionPane.showMessageDialog(frame, menuItem.getName() + " is not available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter quantity for " + menuItem.getName() + ":"));
            if (!menuItem.isAvailable(quantity)) {
                JOptionPane.showMessageDialog(frame, "Not enough quantity available for " + menuItem.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentOrder.add(new OrderItem(menuItem, quantity));
        }

        // Update the quantity of each ordered item
        for (OrderItem orderItem : currentOrder) {
            MenuItem menuItem = orderItem.getMenuItem();
            menuItem.decreaseQuantity(orderItem.getQuantity());
        }

        menuManagement.orderQueue.addAll(currentOrder);
        menuManagement.saveToFile(menuManagement.menuBST);  // Save the updated menu to the file
        JOptionPane.showMessageDialog(frame, "Your order has been placed successfully!", "Order Placed", JOptionPane.INFORMATION_MESSAGE);
        displayMenu();
    }

    private void displayOrderSummary() {
        orderDisplayArea.setText("");
        double totalBill = 0;
        for (OrderItem orderItem : menuManagement.orderQueue) {
            String orderSummary = orderItem.getMenuItem().getName() + " x " + orderItem.getQuantity() +
                    " = $" + orderItem.getTotalPrice() + "\n";
            orderDisplayArea.append(orderSummary);
            totalBill += orderItem.getTotalPrice();
        }
        orderDisplayArea.append("Total Bill: $" + totalBill + "\n");
        processPayment(totalBill);
    }

    private void processPayment(double totalBill) {
        double payment = Double.parseDouble(JOptionPane.showInputDialog("Enter payment amount: $"));
        double change = payment - totalBill;
        if (change >= 0) {
            JOptionPane.showMessageDialog(frame, "Payment successful. Your change: $" + change, "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "Insufficient payment. Please provide more money.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
            processPayment(totalBill); // Retry payment
        }
    }

    private void processNextOrder() {
        if (!menuManagement.orderQueue.isEmpty()) {
            OrderItem orderItem = menuManagement.orderQueue.poll();
            MenuItem menuItem = orderItem.getMenuItem();
            double totalCost = orderItem.getTotalPrice();
            JOptionPane.showMessageDialog(frame, "Processing order: " + menuItem.getName() +
                    " x " + orderItem.getQuantity() + ", Total Cost: $" + totalCost, "Order Processing", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No orders to process.", "Order Processing", JOptionPane.INFORMATION_MESSAGE);
        }
        menuManagement.saveToFile(menuManagement.menuBST);  // Save the updated menu to the file
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MenuManagementGUI();
            }
        });
    }

    private class MenuItemPrinter implements BST.MenuItemVisitor {
        @Override
        public void visit(MenuItem menuItem) {
            menuDisplayArea.append(menuItem.toString() + "\n");
        }
    }
}





