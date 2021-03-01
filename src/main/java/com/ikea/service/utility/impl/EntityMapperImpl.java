package com.ikea.service.utility.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikea.constants.Entity;
import com.ikea.exception.InventoryProcessException;
import com.ikea.model.entity.BuildProductsReq;
import com.ikea.model.entity.InventoryReq;
import com.ikea.service.utility.EntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author nandk
 */
@Component
public class EntityMapperImpl implements EntityMapper {

    /**
     * The Mapper function that converts json file into respective entity (Article/Product) object.
     * @param multipartFile
     * @param entity
     * @return
     * @throws InventoryProcessException
     */
    @Override
    public Object fileToEntityMapper(MultipartFile multipartFile, Entity entity) throws InventoryProcessException {
        if (multipartFile.isEmpty()){
            throw new InventoryProcessException("Requested file cannot be empty");
        }
        ObjectMapper objectMapper= new ObjectMapper();
        try{
            switch (entity){
                case ARTICLE:
                    return objectMapper.readValue(multipartFile.getInputStream(), InventoryReq.class);
                case PRODUCT:
                    return objectMapper.readValue(multipartFile.getInputStream(), BuildProductsReq.class);
                default:
                    throw new InventoryProcessException("Entity type not recognised");
            }
        }catch (IOException ioException){
            throw new InventoryProcessException(ioException.getMessage());
        }
    }
}
