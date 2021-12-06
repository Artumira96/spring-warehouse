package nl.averageflow.joeswarehouse.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Table(name = "products")
@Entity
public final class Product {
    @Id
    @SequenceGenerator(name = "products_sequence_generator", sequenceName = "products_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_sequence_generator")
    @NonNull
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "item_name")
    private String name;

    @NonNull
    @Column(name = "price")
    private Double price;

    @NonNull
    @Column(name = "created_at")
    private Long createdAt;

    @NonNull
    @Column(name = "updated_at")
    private Long updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private Set<ArticleAmountInProduct> articleProductRelation;

    protected Product() {
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Double getPrice() {
        return this.price;
    }

    public Long getCreatedAt() {
        return this.createdAt;
    }

    public Long getUpdatedAt() {
        return this.updatedAt;
    }

    public Long getProductStock() {
        List<Long> amountOfProductsPossibleList = new ArrayList<Long>();

        for (ArticleAmountInProduct articleAmountInProduct : articleProductRelation) {
            Long articleAmountNeeded = articleAmountInProduct.getAmountOf();
            Long articleStockPresent = articleAmountInProduct.getArticle().getStock().getStock();

            // System.out.printf("articleAmountNeeded: %d, articleStockPresent: %d\n",
            // articleAmountNeeded,
            // articleStockPresent);
            if (articleStockPresent < articleAmountNeeded) {
                return (long) 0;
            } else {
                amountOfProductsPossibleList.add(articleStockPresent / articleAmountNeeded);
            }
        }

        return amountOfProductsPossibleList.stream()
                .min(Comparator.naturalOrder())
                .get();
    }

    public Set<ArticleAmountInProduct> getArticles() {
        // return new ArticleResponse(this.articles);
        return this.articleProductRelation;
    }

}