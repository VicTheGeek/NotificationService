package ru.vicsergeev.NotificationService.dto;

/**
 * Created by Victor 16.10.2025
 */

public record UserEventDTO(String operation, String email, String name) {
    public static final String CREATE = "CREATE";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";
}
