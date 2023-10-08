package dev.naman.productservice.dtos;

import dev.naman.productservice.models.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericProductDto {
    private UUID id;
    private String title;
    private String description;
    private String image;
    private String category;
    private double price;
}
