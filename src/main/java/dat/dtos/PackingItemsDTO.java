package dat.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
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

        @Setter
        @Getter
        public static class BuyingOption {
            private String shopName;
            private String shopUrl;
            private double price;
        }
    }
}
