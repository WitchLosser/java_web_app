package org.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;
@Data
@Entity
@Table(name = "tbl_product_images")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500, nullable = false)
    private String name;
    private int priority;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    private boolean isDelete;
    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductEntity product;
}