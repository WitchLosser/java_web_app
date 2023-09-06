package org.example.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_products")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500, nullable = false)
    private String name;
    private double price;
    @Column(length = 4000)
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private boolean isDelete;
    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CategoryEntity category;

    @OneToMany(mappedBy="product")
    private List<ProductImageEntity> productImages;
}