package com.aguardientes.azarcafetero.user_service.domain.model;

public class Username {

    private final String value;
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;

    public Username(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH)
            throw new IllegalArgumentException("El nombre debe tener entre " + MIN_LENGTH + " y " + MAX_LENGTH + " caracteres");
        if (!value.matches("^[a-zA-Z0-9_]+$"))
            throw new IllegalArgumentException("Solo letras, números y guiones bajos");
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Username)) return false;
        return value.equalsIgnoreCase(((Username) o).value);
    }

    @Override
    public int hashCode() { return value.toLowerCase().hashCode(); }

    @Override
    public String toString() { return value; }
}
