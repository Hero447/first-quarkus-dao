CREATE OR REPLACE FUNCTION auditlog_product() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO product_aud(changed_at, operation, product_id, product_name, product_price)
        VALUES (current_timestamp, 'CREATE', NEW.id, NEW.name, NEW.price);
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO product_aud(changed_at, operation, product_id, product_name, product_price)
        VALUES (current_timestamp, 'UPDATE', NEW.id, NEW.name, NEW.price);
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO product_aud(changed_at, operation, product_id, product_name, product_price)
        VALUES (current_timestamp, 'DELETE', OLD.id, OLD.name, OLD.price);
    END IF;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;