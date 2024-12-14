package com.novisign.slideshow.task.slideshow.database.query;

public enum ImageSearchEngineQuery {

    CREATE_IMAGE_SEARCH("""
            INSERT INTO image_search_engine (value, type, image_id)
            VALUES (:value, :type, :imageId)
            RETURNING id
            """
    );

    private final String query;

    ImageSearchEngineQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
