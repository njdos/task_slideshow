package com.novisign.slideshow.task.slideshow.database.query;

public enum ImageSearchEngineQuery {

    CREATE_IMAGE_SEARCH("""
            INSERT INTO image_search_engine (value, type, image_id)
            VALUES (:value, :type, :imageId)
            RETURNING id
            """
    ),
    GET_IMAGE_SEARCH_BY_IMAGE_ID(
            "SELECT id FROM image_search_engine WHERE image_id = :image_id"
    ),
    DELETE_IMAGE_SEARCH_BY_ID(
            "DELETE FROM image_search_engine WHERE id = :id"
    );

    private final String query;

    ImageSearchEngineQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
