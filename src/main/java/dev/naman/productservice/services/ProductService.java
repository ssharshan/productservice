package dev.naman.productservice.services;

import dev.naman.productservice.dtos.GenericProductDto;

public interface ProductService {

    GenericProductDto createProduct(GenericProductDto product);

    GenericProductDto getProductById(Long id);

    GenericProductDto[] getProducts();

    GenericProductDto updateProductById(Long id,GenericProductDto product);

    GenericProductDto deleteProductById(Long id);
}
