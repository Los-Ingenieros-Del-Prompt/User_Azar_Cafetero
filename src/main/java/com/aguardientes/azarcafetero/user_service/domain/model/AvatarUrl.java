package com.aguardientes.azarcafetero.user_service.domain.model;

public class AvatarUrl {

    private final String value;

    public AvatarUrl(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("La URL del avatar no puede estar vacía");
        if (!value.startsWith("https://"))
            throw new IllegalArgumentException("La URL debe ser segura (https)");
        this.value = value;
    }

    public String getValue() { return value; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AvatarUrl)) return false;
        return value.equals(((AvatarUrl) o).value);
    }

    @Override
    public int hashCode() { return value.hashCode(); }

    @Override
    public String toString() { return value; }
}
