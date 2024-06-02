package back.spoonacular;

import back.pojo.Intolerance;
import back.services.EnvironmentVariables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ApiRequest {

  private final String apiKey;

  public ApiRequest() {
    this.apiKey = EnvironmentVariables.API_KEY;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String request(String urlRequest) {
    try {
      URL url = new URL(urlRequest);
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

      try {
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
          try (BufferedReader reader =
              new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
              response.append(line);
            }
            return response.toString();
          }
        } else {
          System.err.println("Error: " + responseCode);
        }
      } finally {
        httpURLConnection.disconnect();
      }
    } catch (IOException e) {
      throw new RuntimeException("Error making API request", e);
    }
    return null;
  }

  // Helper method to build the request URL for recipes by ingredients
  public String buildRecipesByIngredientsUrl(List<String> ingredients) {
    StringBuilder params = new StringBuilder();
    for (String ingredient : ingredients) {
      params.append(ingredient).append(",");
    }

    return "https://api.spoonacular.com/recipes/findByIngredients/?apiKey="
        + apiKey
        + "&ingredients="
        + params.toString();
  }

  public String buildNutritionDetailsUrl(int recipeId) {
    return "https://api.spoonacular.com/recipes/"
        + recipeId
        + "/nutritionWidget.json?apiKey="
        + apiKey;
  }

  public String complexSearch(List<String> ingredients, List<Intolerance> intolerances) {
    StringBuilder ingredientParams = new StringBuilder();
    StringBuilder intoleranceParams = new StringBuilder();
    for (String ingredient : ingredients) {
      ingredientParams.append(ingredient).append(",");
    }
    for (Intolerance intolerance : intolerances) {
      intoleranceParams.append(intolerance.getName()).append(",");
    }
    return "https://api.spoonacular.com/recipes/complexSearch/?apiKey="
        + apiKey
        + "&includeIngredients="
        + ingredientParams
        + "&intolerances="
        + intoleranceParams
        + "&addRecipeInformation=true"
        + "&fillIngredients=true";
  }
}
