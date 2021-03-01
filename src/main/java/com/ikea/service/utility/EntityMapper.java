package com.ikea.service.utility;

import com.ikea.constants.Entity;
import com.ikea.exception.InventoryProcessException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nandk
 * Entity mapper used to convert file into respective entities.
 */
public interface EntityMapper {

    Object fileToEntityMapper(MultipartFile multipartFile,Entity entity) throws InventoryProcessException;
}
