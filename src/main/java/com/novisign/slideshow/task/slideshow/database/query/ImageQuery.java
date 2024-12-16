package com.novisign.slideshow.task.slideshow.database.query;

public enum ImageQuery {

    GET_IMAGE_BY_URL(
            "SELECT * FROM image WHERE url = :url"
    ),
    CREATE_IMAGE("""
            INSERT INTO image (url, duration, type, added_time)
            VALUES (:url, :duration, :type, :addedTime)
            RETURNING id
            """
    ),
    DELETE_IMAGE_BY_ID(
            "DELETE FROM image WHERE id = :id"
    );

    private final String query;

    ImageQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

}
