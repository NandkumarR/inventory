package com.ikea.service.controllers;

import com.ikea.constants.Entity;
import com.ikea.exception.InventoryProcessException;
import com.ikea.model.entity.Article;
import com.ikea.model.entity.BuildProductsReq;
import com.ikea.model.entity.InventoryReq;
import com.ikea.model.entity.Product;
import com.ikea.service.repository.InventoryRepositoryService;
import com.ikea.service.utility.EntityMapper;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author nandk on 26/02/2021.
 */
@RestController
@RequestMapping(value = "/inventory")
public class InventoryController {

    @Resource
    EntityMapper entityMapper;

    @Resource
    InventoryRepositoryService inventoryRepositoryService;

    /**
     * Put-Away articles that need to be stocked up in the inventory.
     * @param inventoryArticles Accepts a json file containing Articles that need to be uploaded.
     * @throws InventoryProcessException
     */
    @PostMapping(value = "/articles")
    public List<Article> putAwayArticles(@RequestParam("inventory") MultipartFile inventoryArticles) throws InventoryProcessException {
        InventoryReq inventoryReq  = (InventoryReq) entityMapper.fileToEntityMapper(inventoryArticles, Entity.ARTICLE);
        return inventoryRepositoryService.stockUpArticles(inventoryReq.getArticles());
    }

    /**
     * Update current stock of articles when new article (stock(s)) arrives.
     * @param inventoryArticles Accepts a json file containing Articles that need to be uploaded.
     * @throws InventoryProcessException
     */
    @PatchMapping(value = "/articles")
    public List<Article> updateStockForArticles(@RequestParam("inventory") MultipartFile inventoryArticles) throws InventoryProcessException {
        InventoryReq inventoryReq  = (InventoryReq) entityMapper.fileToEntityMapper(inventoryArticles, Entity.ARTICLE);
        return inventoryRepositoryService.updateStockOfArticles(inventoryReq.getArticles());
    }

    /**
     * Build Product (spec) using articles. Defines how the product is made up of.
     * @param productList
     * @throws InventoryProcessException
     */
    @PostMapping(value = "/products")
    public List<Product> buildProductConfigurations(@RequestParam("products") MultipartFile productList) throws InventoryProcessException {
        BuildProductsReq buildProductList= (BuildProductsReq) entityMapper.fileToEntityMapper(productList, Entity.PRODUCT);
        return inventoryRepositoryService.buildProducts(buildProductList.getProducts());
    }

    /**
      * Fetch the Products inventory level. (Assumption: The current stock of products required)
      * @return Map of products and corresponding article stock.
     */
    @GetMapping(value="/products/inventorylevel")
    public Map<String,Integer> fetchProductsInventoryLevel(){
        return inventoryRepositoryService.fetchProductsAndInventoryLevel();
    }

    /**
     * Fetch Articles current inventory level
     * @return List of all Artciles and their correspondng inventory level
     */
    @GetMapping(value="/articles/inventorylevel")
    public List<Map<String,Object>> fetchArticlesInventoryLevel(){
        return inventoryRepositoryService.fetchArticlesAndInventoryLevel();
    }

    /**
     * Product sale at POS. The corresponding product and articles are updated in the inventory.
     * @param productId
     * @throws InventoryProcessException
     */
    @DeleteMapping(value = "/product/purchase/{productId}")
    public String productPurchased(@PathVariable String productId) throws InventoryProcessException {
        return inventoryRepositoryService.productPurchaseAndInventoryUpdate(productId);
    }
}
