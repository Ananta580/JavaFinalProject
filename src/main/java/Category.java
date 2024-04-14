/**
 * Represents a category with an ID and a name.
 */
public class Category {
    private int id;
    private String name;

    /**
     * Constructs a new Category with the given ID and name.
     *
     * @param id   The ID of the category.
     * @param name The name of the category.
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the category.
     *
     * @return The ID of the category.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the category.
     *
     * @param id The ID of the category.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the category.
     *
     * @return The name of the category.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the category.
     *
     * @param name The name of the category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the Category object.
     *
     * @return A formatted string containing the ID and name of the category.
     */
    @Override
    public String toString() {
        return String.format("|    %-5s |   %-46s |", id, name);
    }
}
