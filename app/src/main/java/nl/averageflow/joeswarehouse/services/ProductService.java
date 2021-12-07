package nl.averageflow.joeswarehouse.services;

import nl.averageflow.joeswarehouse.models.ArticleAmountInProduct;
import nl.averageflow.joeswarehouse.models.Product;
import nl.averageflow.joeswarehouse.repositories.ArticleRepository;
import nl.averageflow.joeswarehouse.repositories.ProductArticleRepository;
import nl.averageflow.joeswarehouse.repositories.ProductRepository;
import nl.averageflow.joeswarehouse.requests.AddProductRequest;
import nl.averageflow.joeswarehouse.requests.AddProductsRequestItem;
import nl.averageflow.joeswarehouse.requests.SellProductsRequest;
import nl.averageflow.joeswarehouse.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public final class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ProductArticleRepository productArticleRepository;

    private static Iterable<Product> convertAddProductRequestToProductList(Iterable<AddProductsRequestItem> rawItems) {
        return StreamSupport.stream(rawItems.spliterator(), false).map(Product::new).collect(Collectors.toList());
    }

    private Iterable<Iterable<ArticleAmountInProduct>> convertAddProductArticleRequestToList(Iterable<AddProductsRequestItem> rawItems) {
        return StreamSupport.stream(rawItems.spliterator(), false)
                .map(item -> StreamSupport.stream(item.getContainArticles().spliterator(), false)
                        .map(articleItem -> new ArticleAmountInProduct(
                                        this.productRepository.findByItemId(item.getItemId()).get(),
                                        this.articleRepository.findByItemId(articleItem.getItemId()).get(),
                                        articleItem.getAmountOf()
                                )
                        ).collect(Collectors.toList())
                ).collect(Collectors.toList());
    }

    public ProductResponse getProducts() {
        return new ProductResponse(this.productRepository.findAll());
    }

    public Optional<Product> getProductByUid(UUID uid) {
        return this.productRepository.findByUid(uid);
    }

    public void deleteProductByUid(UUID uid) {
        this.productRepository.deleteByUid(uid);
    }

    public void addProducts(AddProductRequest request) {
        Iterable<Product> convertedProducts = convertAddProductRequestToProductList(request.getProducts());
        this.productRepository.saveAll(convertedProducts);

        Iterable<Iterable<ArticleAmountInProduct>> convertedArticleProductRelations = convertAddProductArticleRequestToList(request.getProducts());
        convertedArticleProductRelations.forEach(item -> this.productArticleRepository.saveAll(item));
    }

    public void sellProducts(SellProductsRequest request) {

    }
}
