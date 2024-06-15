package DineEase;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class MenuManagement {

    public final BST menuBST;
    public final Queue<OrderItem> orderQueue;

    public MenuManagement() {
        menuBST = new BST();
        orderQueue = new LinkedList<>();
    }

    public void addMenuItem(String name, double price, int availableQuantity) {
        MenuItem menuItem = new MenuItem(name, price, availableQuantity);
        menuBST.insert(menuItem);
        saveToFile(menuBST);  // Save the updated menu to the file
    }

    public void deleteMenuItem(String name) {
        menuBST.delete(name);
        saveToFile(menuBST);  // Save the updated menu to the file
    }

    public void displayMenuForCustomer() {
        System.out.println("Menu for Customer:");
        menuBST.displayInOrder();
    }

    public void takeCustomerOrder(Scanner scanner) {
        System.out.print("How many items do you want to order? ");
        int numItems = scanner.nextInt();
        scanner.nextLine(); // consume newline

        List<OrderItem> currentOrder = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            System.out.print("Enter item name: ");
            String itemName = scanner.nextLine();

            MenuItem menuItem = menuBST.search(itemName);
            if (menuItem == null) {
                System.out.println("Item not found in the menu.");
                return;
            }

            if (!menuItem.isAvailable(1)) {
                System.out.println(menuItem.getName() + " is not available.");
                return;
            }

            System.out.print("Enter quantity for " + menuItem.getName() + ": ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (!menuItem.isAvailable(quantity)) {
                System.out.println("Not enough quantity available for " + menuItem.getName());
                return;
            }

            currentOrder.add(new OrderItem(menuItem, quantity));
        }
 
        // Update the quantity of each ordered item
        for (OrderItem orderItem : currentOrder) {
            MenuItem menuItem = orderItem.getMenuItem();
            menuItem.decreaseQuantity(orderItem.getQuantity());
        }

        orderQueue.addAll(currentOrder);
        System.out.println("Your order has been placed successfully.");
        saveToFile(menuBST);  // Save the updated menu to the file
    }

    public void displayOrderSummary() {
        System.out.println("Order Summary:");
        double totalBill = 0;
        for (OrderItem orderItem : orderQueue) {
            System.out.println(orderItem);
            totalBill += orderItem.getTotalPrice();
        }
        System.out.println("Total Bill: $" + totalBill);
    }

    public void processNextOrder() {
        if (!orderQueue.isEmpty()) {
            OrderItem orderItem = orderQueue.poll();
            System.out.println("Processing order: " + orderItem);
        } else {
            System.out.println("No orders to process.");
        }
        saveToFile(menuBST);  // Save the updated menu to the file
    }

    public void saveToFile(BST menuBST) {
        try (FileWriter writer = new FileWriter("menu.txt")) {
            menuBST.writeToFile(writer);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public void writeToFile(BST menuBST) {
        try (FileWriter writer = new FileWriter("menu.txt")) {
            menuBST.writeToFile(writer);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }



}