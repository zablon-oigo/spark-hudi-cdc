package gmart.domain.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    @Column(name="id", updatable=false, nullable=false)
    private UUID id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="category", nullable=false)
    private String category;

    @Column(name="quantity", nullable=false)
    private int quantity;

    @Column(name="price", nullable=false)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable=false)
    private Status status;

    @CreationTimestamp
    @Column(name="created", nullable=false,updatable=false)
    private Instant created;

    @UpdateTimestamp
    @Column(name="updated", nullable=false)
    private Instant updated;

    public Inventory() {
    }

    public Inventory(UUID id, String name, String category, int quantity,
                     double price, Status status,
                     Instant created, Instant updated){
        this.id=id;
        this.name=name;
        this.category=category;
        this.quantity=quantity;
        this.price=price;
        this.status=status;
        this.created=created;
        this.updated=updated;
    }

    public UUID getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price=price;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status=status;
    }

    public Instant getCreated(){
        return created;
    }

    public void setCreated(Instant created){
        this.created=created;
    }

    public Instant getUpdated(){
        return updated;
    }

    public void setUpdated(Instant updated){
        this.updated=updated;
    }

    @Override
    public String toString(){
        return "Inventory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        Inventory inventory = (Inventory) o;
        return Objects.equals(id, inventory.id);
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }
}
