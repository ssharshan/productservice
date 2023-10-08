package dev.naman.productservice.services;

import dev.naman.productservice.dtos.GenericProductDto;
import dev.naman.productservice.exceptions.NotFoundException;
import dev.naman.productservice.models.Category;
import dev.naman.productservice.models.Price;
import dev.naman.productservice.models.Product;
import dev.naman.productservice.repositories.ProductRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Primary
@Service("selfProductServiceImpl")
public class SelfProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private CategoryService categoryService;

    public SelfProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public GenericProductDto getProductById(UUID id) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return toGenericProductDto(product);
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto product) {
        Product productToSave = toProduct(product);
        productToSave = productRepository.save(productToSave);
        return toGenericProductDto(productToSave);
    }

    @Override
    public List<GenericProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<GenericProductDto> genericProductDtos = products.stream().map(this::toGenericProductDto).toList();
        return genericProductDtos;
    }

    @Override
    public GenericProductDto deleteProduct(UUID id) throws NotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
        return toGenericProductDto(product);
    }

    @Override
    public GenericProductDto updateProduct(UUID id, GenericProductDto product) throws NotFoundException {

        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        productToUpdate.setTitle(product.getTitle());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.getPrice().setPrice(product.getPrice());
        productToUpdate.setImage(product.getImage());

        Category category = categoryService.getOrCreateCategory(product.getCategory());
        productToUpdate.setCategory(category);

        productToUpdate = productRepository.save(productToUpdate);

        return toGenericProductDto(productToUpdate);
    }


    public GenericProductDto toGenericProductDto(Product product) {
        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(product.getUuid());
        genericProductDto.setTitle(product.getTitle());
        genericProductDto.setDescription(product.getDescription());
        genericProductDto.setPrice(product.getPrice().getPrice());
        genericProductDto.setCategory(product.getCategory().getName());
        genericProductDto.setImage(product.getImage());
        return genericProductDto;
    }

    public Product toProduct(GenericProductDto genericProductDto) {
        Product product = new Product();
        product.setUuid(genericProductDto.getId());
        product.setTitle(genericProductDto.getTitle());
        product.setDescription(genericProductDto.getDescription());
        Price price = new Price("rupee", genericProductDto.getPrice());
        product.setPrice(price);
        product.setImage(genericProductDto.getImage());
        Category category = categoryService.getOrCreateCategory(genericProductDto.getCategory());
        product.setCategory(category);
        return product;
    }


}
