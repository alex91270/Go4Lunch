package com.example.go4lunchAlx.di;

import com.example.go4lunchAlx.service.RestApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static RestApiService service = new RestApiService();

    /**
     * Get an instance on @{@link RestApiService}
     * @return
     */
    public static RestApiService getRestApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link RestApiService}. Useful for tests, so we ensure the context is clean.
     * @return
     */
    public static RestApiService getNewInstanceApiService() {
        return new RestApiService();
    }
}
