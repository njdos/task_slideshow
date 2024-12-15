CREATE TABLE image
(
    id         BIGSERIAL PRIMARY KEY,
    url        VARCHAR(255) NOT NULL,
    duration   INT,
    type       VARCHAR(50),
    added_time TIMESTAMP    NOT NULL
);

CREATE TABLE image_search_engine
(
    id       BIGSERIAL PRIMARY KEY,
    value    VARCHAR(255) NOT NULL,
    type     VARCHAR(50),
    image_id BIGINT       NOT NULL,
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image (id)
);

CREATE TABLE slideshow
(
    id           BIGSERIAL PRIMARY KEY,
    created_time TIMESTAMP NOT NULL
);

CREATE TABLE slideshow_image
(
    id           BIGSERIAL PRIMARY KEY,
    slideshow_id BIGINT NOT NULL,
    image_id     BIGINT NOT NULL,
    duration     INT,
    CONSTRAINT fk_slideshow_id FOREIGN KEY (slideshow_id) REFERENCES slideshow (id),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image (id)
);

CREATE TABLE proof_of_play
(
    id           BIGSERIAL PRIMARY KEY,
    slideshow_id BIGINT    NOT NULL,
    image_id     BIGINT    NOT NULL,
    play_time    TIMESTAMP NOT NULL,
    CONSTRAINT fk_slideshow_id FOREIGN KEY (slideshow_id) REFERENCES slideshow (id),
    CONSTRAINT fk_image_id FOREIGN KEY (image_id) REFERENCES image (id)
);
