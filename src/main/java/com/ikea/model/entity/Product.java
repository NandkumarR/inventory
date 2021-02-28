package com.ikea.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * @author nandk
 * Product that is shipped to the customer and is made up of Article Object(s).
 */
@Document(collection = "products")
public class Product implements Serializable{
    @Id
    private String id;
    @JsonProperty("name")
    private String productName;
    @NotEmpty
    @JsonProperty("price")
    private Double productPrice;
    @NotEmpty
    @JsonProperty ("contain_articles")
    private List<ContainsArticle> containingArticles;
    public Product(){

    }
    public Product(String productName, Double productPrice,List<ContainsArticle> containingArticles) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.containingArticles=containingArticles;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public List<ContainsArticle> getContainingArticles() {
        return containingArticles;
    }

    public void setContainingArticles(List<ContainsArticle> containingArticles) {
        this.containingArticles = containingArticles;
    }

        public static class ContainsArticle{
            @NotEmpty
            @JsonProperty("art_id")
            private Integer articleId;
            @NotEmpty
            @JsonProperty("amount_of")
            private Integer requiredNumber;
            public ContainsArticle(){

            }
            public ContainsArticle(Integer articleId, Integer requiredNumber) {
                this.articleId = articleId;
                this.requiredNumber = requiredNumber;
            }

            public Integer getArticleId() {
                return articleId;
            }

            public void setArticleId(Integer articleId) {
                this.articleId = articleId;
            }

            public Integer getRequiredNumber() {
                return requiredNumber;
            }

            public void setRequiredNumber(Integer requiredNumber) {
                this.requiredNumber = requiredNumber;
            }
    }
}
