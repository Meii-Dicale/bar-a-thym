package com.barathym.dtos.cocktailapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CocktailApiItemDTO {

    @JsonProperty("idDrink")        private String idDrink;
    @JsonProperty("strDrink")       private String strDrink;
    @JsonProperty("strCategory")    private String strCategory;
    @JsonProperty("strDrinkThumb")  private String strDrinkThumb;
    @JsonProperty("strInstructionsFR") private String strInstructionsFR;
    @JsonProperty("strAlcoholic")   private String strAlcoholic;

    @JsonProperty("strIngredient1")  private String strIngredient1;
    @JsonProperty("strIngredient2")  private String strIngredient2;
    @JsonProperty("strIngredient3")  private String strIngredient3;
    @JsonProperty("strIngredient4")  private String strIngredient4;
    @JsonProperty("strIngredient5")  private String strIngredient5;
    @JsonProperty("strIngredient6")  private String strIngredient6;
    @JsonProperty("strIngredient7")  private String strIngredient7;
    @JsonProperty("strIngredient8")  private String strIngredient8;
    @JsonProperty("strIngredient9")  private String strIngredient9;
    @JsonProperty("strIngredient10") private String strIngredient10;
    @JsonProperty("strIngredient11") private String strIngredient11;
    @JsonProperty("strIngredient12") private String strIngredient12;
    @JsonProperty("strIngredient13") private String strIngredient13;
    @JsonProperty("strIngredient14") private String strIngredient14;
    @JsonProperty("strIngredient15") private String strIngredient15;

    @JsonProperty("strMeasure1")  private String strMeasure1;
    @JsonProperty("strMeasure2")  private String strMeasure2;
    @JsonProperty("strMeasure3")  private String strMeasure3;
    @JsonProperty("strMeasure4")  private String strMeasure4;
    @JsonProperty("strMeasure5")  private String strMeasure5;
    @JsonProperty("strMeasure6")  private String strMeasure6;
    @JsonProperty("strMeasure7")  private String strMeasure7;
    @JsonProperty("strMeasure8")  private String strMeasure8;
    @JsonProperty("strMeasure9")  private String strMeasure9;
    @JsonProperty("strMeasure10") private String strMeasure10;
    @JsonProperty("strMeasure11") private String strMeasure11;
    @JsonProperty("strMeasure12") private String strMeasure12;
    @JsonProperty("strMeasure13") private String strMeasure13;
    @JsonProperty("strMeasure14") private String strMeasure14;
    @JsonProperty("strMeasure15") private String strMeasure15;

    public List<IngredientMesure> getIngredientsMesures() {
        List<String> noms = List.of(
            nvl(strIngredient1),  nvl(strIngredient2),  nvl(strIngredient3),
            nvl(strIngredient4),  nvl(strIngredient5),  nvl(strIngredient6),
            nvl(strIngredient7),  nvl(strIngredient8),  nvl(strIngredient9),
            nvl(strIngredient10), nvl(strIngredient11), nvl(strIngredient12),
            nvl(strIngredient13), nvl(strIngredient14), nvl(strIngredient15)
        );
        List<String> mesures = List.of(
            nvl(strMeasure1),  nvl(strMeasure2),  nvl(strMeasure3),
            nvl(strMeasure4),  nvl(strMeasure5),  nvl(strMeasure6),
            nvl(strMeasure7),  nvl(strMeasure8),  nvl(strMeasure9),
            nvl(strMeasure10), nvl(strMeasure11), nvl(strMeasure12),
            nvl(strMeasure13), nvl(strMeasure14), nvl(strMeasure15)
        );

        List<IngredientMesure> result = new ArrayList<>();
        for (int i = 0; i < noms.size(); i++) {
            if (!noms.get(i).isBlank()) {
                result.add(new IngredientMesure(noms.get(i).trim(), mesures.get(i).trim()));
            }
        }
        return result;
    }

    private String nvl(String s) {
        return s != null ? s : "";
    }

    public record IngredientMesure(String nom, String mesure) {}
}
