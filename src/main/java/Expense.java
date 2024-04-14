import java.time.LocalDate;

/**
 * Represents an expense entity, which is a type of transaction, with its properties and methods.
 */
public class Expense extends Transaction {

    /**
     * Constructs an Expense object with the specified parameters.
     *
     * @param id           The ID of the expense.
     * @param description  The description of the expense.
     * @param amount       The amount of the expense.
     * @param categoryId   The category ID of the expense.
     * @param createdDate  The creation date of the expense.
     */
    public Expense(int id, String description, double amount, int categoryId, LocalDate createdDate) {
        super(id, description, amount, categoryId, createdDate);
    }
}
