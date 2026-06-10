package com.novaq.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tb_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String marca;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private List<ProductVariant> variacoes;
}
