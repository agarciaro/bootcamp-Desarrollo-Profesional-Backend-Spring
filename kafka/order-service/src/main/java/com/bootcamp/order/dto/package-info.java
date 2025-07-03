/**
 * Data Transfer Objects (DTOs) for the Order Service
 * 
 * This package contains DTOs used for API communication between the client
 * and the Order Service. These objects are designed to transfer data
 * without exposing internal domain models.
 * 
 * <h3>DTOs in this package:</h3>
 * <ul>
 *   <li>{@link com.bootcamp.order.dto.OrderRequest} - For order creation requests</li>
 *   <li>{@link com.bootcamp.order.dto.StatusRequest} - For order status update requests</li>
 * </ul>
 * 
 * <h3>Key Features:</h3>
 * <ul>
 *   <li>Input validation using Jakarta Validation annotations</li>
 *   <li>Clear separation between API contracts and domain models</li>
 *   <li>Immutable design where appropriate</li>
 *   <li>Comprehensive documentation</li>
 * </ul>
 * 
 * @author Bootcamp Instructor
 * @version 1.0
 */
package com.bootcamp.order.dto; 