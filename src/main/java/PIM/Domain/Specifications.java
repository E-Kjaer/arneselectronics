package PIM.Domain;

public class Specifications {
        private int id;
        private String key; //specification_name
        private String value; //specificiation_value
        private Product product_id;

        //Constructor
        public Specifications(int id, String key, String value, Product product_id) {
            this.id = id;
            this.key = key;
            this.value = value;
            this.product_id = product_id;
        }

        @Override
        public String toString() {
            return "Specifications{" + "id=" + id
                    + ", key='" + key
                    + ", value='" + value
                    + ", product_id=" + product_id
                    + '}';
        }

        //Getters and Setters
        public int getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public Product getProduct_id() {
            return product_id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setProduct_id(Product product_id) {
            this.product_id = product_id;
        }
    }
