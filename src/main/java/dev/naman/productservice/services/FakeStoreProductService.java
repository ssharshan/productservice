package dev.naman.productservice.services;

import dev.naman.productservice.dtos.FakeStoreProductDto;
import dev.naman.productservice.dtos.GenericProductDto;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    private RestTemplateBuilder restTemplateBuilder;
    private String getProductRequestUrl = "https://fakestoreapi.com/products/{id}";
    private String createProductRequestUrl = "https://fakestoreapi.com/products";

    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto product) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<GenericProductDto> response = restTemplate.postForEntity(
                createProductRequestUrl, product, GenericProductDto.class
        );

        return response.getBody();
    }

    @Override
    public GenericProductDto getProductById(Long id) {
//        FakeStoreProductService fakeStoreProductService = new FakeStoreProductService();
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response =
                restTemplate.getForEntity(getProductRequestUrl, FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        return toGenericProductDto(fakeStoreProductDto);
//        GenericProductDto product = new GenericProductDto();
//        product.setImage(fakeStoreProductDto.getImage());
//        product.setDescription(fakeStoreProductDto.getDescription());
//        product.setTitle(fakeStoreProductDto.getTitle());
//        product.setPrice(fakeStoreProductDto.getPrice());
//        product.setCategory(fakeStoreProductDto.getCategory());
////        response.getStatusCode()
//
//        return product;
//        return null;
    }

    @Override
    public GenericProductDto[] getProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto[]> productResponseEntity = restTemplate.getForEntity(createProductRequestUrl,FakeStoreProductDto[].class);
        FakeStoreProductDto[] products = productResponseEntity.getBody();
        GenericProductDto[] productDtos = new GenericProductDto[products.length];

        for(int i=0;i<products.length;i++){
            productDtos[i] = toGenericProductDto(products[i]);
        }

        return productDtos;
    }

    @Override
    public GenericProductDto updateProductById(Long id, GenericProductDto product) {

        RestTemplate restTemplate = restTemplateBuilder.build();
        FakeStoreProductDto fakeStoreProductDto = toFakeStoreProductDto(product);
        HttpEntity<FakeStoreProductDto> fakeStoreProductDtoHttpEntity = new HttpEntity<>(fakeStoreProductDto);
        ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(getProductRequestUrl, HttpMethod.PUT, fakeStoreProductDtoHttpEntity, FakeStoreProductDto.class, id);
        return toGenericProductDto(response.getBody());

    }

    @Override
    public GenericProductDto deleteProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(getProductRequestUrl, HttpMethod.DELETE, null, FakeStoreProductDto.class, id);

        return toGenericProductDto(response.getBody());
    }

    public GenericProductDto toGenericProductDto(FakeStoreProductDto fakeStoreProductDto){
        GenericProductDto product = new GenericProductDto();
        product.setId(fakeStoreProductDto.getId());
        product.setImage(fakeStoreProductDto.getImage());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setTitle(fakeStoreProductDto.getTitle());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setCategory(fakeStoreProductDto.getCategory());
        return product;
    }

    public FakeStoreProductDto toFakeStoreProductDto(GenericProductDto genericProductDto){
        FakeStoreProductDto product = new FakeStoreProductDto();
        product.setId(genericProductDto.getId());
        product.setImage(genericProductDto.getImage());
        product.setDescription(genericProductDto.getDescription());
        product.setTitle(genericProductDto.getTitle());
        product.setPrice(genericProductDto.getPrice());
        product.setCategory(genericProductDto.getCategory());
        return product;
    }
}
