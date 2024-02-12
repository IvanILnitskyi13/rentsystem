CREATE TABLE IF NOT EXISTS customer (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS rental_object (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    landlord_id BIGINT NOT NULL,
    unit_price  DECIMAL(19, 4) NOT NULL,
    area        DECIMAL(8, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    FOREIGN KEY (landlord_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS reservation (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    rental_object_id   BIGINT NOT NULL,
    tenant_id          BIGINT NOT NULL,
    start_date         DATE NOT NULL,
    end_date           DATE NOT NULL,
    cost               DECIMAL(19, 4) NOT NULL,
    FOREIGN KEY (rental_object_id) REFERENCES rental_object(id),
    FOREIGN KEY (tenant_id) REFERENCES customer(id)
);