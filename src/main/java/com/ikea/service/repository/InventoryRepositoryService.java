package com.ikea.service.repository;

import com.ikea.exception.InventoryProcessException;
import com.ikea.model.entity.Article;
import com.ikea.model.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * @author nandk on 27/02/2021.
 */
public interface InventoryRepositoryService {
    /**
     * Service used for Article creation in the inventory.
     * @param putAwayArticles
     * @return List of Article objects created.
     */
    List<Article> stockUpArticles(List<Article> putAwayArticles);

    /**
     * Service used for updating  existing Article objects. If no Articles are found throws an exception.
     * Else stores the Articles that are available.
     * @param updateStockArtciles
     * @return List of Articles updated.
     * @throws InventoryProcessException if no Articles found in the Inventory.
     */
    List<Article> updateStockOfArticles(List<Article> updateStockArtciles) throws InventoryProcessException;

    /**
     * Fetch all Artciles inventory level. Returns a List of Articles and corresponding inventory level.
     * @return List of Articles and inventory level.
     */
    List<Map<String,Object>>  fetchArticlesAndInventoryLevel();

    /**
     * Fetch all the Articles available in the Inventory.
     * @return List of Articles.
     */
    List<Article> findAllArticles();

    /**
     * Fetch all Articles for the given list of Identification Numbers.
     * @param identificationNumbers
     * @return List of Articles corresponding to the identificationNumber.
     */
    List<Article> findArticleByIdentificationNumber(List<Integer> identificationNumbers);

    /**
     * Create Products in the Inventory. ( Understanding is ,its more of a specification/configuration)
     * @param productList
     * @return List of Products created.
     * @throws InventoryProcessException
     */
    List<Product> buildProducts(List<Product> productList) throws InventoryProcessException;

    /**
     * Fetch current products and its corresponding Articles inventory level in the Inventory.
     * @return List of Products and Artcile Identification number and its inventory level.
     */
    Map<String,Integer> fetchProductsAndInventoryLevel();

    /**
     * Fetch all the Products available in the Inventory.
     * @return List of Products available in the Inventory.
     */
    List<Product> findAllProducts();

    /**
     * Service executed whenever a product is sold out at POS. The inventory is updated with the stock of Articles post the purchase.
     * @param productId
     * @throws InventoryProcessException When no Product is found.
     */
    String productPurchaseAndInventoryUpdate(String productId) throws InventoryProcessException;
}
