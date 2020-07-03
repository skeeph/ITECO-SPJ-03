package me.khabib.chat.repo;

import me.khabib.chat.entities.DeveloperSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevSettingsRepo extends JpaRepository<DeveloperSettings, String> {
}
