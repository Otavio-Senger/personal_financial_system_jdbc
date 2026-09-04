package com.otaviosenger.financial.model;

public class Category {
    private int id;
    private String name;
    private Type type;

    //constructors
    public Category(int id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Category(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Category() {
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
