package com.ikea.service.utility;

import com.ikea.constants.Entity;
import com.ikea.exception.InventoryProcessException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nandk on 26/02/2021.
 */
public interface EntityMapper {

    Object fileToEntityMapper(MultipartFile multipartFile,Entity entity) throws InventoryProcessException;
}
