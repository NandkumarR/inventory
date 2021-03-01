package com.ikea.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author nandk
 * Holds stock of articles to be used for making Products.
 */
@Document(collection = "articles")
public class Article implements Serializable {
    @Id
    private String id;
    @JsonProperty("art_id")
    @NotEmpty
    private Integer identificationNumber;
    @JsonProperty("name")
    private String articleName;
    @JsonProperty("stock")
    private Integer availableStock;
    public Article(){
    }
    public Article(Integer identificationNumber, String articleName, Integer availableStock) {
        this.identificationNumber = identificationNumber;
        this.articleName = articleName;
        this.availableStock = availableStock;
    }

    public Integer getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(Integer identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    //Bean mapper for copying source to target data.
    public void beanMapper(Article sourceArticle) {
        if(sourceArticle.getArticleName()!=null){
            this.setArticleName(sourceArticle.getArticleName());
        }
        if (sourceArticle.getAvailableStock()!=null){
            this.setAvailableStock(this.getAvailableStock()+sourceArticle.getAvailableStock());
        }
    }
}
