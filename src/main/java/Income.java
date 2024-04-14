import java.time.LocalDate;

/**
 * Represents an income entity, which is a type of transaction, with its properties and methods.
 */
public class Income extends Transaction {

    /**
     * Constructs an Income object with the specified parameters.
     *
     * @param id           The ID of the income.
     * @param description  The description of the income.
     * @param amount       The amount of the income.
     * @param categoryId   The category ID of the income.
     * @param createdDate  The creation date of the income.
     */
    public Income(int id, String description, double amount, int categoryId, LocalDate createdDate) {
        super(id, description, amount, categoryId, createdDate);
    }
}
