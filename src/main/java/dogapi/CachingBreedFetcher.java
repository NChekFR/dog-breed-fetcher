package dogapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingBreedFetcher implements BreedFetcher {
    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // If result is cached, just return it — no new API call
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        // Always increment callsMade when the underlying fetcher is called
        callsMade++;
        try {
            List<String> result = fetcher.getSubBreeds(breed);
            // Cache only successful results
            cache.put(breed, result);
            return result;
        } catch (BreedNotFoundException e) {
            // Do not cache failures
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
