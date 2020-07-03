package me.khabib.chat.repo;

import me.khabib.chat.entities.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingsRepo extends JpaRepository<AppSettings, String> {
}
