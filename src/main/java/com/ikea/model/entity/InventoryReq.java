package com.ikea.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author nandk
 * InventoryReq class that holds articels required by the products.
 */
public class InventoryReq implements Serializable{
    @JsonProperty("inventory")
    private List<Article> articles;

    public InventoryReq(){
    }
    public InventoryReq(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
