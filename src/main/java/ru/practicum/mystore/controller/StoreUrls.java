package ru.practicum.mystore.controller;

public interface StoreUrls {
    String ROOT = "";

    interface Main {
        String PART = "main";
        String FULL = ROOT + "/" + PART;
    }

    interface Items {
        String PART = "items";
        String FULL = ROOT + "/" + PART;

        interface ItemId {
            String PART = "{itemId}";
            String FULL = Items.FULL + "/" + PART;
        }

        interface Add {
            String PART = "add";
            String FULL = Items.FULL + "/" + PART;
        }
    }

    interface Cart {
        String PART = "cart";
        String FULL = ROOT + "/" + PART;

        interface ItemId {
            String PART = "{itemId}";
            String FULL = Cart.FULL + "/" + PART;
        }
    }

    interface Orders {
        String PART = "orders";
        String FULL = ROOT + "/" + PART;

        interface OrderId {
            String PART = "{orderId}";
            String FULL = Orders.FULL + "/" + PART;
        }
    }
}
