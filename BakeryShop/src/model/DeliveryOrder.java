package model;

public class DeliveryOrder {
    private String id;
    private String date;
    private String customerId;
    private String driverId;
    private String vehicleId;

    public DeliveryOrder() {
    }

    public DeliveryOrder(String id, String date, String customerId, String driverId, String vehicleId) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
    }

    public DeliveryOrder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", customerId='" + customerId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                '}';
    }
}
