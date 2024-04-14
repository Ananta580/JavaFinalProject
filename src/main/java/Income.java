import java.time.LocalDate;

public class Income {
    private int id;
    private String description;
    private double amount;
    private int categoryId;
    private LocalDate createdDate;

    public Income(int id, String description, double amount, int categoryId, LocalDate createdDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return String.format("|    %-5s |   %-54s |    %-9s |    %-14s |    %-22s |", id, description, amount, categoryId, createdDate.toString());
    }

}
