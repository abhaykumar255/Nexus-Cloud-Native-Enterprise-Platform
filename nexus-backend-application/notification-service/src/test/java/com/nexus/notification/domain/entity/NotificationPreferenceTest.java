package com.nexus.notification.domain.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("NotificationPreference Unit Tests")
class NotificationPreferenceTest {

    private NotificationPreference preference;
    private Map<String, Boolean> channelPrefs;
    private Map<String, Boolean> categoryPrefs;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        
        channelPrefs = new HashMap<>();
        channelPrefs.put("email", true);
        channelPrefs.put("sms", false);
        
        categoryPrefs = new HashMap<>();
        categoryPrefs.put("task", true);
        categoryPrefs.put("user", true);
        
        preference = NotificationPreference.builder()
            .id("pref-123")
            .userId("user-123")
            .channelPreferences(channelPrefs)
            .categoryPreferences(categoryPrefs)
            .globalEnabled(true)
            .quietHoursStart("22:00")
            .quietHoursEnd("08:00")
            .timezone("UTC")
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    @Test
    @DisplayName("Builder creates preference with all fields")
    void builder_createsPreferenceWithAllFields() {
        assertThat(preference.getId()).isEqualTo("pref-123");
        assertThat(preference.getUserId()).isEqualTo("user-123");
        assertThat(preference.getChannelPreferences()).containsEntry("email", true);
        assertThat(preference.getCategoryPreferences()).containsEntry("task", true);
        assertThat(preference.isGlobalEnabled()).isTrue();
        assertThat(preference.getQuietHoursStart()).isEqualTo("22:00");
        assertThat(preference.getQuietHoursEnd()).isEqualTo("08:00");
        assertThat(preference.getTimezone()).isEqualTo("UTC");
        assertThat(preference.getCreatedAt()).isEqualTo(now);
        assertThat(preference.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("No-args constructor creates empty preference")
    void noArgsConstructor_createsEmptyPreference() {
        NotificationPreference empty = new NotificationPreference();
        
        assertThat(empty.getId()).isNull();
        assertThat(empty.getUserId()).isNull();
        assertThat(empty.isGlobalEnabled()).isFalse();
    }

    @Test
    @DisplayName("All-args constructor creates preference")
    void allArgsConstructor_createsPreference() {
        NotificationPreference pref = new NotificationPreference(
            "pref-456", "user-456", channelPrefs, categoryPrefs, true,
            "23:00", "07:00", "America/New_York", now, now
        );
        
        assertThat(pref.getId()).isEqualTo("pref-456");
        assertThat(pref.getUserId()).isEqualTo("user-456");
        assertThat(pref.getTimezone()).isEqualTo("America/New_York");
    }

    @Test
    @DisplayName("Setters work correctly")
    void setters_workCorrectly() {
        NotificationPreference pref = new NotificationPreference();
        
        pref.setId("pref-789");
        pref.setUserId("user-789");
        pref.setGlobalEnabled(false);
        pref.setQuietHoursStart("21:00");
        pref.setQuietHoursEnd("09:00");
        pref.setTimezone("Europe/London");
        pref.setCreatedAt(now);
        pref.setUpdatedAt(now);
        
        assertThat(pref.getId()).isEqualTo("pref-789");
        assertThat(pref.getUserId()).isEqualTo("user-789");
        assertThat(pref.isGlobalEnabled()).isFalse();
        assertThat(pref.getQuietHoursStart()).isEqualTo("21:00");
        assertThat(pref.getTimezone()).isEqualTo("Europe/London");
    }

    @Test
    @DisplayName("Equals and hashCode work correctly")
    void equalsAndHashCode_workCorrectly() {
        NotificationPreference pref1 = NotificationPreference.builder()
            .id("pref-1")
            .userId("user-1")
            .globalEnabled(true)
            .build();
        
        NotificationPreference pref2 = NotificationPreference.builder()
            .id("pref-1")
            .userId("user-1")
            .globalEnabled(true)
            .build();
        
        NotificationPreference pref3 = NotificationPreference.builder()
            .id("pref-2")
            .userId("user-2")
            .build();
        
        assertThat(pref1).isEqualTo(pref2);
        assertThat(pref1).isNotEqualTo(pref3);
        assertThat(pref1.hashCode()).isEqualTo(pref2.hashCode());
        assertThat(pref1).isNotEqualTo(null);
        assertThat(pref1).isNotEqualTo(new Object());
    }

    @Test
    @DisplayName("ToString contains key fields")
    void toString_containsKeyFields() {
        String result = preference.toString();
        
        assertThat(result).contains("NotificationPreference");
        assertThat(result).contains("pref-123");
        assertThat(result).contains("user-123");
        assertThat(result).contains("22:00");
        assertThat(result).contains("UTC");
    }

    @Test
    @DisplayName("Channel preferences can be modified")
    void channelPreferences_canBeModified() {
        Map<String, Boolean> newChannels = new HashMap<>();
        newChannels.put("push", true);

        preference.setChannelPreferences(newChannels);

        assertThat(preference.getChannelPreferences()).containsEntry("push", true);
        assertThat(preference.getChannelPreferences()).doesNotContainKey("email");
    }

    @Test
    @DisplayName("Category preferences can be modified")
    void categoryPreferences_canBeModified() {
        Map<String, Boolean> newCategories = new HashMap<>();
        newCategories.put("alert", true);
        newCategories.put("reminder", false);

        preference.setCategoryPreferences(newCategories);

        assertThat(preference.getCategoryPreferences()).containsEntry("alert", true);
        assertThat(preference.getCategoryPreferences()).containsEntry("reminder", false);
        assertThat(preference.getCategoryPreferences()).doesNotContainKey("task");
    }

    @Test
    @DisplayName("Builder with minimal fields works correctly")
    void builderWithMinimalFields_works() {
        NotificationPreference minimal = NotificationPreference.builder()
            .userId("user-minimal")
            .globalEnabled(false)
            .build();

        assertThat(minimal.getUserId()).isEqualTo("user-minimal");
        assertThat(minimal.isGlobalEnabled()).isFalse();
        assertThat(minimal.getId()).isNull();
        assertThat(minimal.getTimezone()).isNull();
    }
}

