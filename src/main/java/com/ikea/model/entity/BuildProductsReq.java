package com.ikea.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author nandk
 * Object mapper for Product request.
 */
public class BuildProductsReq {
    @JsonProperty("products")
    List<Product> products;

    public BuildProductsReq() {
    }

    public BuildProductsReq(List<Product> products) {
        this.products = products;
    }
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
