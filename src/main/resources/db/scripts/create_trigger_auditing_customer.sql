CREATE TRIGGER auditing_customer
    AFTER INSERT OR UPDATE OR DELETE ON customer
    FOR EACH ROW
    EXECUTE FUNCTION auditlog_customer();