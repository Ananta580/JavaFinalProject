import java.time.LocalDate;

/**
 * Represents a transaction entity with its properties and methods.
 */
public class Transaction {
    private int id;
    private String description;
    private double amount;
    private int categoryId;
    private LocalDate createdDate;

    /**
     * Constructs a Transaction object with the specified parameters.
     *
     * @param id           The ID of the transaction.
     * @param description  The description of the transaction.
     * @param amount       The amount of the transaction.
     * @param categoryId   The category ID of the transaction.
     * @param createdDate  The creation date of the transaction.
     */
    public Transaction(int id, String description, double amount, int categoryId, LocalDate createdDate) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
    }

    /**
     * Gets the ID of the transaction.
     *
     * @return The ID of the transaction.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the transaction.
     *
     * @param id The ID of the transaction to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the description of the transaction.
     *
     * @return The description of the transaction.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description The description of the transaction to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the amount of the transaction.
     *
     * @return The amount of the transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.
     *
     * @param amount The amount of the transaction to set.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Gets the category ID of the transaction.
     *
     * @return The category ID of the transaction.
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID of the transaction.
     *
     * @param categoryId The category ID of the transaction to set.
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the creation date of the transaction.
     *
     * @return The creation date of the transaction.
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the creation date of the transaction.
     *
     * @param createdDate The creation date of the transaction to set.
     */
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Returns a string representation of the Transaction object.
     *
     * @return A string representation of the Transaction object.
     */
    @Override
    public String toString() {
        return String.format("|    %-5s |   %-54s |    %-9s |    %-14s |    %-22s |", id, description, amount, categoryId, createdDate.toString());
    }
}
