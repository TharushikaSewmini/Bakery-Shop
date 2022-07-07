package model;

public class OrderDetails {
    private String orderId;
    private String productCode;
    private int qty;
    private double unitPrice;
    private double discount;

    public OrderDetails() {
    }

    public OrderDetails(String orderId, String productCode, int qty, double unitPrice, double discount) {
        this.orderId = orderId;
        this.productCode = productCode;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.discount = discount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId='" + orderId + '\'' +
                ", productCode='" + productCode + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", discount=" + discount +
                '}';
    }
}
