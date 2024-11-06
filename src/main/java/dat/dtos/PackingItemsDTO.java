package dat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PackingItemsDTO {
    private List<PackingItem> items; // List of packing items

    public static class PackingItem {
        private String name;
        private int weightInGrams;
        private int quantity;
        private String description;
        private String category;

        @JsonProperty("createdAt")
        private String createdAt;

        @JsonProperty("updatedAt")
        private String updatedAt;

        // Declare buying options field
        private List<BuyingOption> buyingOptions;

        // Buying option class defined within PackingItem
        public static class BuyingOption {
            private String shopName;
            private String shopUrl;
            private double price;

            // Getters for BuyingOption
            public String getShopName() {
                return shopName;
            }

            public String getShopUrl() {
                return shopUrl;
            }

            public double getPrice() {
                return price;
            }
        }

        // Getters for PackingItem fields
        public String getName() {
            return name;
        }

        public int getWeightInGrams() {
            return weightInGrams;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        // Getter for buying options
        public List<BuyingOption> getBuyingOptions() {
            return buyingOptions;
        }
    }

    // Getter for the list of items
    public List<PackingItem> getItems() {
        return items;
    }
}
