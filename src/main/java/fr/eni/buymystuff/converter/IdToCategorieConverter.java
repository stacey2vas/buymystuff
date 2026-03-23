package fr.eni.buymystuff.converter;

import fr.eni.buymystuff.bo.Categories;
import fr.eni.buymystuff.services.ArticleService;
import fr.eni.buymystuff.services.ServicesCategories;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class IdToCategorieConverter implements Converter<String, Categories> {

    private final ServicesCategories servicesCategories;

    public IdToCategorieConverter(ServicesCategories servicesCategories) {
        this.servicesCategories = servicesCategories;
    }

    @Override
    public Categories convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            Long categorieId = Long.parseLong(id);
            return servicesCategories.selectById(categorieId).data;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
