CREATE TRIGGER auditing_product
    AFTER INSERT OR UPDATE OR DELETE ON product
    FOR EACH ROW
    EXECUTE FUNCTION auditlog_product();