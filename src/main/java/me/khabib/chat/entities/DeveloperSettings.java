package me.khabib.chat.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("dev")
@Data
public class DeveloperSettings extends Settings {
    private String key;
    private String value;
}
