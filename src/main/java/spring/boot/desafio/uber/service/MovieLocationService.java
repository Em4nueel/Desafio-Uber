package spring.boot.desafio.uber.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import spring.boot.desafio.uber.model.MovieLocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MovieLocationService {

    private final WebClient webClient;

    public MovieLocationService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://data.sfgov.org/resource/yitu-d5am.json") // Consumindo base de dados do SFOGOV
                .build();
    }

    public List<MovieLocation> getAllMovies() {
        return webClient.get()
                .retrieve()
                .bodyToFlux(MovieLocation.class)
                .collectList()
                .block(); // Bloqueia até que a lista seja retornada
    }

    public List<MovieLocation> filterByTitle(String query) {
        return getAllMovies().stream()
                .filter(movie -> movie.getTitle() != null && movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList()); // Filtra os filmes pelo título
    }

    public List<String> autocomplete(String prefix) {
        return getAllMovies().stream()
                .map(MovieLocation::getTitle)
                .filter(Objects::nonNull)
                .filter(t -> t.toLowerCase().startsWith(prefix.toLowerCase()))
                .distinct()
                .sorted()
                .limit(10) // Limita a 10 resultados
                .collect(Collectors.toList()); // Coleta os resultados em uma lista
    }
}
