package com.project.shopapp.Repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);//kiểm tra xem có tồn tại sản phẩm bằng name
    Page<Product> findAll(Pageable pageable);// phân trang
}
