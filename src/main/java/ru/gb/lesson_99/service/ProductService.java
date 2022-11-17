package ru.gb.lesson_99.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.lesson_99.dao.CartDao;
import ru.gb.lesson_99.dao.ProductDao;
import ru.gb.lesson_99.entity.Cart;
import ru.gb.lesson_99.entity.Product;
import ru.gb.lesson_99.entity.enums.Status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductDao productDao;
    private final CartDao cartDao;

    @Transactional(propagation = Propagation.NEVER, isolation = Isolation.DEFAULT)
    public long count() {
        System.out.println(productDao.count());
        return productDao.count();
    }

    public Product save(Product product) {
        if (product.getId() != null) {
            Optional<Product> productFromDBOptional = productDao.findById(product.getId());
            if (productFromDBOptional.isPresent()) {
                Product productFromDB = productFromDBOptional.get();
                productFromDB.setTitle(product.getTitle());
                productFromDB.setAmt(product.getAmt());
                productFromDB.setCost(product.getCost());
                productFromDB.setManufactureDate(product.getManufactureDate());
                productFromDB.setStatus(product.getStatus());
                return productDao.save(productFromDB);
            }
        }
        return productDao.save(product);
    }

    public void cartSave(String flag, Long id){

        Cart addCart = new Cart();
        
        Optional<Product> product = productDao.findById(id);
        Product productC = product.get();
        if (flag.equals("save")){
            Optional<Cart> cart = cartDao.findByTitle(productC.getTitle());
            if (cart.isEmpty()){
                addCart.setId(productC.getId());
                addCart.setTitle(productC.getTitle());
                addCart.setPrice(productC.getCost());
                addCart.setStatus(productC.getStatus());
                addCart.setAmt(1);
                addCart.setCost(addCart.getPrice());

            }else {
                addCart = cart.get();
                addCart.setAmt(addCart.getAmt() + 1);
                BigDecimal price = addCart.getPrice();
                BigDecimal amt = new BigDecimal(addCart.getAmt());
                addCart.setCost(price.multiply(amt));

        }
            productC.setAmt(productC.getAmt() - 1);
        }else if (flag.equals("delete")){
            Optional<Cart> cart = cartDao.findById(id);
            addCart = cart.get();
            addCart.setAmt(addCart.getAmt() - 1);
            BigDecimal price = addCart.getPrice();
            BigDecimal amt = new BigDecimal(addCart.getAmt());
            addCart.setCost(price.multiply(amt));
        }
        productDao.save(productC);
        cartDao.save(addCart);
    }


    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    public List<Product> findAll() {
        return productDao.findAll(Sort.by("title"));
    }

    public List<Cart> findCartAll() {
        return cartDao.findAll();
    }

    public List<Product> findAllActive() {
        return productDao.findAllByStatus(Status.ACTIVE);
    }

    public void deleteById(Long id) {
        try {
            productDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void deleteByIdCart(Long id) {
        try {
            Optional<Cart> cartDelete = cartDao.findById(id);
            Cart cart = cartDelete.get();
            if (cart.getAmt() < 2){
            cartDao.deleteById(id);
            }else {
                String flag = "delete";
                cartSave(flag, id);
            }
            Optional<Product> product = productDao.findByTitle(cart.getTitle());
            Product productC = product.get();
            productC.setAmt(productC.getAmt() + 1);
            productDao.save(productC);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }

    public void disable(Long id) {
        Optional<Product> product = productDao.findById(id);
        product.ifPresent(p -> {
            p.setStatus(Status.DISABLED);
            productDao.save(p);
        });
    }

    public List<Product> findAll(int page, int size) {
        return productDao.findAll(PageRequest.of(page, size)).getContent();
    }

//    public List<Product> findAll(int page, int size) {
//        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size));
//    }

    public List<Product> findAllSortedById() {
        return productDao.findAllByStatus(Status.ACTIVE, Sort.by("id"));
    }

    public List<Product> findAllSortedById(int page, int size) {
        return productDao.findAllByStatus(Status.ACTIVE, PageRequest.of(page, size, Sort.by("id")));
    }


}
