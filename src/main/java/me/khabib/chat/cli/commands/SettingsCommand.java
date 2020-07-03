package me.khabib.chat.cli.commands;

import me.khabib.chat.cli.Event;
import me.khabib.chat.entities.AppSettings;
import me.khabib.chat.entities.DeveloperSettings;
import me.khabib.chat.entities.Settings;
import me.khabib.chat.repo.AppSettingsRepo;
import me.khabib.chat.repo.DevSettingsRepo;
import me.khabib.chat.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SettingsCommand extends AbstractCommand {
    private final UserService userService;
    private final DevSettingsRepo devSettingsRepo;
    private final AppSettingsRepo appSettingsRepo;

    public SettingsCommand(UserService userService, DevSettingsRepo devSettingsRepo, AppSettingsRepo appSettingsRepo) {
        this.userService = userService;
        this.devSettingsRepo = devSettingsRepo;
        this.appSettingsRepo = appSettingsRepo;
    }

    @Override
    public String command() {
        return "set";
    }

    @Override
    public String description() {
        return "Установка настроек";
    }

    @Override
    @EventListener(condition = "#event.command eq 'set'")
    public void processEvent(Event event) {
        boolean appSettings = false;
        if (userService.isCurrentUserAdmin()) {
            System.out.println("Введите тип настроек \"app\" для настроек приложения, остальное для настроек пользователя");
            appSettings = "app".equals(scanner.nextLine());
        }

        if (appSettings) {
            System.out.println("Введите название OS");
            String os = scanner.nextLine();
            System.out.println("Введите величину памяти");
            Long memory = Long.parseLong(scanner.nextLine());
            saveAppSettings(os, memory);
        } else {
            System.out.println("Введите название настройки");
            String key = scanner.nextLine();
            System.out.println("Введите значение настройки");
            String value = scanner.nextLine();
            saveDevSettings(key, value);
        }
    }

    private void fillCommonSettings(Settings settings) {
        settings.setIsActive(true);
        settings.setIp("current-ip");
    }

    private void saveAppSettings(String os, Long memory) {
        AppSettings settings = new AppSettings();
        fillCommonSettings(settings);
        settings.setOs(os);
        settings.setMemory(memory);
        appSettingsRepo.save(settings);
    }

    private void saveDevSettings(String key, String value) {
        DeveloperSettings settings = new DeveloperSettings();
        fillCommonSettings(settings);
        settings.setKey(key);
        settings.setValue(value);
        devSettingsRepo.save(settings);
    }
}
