package com.ikea.service.repository.impl;

import com.ikea.exception.InventoryProcessException;
import com.ikea.model.entity.Article;
import com.ikea.model.entity.Product;
import com.ikea.service.repository.ArticleRepository;
import com.ikea.service.repository.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author nandk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = InventoryRepositoryServiceImplTest.class )
public class InventoryRepositoryServiceImplTest {

    @InjectMocks
    private InventoryRepositoryServiceImpl inventoryRepositoryService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ProductRepository productRepository;

    private Product product1;

    private Product.ContainsArticle containsArticle1;

    private Article article1;

    @Before
    public void initialSetUp(){
        MockitoAnnotations.initMocks(this);
        product1=new Product("p1",1.0,null);
        containsArticle1= new Product.ContainsArticle(1,1);
        product1.setContainingArticles(Arrays.asList(containsArticle1));
        article1=new Article(1,"article1",10);
    }

    /**
     * Check for articles update happy flow.
     */
    @Test
    public void updateStockOfArticles_Success(){
        Mockito.when(articleRepository.findByIdentificationNumber(Mockito.anyInt())).thenReturn(article1);
        Mockito.when(articleRepository.saveAll(Mockito.anyCollection())).thenReturn(Arrays.asList(article1));
        Article updateNew= new Article(1,null,1);// Increase stock by 1.
        try{
            List<Article> updatedArticle=inventoryRepositoryService.updateStockOfArticles(Arrays.asList(updateNew));
            Assert.assertTrue(updatedArticle.stream().findAny().get().getAvailableStock()==11);//Stock rises by 1
        }catch (InventoryProcessException ie){
            Assert.fail(ie.getMessage());
        }
    }

    /**
     * Article update for an article not available in the inventory.
     */
    @Test
    public void updateStockOfArticles_ArticleNotFound(){
        Mockito.when(articleRepository.findByIdentificationNumber(Mockito.anyInt())).thenReturn(null);
        Article updateNew= new Article(11,null,1);// Increase stock by 1.
        try{
            inventoryRepositoryService.updateStockOfArticles(Arrays.asList(updateNew));
        }catch (InventoryProcessException ie){
            Assert.assertTrue(true);
        }
    }

    /**
     * Saves product with all validations passed.
     */
    @Test
    public void buildProducts_Success(){
        Mockito.when(articleRepository.findByIdentificationNumberIn(Mockito.anyList())).thenReturn(Arrays.asList(article1));
        try{
            inventoryRepositoryService.buildProducts(Arrays.asList(product1));
            Mockito.verify(productRepository,Mockito.times(1)).saveAll(Mockito.anyCollection());
        }catch (InventoryProcessException ie){
            Assert.fail(ie.getMessage());
        }
    }

    /**
     * Build a Product that doesnt have a corresponding article.
     */
    @Test
    public void buildProducts_NoArticleAvailable(){
        Mockito.when(articleRepository.findByIdentificationNumberIn(Mockito.anyList())).thenReturn(new ArrayList());//Empty arraylist
        try{
            inventoryRepositoryService.buildProducts(Arrays.asList(product1));
        }catch (InventoryProcessException ie){
            Assert.assertTrue(true);
        }
    }

    /**
     * Product purchase is successfule
     */
    @Test
    public void productPurchaseAndInventoryUpdate_Success(){
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product1));
        Mockito.when(articleRepository.findByIdentificationNumberIn(Mockito.anyList())).thenReturn(Arrays.asList(article1));
        try{
            inventoryRepositoryService.productPurchaseAndInventoryUpdate("123");
            Mockito.verify(articleRepository,Mockito.times(1)).saveAll(Mockito.anyCollection());
        }catch (InventoryProcessException ie){
            Assert.fail(ie.getMessage());
        }
    }

    /**
     * Product purchase fails since there are no Articles available required by the Product.
     */
    @Test
    public void productPurchaseAndInventoryUpdate_NotEnoughStock(){
        Mockito.when(productRepository.findById(Mockito.anyString())).thenReturn(Optional.of(product1));
        article1.setAvailableStock(0);//No Stock
        Mockito.when(articleRepository.findByIdentificationNumberIn(Mockito.anyList())).thenReturn(Arrays.asList(article1));
        try{
            inventoryRepositoryService.productPurchaseAndInventoryUpdate("123");
            Mockito.verify(articleRepository,Mockito.times(1)).saveAll(Mockito.anyCollection());
        }catch (InventoryProcessException ie){
            Assert.assertTrue(true);
        }
    }
}
