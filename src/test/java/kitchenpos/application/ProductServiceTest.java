package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 떡볶이;
    private Product 튀김;
    private Product 순대;

    @BeforeEach
    void setUp() {
        떡볶이 = new Product(1L, "떡볶이", BigDecimal.valueOf(4500));
        튀김 = new Product(2L, "튀김", BigDecimal.valueOf(2500));
        순대 = new Product(3L, "순대", BigDecimal.valueOf(4000));
    }


    @DisplayName("상품 생성")
    @Test
    void 상품_생성() {
        when(productDao.save(떡볶이)).thenReturn(떡볶이);

        Product 생성된_상품 = productService.create(떡볶이);

        assertAll(
                () -> assertThat(생성된_상품.getId()).isEqualTo(1L),
                () -> assertThat(생성된_상품.getName()).isEqualTo("떡볶이"),
                () -> assertThat(생성된_상품.getPrice()).isEqualTo(BigDecimal.valueOf(4500))
        );
    }

    @DisplayName("가격이 0원 보다 작은 상품 생성 요청 시 예외처리")
    @Test
    void 올바르지_않은_가격_예외처리() {
        Product 잘못된_가격_상품 = new Product(2L, "잘못된_가격_상품", BigDecimal.valueOf(-2500));

        assertThatThrownBy(() -> productService.create(잘못된_가격_상품)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 이름으로 상품 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_예외처리() {
        Product 비어있는_이름_상품 = new Product(2L, "", BigDecimal.valueOf(4000));

        assertThatThrownBy(() -> productService.create(비어있는_이름_상품)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 조회")
    @Test
    void 상품_조회() {
        when(productDao.findAll()).thenReturn(Arrays.asList(떡볶이, 튀김, 순대));
        List<Product> 조회된_상품_목록 = productService.list();

        assertThat(조회된_상품_목록).containsAll(Arrays.asList(떡볶이, 튀김, 순대));
    }
}