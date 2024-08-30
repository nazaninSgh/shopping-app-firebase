# shopping-app-firebase

## Overview

- In this project, I'm modifying [my previous store app](https://github.com/nazaninSgh/offline-store-app) to integrate Google Cloud Firebase, enhancing its functionality with real-time updates and cloud storage.
- The authentication is done using Firebase authentication.
- Firestore is used to manage data in the form of collections and documents. This NoSQL database fits this application's requirements due to real-time updates and extensive querying tools. (In progress)
- Firestore transactions are used for order confirmation, stock, and sales count changes, shopping cart status updates, and user credit updates.
- The real-time nature and asynchronous calls are particularly useful for providing users with live product stock information and managing their shopping cart.
- Firebase storage is used to store media files on the cloud and retrieve them using their download URL.

## Features

### Admin

- **View Categories**: Admins can view different product categories.
- **Best Seller Items**: Admins can view items that are the most popular.
- **Manage Orders**: Admins can view and manage customer orders.
- **Manage Customers**: Admins can search for customers and manage their details.
- **Product Management**: Admins can add, delete, search, and edit products.

### Customer

- **View Categories**: Customers can browse through product categories.
- **Best Seller Items**: Customers can view popular items.
- **Shopping Cart**: Customers can manage their shopping cart.
- **Profile Info**: Customers can view and update their profile information.

## Some of the employed technologies

- **Android Studio**
- **Firebase Authentication**
- **Firebase Storage**
- **Firestore**
- **Picasso**
- **Lists**
- **RecyclerView**
- **ViewPager**
- **Fragments**
- **Dialouges**
- **File Manager**
- **SQLite**
- ...


