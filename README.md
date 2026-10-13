# Project 4 - Restaurant Order and Billing System

A Java console application for a busy restaurant. It takes orders from tables,
tracks kitchen progress, and prints a bill at the end.

## Core Features

- Manage the menu (dishes, categories, prices, availability, stock)
- Open a table order and add or remove items
- Send items to the kitchen queue and mark them preparing, ready, or served
- Compute the bill including tax and optional discount
- Track daily sales and write a summary to a text file at end of day
- Handle out-of-stock items gracefully with custom exceptions

## OOP Design

| Requirement                                   | Implementation                                          |
|-----------------------------------------------|---------------------------------------------------------|
| Abstract class MenuItem                       | `model/MenuItem` with subclasses `FoodItem`, `DrinkItem` |
| Interface KitchenStation                      | `kitchen/KitchenStation` implemented by `Grill`, `Fryer`, `Bar` |
| Interface Discount (strategies)               | `billing/Discount` implemented by `HappyHourDiscount`, `StudentDiscount`, `LoyaltyDiscount`, `NoDiscount` |
| Queue<OrderItem> for the kitchen              | `kitchen/Kitchen` (FIFO `LinkedList` queue)             |
| Custom exceptions                             | `OutOfStockException`, `ItemNotFoundException`, `InvalidOrderException` |

## Freedom Points (our choices)

- Menu: 13 Kenyan-style items across starters, mains, sides, dessert and drinks
- Tax rate: 16% (VAT), currency KES
- Discounts: Happy Hour 20%, Student 10%, Loyalty 5% + 1% per 100 points (max 15%)
- Kitchen workflow: NEW -> QUEUED -> PREPARING -> READY -> SERVED
- Extras: tip calculation and split bills

## Project Structure

```
src/restaurant/
  RestaurantSystem.java      main class (console menu + demo)
  model/                     MenuItem, FoodItem, DrinkItem, Menu, TableOrder, OrderItem, enums
  kitchen/                   KitchenStation, Grill, Fryer, Bar, Kitchen
  billing/                   Discount strategies and Bill
  sales/                     SalesTracker (daily summary file)
  exception/                 custom exceptions
```

## Requirements

- JDK 17 or newer (developed on JDK 21)

## Build and Run

```
mkdir out
javac -d out $(find src -name "*.java")
java -cp out restaurant.RestaurantSystem
```

Windows (PowerShell):

```
mkdir out
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out restaurant.RestaurantSystem
```

Run the scripted demo (shows ordering, out-of-stock handling, kitchen queue,
discounts, split bill and the end-of-day file):

```
java -cp out restaurant.RestaurantSystem --demo
```

## How to Use (interactive)

1. Open a table order (option 3)
2. Add items by menu id (option 4); out-of-stock items are rejected with a message
3. Send to kitchen (7), then in the kitchen board (8) start next, mark ready, mark served
4. Bill and pay (9): choose discount, tip and optional split
5. End of day summary (10) writes `daily_sales_YYYY-MM-DD.txt`

## Sample Output

```
Subtotal                   KES   3000.00
Discount: Happy Hour (20%) KES   -600.00
Tax (16%)                  KES    384.00
Tip (10%)                  KES    240.00
TOTAL                      KES   3024.00
```

## Group Members

- (add names and admission numbers here)
