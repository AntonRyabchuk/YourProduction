package entities;

public class Component {
    private int    id;
    private String name;
    private String category;
    private String measureUnit;
    private Double prisePerUnit;

    public Component(String name, String category, String measureUnit, Double prisePerUnit) {
        this.name = name;
        this.category = category;
        this.measureUnit = measureUnit;
        this.prisePerUnit = prisePerUnit;
    }

    // GETTERS AND SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Double getPrisePerUnit() {
        return prisePerUnit;
    }

    public void setPrisePerUnit(Double prisePerUnit) {
        this.prisePerUnit = prisePerUnit;
    }
}
