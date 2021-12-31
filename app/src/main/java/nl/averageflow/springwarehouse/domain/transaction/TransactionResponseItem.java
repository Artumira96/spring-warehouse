package nl.averageflow.springwarehouse.domain.transaction;

import nl.averageflow.springwarehouse.domain.product.dto.ProductResponseItem;
import nl.averageflow.springwarehouse.domain.user.UserResponseItem;

import java.sql.Timestamp;
import java.util.UUID;

public record TransactionResponseItem(
        UUID uid,
        Iterable<ProductResponseItem> products,
        UserResponseItem user,
        Timestamp createdAt) {
}