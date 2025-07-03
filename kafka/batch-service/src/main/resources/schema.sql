-- Inventory Item Entity
CREATE TABLE IF NOT EXISTS inventory_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) NOT NULL UNIQUE,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit_price DECIMAL(10,2) NOT NULL,
    supplier VARCHAR(255),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Batch Job Execution History
CREATE TABLE IF NOT EXISTS batch_job_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_name VARCHAR(100) NOT NULL,
    job_instance_id BIGINT,
    execution_id BIGINT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    exit_code VARCHAR(20),
    exit_message TEXT,
    records_processed INTEGER DEFAULT 0,
    records_skipped INTEGER DEFAULT 0,
    records_failed INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Batch Processing Errors
CREATE TABLE IF NOT EXISTS batch_processing_errors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_execution_id BIGINT,
    step_name VARCHAR(100),
    line_number INTEGER,
    raw_data TEXT,
    error_message TEXT,
    error_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inventory Alerts
CREATE TABLE IF NOT EXISTS inventory_alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) NOT NULL,
    alert_type VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,
    severity VARCHAR(20) DEFAULT 'MEDIUM',
    is_resolved BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP
);

-- Batch Job Parameters
CREATE TABLE IF NOT EXISTS batch_job_parameters (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_name VARCHAR(100) NOT NULL,
    parameter_name VARCHAR(100) NOT NULL,
    parameter_value TEXT,
    parameter_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_job_param (job_name, parameter_name)
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_inventory_product_code ON inventory_item(product_code);
CREATE INDEX IF NOT EXISTS idx_inventory_category ON inventory_item(category);
CREATE INDEX IF NOT EXISTS idx_inventory_status ON inventory_item(status);
CREATE INDEX IF NOT EXISTS idx_batch_job_history_job_name ON batch_job_history(job_name);
CREATE INDEX IF NOT EXISTS idx_batch_job_history_status ON batch_job_history(status);
CREATE INDEX IF NOT EXISTS idx_batch_errors_job_execution ON batch_processing_errors(job_execution_id);
CREATE INDEX IF NOT EXISTS idx_inventory_alerts_product_code ON inventory_alerts(product_code);
CREATE INDEX IF NOT EXISTS idx_inventory_alerts_resolved ON inventory_alerts(is_resolved); 