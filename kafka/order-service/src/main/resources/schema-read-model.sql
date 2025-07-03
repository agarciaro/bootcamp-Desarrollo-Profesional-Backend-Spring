-- Schema for order read model (CQRS)
-- This table maintains eventual consistency with the main orders table

CREATE TABLE IF NOT EXISTS order_read_models (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    shipping_address TEXT,
    notes TEXT,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    
    -- Additional fields for optimized queries
    user_username VARCHAR(100),
    user_email VARCHAR(255),
    items_count INTEGER DEFAULT 0,
    
    -- Indexes to optimize queries
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_user_status (user_id, status),
    INDEX idx_is_deleted (is_deleted),
    INDEX idx_user_username (user_username),
    INDEX idx_total_amount (total_amount)
);

-- Table comments
COMMENT ON TABLE order_read_models IS 'Optimized read model for order queries (CQRS)';
COMMENT ON COLUMN order_read_models.id IS 'Unique order ID';
COMMENT ON COLUMN order_read_models.user_id IS 'ID of the user who placed the order';
COMMENT ON COLUMN order_read_models.total_amount IS 'Total order amount';
COMMENT ON COLUMN order_read_models.status IS 'Current order status';
COMMENT ON COLUMN order_read_models.created_at IS 'Order creation date and time';
COMMENT ON COLUMN order_read_models.updated_at IS 'Last update date and time';
COMMENT ON COLUMN order_read_models.shipping_address IS 'Shipping address';
COMMENT ON COLUMN order_read_models.notes IS 'Additional order notes';
COMMENT ON COLUMN order_read_models.is_deleted IS 'Indicates if the order is marked as deleted';
COMMENT ON COLUMN order_read_models.user_username IS 'Username (denormalized for queries)';
COMMENT ON COLUMN order_read_models.user_email IS 'User email (denormalized for queries)';
COMMENT ON COLUMN order_read_models.items_count IS 'Number of items in the order (denormalized for queries)'; 