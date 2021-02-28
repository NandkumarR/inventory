package com.ikea.service.repository.impl;

import com.ikea.exception.InventoryProcessException;
import com.ikea.model.entity.Article;
import com.ikea.model.entity.Product;
import com.ikea.service.repository.ArticleRepository;
import com.ikea.service.repository.InventoryRepositoryService;
import com.ikea.service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author nandk on 26/02/2021.
 * Implementation class for InventoryRepositryService.
 */
@Service
public class InventoryRepositoryServiceImpl implements InventoryRepositoryService{

    Logger logger = LoggerFactory.getLogger(InventoryRepositoryServiceImpl.class);

    @Resource
    private ArticleRepository articleRepository;

    @Resource
    private ProductRepository productRepository;

    @Override
    public List<Article> stockUpArticles(List<Article> putAwayArticles){
        return articleRepository.saveAll(putAwayArticles);
    }

    @Override
    public List<Article> updateStockOfArticles(List<Article> updateStockArticles) throws InventoryProcessException{
        List<Article> updateStockOfArticles = new ArrayList<>();
        updateStockArticles.stream().forEach(a->{
            Article updateArticle=articleRepository.findByIdentificationNumber(a.getIdentificationNumber());
            if (updateArticle==null){
                // Article not found and would be skipped to move to others.
                logger.warn("Article not found and will not be updated : -"+ a.getIdentificationNumber());
            }else{
                updateArticle.beanMapper(a);
                updateStockOfArticles.add(updateArticle);
            }
        });
        if (updateStockOfArticles.size()>0){
            return stockUpArticles(updateStockOfArticles);
        }else{
            throw new InventoryProcessException("No Article available to update as validations failed");
        }

    }

    @Override
    public List<Map<String,Object>> fetchArticlesAndInventoryLevel(){
        List<Map<String,Object>> inventoryLevelArticle= new ArrayList<>();
        Map<String,Integer> articleList= findAllArticles().stream()
                                                .collect(Collectors.toMap(e->e.getArticleName(), e->e.getAvailableStock()));
        articleList.forEach((k,v)->{
            Map<String,Object> articleId= new HashMap<>();
            articleId.put("articleID",k);
            articleId.put("stockAvailable",v);
            inventoryLevelArticle.add(articleId);
        });
        return inventoryLevelArticle;
    }

    @Override
    public List<Article> findAllArticles(){
        return articleRepository.findAll();
    }

    @Override
    public List<Article> findArticleByIdentificationNumber(List<Integer> identificationNumbers){
        return articleRepository.findByIdentificationNumberIn(identificationNumbers);
    }

    @Override
    public List<Product> buildProducts(List<Product> productList) throws InventoryProcessException {
        //Before the product is saved the articleId is checked if its a valid Article in the inventory, else the Product is skipped.
        // Above validation can also be skipped from here and can be validated at the purchase API service.
        productList=productList.stream()
                                .filter(a->{
                                            int totalArticlesRequired=a.getContainingArticles().size();
                                            int articlesAvailable = findArticleByIdentificationNumber(a.getContainingArticles().stream()
                                                                                                                               .map(b->b.getArticleId())
                                                                                                                               .collect(Collectors.toList()))
                                                                                                                               .size();
                                            // Idea is that count of articles required by Product and Available in Inventory should match.
                                            if (totalArticlesRequired!=articlesAvailable){
                                                logger.debug("Product is being skipped - "+a.getProductName());
                                            }
                                            return totalArticlesRequired==articlesAvailable;
        }).collect(Collectors.toList());
        if (productList.size()>0){
            return productRepository.saveAll(productList);
        }else{
            throw new InventoryProcessException("No product available to store as validations failed. Artciles not found in the inventory");
        }

    }
    /*  Assignment reference -
      * Get all products and quantity of each that is an available with the current inventory. Ideally this should be grouped by productid (unique identifier)
    */
    @Override
    public Map<String,Integer> fetchProductsAndInventoryLevel(){
        return findAllProducts().stream()
                                .collect(Collectors.groupingBy(p->p.getProductName()))
                                .entrySet().stream()
                                           .collect(Collectors.toMap(e->e.getKey(),e->e.getValue().size()));
    }

    @Override
    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    /*  Assignment reference -
      * Remove(Sell) a product and update the inventory accordingly
    */
    @Override
    public String productPurchaseAndInventoryUpdate(String productId) throws InventoryProcessException {
        //Identifier being used is @Document _id.
        Optional<Product> soldProduct= productRepository.findById(productId);
        if (!soldProduct.isPresent()){
            throw new InventoryProcessException("No product found - : "+productId);
        }
        //Stores the Map of Article identifier and its required stock for Product sale.
        Map<Integer,Integer> requiredProductInventory= soldProduct.get().getContainingArticles().stream()
                                                                                        .collect(Collectors.toMap(e->e.getArticleId(),e->e.getRequiredNumber()));
        List<Article> currentInventoryArticle=findArticleByIdentificationNumber(requiredProductInventory.entrySet().stream()
                                                                                                              .map(e->e.getKey())
                                                                                                              .collect(Collectors.toList()));
        //If required Articles for Product is more than available in the Inventory throw an error.
        Optional<Article> insufficientInventory= currentInventoryArticle.stream()
                .filter(availableArticle-> (requiredProductInventory.get(availableArticle.getIdentificationNumber()) > availableArticle.getAvailableStock()))
                .findAny();
        if (insufficientInventory.isPresent()){
            throw new InventoryProcessException("Insufficient inventory for product sale. Missing ArticleID - : "+insufficientInventory.get().getIdentificationNumber());
        }
        //Update the inventory level for the Article.
        currentInventoryArticle.stream().forEach(availableArticle->{
            availableArticle.setAvailableStock(availableArticle.getAvailableStock()-requiredProductInventory.get(availableArticle.getIdentificationNumber()));
        });
        //update inventory
        articleRepository.saveAll(currentInventoryArticle);
        //remove product.
        productRepository.delete(soldProduct.get());
        return "Sale approved for Product -: "+soldProduct.get().getProductName();
    }
}
