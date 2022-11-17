package ru.gb.lesson_99.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.gb.lesson_99.entity.enums.Status;


import javax.persistence.*;
import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "Cart")
@EntityListeners(AuditingEntityListener.class)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;

    @Column(name = "amt")
    private int amt;
    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "price")
    private BigDecimal price;

//    @ManyToOne
//    @JoinColumn(name = "manufacturer_id")
//    private Manufacturer manufacturer;



    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

//    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(name = "cart_product",
//    joinColumns = @JoinColumn(name = "product_id"),
//    inverseJoinColumns = @JoinColumn(name = "cart_id"))
//    private Set<Cart> carts;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                "}\n";
    }
}

