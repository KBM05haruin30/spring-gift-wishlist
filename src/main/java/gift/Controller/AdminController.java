package gift.Controller;

import gift.Model.Product;
import gift.Repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

    private final ProductRepository productRepository;

    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findProductsAll());
        return "product_list";
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product(null, "", 0, ""));
        return "add_product_form";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid Product product, Model model) {
        validateKaKaoKeyword(product.name(), model);
        productRepository.saveProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable("id") long id, Model model) {
        Product product = productRepository.findProductsById(id);
        model.addAttribute("product", product);
        return "edit_product_form";
    }

    @PutMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") long id,
        @ModelAttribute @Valid Product updatedProduct,
        Model model) {
        validateKaKaoKeyword(updatedProduct.name(), model);
        productRepository.updateProduct(updatedProduct, id);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productRepository.deleteProduct(id);
        return "redirect:/admin/products";
    }

    private void validateKaKaoKeyword(String name, Model model) {
        if (name.contains("카카오")) {
            model.addAttribute("name", "상품 이름에 '카카오'를 포함하려면 담당 MD와 협의가 필요합니다.");
            throw new IllegalArgumentException("상품 이름에 '카카오'를 포함하려면 담당 MD와 협의가 필요합니다.");
        }
    }
}
