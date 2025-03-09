package com.project.backend.services;

import com.project.backend.entities.Estoque;
import com.project.backend.entities.Produto;
import com.project.backend.repositories.EstoqueRepository;
import com.project.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProductRepository productRepository;


    public Produto addProduto(Produto produto) {
        Produto newProduct = productRepository.save(produto);

        Estoque estoque = new Estoque();
        estoque.setProduto(produto);
        estoque.setQuantidade(0);
        estoque.setTipoMovimento("entrada");
        estoque.setDataMovimento(java.time.LocalDate.now());

        estoqueRepository.save(estoque);
        return newProduct;
    }


    public Produto registerProduct(Produto produto) {
        return productRepository.save(produto);
    }


    public List<Produto> getAllProducts() {
        return productRepository.findAll();
    }


    public Optional<Produto> getProductById(long id) {
        return productRepository.findById(id);
    }


    public Produto updateProduct(Produto produto) {
        return productRepository.save(produto);
    }


    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
}
