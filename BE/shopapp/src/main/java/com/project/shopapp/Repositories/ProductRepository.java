package com.project.shopapp.Repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);//kiểm tra xem có tồn tại sản phẩm bằng name
    Page<Product> findAll(Pageable pageable);// phân trang

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id =:categoryId) "+
            "AND (:keyword IS NULL OR :keyword ='' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%)")
//    @Query("select p from Product p where (:categoryId IS NULL OR :categoryId = 0 OR p.category.id =:categoryId)")
    Page<Product> searchProducts(@Param("categoryId") int categoryId,
                               @Param("keyword") String keyword,
                               Pageable pageable);
}
