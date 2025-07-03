-- Analytics Service Database Schema
-- Stores real-time analytics data and metrics

-- Real-time order metrics table
CREATE TABLE IF NOT EXISTS order_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_type VARCHAR(50) NOT NULL,
    metric_value DECIMAL(15,2) NOT NULL,
    user_id BIGINT,
    order_id BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    window_start TIMESTAMP,
    window_end TIMESTAMP,
    INDEX idx_metric_type (metric_type),
    INDEX idx_user_id (user_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_window (window_start, window_end)
);

-- Real-time user activity metrics
CREATE TABLE IF NOT EXISTS user_activity_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_count BIGINT DEFAULT 0,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    window_start TIMESTAMP,
    window_end TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_last_activity (last_activity)
);

-- Real-time revenue analytics
CREATE TABLE IF NOT EXISTS revenue_analytics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    revenue_amount DECIMAL(15,2) NOT NULL,
    order_count BIGINT DEFAULT 0,
    user_id BIGINT,
    time_period VARCHAR(20) NOT NULL, -- 'hourly', 'daily', 'weekly', 'monthly'
    period_start TIMESTAMP,
    period_end TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_time_period (time_period),
    INDEX idx_period_start (period_start),
    INDEX idx_user_id (user_id)
);

-- Real-time system health metrics
CREATE TABLE IF NOT EXISTS system_health_metrics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    metric_name VARCHAR(100) NOT NULL,
    metric_value DECIMAL(15,2) NOT NULL,
    metric_unit VARCHAR(20),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_metric_name (metric_name),
    INDEX idx_timestamp (timestamp)
);

-- Real-time alert thresholds
CREATE TABLE IF NOT EXISTS alert_thresholds (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    threshold_value DECIMAL(15,2) NOT NULL,
    comparison_operator VARCHAR(10) NOT NULL, -- 'gt', 'lt', 'eq', 'gte', 'lte'
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_alert_type (alert_type),
    INDEX idx_is_active (is_active)
);

-- Real-time alerts history
CREATE TABLE IF NOT EXISTS alerts_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    alert_message TEXT NOT NULL,
    metric_value DECIMAL(15,2),
    threshold_value DECIMAL(15,2),
    severity VARCHAR(20) DEFAULT 'INFO', -- 'INFO', 'WARNING', 'CRITICAL'
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    is_resolved BOOLEAN DEFAULT FALSE,
    INDEX idx_alert_type (alert_type),
    INDEX idx_severity (severity),
    INDEX idx_triggered_at (triggered_at),
    INDEX idx_is_resolved (is_resolved)
);

-- Insert default alert thresholds
INSERT INTO alert_thresholds (alert_type, threshold_value, comparison_operator) VALUES
('HIGH_ORDER_VOLUME', 100, 'gt'),
('LOW_REVENUE', 1000.00, 'lt'),
('USER_ACTIVITY_SPIKE', 50, 'gt'),
('SYSTEM_ERROR_RATE', 5.0, 'gt');

-- Comments for documentation
COMMENT ON TABLE order_metrics IS 'Stores real-time order-related metrics and KPIs';
COMMENT ON TABLE user_activity_metrics IS 'Tracks user activity patterns and engagement metrics';
COMMENT ON TABLE revenue_analytics IS 'Real-time revenue analysis and reporting data';
COMMENT ON TABLE system_health_metrics IS 'System performance and health monitoring metrics';
COMMENT ON TABLE alert_thresholds IS 'Configurable thresholds for real-time alerting';
COMMENT ON TABLE alerts_history IS 'Historical record of all triggered alerts'; 