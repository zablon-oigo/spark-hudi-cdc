package gmart.exception;

import java.util.UUID;

public class InventoryNotFoundException extends RuntimeException {

    private final UUID id;

    public InventoryNotFoundException(UUID id) {
        super(String.format("Inventory with ID '%s' does not exist.", id));
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}