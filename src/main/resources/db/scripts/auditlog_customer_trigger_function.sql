CREATE OR REPLACE FUNCTION auditlog_customer() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO customer_aud(changed_at, operation, customer_id, customer_name, customer_email)
        VALUES (current_timestamp, 'CREATE', NEW.id, NEW.name, NEW.email);
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO customer_aud(changed_at, operation, customer_id, customer_name, customer_email)
        VALUES (current_timestamp, 'UPDATE', NEW.id, NEW.name, NEW.email);
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO customer_aud(changed_at, operation, customer_id, customer_name, customer_email)
        VALUES (current_timestamp, 'DELETE', OLD.id, OLD.name, OLD.email);
    END IF;
RETURN NULL;
END;
$$ LANGUAGE plpgsql;